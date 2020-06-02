package com.yzy.baselibrary.http

class ResponseThrowable : Exception {
    var code: Int
    var errMsg: String

    constructor(error: ERROR, e: Throwable? = null) : super(e) {
        code = error.getKey()
        errMsg = error.getValue()
    }
    constructor(code: Int, msg: String) : super(msg) {
        this.code = code
        this.errMsg = msg
    }
}

