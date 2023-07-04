package com.service.boot.common

import org.springframework.util.StringUtils
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

object NumberUtils {

    @Suppress("UNCHECKED_CAST")
    fun <T : Number> parseNumber(text: String?, targetClass: Class<T>): T? {
        if (text == null) return null
        if (text.trim().isEmpty()) return null
        try {
            val trimmed = StringUtils.trimAllWhitespace(text)
            return if (Byte::class.java == targetClass || java.lang.Byte.TYPE == targetClass) {
                (if (isHexNumber(trimmed)) java.lang.Byte.decode(trimmed) else java.lang.Byte.valueOf(trimmed)) as T
            } else if (Short::class.java == targetClass || java.lang.Short.TYPE == targetClass) {
                (if (isHexNumber(trimmed)) java.lang.Short.decode(trimmed) else java.lang.Short.valueOf(trimmed)) as T
            } else if (Int::class.java == targetClass || java.lang.Integer::class.java == targetClass || java.lang.Integer.TYPE == targetClass) {
                (if (isHexNumber(trimmed)) Integer.decode(trimmed) else Integer.valueOf(trimmed)) as T
            } else if (Long::class.java == targetClass || java.lang.Long::class.java == targetClass || java.lang.Long.TYPE == targetClass) {
                (if (isHexNumber(trimmed)) java.lang.Long.decode(trimmed) else java.lang.Long.valueOf(trimmed)) as T
            } else if (BigInteger::class.java == targetClass) {
                (if (isHexNumber(trimmed)) decodeBigInteger(trimmed) else BigInteger(trimmed)) as T
            } else if (Float::class.java == targetClass || java.lang.Float::class.java == targetClass || java.lang.Float.TYPE == targetClass) {
                java.lang.Float.valueOf(trimmed) as T
            } else if (Double::class.java == targetClass || java.lang.Double::class.java == targetClass || java.lang.Double.TYPE == targetClass) {
                java.lang.Double.valueOf(trimmed) as T
            } else if (BigDecimal::class.java == targetClass || Number::class.java == targetClass) {
                BigDecimal(trimmed) as T
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun isHexNumber(value: String): Boolean {
        val index = if (value.startsWith("-")) 1 else 0
        return value.startsWith("0x", index) || value.startsWith("0X", index) || value.startsWith("#", index)
    }

    fun decodeBigInteger(value: String): BigInteger {
        var radix = 10
        var index = 0
        var negative = false
        if (value.startsWith("-")) {
            negative = true
            index++
        }
        if (value.startsWith("0x", index) || value.startsWith("0X", index)) {
            index += 2
            radix = 16
        } else if (value.startsWith("#", index)) {
            index++
            radix = 16
        } else if (value.startsWith("0", index) && value.length > 1 + index) {
            index++
            radix = 8
        }
        val result = BigInteger(value.substring(index), radix)
        return if (negative) result.negate() else result
    }

    fun BigDecimal.加(augend: BigDecimal, scale: Int? = null, roundingMode: RoundingMode = RoundingMode.HALF_UP): BigDecimal {
        var result = this.add(augend)
        if (scale != null) {
            result = result.setScale(scale, roundingMode)
        }
        return result
    }

    fun BigDecimal.减(subtrahend: BigDecimal, scale: Int? = null, roundingMode: RoundingMode = RoundingMode.HALF_UP): BigDecimal {
        var result = this.subtract(subtrahend)
        if (scale != null) {
            result = result.setScale(scale, roundingMode)
        }
        return result
    }

    fun BigDecimal.乘(multiplicand: BigDecimal, scale: Int? = null, roundingMode: RoundingMode = RoundingMode.HALF_UP): BigDecimal {
        var result = this.multiply(multiplicand)
        if (scale != null) {
            result = result.setScale(scale, roundingMode)
        }
        return result
    }

    fun BigDecimal.除(divisor: BigDecimal, scale: Int? = null, roundingMode: RoundingMode = RoundingMode.HALF_UP): BigDecimal {
        if (scale != null) {
            return this.divide(divisor, scale, roundingMode)
        }
        return this.divide(divisor)
    }

    fun BigDecimal.大于(other: BigDecimal): Boolean {
        return this.compareTo(other) > 0
    }

    fun BigDecimal.大于等于(other: BigDecimal): Boolean {
        return this.compareTo(other) >= 0
    }

    fun BigDecimal.小于(other: BigDecimal): Boolean {
        return this.compareTo(other) < 0
    }

    fun BigDecimal.小于等于(other: BigDecimal): Boolean {
        return this.compareTo(other) <= 0
    }
}
