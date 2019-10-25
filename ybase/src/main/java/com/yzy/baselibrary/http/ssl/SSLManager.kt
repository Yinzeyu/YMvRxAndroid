package com.yzy.baselibrary.http.ssl

import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.Arrays

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object SSLManager {
    fun createSSLSocketFactory(): SSLSocketFactory {
        var ssfFactory: SSLSocketFactory? = null
        try {
            //            SSLContext sslContext = SSLContext.getInstance("TLS");
            val sc = SSLContext.getInstance("SSL")
            sc.init(null, arrayOf<TrustManager>(createX509TrustManager()), SecureRandom())
            ssfFactory = sc.socketFactory
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ssfFactory!!
    }


    fun createX509TrustManager(): X509TrustManager {
        var x509TrustManager: X509TrustManager? = null
        try {
            val trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(null as KeyStore?)
            val trustManagers = trustManagerFactory.trustManagers
            if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
                throw IllegalStateException(
                    "Unexpected default trust managers:" + Arrays.toString(
                        trustManagers
                    )
                )
            }
            x509TrustManager = trustManagers[0] as X509TrustManager
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        }

        return x509TrustManager!!
    }

    val hostnameVerifier: HostnameVerifier get() = HostnameVerifier { _, _ -> true }
}
