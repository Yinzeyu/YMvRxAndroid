package com.yzy.example.component.comm.bindadapter

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.api.load
import coil.transform.CircleCropTransformation
import com.yzy.example.R
import com.yzy.example.extention.loadUrl

/**
 * 描述　: 自定义 BindingAdapter
 */
object CustomBindAdapter {

    @BindingAdapter(value = ["checkChange"])
    @JvmStatic
    fun checkChange(checkbox: CheckBox, listener: CompoundButton.OnCheckedChangeListener) {
        checkbox.setOnCheckedChangeListener(listener)
    }

    @BindingAdapter(value = ["showPwd"])
    @JvmStatic
    fun showPwd(view: EditText, boolean: Boolean) {
        if (boolean) {
            view.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            view.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        view.setSelection(view.text.toString().length)
    }

    @BindingAdapter(value = ["imageUrl"])
    @JvmStatic
    fun imageUrl(view: ImageView, url: String) {
        view.loadUrl(url)
//        holder.getView<ImageView>(R.id.item_project_imageview).load(envelopePic)
//        Glide.with(view.context.applicationContext)
//            .load(url)
//            .transition(DrawableTransitionOptions.withCrossFade(500))
//            .into(view)
    }

    @BindingAdapter(value = ["circleImageUrl"])
    @JvmStatic
    fun circleImageUrl(view: ImageView, url: String) {
        view.loadUrl(url,transformations = CircleCropTransformation())
//        Glide.with(view.context.applicationContext)
//            .load(url)
//            .apply(RequestOptions.bitmapTransform(CircleCrop()))
//            .transition(DrawableTransitionOptions.withCrossFade(500))
//            .into(view)
    }


    @BindingAdapter(value = ["afterTextChanged"])
    @JvmStatic
    fun EditText.afterTextChanged(action: (String) -> Unit) {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                action(s.toString())
            }
        })
    }

}