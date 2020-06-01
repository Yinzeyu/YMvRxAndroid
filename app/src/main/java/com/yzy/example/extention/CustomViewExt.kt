package com.yzy.example.extention

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yzy.baselibrary.app.BaseApplication
import com.yzy.example.R
import com.yzy.example.app.App
import com.yzy.example.utils.SettingUtil

//
//
//fun BannerViewPager<*, *>.setPageListener(onPageSelected: (Int) -> Unit) {
//    this.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//        override fun onPageScrollStateChanged(state: Int) {
//        }
//
//        override fun onPageScrolled(
//            position: Int,
//            positionOffset: Float,
//            positionOffsetPixels: Int
//        ) {
//        }
//
//        override fun onPageSelected(position: Int) {
//            onPageSelected.invoke(position)
//        }
//    })
//}
//
//fun LoadService<*>.setErrorText(message: String) {
//    this.setCallBack(ErrorCallback::class.java) { _, view ->
//        view.findViewById<TextView>(R.id.error_text).text = message
//    }
//}
//
//fun LoadServiceInit(view: View, callback: () -> Unit): LoadService<Any> {
//    val loadsir = LoadSir.getDefault().register(view) {
//        //点击重试时触发的操作
//        callback.invoke()
//    }
//    loadsir.showCallback(LoadingCallback::class.java)
//    SettingUtil.setLoadingColor(SettingUtil.getColor(App.instance), loadsir)
//    return loadsir
//}
//
////绑定普通的Recyclerview
//fun RecyclerView.init(
//    layoutManger: RecyclerView.LayoutManager,
//    bindAdapter: RecyclerView.Adapter<*>,
//    isScroll: Boolean = true
//): RecyclerView {
//    layoutManager = layoutManger
//    setHasFixedSize(true)
//    adapter = bindAdapter
//    isNestedScrollingEnabled = isScroll
//    return this
//}
//
////绑定SwipeRecyclerView
//fun SwipeRecyclerView.init(
//    layoutManger: RecyclerView.LayoutManager,
//    bindAdapter: RecyclerView.Adapter<*>,
//    isScroll: Boolean = true
//): SwipeRecyclerView {
//    layoutManager = layoutManger
//    setHasFixedSize(true)
//    adapter = bindAdapter
//    isNestedScrollingEnabled = isScroll
//    return this
//}
//
//fun SwipeRecyclerView.initFooter(loadmoreListener: SwipeRecyclerView.LoadMoreListener): DefineLoadMoreView {
//    val footerView = DefineLoadMoreView(App.instance)
//    //给尾部设置颜色
//    footerView.setLoadViewColor(SettingUtil.getOneColorStateList(App.instance))
//    //设置尾部点击回调
//    footerView.setmLoadMoreListener(SwipeRecyclerView.LoadMoreListener {
//        footerView.onLoading()
//        loadmoreListener.onLoadMore()
//    })
//    this.run {
//        //添加加载更多尾部
//        addFooterView(footerView)
//        setLoadMoreView(footerView)
//        //设置加载更多回调
//        setLoadMoreListener(loadmoreListener)
//    }
//    return footerView
//}
//
fun RecyclerView.initFloatBtn(floatbtn: FloatingActionButton) {
    //监听recyclerview滑动到顶部的时候，需要把向上返回顶部的按钮隐藏
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        @SuppressLint("RestrictedApi")
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!canScrollVertically(-1)) {
                floatbtn.visibility = View.INVISIBLE
            }
        }
    })
    floatbtn.backgroundTintList = SettingUtil.getOneColorStateList(BaseApplication.instance())
    floatbtn.setOnClickListener {
        val layoutManager = layoutManager as LinearLayoutManager
        //如果当前recyclerview 最后一个视图位置的索引大于等于40，则迅速返回顶部，否则带有滚动动画效果返回到顶部
        if (layoutManager.findLastVisibleItemPosition() >= 40) {
            scrollToPosition(0)//没有动画迅速返回到顶部(马上)
        } else {
            smoothScrollToPosition(0)//有滚动动画返回到顶部(有点慢)
        }
    }
}
//
////初始化 SwipeRefreshLayout
//fun SwipeRefreshLayout.init(onRefreshListener: () -> Unit) {
//    this.run {
//        setOnRefreshListener {
//            onRefreshListener.invoke()
//        }
//        //设置主题颜色
//        setColorSchemeColors(SettingUtil.getColor(App.instance))
//    }
//}
//
///**
// * 初始化普通的toolbar 只设置标题
// */
fun Toolbar.init(titleStr: String = ""): Toolbar {
    setBackgroundColor(SettingUtil.getColor(BaseApplication.instance()))
    title = titleStr
    return this
}

/**
 * 初始化有返回键的toolbar
 */
fun Toolbar.initClose(
    titleStr: String = "",
    backImg: Int = R.drawable.ic_back,
    onBack: (toolbar: Toolbar) -> Unit
): Toolbar {
    setBackgroundColor(SettingUtil.getColor(BaseApplication.instance()))
    title = titleStr.toHtml()
    setNavigationIcon(backImg)
    setNavigationOnClickListener { onBack.invoke(this) }
    return this
}
//
///**
// * 根据控件的类型设置主题，注意，控件具有优先级， 基本类型的控件建议放到最后，像 Textview，FragmentLayout，不然会出现问题，
// * 列如下面的BottomNavigationViewEx他的顶级父控件为FragmentLayout，如果先 is Fragmentlayout判断在 is BottomNavigationViewEx上面
// * 那么就会直接去执行 is FragmentLayout的代码块 跳过 is BottomNavigationViewEx的代码块了
// */
//fun setUiTheme(color: Int, vararg anylist: Any) {
//    anylist.forEach {
//        when (it) {
//            is LoadService<*> -> SettingUtil.setLoadingColor(color, it as LoadService<Any>)
//            is FloatingActionButton -> it.backgroundTintList =
//                SettingUtil.getOneColorStateList(color)
//            is SwipeRefreshLayout -> it.setColorSchemeColors(color)
//            is DefineLoadMoreView -> it.setLoadViewColor(SettingUtil.getOneColorStateList(color))
//            is BottomNavigationViewEx -> {
//                it.itemIconTintList = SettingUtil.getColorStateList(color)
//                it.itemTextColor = SettingUtil.getColorStateList(color)
//            }
//            is Toolbar -> it.setBackgroundColor(color)
//            is TextView -> it.setTextColor(color)
//            is LinearLayout -> it.setBackgroundColor(color)
//            is ConstraintLayout -> it.setBackgroundColor(color)
//            is FrameLayout -> it.setBackgroundColor(color)
//        }
//    }
//}

//设置适配器的列表动画
fun BaseQuickAdapter<*, *>.setAdapterAnimion(mode: Int) {
    //等于0，关闭列表动画 否则开启
    if (mode == 0) {
        this.animationEnable = false
    } else {
        this.animationEnable = true
        this.setAnimationWithDefault(BaseQuickAdapter.AnimationType.values()[mode - 1])
    }
}
fun String.toHtml(flag: Int =-1 ): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this,flag)
    } else {
        Html.fromHtml(this)
    }
}


//fun MagicIndicator.bindViewPager2(
//    viewPager: ViewPager2,
//    mDataList: ArrayList<ClassifyResponse> = arrayListOf(),
//    mStringList: ArrayList<String> = arrayListOf(),
//    action: (index: Int) -> Unit = {}
//) {
//    val commonNavigator = CommonNavigator(App.instance)
//    commonNavigator.adapter = object : CommonNavigatorAdapter() {
//        override fun getCount(): Int {
//            return if (mDataList.size != 0) {
//                mDataList.size
//            } else {
//                mStringList.size
//            }
//        }
//
//        override fun getTitleView(context: Context, index: Int): IPagerTitleView {
//            return ScaleTransitionPagerTitleView(App.instance).apply {
//                text = if (mDataList.size != 0) {
//                    mDataList[index].name.toHtml()
//                } else {
//                    mStringList[index].toHtml()
//                }
//                textSize = 17f
//                normalColor = Color.WHITE
//                selectedColor = Color.WHITE
//                setOnClickListener {
//                    viewPager.currentItem = index
//                    action.invoke(index)
//                }
//            }
//        }
//
//        override fun getIndicator(context: Context): IPagerIndicator {
//            return LinePagerIndicator(context).apply {
//                mode = LinePagerIndicator.MODE_EXACTLY
//                //线条的宽高度
//                lineHeight = UIUtil.dip2px(App.instance, 3.0).toFloat()
//                lineWidth = UIUtil.dip2px(App.instance, 30.0).toFloat()
//                //线条的圆角
//                roundRadius = UIUtil.dip2px(App.instance, 6.0).toFloat()
//                startInterpolator = AccelerateInterpolator()
//                endInterpolator = DecelerateInterpolator(2.0f)
//                //线条的颜色
//                setColors(Color.WHITE)
//            }
//        }
//    }
//    this.navigator = commonNavigator
//
//    viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//        override fun onPageSelected(position: Int) {
//            super.onPageSelected(position)
//            this@bindViewPager2.onPageSelected(position)
//            action.invoke(position)
//        }
//
//        override fun onPageScrolled(
//            position: Int,
//            positionOffset: Float,
//            positionOffsetPixels: Int
//        ) {
//            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//            this@bindViewPager2.onPageScrolled(position, positionOffset, positionOffsetPixels)
//        }
//
//        override fun onPageScrollStateChanged(state: Int) {
//            super.onPageScrollStateChanged(state)
//            this@bindViewPager2.onPageScrollStateChanged(state)
//        }
//    })
//}
//
fun ViewPager2.init(
    fragment: Fragment,
    fragments: ArrayList<Fragment>,
    isUserInputEnabled: Boolean = true
): ViewPager2 {
    //是否可滑动
    this.isUserInputEnabled = isUserInputEnabled
    //设置适配器
    adapter = object : FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int) = fragments[position]
        override fun getItemCount() = fragments.size
    }
    return this
}
//
///**
// * 隐藏软键盘
// */
//fun hideSoftKeyboard(activity: Activity?) {
//    activity?.let { act ->
//        val view = act.currentFocus
//        view?.let {
//            val inputMethodManager =
//                act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
//            inputMethodManager.hideSoftInputFromWindow(
//                view.windowToken,
//                InputMethodManager.HIDE_NOT_ALWAYS
//            )
//        }
//    }
//}
//
///**
// * 防止重复点击事件 默认0.5秒内不可重复点击 跳转前做登录校验
// * @param interval 时间间隔 默认0.5秒
// * @param action 执行方法
// */
//var lastloginClickTime = 0L
//fun View.clickNoRepeatLogin(interval: Long = 500, action: (view: View) -> Unit) {
//    setOnClickListener {
//        val currentTime = System.currentTimeMillis()
//        if (lastloginClickTime != 0L && (currentTime - lastloginClickTime < interval)) {
//            return@setOnClickListener
//        }
//        lastloginClickTime = currentTime
//        if (CacheUtil.isLogin()) {
//            action(it)
//        } else {
//            //注意一下，这里我是确定我所有的拦截登录都是在MainFragment中的，所以我可以写死，但是如果不在MainFragment中时跳转，你会报错
//            nav(it).navigate(R.id.action_mainFragment_to_loginFragment)
//        }
//    }
//}
//
///**
// * 防止重复点击事件 默认0.5秒内不可重复点击 跳转前做登录校验
// * @param view 触发的view集合
// * @param interval 时间间隔 默认0.5秒
// * @param action 执行方法
// */
//fun clickNoRepeatLogin(vararg view: View?, interval: Long = 500, action: (view: View) -> Unit) {
//    view.forEach {view1 ->
//        view1?.setOnClickListener { view2 ->
//            val currentTime = System.currentTimeMillis()
//            if (lastloginClickTime != 0L && (currentTime - lastloginClickTime < interval)) {
//                return@setOnClickListener
//            }
//            lastloginClickTime = currentTime
//            if (CacheUtil.isLogin()) {
//                action(view2)
//            } else {
//                //注意一下，这里我是确定我所有的拦截登录都是在MainFragment中的，所以我可以写死，但是如果不在MainFragment中时跳转，你会报错
//                nav(view2).navigate(R.id.action_mainFragment_to_loginFragment)
//            }
//        }
//    }
//}
