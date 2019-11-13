package com.yzy.baselibrary.http

import okhttp3.internal.closeQuietly
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.util.*
import java.util.zip.*

class HttpZipHelper {
    companion object {
        /**
         * zlib decompress 2 String
         */
        fun decompressToStringForZlib(bytesToDecompress: ByteArray): String? {
            val bytesDecompressed = decompressForZlib(
                bytesToDecompress
            )
            var returnValue: String? = null
            bytesDecompressed?.let {
                try {
                    returnValue = String(
                        it,
                        0,
                        it.size,
                        charset("UTF-8")
                    )
                } catch (uee: UnsupportedEncodingException) {
                    uee.printStackTrace()
                }
            }

            return returnValue
        }

        /**
         * zlib decompress 2 byte
         */
        private fun decompressForZlib(bytesToDecompress: ByteArray): ByteArray? {
            var returnValues: ByteArray? = null
            val inflater = Inflater()
            val numberOfBytesToDecompress = bytesToDecompress.size
            inflater.setInput(
                bytesToDecompress,
                0,
                numberOfBytesToDecompress
            )
            var numberOfBytesDecompressedSoFar = 0
            val bytesDecompressedSoFar: MutableList<Byte> = ArrayList()
            try {
                while (!inflater.needsInput()) {
                    val bytesDecompressedBuffer = ByteArray(numberOfBytesToDecompress)
                    val numberOfBytesDecompressedThisTime =
                        inflater.inflate(bytesDecompressedBuffer)
                    numberOfBytesDecompressedSoFar += numberOfBytesDecompressedThisTime
                    for (b in 0 until numberOfBytesDecompressedThisTime) {
                        bytesDecompressedSoFar.add(bytesDecompressedBuffer[b])
                    }
                }
                returnValues = ByteArray(bytesDecompressedSoFar.size)

                for (b in returnValues.indices) {
                    returnValues[b] = bytesDecompressedSoFar[b]
                }
            } catch (dfe: DataFormatException) {
                dfe.printStackTrace()
            }
            inflater.end()
            return returnValues
        }

        /**
         * zlib compress 2 byte
         */
        fun compressForZlib(bytesToCompress: ByteArray?): ByteArray? {
            val deflater = Deflater()
            deflater.setInput(bytesToCompress)
            deflater.finish()
            val bytesCompressed = ByteArray(java.lang.Short.MAX_VALUE.toInt())
            val numberOfBytesAfterCompression = deflater.deflate(bytesCompressed)
            val returnValues = ByteArray(numberOfBytesAfterCompression)
            System.arraycopy(
                bytesCompressed,
                0,
                returnValues,
                0,
                numberOfBytesAfterCompression
            )
            return returnValues
        }

        /**
         * zlib compress 2 byte
         */
        fun compressForZlib(stringToCompress: String): ByteArray? {
            var returnValues: ByteArray? = null
            try {
                returnValues = compressForZlib(
                    stringToCompress.toByteArray(charset("UTF-8"))
                )
            } catch (uee: UnsupportedEncodingException) {
                uee.printStackTrace()
            }
            return returnValues
        }

        /**
         * gzip compress 2 byte
         */
        fun compressForGzip(string: String): ByteArray? {
            var os: ByteArrayOutputStream? = null
            var gos: GZIPOutputStream? = null
            try {
                os = ByteArrayOutputStream(string.length)
                gos = GZIPOutputStream(os)
                gos.write(string.toByteArray(charset("UTF-8")))
                return os.toByteArray()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                gos?.closeQuietly()
                os?.closeQuietly()
            }
            return null
        }

        /**
         * gzip decompress 2 string
         */
        fun decompressForGzip(compressed: ByteArray): String? {
            val BUFFER_SIZE = compressed.size
            var gis: GZIPInputStream? = null
            var `is`: ByteArrayInputStream? = null
            try {
                `is` = ByteArrayInputStream(compressed)
                gis = GZIPInputStream(`is`, BUFFER_SIZE)
                val string = StringBuilder()
                val data = ByteArray(BUFFER_SIZE)
                var bytesRead: Int
                while (gis.read(data).also { bytesRead = it } != -1) {
                    string.append(String(data, 0, bytesRead, charset("UTF-8")))
                }
                return string.toString()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                gis?.closeQuietly()
                `is`?.closeQuietly()
            }
            return null
        }
    }
}