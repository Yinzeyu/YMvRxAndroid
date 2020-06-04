package com.yzy.baselibrary.extention

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yzy.baselibrary.app.BaseApplication
import com.yzy.baselibrary.base.BaseViewModel
import java.lang.reflect.ParameterizedType


/**
 * 获取当前类绑定的泛型ViewModel-clazz
 */
@Suppress("UNCHECKED_CAST")
fun <VM> getVmClazz(obj: Any): VM {
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
}

/**
 * 在Activity中得到Application上下文的ViewModel
 */
inline fun <reified VM : BaseViewModel<*>> AppCompatActivity.getAppViewModel(): VM {
    return getAppViewModelProvider().get(VM::class.java)
}

/**
 * 在Fragment中得到Application上下文的ViewModel
 * 提示，在fragment中调用该方法时，请在该Fragment onCreate以后调用或者请用by lazy方式懒加载初始化调用，不然会提示requireActivity没有导致错误
 */
inline fun <reified VM : BaseViewModel<*>> Fragment.getAppViewModel(): VM {
    return getAppViewModelProvider().get(VM::class.java)
}

/**
 * 得到当前Activity上下文的ViewModel
 */
inline fun <reified VM : BaseViewModel<*>> AppCompatActivity.getViewModel(): VM {
    return ViewModelProvider(
        this,
        ViewModelProvider.AndroidViewModelFactory(application)
    ).get(VM::class.java)
}

/**
 * 得到当前Fragment上下文的ViewModel
 * 提示，在fragment中调用该方法时，请在该Fragment onCreate以后调用或者请用by lazy方式懒加载初始化调用，不然会提示requireActivity没有导致错误
 */
inline fun <reified VM : BaseViewModel<*>> Fragment.getViewModel(): VM {
    return ViewModelProvider(
        this,
        ViewModelProvider.AndroidViewModelFactory(this.requireActivity().application)
    ).get(VM::class.java)
}
inline fun <reified VM : BaseViewModel<*>> getViewModel(): VM {
    return ViewModelProvider(
        BaseApplication.instance(),
        ViewModelProvider.AndroidViewModelFactory(BaseApplication.instance())
    ).get(VM::class.java)
}
/**
 * 在Fragment中得到父类Activity的共享ViewModel
 * 提示，在fragment中调用该方法时，请在该Fragment onCreate以后调用或者请用by lazy方式懒加载初始化调用，不然会提示requireActivity没有导致错误
 */
inline fun <reified VM : BaseViewModel<*>> Fragment.getActivityViewModel(): VM {
    return ViewModelProvider(
        requireActivity(),
        ViewModelProvider.AndroidViewModelFactory(this.requireActivity().application)
    ).get(VM::class.java)
}

/**
 * 获取一个全局的ViewModel
 */
fun getAppViewModelProvider(): ViewModelProvider {
    return ViewModelProvider(BaseApplication.instance(), getAppFactory())
}

private var mFactory: ViewModelProvider.Factory? = null
private fun getAppFactory(): ViewModelProvider.Factory {
    if (mFactory == null) {
        mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(BaseApplication.instance())
    }
    return mFactory as ViewModelProvider.Factory
}




