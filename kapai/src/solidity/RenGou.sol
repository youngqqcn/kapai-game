// SPDX-License-Identifier: MIT
pragma solidity >0.4.0 <= 0.9.0;

interface IBEP20 {
    function balanceOf(address account) external view returns (uint256);

    function allowance(address _owner, address spender) external view returns (uint256);

    function transfer(address recipient, uint256 amount) external returns (bool);

    function transferFrom(address sender, address recipient, uint256 amount) external returns (bool);
}

library SafeMath {
    function add(uint256 a, uint256 b) internal pure returns (uint256) {
        uint256 c = a + b;
        require(c >= a, "SafeMath: addition overflow");

        return c;
    }

    function sub(uint256 a, uint256 b) internal pure returns (uint256) {
        return sub(a, b, "SafeMath: subtraction overflow");
    }

    function sub(uint256 a, uint256 b, string memory errorMessage) internal pure returns (uint256) {
        require(b <= a, errorMessage);
        uint256 c = a - b;

        return c;
    }

    function mul(uint256 a, uint256 b) internal pure returns (uint256) {
        // Gas optimization: this is cheaper than requiring 'a' not being zero, but the
        // benefit is lost if 'b' is also tested.
        // See: https://github.com/OpenZeppelin/openzeppelin-contracts/pull/522
        if (a == 0) {
            return 0;
        }

        uint256 c = a * b;
        require(c / a == b, "SafeMath: multiplication overflow");

        return c;
    }

    function div(uint256 a, uint256 b) internal pure returns (uint256) {
        return div(a, b, "SafeMath: division by zero");
    }

    function div(uint256 a, uint256 b, string memory errorMessage) internal pure returns (uint256) {
        // Solidity only automatically asserts when dividing by 0
        require(b > 0, errorMessage);
        uint256 c = a / b;
        // assert(a == b * c + a % b); // There is no case in which this doesn't hold

        return c;
    }

    function mod(uint256 a, uint256 b) internal pure returns (uint256) {
        return mod(a, b, "SafeMath: modulo by zero");
    }

    function mod(uint256 a, uint256 b, string memory errorMessage) internal pure returns (uint256) {
        require(b != 0, errorMessage);
        return a % b;
    }
}

contract RenGou {
    using SafeMath for uint256;
    struct Node {
        uint price;
        uint count;
        uint ep;
        uint tokenA;
        uint buyCount;
    }

    mapping(address => mapping(uint => mapping(uint => uint))) private buyAddress;
    mapping(address => bool) private hasBuyNode;
    //test net: 0x3966aeb2c687ea5f64440ea494e6aa7690fac518
    IBEP20 private USDT = IBEP20(0x55d398326f99059fF775485246999027B3197955);
    IBEP20 private EP = IBEP20(IOT代币地址);
    IBEP20 private TOKEN_A = IBEP20(ART代币地址);
    uint private decimals = 10 ** 18;
    mapping(uint => Node[]) private nodes;

    constructor() {
        nodes[1].push(Node(500 * decimals, 631, 1500, 2000 * decimals, 0));
        nodes[1].push(Node(750 * decimals, 631, 2250, 2000 * decimals, 0));
        nodes[1].push(Node(1025 * decimals, 631, 3075, 2000 * decimals, 0));

        nodes[2].push(Node(1500 * decimals, 231, 6000, 9000 * decimals, 0));
        nodes[2].push(Node(2250 * decimals, 231, 9000, 9000 * decimals, 0));
        nodes[2].push(Node(3375 * decimals, 231, 13500, 9000 * decimals, 0));

        nodes[3].push(Node(5000 * decimals, 31, 25000, 40000 * decimals, 0));
        nodes[3].push(Node(7500 * decimals, 31, 37500, 40000 * decimals, 0));
        nodes[3].push(Node(10250 * decimals, 31, 51250, 40000 * decimals, 0));
    }

    function buy(uint nodeLevel, address recommender, uint no) public {
        require(msg.sender != recommender);
        require(nodeLevel > 0);
        require(nodeLevel < 4);
        for (uint i = 0; i < nodes[nodeLevel].length; i++) {
            if (nodes[nodeLevel][i].buyCount >= nodes[nodeLevel][i].count) {
                continue;
            }
            require(nodes[nodeLevel][i].buyCount < nodes[nodeLevel][i].count, "sold out");
            require(USDT.allowance(msg.sender, address(this)) >= nodes[nodeLevel][i].price, "transfer amount exceeds allowance");
            require(USDT.balanceOf(msg.sender) >= nodes[nodeLevel][i].price, "Insufficient balance");
            if (hasBuyNode[recommender]) {
                USDT.transferFrom(msg.sender, recommender, nodes[nodeLevel][i].price.div(10).mul(2)); //直推20%
            } else {
                recommender = address(0);
                USDT.transferFrom(msg.sender, 0x19C37Bf339fb205bD1A189c66cF1859F44bCD79d, nodes[nodeLevel][i].price.div(10).mul(2)); //直推20%
            }
            USDT.transferFrom(msg.sender, 0xF722F398dE2271DAAb3C7Dc77b6D99Ba53a97A9d, nodes[nodeLevel][i].price.div(10).mul(1)); //社区10%
            USDT.transferFrom(msg.sender, 0x7a6cAEB5F2681208bd994B242aBf257cB6d12869, nodes[nodeLevel][i].price.div(10).mul(2)); //运维20%
            USDT.transferFrom(msg.sender, 0xD4d397991c72a02c86E9D4a312D8771B9CBd2919, nodes[nodeLevel][i].price.div(10).mul(5)); //50%底池
            EP.transfer(msg.sender, nodes[nodeLevel][i].ep * decimals);
            TOKEN_A.transfer(0xcFF7283f3126fe907aA0514Ad36659C7fF5DCe86, nodes[nodeLevel][i].tokenA); //转一笔锁仓ART到分币钱包
            nodes[nodeLevel][i].buyCount = nodes[nodeLevel][i].buyCount.add(1);
            buyAddress[msg.sender][nodeLevel][i + 1] = buyAddress[msg.sender][nodeLevel][i + 1].add(1);
            hasBuyNode[msg.sender] = true;
            emit BuyNode(msg.sender, recommender, nodeLevel, i + 1, nodes[nodeLevel][i].price, nodes[nodeLevel][i].ep, no);
            return;
        }
        revert("sold out");
    }

    function getBuyAmount(address account, uint nodeLevel, uint period) public view returns (uint) {
        return buyAddress[account][nodeLevel][period];
    }

    function getBuyNode(uint nodeLevel) public view returns (uint, uint, uint, uint, uint) {
        Node[] memory array = nodes[nodeLevel];
        for (uint i = 0; i < array.length; i++) {
            Node memory node = array[i];
            if (node.buyCount >= node.count) {
                continue;
            }
            return (node.price, i + 1, node.count, node.buyCount, node.ep);
        }
        return (0, 0, 0, 0, 0);
    }

    function isSoldOut(uint nodeLevel) public view returns (bool) {
        Node[] memory array = nodes[nodeLevel];
        for (uint i = 0; i < array.length; i++) {
            Node memory node = array[i];
            if (node.buyCount >= node.count) {
                continue;
            }
            return false;
        }
        return true;
    }

    event BuyNode(address indexed account, address indexed recommender, uint node, uint period, uint price, uint ep, uint no);
}