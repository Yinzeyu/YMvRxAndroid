package com.yzy.example.imModel.emoji

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import com.yzy.example.R

class EmotionTab @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defAttrStyle: Int = 0
) : RelativeLayout(context, attributeSet, defAttrStyle) {

    private var mIvIcon: ImageView

    private var iconSrc: Int = R.drawable.imui_ic_tab_emoji
    private var stickerCoverImgPath: String? = null

    constructor(context: Context, iconSrc: Int) : this(context) {
        this.iconSrc = iconSrc
        init()
    }

    constructor(context: Context, stickerCoverImgPath: String) : this(context) {
        this.stickerCoverImgPath = stickerCoverImgPath
        init()
    }

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.imui_layout_emotion_tab, this)
        mIvIcon = findViewById(R.id.ivIcon)
    }

    private fun init() {
        if (TextUtils.isEmpty(stickerCoverImgPath)) {
            mIvIcon.setImageResource(iconSrc)
        } else {
            stickerCoverImgPath?.let {
                ImUiManager.getImageLoader()?.displayImage(context, it, mIvIcon)
            }
        }
    }
}