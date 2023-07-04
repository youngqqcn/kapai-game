package com.service.kapai

import java.math.BigDecimal

const val BLACK_HOLE_ADDRESS_1 = "0x0000000000000000000000000000000000000111"
const val BLACK_HOLE_ADDRESS_2 = "0x0000000000000000000000000000000000000222"

var TOKEN_A_PRICE = BigDecimal.ZERO
var TOKEN_B_PRICE = BigDecimal.ZERO

var DESTROY_VALUE = BigDecimal.valueOf(10000)

const val REDIS_KEY_ORDER = "order"
const val REDIS_KEY_USER = "user:"
const val REDIS_KEY_USER_CARD = "user-card:"
const val REDIS_KEY_USER_CHILD_1 = "user-child-1:"
const val REDIS_KEY_USER_CHILD_ALL = "user-child-all:"
const val REDIS_KEY_TOTAL_B_POWER = "total_b_power"

const val REDIS_KEY_TOKEN_A_TODAY_DESTROY = "token_a_today_destroy"
const val REDIS_KEY_TOKEN_A_CIRCULATING = "token_a_circulating"
const val REDIS_KEY_TOKEN_B_TODAY_DESTROY = "token_b_today_destroy"
const val REDIS_KEY_TOKEN_B_CIRCULATING = "token_b_circulating"
const val REDIS_KEY_TOKEN_A_STATIC_POWER = "token_a_static_power"
const val REDIS_KEY_TOKEN_A_DYNAMIC_POWER = "token_a_dynamic_power"

fun Long.REDIS_KEY_USER(): String {
    return REDIS_KEY_USER + this
}

fun Long.REDIS_KEY_USER_CHILD_1(): String {
    return REDIS_KEY_USER_CHILD_1 + this
}

fun Long.REDIS_KEY_USER_CHILD_ALL(): String {
    return REDIS_KEY_USER_CHILD_ALL + this
}

fun Long.REDIS_KEY_USER_CARD(): String {
    return REDIS_KEY_USER_CARD + this
}

object ApiResponseExamples {
    const val default: String = """
{
  "code": "int - 状态码或者错误码",
  "message": "错误提示或者其他说明",
  "data": "data - 可能是数组，也可能是对象"
}
    """

    const val auth_signMessage = """
{
  "code": "int - 状态码或者错误码",
  "message": "错误提示或者其他说明",
  "data": "需要签名的信息"
}
    """

    const val token_price = """
{
  "code": "int - 状态码或者错误码",
  "message": "错误提示或者其他说明",
  "data": {
    "tokenA":"string - A代币价格",
    "tokenB":"string - B代币价格"
  }
}
    """

    const val power_info = """
{
  "code": "int - 状态码或者错误码",
  "message": "错误提示或者其他说明",
  "data": {
    "todayDestroy":"string - 今日销毁",
    "circulating":"string - 全网流通",
    "staticPower":"string - 全网静态",
    "dynamicPower":"string - 全网动态",
    "totalPowerB":"string - B代币全网动态"
  }
}
    """

    const val api_auth_sign = """
{
  "code": "int - 状态码或者错误码",
  "message": "错误提示或者其他说明",
  "data": {
    "id":"int - 用户ID",
    "wallet":"string - 用户钱包",
    "isBind":"boolean - 是否绑定推荐人"
  }
}
    """

    const val card_mold_data = """
{
  "code": "int - 状态码或者错误码",
  "message": "错误提示或者其他说明",
  "data": {
    "orderId": "int - 订单ID",
    "chainId": "int - 公链ID",
    "wallet": "string - 需要使用的钱包地址",
    "contract": "string - 合约地址",
    "data": "string - 需要签名的交易"
  }
}
    """

    const val card_mold_send = """
{
  "code": "int - 状态码或者错误码",
  "message": "错误提示或者其他说明",
  "data": "string - 链上交易hash"
}
    """

    const val wallet_cards = """
{
  "code": "int - 状态码或者错误码",
  "message": "错误提示或者其他说明",
  "data": [
    {
      "id": "int - 卡牌ID",
      "name": "string - 卡牌名称",
      "unitPrice": "string - 卡牌单价",
      "power": "string - 卡牌算力",
      "output": "string - 卡牌产出",
      "walletId": "string - 钱包ID",
      "myPower": "string - 我的算力",
      "days": "int - 产出天数",
      "status": "int - 0:待铸造, 1:生产中, -1:已过期",
      "myOutput": "string - 我的产出",
      "myPowerB": "string - B算力",
      "quantity": "string - 我的购买数量"
    }
  ]
}
    """

    const val wallet_withdraw = """
{
  "code": "int - 状态码或者错误码",
  "message": "错误提示或者其他说明",
  "data": "string - 链上交易hash"
}
    """

    const val wallet_info = """
{
  "code": "int - 状态码或者错误码",
  "message": "错误提示或者其他说明",
  "data": [
    {
      "wallet": "string - 钱包地址",
      "superior": "string - 推荐人钱包",
      "ztPower": "long - 直推算力",
      "ep": "string - EP数量",
      "tokenA": "string - tokenA数量",
      "tokenB": "string - tokenB数量",
      "level": "int - 团队等级",
      "power": "int - 算力",
      "smallPower": "int - 小区算力",
      "bigPower": "int - 大区算力",
      "dynamicPower": "string - 动态算力"
    }
  ]
}
    """

    const val wallet_fans = """
{
  "code": "int - 状态码或者错误码",
  "message": "错误提示或者其他说明",
  "data": [
    {
      "wallet": "string - 钱包地址",
      "power": "int - 算力"
    }
  ]
}
    """

    const val wallet_logs = """
{
  "code": "int - 状态码或者错误码",
  "message": "错误提示或者其他说明",
  "data": [
    {
      "type": "int - 类型",
      "linkId": "int - 关联ID",
      "amount": "string - 变更数量",
      "source": "int - 来源",
      "extra": "string - 附带信息",
      "createTime": "string - 创建时间"
    }
  ]
}
    """

    const val card_list = """
{
  "code": "int - 状态码或者错误码",
  "message": "错误提示或者其他说明",
  "data": [
    {
      "id": "int",
      "name": "string - 名称",
      "unitPrice": "int - 价格",
      "power": "int - 算力",
      "output": "int - 产出",
      "ep": "int - EP数量",
      "ztPower": "int - 直推算力"
    }
  ]
}
    """

    const val node_buy_data = """
{
  "code": "int - 状态码或者错误码",
  "message": "错误提示或者其他说明",
  "data": {
    "orderId": "int - 订单ID",
    "chainId": "int - 公链ID",
    "wallet": "string - 需要使用的钱包地址",
    "contract": "string - 合约地址",
    "usdt": "string - USDT合约地址",
    "data": "string - 需要签名的交易"
  }
}
    """

    const val node_list = """
{
  "code": "int - 状态码或者错误码",
  "message": "错误提示或者其他说明",
  "data": [
    {
      "node": "int - 节点ID",
      "name": "string - 名称",
      "price": "int - 价格"
    }
  ]
}
    """

    const val node_order_list = """
{
  "code": "int - 状态码或者错误码",
  "message": "错误提示或者其他说明",
  "data": [
    {
      "node": "int - 节点ID",
      "name": "string - 名称",
      "price": "int - 价格",
      "status": "string - 状态 -1失败 0未完成 1已完成",
      "txHash": "string - 交易hash",
      "createTime": "string - 时间"
    }
  ]
}
    """
}

object ApiRequestExamples {
    const val api_auth_sign = """
{
  "wallet": "string - 钱包地址",
  "message": "string - 原始签名内容",
  "sign": "string - 已签名的信息"
}
    """

    const val card_mold_data = """
{
  "cardId": "int - 卡牌ID",
  "type": "int - 1:使用EP, 默认不实用",
  "quantity": "int - 数量"
}
    """

    const val card_mold_send = """
{
  "orderId": "int - 订单ID",
  "hash": "string - 签名的交易数据"
}
    """

    const val wallet_bind = """
{
  "wallet": "string - 绑定的钱包地址"
}
    """

    const val wallet_withdraw = """
{
  "token": "int - 1:A, 2:B"
}
    """

    const val node_buy_node = """
{
  "node": "int - 节点ID"
}
    """

    const val node_send = """
{
    "orderId": "int - 订单ID",
    "hash": "string - 签名的交易数据"
}
    """

    const val admin_swap_ep = """
{
    "data": "加密内容：{"orderId": "string - 订单ID", "wallet": "string - 钱包地址", "amount": "string - 数量"}"
}
    """
}