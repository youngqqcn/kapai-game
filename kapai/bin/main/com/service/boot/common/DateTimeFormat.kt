package com.service.boot.common

import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

object DateTimeFormat {

    fun formatDate(date: LocalDate): String {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    fun formatDateTime(date: Date): String {
        return formatDateTime("yyyy-MM-dd HH:mm:ss", date)
    }

    fun formatDateTime(pattern: String, date: Date): String {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern(pattern))
    }
}
