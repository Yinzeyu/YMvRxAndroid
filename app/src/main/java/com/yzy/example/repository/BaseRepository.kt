package com.yzy.example.repository

import com.yzy.baselibrary.http.RetrofitAPi
import com.yzy.example.http.response.ApiException
import com.yzy.example.http.response.BaseResponse
import com.yzy.example.http.response.EmptyException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import java.io.IOException
import com.yzy.example.repository.bean.DataResult
import com.yzy.example.repository.bean.GankBaseBean

/**
 * Created by luyao
 * on 2019/4/10 9:41
 */
open class BaseRepository {
   val  mAPi= RetrofitAPi.instance
}