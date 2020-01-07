package com.yzy.example.repository.bean

class BaseDataBean<T>(
    var hasMore: Boolean = false,
    var bean:T,
    var exception: Exception? = null


)