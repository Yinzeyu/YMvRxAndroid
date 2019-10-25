package com.yzy.pj.ui

import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.airbnb.epoxy.EpoxyVisibilityTracker
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.yzy.baselibrary.base.MvRxEpoxyController
import com.yzy.baselibrary.extention.click
import com.yzy.commonlibrary.comm.CommDialogFragment
import com.yzy.commonlibrary.repository.model.ConversationDetailState
import com.yzy.commonlibrary.repository.model.GankViewModel
import com.yzy.pj.R
import kotlinx.android.synthetic.main.dialog_add_friend_layout.*

class AddFriendDialog() :
    CommDialogFragment() {
    override fun contentLayout(): Int= R.layout.dialog_add_friend_layout
    var goMyCodeCallback: (() -> Unit)? = null
    var goPasswordCallback: (() -> Unit)? = null
    var goScanFriendCallback: (() -> Unit)? = null
    private val gankViewModel: GankViewModel by lazy {
        GankViewModel()
    }

    private val epoxyController = MvRxEpoxyController<ConversationDetailState> { state ->
        if (!state.banners.isNullOrEmpty()) {
            state.banners.forEach {
                atMeMessageItem {
                    id("home_banner_${it.hashCode() + it.id}")
                    messageBean(it)
                }
            }
        }
    }

    override fun initView(view: View) {


        ivDialogFriend?.click {
            dismiss()
        }
        vMyCode?.click {
            showLoadingView()
        }

        vMyPassword?.click {
            dismissLoadingView()
        }

        tvAddFriend?.click {
            dismissLoadingView()
        }
        dialgView.setController(epoxyController)
        EpoxyVisibilityTracker().attach(dialgView)
        gankViewModel.loadData()
        //请求状态和结果监听
        gankViewModel.subscribe { state ->
            if (state.request is Loading) {//请求开始
                //如果没有显示下拉刷新则显示loading
                if (state.banners.isNullOrEmpty()) {
                    //显示loading
                    showLoadingView()
                    //为了防止loading结束后还存在失败的view所以需刷新一下
                    epoxyController.data = state//透明背景的loading需要这样设置
                    //epoxyController.requestModelBuild()非透明背景的loading还可以这样设置
                }
            } else if (state.request.complete) {//请求结束
                dismissLoadingView()
                epoxyController.data = state
                if (state.request is Fail) {//请求失败
                    Log.e("CASE", "失败原因:${(state.request as Fail<Any>).error.message ?: ""}")
                }
            }
        }
    }

    companion object {
        fun newInstance(): AddFriendDialog = AddFriendDialog()
    }

}

//  DSL style
inline fun initAddFriendDialog(
    fragmentManager: FragmentManager,
    dsl: AddFriendDialog.() -> Unit
) {
    val dialog = AddFriendDialog.newInstance().apply(dsl)
    dialog.mGravity = Gravity.BOTTOM
    dialog.mWidth = ViewGroup.LayoutParams.MATCH_PARENT
    dialog.touchOutside = false
    dialog.show(fragmentManager, "AddFriendAlertDialog")
}