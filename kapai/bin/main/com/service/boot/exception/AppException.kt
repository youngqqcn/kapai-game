package com.service.boot.exception

open class AppException : RuntimeException {
    var code: Int = 500

    constructor(code: Int, message: String) : super(message) {
        this.code = code
    }

    constructor(message: String) : this(500, message)
}