package com.yzy.baselibrary.imageloader.cache

import com.bumptech.glide.load.Key

import java.security.MessageDigest

/**
 *description: Glide缓存的key.
 *@date 2019/7/15
 *@author: yzy.
 */
class DataCacheKey(val sourceKey: Key, private val signature: Key) : Key {

    override fun toString(): String {
        return "DataCacheKey{sourceKey=$sourceKey, signature=$signature}"
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        sourceKey.updateDiskCacheKey(messageDigest)
        signature.updateDiskCacheKey(messageDigest)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DataCacheKey

        if (sourceKey != other.sourceKey) return false
        if (signature != other.signature) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sourceKey.hashCode()
        result = 31 * result + signature.hashCode()
        return result
    }


}