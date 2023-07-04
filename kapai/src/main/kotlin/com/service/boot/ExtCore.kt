package com.service.boot

import java.math.BigDecimal
import java.math.RoundingMode

fun BigDecimal.number(newScale: Int? = null, stripTrailingZeros: Boolean? = true): String {
    var number = this
    if (newScale != null) {
        number = this.setScale(newScale, RoundingMode.HALF_UP)
    }
    return if (stripTrailingZeros == true) number.stripTrailingZeros().toPlainString() else number.toPlainString()
}
