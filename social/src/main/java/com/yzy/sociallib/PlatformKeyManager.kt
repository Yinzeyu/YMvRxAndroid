package com.yzy.sociallib

import com.tencent.mm.opensdk.openapi.IWXAPI

class PlatformKeyManager private constructor() {
    var wxAPI: IWXAPI? = null

    companion object {
        val instance: PlatformKeyManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            PlatformKeyManager()
        }
    }
}