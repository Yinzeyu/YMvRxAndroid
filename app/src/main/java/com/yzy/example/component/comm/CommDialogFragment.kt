package com.yzy.example.component.comm

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import androidx.databinding.ViewDataBinding
import com.airbnb.lottie.*
import com.yzy.baselibrary.base.BaseActivity
import com.yzy.baselibrary.base.BaseDialogFragment
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.extention.removeParent
import com.yzy.example.component.dialog.ActionDialog

/**
 * Description:
 * @author: caiyoufei
 * @date: 2019/10/8 10:03
 */
abstract class CommDialogFragment<VM : BaseViewModel<*>, DB : ViewDataBinding> : BaseDialogFragment<VM, DB>() {
    //#################################镶嵌在页面中的loading<-END#################################//

    private var mActionDialog: ActionDialog? = null

    fun showActionLoading(text: String? = null) {
        if (mActionDialog == null) {
            mActionDialog = ActionDialog.newInstance(true)
        }
        mActionDialog?.onDismiss {
            mActionDialog = null
        }
        mActionDialog?.let {
            if (!text.isNullOrBlank()) it.hintText = text
            it.show(requireActivity() .supportFragmentManager)
        }
    }

    fun dismissActionLoading() {
        mActionDialog?.dismissAllowingStateLoss()
    }
}