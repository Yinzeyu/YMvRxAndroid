package com.yzy.example.imModel.emoji

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.*
import android.text.style.ClickableSpan
import android.text.style.ImageSpan
import android.view.View
import android.widget.EditText
import android.widget.TextView
import java.util.ArrayList
import java.util.regex.Pattern

class MoonUtils {

    companion object {
        private const val DEF_SCALE = 0.6f
        private const val SMALL_SCALE = 0.6f

        /**
         * 具体类型的view设置内容
         *
         * @param textView
         * @param mSpannableString
         */
        private fun viewSetText(textView: View, mSpannableString: SpannableString) {
            if (textView is TextView) {
                textView.text = mSpannableString
            } else if (textView is EditText) {
                textView.setText(mSpannableString)
            }
        }

        private fun replaceEmoticons(
            context: Context,
            value: String,
            scale: Float,
            align: Int
        ): SpannableString {
            var value = value
            if (TextUtils.isEmpty(value)) {
                value = ""
            }

            val chars = value.toCharArray()
            val ssb = SpannableStringBuilder(value)

            var codePoint: Int
            var isSurrogatePair: Boolean
            for (i in chars.indices) {
                if (Character.isHighSurrogate(chars[i])) {
                    continue
                } else if (Character.isLowSurrogate(chars[i])) {
                    if (i > 0 && Character.isSurrogatePair(chars[i - 1], chars[i])) {
                        codePoint = Character.toCodePoint(chars[i - 1], chars[i])
                        isSurrogatePair = true
                    } else {
                        continue
                    }
                } else {
                    codePoint = chars[i].toInt()
                    isSurrogatePair = false
                }
                if (EmojiManager.contains(codePoint)) {
                    val d = getEmotDrawable(context, codePoint, scale)
                    if (d != null) {
                        val span = ImageSpan(d, align)
                        ssb.setSpan(
                            span,
                            if (isSurrogatePair) i - 1 else i,
                            i + 1,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            }

            return SpannableString.valueOf(ssb)
        }

        private val mATagPattern = Pattern.compile("<a.*?>.*?</a>")

        fun makeSpannableStringTags(
            context: Context,
            value: String,
            scale: Float,
            align: Int
        ): SpannableString {
            return makeSpannableStringTags(context, value, DEF_SCALE, align, true)
        }

        fun makeSpannableStringTags(
            context: Context,
            value: String,
            scale: Float,
            align: Int,
            bTagClickable: Boolean
        ): SpannableString {
            var value = value
            val tagSpans = ArrayList<ATagSpan>()
            if (TextUtils.isEmpty(value)) {
                value = ""
            }
            //a标签需要替换原始文本,放在moonutil类中
            var aTagMatcher = mATagPattern.matcher(value)

            var start = 0
            var end = 0
            while (aTagMatcher.find()) {
                start = aTagMatcher.start()
                end = aTagMatcher.end()
                val atagString = value.substring(start, end)
                val tagSpan = getTagSpan(atagString)
                value = value.substring(0, start) + tagSpan.tag + value.substring(end)
                tagSpan.setRange(start, start + (tagSpan.tag?.length ?: 0))
                tagSpans.add(tagSpan)
                aTagMatcher = mATagPattern.matcher(value)
            }


            val chars = value.toCharArray()
            val ssb = SpannableStringBuilder(value)

            var codePoint: Int
            var isSurrogatePair: Boolean
            for (i in chars.indices) {
                if (Character.isHighSurrogate(chars[i])) {
                    continue
                } else if (Character.isLowSurrogate(chars[i])) {
                    if (i > 0 && Character.isSurrogatePair(chars[i - 1], chars[i])) {
                        codePoint = Character.toCodePoint(chars[i - 1], chars[i])
                        isSurrogatePair = true
                    } else {
                        continue
                    }
                } else {
                    codePoint = chars[i].toInt()
                    isSurrogatePair = false
                }

                if (EmojiManager.contains(codePoint)) {
                    val d = getEmotDrawable(context, codePoint, scale)
                    if (d != null) {
                        val span = ImageSpan(d, align)
                        ssb.setSpan(
                            span,
                            if (isSurrogatePair) i - 1 else i,
                            i + 1,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            }

            return SpannableString.valueOf(ssb)
        }

        /**
         * 识别表情
         */
        fun identifyFaceExpression(
            context: Context,
            textView: View, value: String, align: Int
        ) {
            identifyFaceExpression(context, textView, value, align, DEF_SCALE)
        }

        /**
         * 识别表情和标签（如：只需显示a标签对应的文本）
         */
        fun identifyFaceExpressionAndATags(
            context: Context,
            textView: View, value: String, align: Int
        ) {
            val mSpannableString = makeSpannableStringTags(context, value, DEF_SCALE, align)
            viewSetText(textView, mSpannableString)
        }

        /**
         * 识别表情，可设置缩放大小
         */
        fun identifyFaceExpression(
            context: Context,
            textView: View, value: String, align: Int, scale: Float
        ) {
            val mSpannableString = replaceEmoticons(context, value, scale, align)
            viewSetText(textView, mSpannableString)
        }

        /**
         * 识别表情和标签（如：只需显示a标签对应的文本），可设置缩放大小
         */
        fun identifyFaceExpressionAndTags(
            context: Context,
            textView: View, value: String, align: Int, scale: Float
        ) {
            val mSpannableString = makeSpannableStringTags(context, value, scale, align, false)
            viewSetText(textView, mSpannableString)
        }

        /**
         * EditText用来转换表情文字的方法，如果没有使用EmoticonPickerView的attachEditText方法，则需要开发人员手动调用方法来又识别EditText中的表情
         */
        fun replaceEmoticons(context: Context, editable: Editable?, start: Int, count: Int) {
            if (count <= 0 || editable?.length ?: 0 < start + count)
                return

            val s = editable?.subSequence(start, start + count)

            val chars = s.toString().toCharArray()

            var codePoint: Int
            var isSurrogatePair: Boolean
            for (i in chars.indices) {
                if (Character.isHighSurrogate(chars[i])) {
                    continue
                } else if (Character.isLowSurrogate(chars[i])) {
                    if (i > 0 && Character.isSurrogatePair(chars[i - 1], chars[i])) {
                        codePoint = Character.toCodePoint(chars[i - 1], chars[i])
                        isSurrogatePair = true
                    } else {
                        continue
                    }
                } else {
                    codePoint = chars[i].toInt()
                    isSurrogatePair = false
                }

                if (EmojiManager.contains(codePoint)) {
                    val d = getEmotDrawable(context, codePoint, SMALL_SCALE)
                    if (d != null) {
                        val span = ImageSpan(d, ImageSpan.ALIGN_BOTTOM)
                        editable?.setSpan(
                            span,
                            if (isSurrogatePair) i - 1 else i,
                            i + 1,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            }
        }

        private fun getEmotDrawable(context: Context, code: Int, scale: Float): Drawable? {
            val drawable = EmojiManager.getDrawable(context, code)

            // scale
            if (drawable != null) {
                val width = (drawable!!.getIntrinsicWidth() * scale).toInt()
                val height = (drawable!!.getIntrinsicHeight() * scale).toInt()
                drawable!!.setBounds(0, 0, width, height)
            }

            return drawable
        }

        private fun getTagSpan(text: String): ATagSpan {
            var href: String? = null
            var tag: String? = null
            if (text.toLowerCase().contains("href")) {
                val start = text.indexOf("\"")
                val end = text.indexOf("\"", start + 1)
                if (end > start)
                    href = text.substring(start + 1, end)
            }
            val start = text.indexOf(">")
            val end = text.indexOf("<", start)
            if (end > start)
                tag = text.substring(start + 1, end)
            return ATagSpan(tag, href)
        }

        private class ATagSpan internal constructor(val tag: String?, private var mUrl: String?) :
            ClickableSpan() {
            private var start: Int = 0
            private var end: Int = 0

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }

            fun setRange(start: Int, end: Int) {
                this.start = start
                this.end = end
            }

            override fun onClick(widget: View) {
                try {
                    if (TextUtils.isEmpty(mUrl))
                        return
                    val uri = Uri.parse(mUrl)
                    val scheme = uri.scheme
                    if (TextUtils.isEmpty(scheme)) {
                        mUrl = "http://" + mUrl!!
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

}