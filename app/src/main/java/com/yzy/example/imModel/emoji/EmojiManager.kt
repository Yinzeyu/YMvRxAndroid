package com.yzy.example.imModel.emoji

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.LruCache
import android.util.Xml
import com.blankj.utilcode.util.Utils
import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import java.io.IOException
import java.io.InputStream
import java.util.ArrayList
import java.util.HashMap
import java.util.regex.Pattern

object EmojiManager {
    private const val EMOJI_DIR = "emoji/"
    private const val CACHE_MAX_SIZE = 1024

    private var mPattern: Pattern? = null

    private val mDefaultEntries = ArrayList<Entry>()
    private val mText2Entry = HashMap<Int, Entry>()
    private var mDrawableCache: LruCache<String, Bitmap>? = null

    init {
        val context = Utils.getApp()

        load(context, EMOJI_DIR + "emoji.xml")

        mPattern = makePattern()

        mDrawableCache = object : LruCache<String, Bitmap>(CACHE_MAX_SIZE) {
            override fun entryRemoved(evicted: Boolean, key: String, oldValue: Bitmap, newValue: Bitmap) {
                if (oldValue != newValue)
                    oldValue.recycle()
            }
        }
    }

    fun getDisplayCount(): Int {
        return mDefaultEntries.size
    }

    fun getDisplayDrawable(context: Context, index: Int): Drawable? {
        val text = if (index >= 0 && index < mDefaultEntries.size) mDefaultEntries[index].text else null
        return if (TextUtils.isEmpty(text)) null else getDrawable(context, Integer.decode(text!!))
    }

    fun getDisplayText(index: Int): String? {
        return if (index >= 0 && index < mDefaultEntries.size) mDefaultEntries[index].text else null
    }

    fun getDrawable(context: Context, code: Int): Drawable? {
        val entry = mText2Entry[code]
        return when {
            entry == null -> null
            TextUtils.isEmpty(entry?.text) -> null
            else -> {
                var cache: Bitmap? = mDrawableCache?.get(entry?.assetPath)
                if (cache == null) {
                    cache = loadAssetBitmap(context, entry?.assetPath)
                }
                BitmapDrawable(context.resources, cache)
            }
        }
    }

    private fun loadAssetBitmap(context: Context, assetPath: String): Bitmap? {
        var inputStream: InputStream? = null
        try {
            val resources = context.resources
            val options = BitmapFactory.Options()
            options.inDensity = DisplayMetrics.DENSITY_HIGH
            options.inScreenDensity = resources.displayMetrics.densityDpi
            options.inTargetDensity = resources.displayMetrics.densityDpi
            inputStream = context.assets.open(assetPath)
            val bitmap = BitmapFactory.decodeStream(inputStream, Rect(), options)
            bitmap?.let {
                mDrawableCache?.put(assetPath, it)
            }
            return bitmap
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            inputStream?.apply {
                try {
                    close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return null
    }

    fun getPattern(): Pattern? {
        return mPattern
    }

    operator fun contains(code: Int): Boolean {
        return mText2Entry.containsKey(code)
    }

    private fun load(context: Context, xmlPath: String) {
        EntryLoader().load(context, xmlPath)
        //补充最后一页少的表情
        val tmp = mDefaultEntries.size % EmotionConstants.EMOJI_PER_PAGE
        if (tmp != 0) {
            val tmp2 =
                EmotionConstants.EMOJI_PER_PAGE - (mDefaultEntries.size - mDefaultEntries.size / EmotionConstants.EMOJI_PER_PAGE * EmotionConstants.EMOJI_PER_PAGE)
            for (i in 0 until tmp2) {
                mDefaultEntries.add(Entry("", ""))
            }
        }
    }

    private fun makePattern(): Pattern {
        return Pattern.compile(patternOfDefault())
    }

    private fun patternOfDefault(): String {
        return "\\[[^\\[]{1,10}\\]"
    }

    private class Entry(internal var text: String, internal var assetPath: String)

    private class EntryLoader : DefaultHandler() {
        private var catalog = ""

        internal fun load(context: Context, assetPath: String) {
            var inputStream: InputStream? = null
            try {
                inputStream = context.assets.open(assetPath)
                Xml.parse(inputStream, Xml.Encoding.UTF_8, this)
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: SAXException) {
                e.printStackTrace()
            } finally {
                inputStream?.apply {
                    try {
                        close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }

        @Throws(SAXException::class)
        override fun startElement(
            uri: String,
            localName: String,
            qName: String,
            attributes: Attributes
        ) {
            if (localName == "Catalog") {
                catalog = attributes.getValue(uri, "Title")
            } else if (localName == "Emoticon") {
                val tag = attributes.getValue(uri, "Tag")
                val fileName = attributes.getValue(uri, "File")
                val entry = Entry(tag, "$EMOJI_DIR$catalog/$fileName")
                mText2Entry[Integer.decode(tag)] = entry
                if (catalog == "default") {
                    mDefaultEntries.add(entry)
                }
            }
        }
    }
}