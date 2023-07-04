import {showToast} from "@nutui/nutui";
export default class DAppError extends Error {

    static CODE_SWITCH_ETHEREUM_CHAIN = 0
    static CODE_GAS_PRICE = 1
    static CODE_ESTIMATE_GAS = 2
    static CODE_SEND_TRANSACTION = 3
    static CODE_CHECK_TRANSACTION = 4
    static CODE_CALL_TRANSACTION = 5
    static CODE_REQUEST_ACCOUNTS = 6
    static CODE_REQUEST_CALL = 7
    static CODE_PERSONAL_SIGN = 8

    constructor(code, message) {
        super('DApp Error! code: ' + code + ' message: ' + message);
        this.code = code;
    }
}

const dapp = {
    ethereum(){
        if (typeof window.ethereum == 'undefined' || !window.ethereum) {
            showToast.warn('请在app中访问');
        }
        return window.ethereum;
    },
    web3() {
        this.ethereum();
        if (window.dappWeb3 === undefined) {
            window.dappWeb3 = new Web3();
            console.log("init web3");
        }
        return window.dappWeb3;
    },

    sendTransaction(from, to, data) {
        return new Promise((resolve, reject) => {
            let tx = {from: from, to: to, data: data};
            console.log(tx);
            this.ethereum().request({method: 'eth_gasPrice', params: []}).then((gasPrice) => {
                console.log("gas price:", gasPrice);
                this.ethereum().request({method: 'eth_estimateGas', params: [tx]}).then((gas) => {
                    console.log("estimate gas:", gas);
                    tx.gasPrice = gasPrice;
                    tx.gas = gas;
                    this.ethereum().request({method: 'eth_sendTransaction', params: [tx]}).then((hash) => {
                        resolve(hash);
                    }).catch((e) => {
                        reject(new DAppError(DAppError.CODE_SEND_TRANSACTION, 'request send transaction err'));
                        console.log("send transaction err:", e);
                    })
                }).catch((e) => {
                    reject(new DAppError(DAppError.CODE_ESTIMATE_GAS, 'request estimate gas error'));
                    console.log("get gas err:", e);
                })
            }).catch((e) => {
                reject(new DAppError(DAppError.CODE_GAS_PRICE, 'request gas price error'));
                console.log("get gas price err:", e);
            })
        });
    },

    loopCheck(hash, resolve, reject) {
        if (typeof hash == 'string' && hash.length > 60 && hash.startsWith("0x")) {
            let checkNum = 1;
            let isRequested = false;
            const interval = setInterval(() => {
                if (isRequested) return;
                isRequested = true;
                this.ethereum().request({method: 'eth_getTransactionByHash', params: [hash]}).then((result) => {
                    const b = result != null && result.blockHash.length > 60;
                    console.log("check hash:", hash, "check num:", checkNum, "up:", b, "data:", result);
                    if (b) {
                        clearInterval(interval);
                        resolve(hash);
                    }
                    if (checkNum >= 30) {
                        clearInterval(interval);
                        reject(new DAppError(DAppError.CODE_CHECK_TRANSACTION, 'check transaction err'));
                    }
                    isRequested = false;
                }).catch((e) => {
                    console.log("check hash:", hash, "check num:", checkNum, "up:", false, "data:", null);
                    if (checkNum >= 30) {
                        clearInterval(interval);
                        reject(new DAppError(DAppError.CODE_CHECK_TRANSACTION, 'check transaction err'));
                    }
                    isRequested = false;
                })
                checkNum++;
            }, 2000)
        }
    },

    call(transaction) {
        console.log(transaction);
        const that = this;
        return new Promise((resolve, reject) => {
            if (typeof transaction != 'function') {
                reject(new DAppError(DAppError.CODE_CALL_TRANSACTION, 'call transaction param type: ' + (typeof transaction)))
                return;
            }
            const result = transaction();
            if (result instanceof Promise) {
                result.then((hash) => {
                    if (typeof hash == 'string' && hash.length > 60 && hash.startsWith("0x")) {
                        that.loopCheck(hash, resolve, reject);
                    } else {
                        resolve(hash);
                    }
                }).catch((err) => {
                    reject(err);
                });
            } else if (typeof result == 'string' && result.length > 60 && result.startsWith("0x")) {
                that.loopCheck(result, resolve, reject);
            } else {
                reject(new DAppError(DAppError.CODE_CALL_TRANSACTION, 'call transaction function result type: ' + (typeof result)));
            }
        });
    },

    async calls(...transactions) {
        let arr = [];
        for (let i = 0; i < transactions.length; i++) {
            const hash = await this.call(transactions[i]);
            arr.push(hash);
        }
        return arr;
    },

    async switchEthereumChain(chainId) {
        const web3 = this.web3();
        try {
            await this.ethereum().request({method: 'wallet_switchEthereumChain', params: [{chainId: web3.utils.numberToHex(chainId)}]});
        } catch (e) {
            throw new DAppError(DAppError.CODE_SWITCH_ETHEREUM_CHAIN, 'switch chain error');
        }
    },

    async signMessage(originMessage, account) {
        try {
            return await this.ethereum().request({method: 'personal_sign', params: [originMessage, account]});
        } catch (e) {
            throw new DAppError(DAppError.CODE_PERSONAL_SIGN, 'sign message error');
        }
    },

    async requestAccounts() {
        try {
            return (await this.ethereum().request({method: 'eth_requestAccounts', params: []}))[0];
        } catch (e) {
            throw new DAppError(DAppError.CODE_REQUEST_ACCOUNTS, 'request account error');
        }
    },

    async requestCall(from, to, data) {
        try {
            return await this.ethereum().request({method: 'eth_call', params: [{from: from, to: to, data: data}]});
        } catch (e) {
            throw new DAppError(DAppError.CODE_REQUEST_CALL, "eth call error")
        }
    },

    async transfer(contract, to, value) {
        const web3 = this.web3();
        const from = await this.requestAccounts();
        console.log("transfer from:", from, "to:", to, "value:", web3.utils.toWei(value.toString(), 'ether'));
        const data = web3.eth.abi.encodeFunctionCall({
            name: 'transfer',
            type: 'function',
            inputs: [{type: 'address', name: 'recipient'}, {type: 'uint256', name: 'amount'}]
        }, [to, web3.utils.toWei(value.toString(), 'ether')]);
        console.log("data:", data);
        return (await this.sendTransaction(from, contract, data))
    },

    async allowance(contract, spender) {
        const web3 = this.web3();
        const owner = await this.requestAccounts();
        const data = web3.eth.abi.encodeFunctionCall({
            name: 'allowance',
            type: 'function',
            inputs: [{name: "owner", type: "address"}, {name: "spender", type: "address"}],
        }, [owner, spender]);
        const result = await this.requestCall(owner, contract, data);
        const amount = web3.eth.abi.decodeParameters([{type: 'uint256', name: 'amount'},], result);
        console.log('allowance result:', web3.utils.fromWei(amount.amount, 'ether'))
        return web3.utils.fromWei(amount.amount, 'ether')
    },

    async balanceOf(contract) {
        const web3 = this.web3();
        const owner = await this.requestAccounts();
        console.log("account:", owner);
        const data = web3.eth.abi.encodeFunctionCall({
            name: 'balanceOf',
            type: 'function',
            inputs: [{name: "account", type: "address"}],
        }, [owner]);
        const result = await this.requestCall(owner, contract, data);
        const amount = web3.eth.abi.decodeParameters([{type: 'uint256', name: 'amount'},], result);
        console.log('balanceOf result:', web3.utils.fromWei(amount.amount, 'ether'))
        return web3.utils.fromWei(amount.amount, 'ether')
    },

    async approve(contract, spender) {
        const web3 = this.web3();
        console.log("approve contract:", contract, "spender:", spender);
        const allowance = await this.allowance(contract, spender)
        let bigInt;
        if (allowance.indexOf(".") > 0) {
            bigInt = web3.utils.BN(allowance.substring(0, allowance.indexOf(".")));
        } else {
            bigInt = web3.utils.BN(allowance);
        }
        if (bigInt.cmp(new web3.utils.BN(100000000)) >= 0) return "skip";
        const owner = await this.requestAccounts();
        console.log("approve owner:", owner, "contract:", contract, "spender:", spender);
        const maxUint256 = web3.utils.toBN("115792089237316195423570985008687907853269984665640564039457584007913129639935");
        const data = web3.eth.abi.encodeFunctionCall({
            name: 'approve',
            type: 'function',
            inputs: [{name: "spender", type: "address"}, {name: "amount", type: "uint256"}],
        }, [spender, web3.utils.toHex(maxUint256)]);
        return (await this.sendTransaction(owner, contract, data))
    },
}
export {dapp}