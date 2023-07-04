package main

import "C"

import (
	"context"
	"encoding/json"
	"fmt"
	ethereum "github.com/ethereum/go-ethereum"
	"github.com/ethereum/go-ethereum/accounts"
	"github.com/ethereum/go-ethereum/accounts/abi"
	"github.com/ethereum/go-ethereum/common"
	"github.com/ethereum/go-ethereum/common/hexutil"
	"github.com/ethereum/go-ethereum/core/types"
	"github.com/ethereum/go-ethereum/crypto"
	"github.com/ethereum/go-ethereum/ethclient"
	"github.com/shopspring/decimal"
	"golib/golog"
	"math/big"
	"os"
	"strings"
	"sync"
)

var (
	PancakeSwapV2ABI, TokenABI, RengGouABI abi.ABI
	config                                 *Config
	EthClient                              *ethclient.Client
	nonceMap                               map[string]int64
	lock                                   sync.Mutex
)

type Config struct {
	HttpProxy string   `json:"http_proxy"`
	ChainId   *big.Int `json:"chain_id"`
	ChainNode string   `json:"chain_node"`
}

//export InitConfig
func InitConfig(jsonStr string) {
	PancakeSwapV2ABI, _ = abi.JSON(strings.NewReader(`[{"inputs":[{"internalType":"address","name":"_factory","type":"address"},{"internalType":"address","name":"_WETH","type":"address"}],"stateMutability":"nonpayable","type":"constructor"},{"inputs":[],"name":"WETH","outputs":[{"internalType":"address","name":"","type":"address"}],"stateMutability":"view","type":"function"},{"inputs":[{"internalType":"address","name":"tokenA","type":"address"},{"internalType":"address","name":"tokenB","type":"address"},{"internalType":"uint256","name":"amountADesired","type":"uint256"},{"internalType":"uint256","name":"amountBDesired","type":"uint256"},{"internalType":"uint256","name":"amountAMin","type":"uint256"},{"internalType":"uint256","name":"amountBMin","type":"uint256"},{"internalType":"address","name":"to","type":"address"},{"internalType":"uint256","name":"deadline","type":"uint256"}],"name":"addLiquidity","outputs":[{"internalType":"uint256","name":"amountA","type":"uint256"},{"internalType":"uint256","name":"amountB","type":"uint256"},{"internalType":"uint256","name":"liquidity","type":"uint256"}],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"address","name":"token","type":"address"},{"internalType":"uint256","name":"amountTokenDesired","type":"uint256"},{"internalType":"uint256","name":"amountTokenMin","type":"uint256"},{"internalType":"uint256","name":"amountETHMin","type":"uint256"},{"internalType":"address","name":"to","type":"address"},{"internalType":"uint256","name":"deadline","type":"uint256"}],"name":"addLiquidityETH","outputs":[{"internalType":"uint256","name":"amountToken","type":"uint256"},{"internalType":"uint256","name":"amountETH","type":"uint256"},{"internalType":"uint256","name":"liquidity","type":"uint256"}],"stateMutability":"payable","type":"function"},{"inputs":[],"name":"factory","outputs":[{"internalType":"address","name":"","type":"address"}],"stateMutability":"view","type":"function"},{"inputs":[{"internalType":"uint256","name":"amountOut","type":"uint256"},{"internalType":"uint256","name":"reserveIn","type":"uint256"},{"internalType":"uint256","name":"reserveOut","type":"uint256"}],"name":"getAmountIn","outputs":[{"internalType":"uint256","name":"amountIn","type":"uint256"}],"stateMutability":"pure","type":"function"},{"inputs":[{"internalType":"uint256","name":"amountIn","type":"uint256"},{"internalType":"uint256","name":"reserveIn","type":"uint256"},{"internalType":"uint256","name":"reserveOut","type":"uint256"}],"name":"getAmountOut","outputs":[{"internalType":"uint256","name":"amountOut","type":"uint256"}],"stateMutability":"pure","type":"function"},{"inputs":[{"internalType":"uint256","name":"amountOut","type":"uint256"},{"internalType":"address[]","name":"path","type":"address[]"}],"name":"getAmountsIn","outputs":[{"internalType":"uint256[]","name":"amounts","type":"uint256[]"}],"stateMutability":"view","type":"function"},{"inputs":[{"internalType":"uint256","name":"amountIn","type":"uint256"},{"internalType":"address[]","name":"path","type":"address[]"}],"name":"getAmountsOut","outputs":[{"internalType":"uint256[]","name":"amounts","type":"uint256[]"}],"stateMutability":"view","type":"function"},{"inputs":[{"internalType":"uint256","name":"amountA","type":"uint256"},{"internalType":"uint256","name":"reserveA","type":"uint256"},{"internalType":"uint256","name":"reserveB","type":"uint256"}],"name":"quote","outputs":[{"internalType":"uint256","name":"amountB","type":"uint256"}],"stateMutability":"pure","type":"function"},{"inputs":[{"internalType":"address","name":"tokenA","type":"address"},{"internalType":"address","name":"tokenB","type":"address"},{"internalType":"uint256","name":"liquidity","type":"uint256"},{"internalType":"uint256","name":"amountAMin","type":"uint256"},{"internalType":"uint256","name":"amountBMin","type":"uint256"},{"internalType":"address","name":"to","type":"address"},{"internalType":"uint256","name":"deadline","type":"uint256"}],"name":"removeLiquidity","outputs":[{"internalType":"uint256","name":"amountA","type":"uint256"},{"internalType":"uint256","name":"amountB","type":"uint256"}],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"address","name":"token","type":"address"},{"internalType":"uint256","name":"liquidity","type":"uint256"},{"internalType":"uint256","name":"amountTokenMin","type":"uint256"},{"internalType":"uint256","name":"amountETHMin","type":"uint256"},{"internalType":"address","name":"to","type":"address"},{"internalType":"uint256","name":"deadline","type":"uint256"}],"name":"removeLiquidityETH","outputs":[{"internalType":"uint256","name":"amountToken","type":"uint256"},{"internalType":"uint256","name":"amountETH","type":"uint256"}],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"address","name":"token","type":"address"},{"internalType":"uint256","name":"liquidity","type":"uint256"},{"internalType":"uint256","name":"amountTokenMin","type":"uint256"},{"internalType":"uint256","name":"amountETHMin","type":"uint256"},{"internalType":"address","name":"to","type":"address"},{"internalType":"uint256","name":"deadline","type":"uint256"}],"name":"removeLiquidityETHSupportingFeeOnTransferTokens","outputs":[{"internalType":"uint256","name":"amountETH","type":"uint256"}],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"address","name":"token","type":"address"},{"internalType":"uint256","name":"liquidity","type":"uint256"},{"internalType":"uint256","name":"amountTokenMin","type":"uint256"},{"internalType":"uint256","name":"amountETHMin","type":"uint256"},{"internalType":"address","name":"to","type":"address"},{"internalType":"uint256","name":"deadline","type":"uint256"},{"internalType":"bool","name":"approveMax","type":"bool"},{"internalType":"uint8","name":"v","type":"uint8"},{"internalType":"bytes32","name":"r","type":"bytes32"},{"internalType":"bytes32","name":"s","type":"bytes32"}],"name":"removeLiquidityETHWithPermit","outputs":[{"internalType":"uint256","name":"amountToken","type":"uint256"},{"internalType":"uint256","name":"amountETH","type":"uint256"}],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"address","name":"token","type":"address"},{"internalType":"uint256","name":"liquidity","type":"uint256"},{"internalType":"uint256","name":"amountTokenMin","type":"uint256"},{"internalType":"uint256","name":"amountETHMin","type":"uint256"},{"internalType":"address","name":"to","type":"address"},{"internalType":"uint256","name":"deadline","type":"uint256"},{"internalType":"bool","name":"approveMax","type":"bool"},{"internalType":"uint8","name":"v","type":"uint8"},{"internalType":"bytes32","name":"r","type":"bytes32"},{"internalType":"bytes32","name":"s","type":"bytes32"}],"name":"removeLiquidityETHWithPermitSupportingFeeOnTransferTokens","outputs":[{"internalType":"uint256","name":"amountETH","type":"uint256"}],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"address","name":"tokenA","type":"address"},{"internalType":"address","name":"tokenB","type":"address"},{"internalType":"uint256","name":"liquidity","type":"uint256"},{"internalType":"uint256","name":"amountAMin","type":"uint256"},{"internalType":"uint256","name":"amountBMin","type":"uint256"},{"internalType":"address","name":"to","type":"address"},{"internalType":"uint256","name":"deadline","type":"uint256"},{"internalType":"bool","name":"approveMax","type":"bool"},{"internalType":"uint8","name":"v","type":"uint8"},{"internalType":"bytes32","name":"r","type":"bytes32"},{"internalType":"bytes32","name":"s","type":"bytes32"}],"name":"removeLiquidityWithPermit","outputs":[{"internalType":"uint256","name":"amountA","type":"uint256"},{"internalType":"uint256","name":"amountB","type":"uint256"}],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"uint256","name":"amountOut","type":"uint256"},{"internalType":"address[]","name":"path","type":"address[]"},{"internalType":"address","name":"to","type":"address"},{"internalType":"uint256","name":"deadline","type":"uint256"}],"name":"swapETHForExactTokens","outputs":[{"internalType":"uint256[]","name":"amounts","type":"uint256[]"}],"stateMutability":"payable","type":"function"},{"inputs":[{"internalType":"uint256","name":"amountOutMin","type":"uint256"},{"internalType":"address[]","name":"path","type":"address[]"},{"internalType":"address","name":"to","type":"address"},{"internalType":"uint256","name":"deadline","type":"uint256"}],"name":"swapExactETHForTokens","outputs":[{"internalType":"uint256[]","name":"amounts","type":"uint256[]"}],"stateMutability":"payable","type":"function"},{"inputs":[{"internalType":"uint256","name":"amountOutMin","type":"uint256"},{"internalType":"address[]","name":"path","type":"address[]"},{"internalType":"address","name":"to","type":"address"},{"internalType":"uint256","name":"deadline","type":"uint256"}],"name":"swapExactETHForTokensSupportingFeeOnTransferTokens","outputs":[],"stateMutability":"payable","type":"function"},{"inputs":[{"internalType":"uint256","name":"amountIn","type":"uint256"},{"internalType":"uint256","name":"amountOutMin","type":"uint256"},{"internalType":"address[]","name":"path","type":"address[]"},{"internalType":"address","name":"to","type":"address"},{"internalType":"uint256","name":"deadline","type":"uint256"}],"name":"swapExactTokensForETH","outputs":[{"internalType":"uint256[]","name":"amounts","type":"uint256[]"}],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"uint256","name":"amountIn","type":"uint256"},{"internalType":"uint256","name":"amountOutMin","type":"uint256"},{"internalType":"address[]","name":"path","type":"address[]"},{"internalType":"address","name":"to","type":"address"},{"internalType":"uint256","name":"deadline","type":"uint256"}],"name":"swapExactTokensForETHSupportingFeeOnTransferTokens","outputs":[],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"uint256","name":"amountIn","type":"uint256"},{"internalType":"uint256","name":"amountOutMin","type":"uint256"},{"internalType":"address[]","name":"path","type":"address[]"},{"internalType":"address","name":"to","type":"address"},{"internalType":"uint256","name":"deadline","type":"uint256"}],"name":"swapExactTokensForTokens","outputs":[{"internalType":"uint256[]","name":"amounts","type":"uint256[]"}],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"uint256","name":"amountIn","type":"uint256"},{"internalType":"uint256","name":"amountOutMin","type":"uint256"},{"internalType":"address[]","name":"path","type":"address[]"},{"internalType":"address","name":"to","type":"address"},{"internalType":"uint256","name":"deadline","type":"uint256"}],"name":"swapExactTokensForTokensSupportingFeeOnTransferTokens","outputs":[],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"uint256","name":"amountOut","type":"uint256"},{"internalType":"uint256","name":"amountInMax","type":"uint256"},{"internalType":"address[]","name":"path","type":"address[]"},{"internalType":"address","name":"to","type":"address"},{"internalType":"uint256","name":"deadline","type":"uint256"}],"name":"swapTokensForExactETH","outputs":[{"internalType":"uint256[]","name":"amounts","type":"uint256[]"}],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"uint256","name":"amountOut","type":"uint256"},{"internalType":"uint256","name":"amountInMax","type":"uint256"},{"internalType":"address[]","name":"path","type":"address[]"},{"internalType":"address","name":"to","type":"address"},{"internalType":"uint256","name":"deadline","type":"uint256"}],"name":"swapTokensForExactTokens","outputs":[{"internalType":"uint256[]","name":"amounts","type":"uint256[]"}],"stateMutability":"nonpayable","type":"function"},{"stateMutability":"payable","type":"receive"}]`))
	TokenABI, _ = abi.JSON(strings.NewReader(`[{"inputs":[],"stateMutability":"nonpayable","type":"constructor"},{"anonymous":false,"inputs":[{"indexed":true,"internalType":"address","name":"owner","type":"address"},{"indexed":true,"internalType":"address","name":"spender","type":"address"},{"indexed":false,"internalType":"uint256","name":"value","type":"uint256"}],"name":"Approval","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"internalType":"address","name":"account","type":"address"},{"indexed":false,"internalType":"uint8","name":"id","type":"uint8"},{"indexed":false,"internalType":"uint256","name":"price","type":"uint256"},{"indexed":false,"internalType":"uint256","name":"ep","type":"uint256"},{"indexed":false,"internalType":"uint256","name":"epRatio","type":"uint256"},{"indexed":false,"internalType":"uint256","name":"tokenA","type":"uint256"},{"indexed":false,"internalType":"uint256","name":"no","type":"uint256"}],"name":"Mold","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"internalType":"address","name":"previousOwner","type":"address"},{"indexed":true,"internalType":"address","name":"newOwner","type":"address"}],"name":"OwnershipTransferred","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"internalType":"address","name":"from","type":"address"},{"indexed":true,"internalType":"address","name":"to","type":"address"},{"indexed":false,"internalType":"uint256","name":"value","type":"uint256"}],"name":"Transfer","type":"event"},{"inputs":[{"internalType":"address","name":"owner","type":"address"},{"internalType":"address","name":"spender","type":"address"}],"name":"allowance","outputs":[{"internalType":"uint256","name":"","type":"uint256"}],"stateMutability":"view","type":"function"},{"inputs":[{"internalType":"address","name":"spender","type":"address"},{"internalType":"uint256","name":"amount","type":"uint256"}],"name":"approve","outputs":[{"internalType":"bool","name":"","type":"bool"}],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"address","name":"account","type":"address"}],"name":"balanceOf","outputs":[{"internalType":"uint256","name":"","type":"uint256"}],"stateMutability":"view","type":"function"},{"inputs":[{"internalType":"address[]","name":"recipients","type":"address[]"},{"internalType":"uint256[]","name":"amounts","type":"uint256[]"}],"name":"batchTransfer","outputs":[],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"uint8","name":"","type":"uint8"}],"name":"cards","outputs":[{"internalType":"uint256","name":"price","type":"uint256"},{"internalType":"uint256","name":"ep","type":"uint256"},{"internalType":"uint256","name":"epRatio","type":"uint256"}],"stateMutability":"view","type":"function"},{"inputs":[],"name":"decimals","outputs":[{"internalType":"uint8","name":"","type":"uint8"}],"stateMutability":"view","type":"function"},{"inputs":[{"internalType":"address","name":"spender","type":"address"},{"internalType":"uint256","name":"subtractedValue","type":"uint256"}],"name":"decreaseAllowance","outputs":[{"internalType":"bool","name":"","type":"bool"}],"stateMutability":"nonpayable","type":"function"},{"inputs":[],"name":"getOwner","outputs":[{"internalType":"address","name":"","type":"address"}],"stateMutability":"view","type":"function"},{"inputs":[{"internalType":"address","name":"spender","type":"address"},{"internalType":"uint256","name":"addedValue","type":"uint256"}],"name":"increaseAllowance","outputs":[{"internalType":"bool","name":"","type":"bool"}],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"uint8","name":"id","type":"uint8"},{"internalType":"uint256","name":"no","type":"uint256"},{"internalType":"bool","name":"useEP","type":"bool"}],"name":"mold","outputs":[],"stateMutability":"nonpayable","type":"function"},{"inputs":[],"name":"name","outputs":[{"internalType":"string","name":"","type":"string"}],"stateMutability":"view","type":"function"},{"inputs":[],"name":"owner","outputs":[{"internalType":"address","name":"","type":"address"}],"stateMutability":"view","type":"function"},{"inputs":[],"name":"renounceOwnership","outputs":[],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"uint8","name":"id","type":"uint8"},{"internalType":"uint256","name":"price","type":"uint256"},{"internalType":"uint256","name":"ep","type":"uint256"},{"internalType":"uint256","name":"epRatio","type":"uint256"}],"name":"setCard","outputs":[],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"address","name":"lp","type":"address"},{"internalType":"address","name":"marketAddress","type":"address"},{"internalType":"address","name":"addressA","type":"address"},{"internalType":"address","name":"addressB","type":"address"}],"name":"setup","outputs":[],"stateMutability":"nonpayable","type":"function"},{"inputs":[],"name":"symbol","outputs":[{"internalType":"string","name":"","type":"string"}],"stateMutability":"view","type":"function"},{"inputs":[],"name":"totalSupply","outputs":[{"internalType":"uint256","name":"","type":"uint256"}],"stateMutability":"view","type":"function"},{"inputs":[{"internalType":"address","name":"recipient","type":"address"},{"internalType":"uint256","name":"amount","type":"uint256"}],"name":"transfer","outputs":[{"internalType":"bool","name":"","type":"bool"}],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"address","name":"sender","type":"address"},{"internalType":"address","name":"recipient","type":"address"},{"internalType":"uint256","name":"amount","type":"uint256"}],"name":"transferFrom","outputs":[{"internalType":"bool","name":"","type":"bool"}],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"address","name":"newOwner","type":"address"}],"name":"transferOwnership","outputs":[],"stateMutability":"nonpayable","type":"function"}]`))
	RengGouABI, _ = abi.JSON(strings.NewReader(`[{"inputs":[],"stateMutability":"nonpayable","type":"constructor"},{"anonymous":false,"inputs":[{"indexed":true,"internalType":"address","name":"account","type":"address"},{"indexed":true,"internalType":"address","name":"recommender","type":"address"},{"indexed":false,"internalType":"uint256","name":"node","type":"uint256"},{"indexed":false,"internalType":"uint256","name":"period","type":"uint256"},{"indexed":false,"internalType":"uint256","name":"price","type":"uint256"},{"indexed":false,"internalType":"uint256","name":"ep","type":"uint256"},{"indexed":false,"internalType":"uint256","name":"no","type":"uint256"}],"name":"BuyNode","type":"event"},{"inputs":[{"internalType":"uint256","name":"nodeLevel","type":"uint256"},{"internalType":"address","name":"recommender","type":"address"},{"internalType":"uint256","name":"no","type":"uint256"}],"name":"buy","outputs":[],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"address","name":"account","type":"address"},{"internalType":"uint256","name":"nodeLevel","type":"uint256"},{"internalType":"uint256","name":"period","type":"uint256"}],"name":"getBuyAmount","outputs":[{"internalType":"uint256","name":"","type":"uint256"}],"stateMutability":"view","type":"function"},{"inputs":[{"internalType":"uint256","name":"nodeLevel","type":"uint256"}],"name":"getBuyNode","outputs":[{"internalType":"uint256","name":"","type":"uint256"},{"internalType":"uint256","name":"","type":"uint256"},{"internalType":"uint256","name":"","type":"uint256"},{"internalType":"uint256","name":"","type":"uint256"},{"internalType":"uint256","name":"","type":"uint256"}],"stateMutability":"view","type":"function"},{"inputs":[{"internalType":"uint256","name":"nodeLevel","type":"uint256"}],"name":"isSoldOut","outputs":[{"internalType":"bool","name":"","type":"bool"}],"stateMutability":"view","type":"function"}]`))
	nonceMap = make(map[string]int64)
	config = &Config{}
	err := json.Unmarshal([]byte(jsonStr), &config)
	if len(config.HttpProxy) > 0 {
		os.Setenv("HTTP_PROXY", config.HttpProxy)
		os.Setenv("HTTPS_PROXY", config.HttpProxy)
	}
	EthClient, err = ethclient.Dial(config.ChainNode)
	if err != nil {
		panic(err)
	}
}

//export VerifyingSignedMessage
func VerifyingSignedMessage(_wallet string, _message string, _sign string) bool {
	wallet := strings.ToLower(_wallet)
	message := accounts.TextHash([]byte(_message))
	sign := hexutil.MustDecode(_sign)
	sign[crypto.RecoveryIDOffset] -= 27
	pk, _ := crypto.SigToPub(message, sign)
	pubAddress := strings.ToLower(crypto.PubkeyToAddress(*pk).Hex())
	return pubAddress == wallet
}

//export GetMoldEncodeData
func GetMoldEncodeData(id uint8, no int64, useEP bool) *C.char {
	data, _ := TokenABI.Pack("mold", id, big.NewInt(no), useEP)
	return C.CString(hexutil.Encode(data))
}

//export GetERC20Balance
func GetERC20Balance(contract string, account string) *C.char {
	data, _ := TokenABI.Pack("balanceOf", common.HexToAddress(account))
	resp, err := CallContract(common.HexToAddress(contract), data)
	if err != nil {
		golog.Error("Get ERC20 Balance Call Remote Error:", err.Error())
		return nil
	}
	outputs, err := TokenABI.Methods["balanceOf"].Outputs.UnpackValues(resp)
	if err != nil {
		golog.Error("Unpack ERC20 Balance Error:", err.Error())
		return nil
	}
	return C.CString(FromWei(outputs[0].(*big.Int)).String())
}

//export SendERC20Transfer
func SendERC20Transfer(fromPrivateHexKey string, contract string, to string, value string) *C.char {
	amount, err := decimal.NewFromString(value)
	if err != nil {
		golog.Error("Send ERC20 Transfer ERROR:", err.Error())
		return nil
	}
	hash := SendContractTransaction(fromPrivateHexKey, contract, func() ([]byte, error) {
		return TokenABI.Pack("transfer", common.HexToAddress(to), ToWei(amount))
	})
	if hash == nil {
		return nil
	}
	return C.CString(*hash)
}

//export GetERC20TransferLog
func GetERC20TransferLog(hash string) *C.char {
	tx, err := EthClient.TransactionReceipt(context.Background(), common.HexToHash(hash))
	if err != nil {
		golog.Error("Get ERC20 Transfer Log ERROR:", err.Error())
		return nil
	}
	var transfer []string
	if tx.Status == 1 && len(tx.Logs) > 0 && len(tx.Logs[0].Topics) > 0 {
		for _, log := range tx.Logs {
			if strings.EqualFold(log.Topics[0].String(), "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef") {
				from := common.HexToAddress(log.Topics[1].Hex()).String()
				to := common.HexToAddress(log.Topics[2].Hex()).String()
				values, err := TokenABI.Events["Transfer"].Inputs.UnpackValues(log.Data)
				if err != nil {
					golog.Error("Unpack ERC20 Transfer Log ERROR:", err.Error())
					continue
				}
				value := FromWei(values[0].(*big.Int)).String()
				transfer = append(transfer, fmt.Sprintf(`{"from":"%s","to":"%s","value":"%s"}`, from, to, value))
			}
		}
		return C.CString(fmt.Sprintf(`{"status":%d,"transfer":[%s]}`, tx.Status, strings.Join(transfer, ",")))
	}
	return nil
}

//export GetMoldLog
func GetMoldLog(hash string) *C.char {
	tx, err := EthClient.TransactionReceipt(context.Background(), common.HexToHash(hash))
	if err != nil {
		golog.Error("Get Mold Log ERROR:", err.Error())
		return nil
	}
	jsonstr := `{"status":%d,"account":"%s","id":%d,"price":%d,"ep":%d,"epRatio":%d,"value":"%s","no":%d}`
	if tx.Status == 1 && len(tx.Logs) > 0 {
		for _, log := range tx.Logs {
			if strings.EqualFold(log.Topics[0].String(), "0xb0192c1e5724c3937f9f8b524d86b295dc7ab53f63d03606d2403580befc7302") {
				account := common.HexToAddress(log.Topics[1].Hex()).String()
				values, err := TokenABI.Unpack("Mold", log.Data)
				if err != nil {
					golog.Error("Unpack Mold Log ERROR:", err.Error())
					return C.CString(fmt.Sprintf(jsonstr, tx.Status, err.Error(), 0, 0, "0", "0"))
				}
				id := values[0]
				price := values[1]
				ep := values[2]
				epRatio := values[3]
				value := values[4]
				no := values[5]
				return C.CString(fmt.Sprintf(jsonstr, tx.Status, account, id, price, ep, epRatio, FromWei(value.(*big.Int)).String(), no))
			}
		}

	} else {
		return C.CString(fmt.Sprintf(jsonstr, tx.Status, "", 0, 0, "0", "0"))
	}
	return nil
}

//export GetPancakeSwapTokenV2Price
func GetPancakeSwapTokenV2Price(tokenIn string, tokenOut string) *C.char {
	var method = "getAmountsOut"
	var data, err = PancakeSwapV2ABI.Pack(method, ToWei(decimal.NewFromInt32(1)), []common.Address{
		common.HexToAddress(tokenIn),
		common.HexToAddress(tokenOut),
	})
	if err != nil {
		golog.Error("Pack PancakeSwap getAmountsOut ERROR:", err.Error())
		return nil
	}
	var router = "0xD99D1c33F9fC3444f8101754aBC46c52416550D1"
	if config.ChainId.Int64() == 56 {
		router = "0x10ED43C718714eb63d5aA57B78B54704E256024E"
	}
	data, err = CallContract(common.HexToAddress(router), data)
	if err != nil {
		golog.Error("Call PancakeSwap getAmountsOut ERROR:", err.Error())
		return nil
	}
	outputs, err := PancakeSwapV2ABI.Unpack(method, data)
	if err != nil {
		golog.Error("Unpack PancakeSwap getAmountsOut ERROR:", err.Error())
		return nil
	}
	out0 := *abi.ConvertType(outputs[0], new([]*big.Int)).(*[]*big.Int)
	price := FromWei(out0[1]).String()
	return C.CString(price)
}

//export GetBuyNodeEncodeData
func GetBuyNodeEncodeData(node int64, recommender string, no int64) *C.char {
	data, _ := RengGouABI.Pack("buy", big.NewInt(node), common.HexToAddress(recommender), big.NewInt(no))
	return C.CString(hexutil.Encode(data))
}

//export GetByNode
func GetByNode(contract string, node int64) *C.char {
	var data, err = RengGouABI.Pack("getBuyNode", big.NewInt(node))
	if err != nil {
		golog.Error("Pack getBuyNode ERROR:", err.Error())
		return nil
	}
	data, err = CallContract(common.HexToAddress(contract), data)
	if err != nil {
		golog.Error("Call getBuyNode ERROR:", err.Error())
		return nil
	}
	values, err := RengGouABI.Unpack("getBuyNode", data)
	if err != nil {
		golog.Error("Unpack getBuyNode ERROR:", err.Error())
		return nil
	}
	price := FromWei(values[0].(*big.Int))
	period := values[1].(*big.Int)
	count := values[2].(*big.Int)
	buyCount := values[3].(*big.Int)
	ep := values[4].(*big.Int)
	return C.CString(fmt.Sprintf(`{"price":%d, "period":%d, "count":%d, "buyCount":%d, "ep":%d}`, price.BigInt(), period, count, buyCount, ep))
}

//export GetBuyNodeLog
func GetBuyNodeLog(hash string) *C.char {
	tx, err := EthClient.TransactionReceipt(context.Background(), common.HexToHash(hash))
	if err != nil {
		golog.Error("Get Buy Node Log ERROR:", err.Error())
		return nil
	}
	jsonstr := `{"status":%d,"account":"%s","recommender":"%s","node":%d,"period":%d,"price":%d,"ep":%d,"no":%d}`
	if tx.Status == 1 && len(tx.Logs) > 0 {
		for _, log := range tx.Logs {
			if strings.EqualFold(log.Topics[0].String(), "0x6c694f4d75732675fc7a095b328d678f403d65c6fcab2de23655282efc9e7413") {
				account := common.HexToAddress(log.Topics[1].Hex()).String()
				recommender := common.HexToAddress(log.Topics[2].Hex()).String()
				values, err := RengGouABI.Events["BuyNode"].Inputs.UnpackValues(log.Data)
				if err != nil {
					golog.Error("Unpack Buy Node Log ERROR:", err.Error())
					return nil
				}
				node := values[0].(*big.Int)
				period := values[1].(*big.Int)
				price := values[2].(*big.Int)
				ep := values[3].(*big.Int)
				no := values[4].(*big.Int)
				return C.CString(fmt.Sprintf(jsonstr, tx.Status, account, recommender, node, period.Int64(), FromWei(price).BigInt(), ep, no))
			}
		}
	} else {
		return C.CString(fmt.Sprintf(jsonstr, tx.Status, "", 0, "0"))
	}
	return nil
}

//export Release
func Release(fromPrivateHexKey string, contract string) *C.char {
	_abi, _ := abi.JSON(strings.NewReader(`[{"inputs":[{"internalType":"uint256","name":"startTime","type":"uint256"}],"stateMutability":"nonpayable","type":"constructor"},{"inputs":[],"name":"blockTimestamp","outputs":[{"internalType":"uint256","name":"","type":"uint256"}],"stateMutability":"view","type":"function"},{"inputs":[],"name":"diffDays","outputs":[{"internalType":"uint256","name":"","type":"uint256"}],"stateMutability":"view","type":"function"},{"inputs":[],"name":"isOver","outputs":[{"internalType":"bool","name":"","type":"bool"}],"stateMutability":"view","type":"function"},{"inputs":[],"name":"lastDay","outputs":[{"internalType":"uint256","name":"","type":"uint256"}],"stateMutability":"view","type":"function"},{"inputs":[],"name":"lastTimestamp","outputs":[{"internalType":"uint256","name":"","type":"uint256"}],"stateMutability":"view","type":"function"},{"inputs":[],"name":"owner","outputs":[{"internalType":"address","name":"","type":"address"}],"stateMutability":"view","type":"function"},{"inputs":[],"name":"release","outputs":[{"internalType":"bool","name":"","type":"bool"}],"stateMutability":"nonpayable","type":"function"},{"inputs":[],"name":"totalDay","outputs":[{"internalType":"uint256","name":"","type":"uint256"}],"stateMutability":"view","type":"function"},{"inputs":[{"internalType":"address","name":"newOwner","type":"address"}],"name":"transferOwnership","outputs":[],"stateMutability":"nonpayable","type":"function"}]`))
	hash := SendContractTransaction(fromPrivateHexKey, contract, func() ([]byte, error) {
		return _abi.Pack("release")
	})
	if hash == nil {
		return nil
	}
	return C.CString(*hash)
}

func GetNonce(account string) int64 {
	nonce, err := EthClient.PendingNonceAt(context.Background(), common.HexToAddress(account))
	if err != nil {
		golog.Error("Get Nonce ERROR:", err.Error())
	}
	return int64(nonce)
}

func GetGasPrice() *big.Int {
	gasPrice, err := EthClient.SuggestGasPrice(context.Background())
	if err != nil {
		golog.Error("Get Gas Price ERROR:", err.Error())
	}
	return gasPrice
}

func GetGasLimit(fromAddress common.Address, toAddress *common.Address, value *big.Int, data []byte) uint64 {
	gasLimit, err := EthClient.EstimateGas(context.Background(), ethereum.CallMsg{
		From:  fromAddress,
		To:    toAddress,
		Data:  data,
		Value: value,
	})
	if err != nil {
		golog.Error("Get Gas Limit ERROR:", err.Error())
	}
	return gasLimit
}

func CallContract(contractAddress common.Address, data []byte) ([]byte, error) {
	bytes, err := EthClient.CallContract(context.Background(), ethereum.CallMsg{
		From: common.HexToAddress("0x0000000000000000000000000000000000000000"),
		To:   &contractAddress,
		Data: data,
	}, nil)
	if err != nil {
		golog.Error("CallContract ERROR:", err.Error())
		return nil, err
	}
	return bytes, nil
}

func SendContractTransaction(fromPrivateHexKey string, contractAddress string, getData func() ([]byte, error)) *string {
	privateKey, err := crypto.HexToECDSA(fromPrivateHexKey)
	if err != nil {
		golog.Error("Send Contract Transaction HexToECDSA Key ERROR:", err.Error())
		return nil
	}
	address := crypto.PubkeyToAddress(privateKey.PublicKey).Hex()
	lock.Lock()
	defer lock.Unlock()
	nonce, ok := nonceMap[address]
	if !ok {
		nonce = GetNonce(address)
		nonceMap[address] = nonce
	}
	data, err := getData()
	if err != nil {
		golog.Error("Send Contract Transaction Get Data ERROR:", err.Error())
		return nil
	}
	contract := common.HexToAddress(contractAddress)
	signer := types.NewEIP155Signer(config.ChainId)
	tx, err := types.SignNewTx(privateKey, signer, &types.LegacyTx{
		Nonce:    uint64(nonce),
		GasPrice: GetGasPrice(),
		Gas:      GetGasLimit(common.HexToAddress(address), &contract, big.NewInt(0), data),
		To:       &contract,
		Value:    big.NewInt(0),
		Data:     data,
	})
	if err != nil {
		golog.Error("Send Contract Transaction Sign Tx ERROR:", err.Error())
		return nil
	}
	err = EthClient.SendTransaction(context.Background(), tx)
	if err != nil {
		golog.Error("Send Contract Transaction ERROR:", err.Error())
		return nil
	}
	nonceMap[address] = nonce + 1
	hash := tx.Hash().String()
	return &hash
}

func ToWei(value decimal.Decimal) *big.Int {
	ten := decimal.NewFromInt32(10)
	eth := ten.Pow(decimal.NewFromInt32(18))
	return value.Mul(eth).BigInt()
}

func FromWei(value *big.Int) decimal.Decimal {
	ten := decimal.NewFromInt32(10)
	eth := ten.Pow(decimal.NewFromInt32(18))
	return decimal.NewFromBigInt(value, 0).DivRound(eth, 18)
}

func main() {
	InitConfig(`{"http_proxy":"127.0.0.1:1081","chain_node":"https://data-seed-prebsc-1-s2.binance.org:8545", "chain_id":97}`)
	//var txHash = SendContractTransaction("8fe7cf7e92570b35383d70ea2a556acc8fe9b70932eabff339f7b071d970191e", "0x87aCb35917309017870eDdDDF76322FEf619bCaA", func() ([]byte, error) {
	//	data := GetMoldEncodeData(1, 0, false)
	//	fmt.Println(C.GoString(data))
	//	return hexutil.MustDecode(C.GoString(data)), nil
	//})
	//fmt.Println(*txHash)
	//fmt.Println(C.GoString(GetMoldLog("0xddaeba73ea945a34795c202df8d1a09191d0411c11e629d2d687ffdde8ddd0eb")))

	//var txHash = SendContractTransaction("8fe7cf7e92570b35383d70ea2a556acc8fe9b70932eabff339f7b071d970191e", "0xeB2861109BeFf328333aFAF7Ac674cDB6a79eb89", func() ([]byte, error) {
	//	data := GetBuyNodeEncodeData(1, "0xeB2861109BeFf328333aFAF7Ac674cDB6a79eb89", 0)
	//	fmt.Println(C.GoString(data))
	//	return hexutil.MustDecode(C.GoString(data)), nil
	//})
	//fmt.Println(*txHash)
	//fmt.Println(C.GoString(GetBuyNodeLog("0x713acdbe1163b4365f84a2efff27c5b70ceb0d0d5ccd170d62ffa7478556a14e")))
}
