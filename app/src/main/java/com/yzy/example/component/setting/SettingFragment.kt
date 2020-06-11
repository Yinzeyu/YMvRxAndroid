package com.yzy.example.component.setting

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.yzy.baselibrary.base.BaseActivity
import com.yzy.baselibrary.extention.click
import com.yzy.baselibrary.extention.nav
import com.yzy.example.R
import com.yzy.example.component.MainFragmentDirections
import com.yzy.example.component.dialog.commAlertDialog
import com.yzy.example.component.web.WebsiteDetailFragmentArgs
import com.yzy.example.utils.DataCleanManager
import com.yzy.example.utils.MMkvUtils

/**
 * 描述　: 系统设置
 */
class SettingFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private var commLayoutTitleBarView: FrameLayout? = null

    //这里不能继承BaseFragment了，所以手动获取一下 AppViewModel
//    val shareViewModel: AppViewModel by lazy { getAppViewModel<AppViewModel>() }

//    private var colorPreview: IconPreference? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //这里重写根据PreferenceFragmentCompat 的布局 ，往他的根布局插入了一个toolbar
        val containerView = view.findViewById<FrameLayout>(android.R.id.list_container)
        containerView.let {
            //转为线性布局
            val linearLayout = it.parent as? LinearLayout
            linearLayout?.run {
                val toolbarView =
                    LayoutInflater.from(activity).inflate(R.layout.layout_comm_title, null)
                commLayoutTitleBarView = toolbarView.findViewById(R.id.commLayoutTitleBarView)
                val commTitleBack =
                    commLayoutTitleBarView?.findViewById<ImageView>(R.id.commTitleBack)
                val commTitleText =
                    commLayoutTitleBarView?.findViewById<TextView>(R.id.commTitleText)
                commTitleBack?.click {
                    nav().navigateUp()
                }

                commTitleText?.text = "设置"
                //添加到第一个
                addView(toolbarView, 0)
            }
        }

    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.root_preferences)

        var nightMode: Boolean = MMkvUtils.instance.getNightMode()
//        val version = "当前版本 " + requireActivity().packageManager.getPackageInfo(
//            requireActivity().packageName, 0
//        ).versionName
        findPreference<SwitchPreference>("night")?.isChecked = nightMode
        findPreference<Preference>("clearCache")?.summary =
            DataCleanManager.getTotalCacheSize(requireActivity())

        findPreference<SwitchPreference>("night")?.setOnPreferenceChangeListener { preference, newValue ->
            val boolValue = newValue as Boolean
            findPreference<SwitchPreference>("night")?.isChecked = !boolValue
            nightMode = boolValue
            val currentMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            (requireActivity() as BaseActivity).delegate.localNightMode =
                if (currentMode == Configuration.UI_MODE_NIGHT_NO) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            val nightModeChanged: Boolean = MMkvUtils.instance.getNightMode()
            AppCompatDelegate.setDefaultNightMode(
                if (nightModeChanged) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )

//            if (nightModeChanged) {
//                ColorUtil.setLastColor(ColorUtil.getColor(parentActivity))
//                ColorUtil.setColor(
//                    ContextCompat.getColor(parentActivity, R.color.colorGray666)
//                )
//            } else {
//                ColorUtil.setColor(ColorUtil.getLastColor(parentActivity))
//            }
            true
        }

        // 绑定清理缓存响应事件
        findPreference<Preference>("clearCache")?.setOnPreferenceClickListener {
            commAlertDialog(childFragmentManager) {
                title = getString(R.string.title)
                content = "确定清除缓存吗？"
                confirmCallback = {
                    DataCleanManager.clearAllCache(requireContext())
                    findPreference<Preference>("clearCache")?.summary =
                        DataCleanManager.getTotalCacheSize(requireContext())
                }
            }
            false
        }

//        findPreference<Preference>("version")?.setOnPreferenceClickListener {
//            checkUpdate(parentActivity, true)
//            false
//        }
//
//        findPreference<Preference>("csdn")?.setOnPreferenceClickListener {
//            CommonUtil.startWebView(parentActivity, "https://blog.csdn.net/qq_39424143", "DLUT_WJX")
//            false
//        }

        findPreference<Preference>("project")?.setOnPreferenceClickListener {
            nav().navigate(SettingFragmentDirections.actionSettingFragmentToWebFragment("https://github.com/Yinzeyu/YMvRxAndroid"))
            false
        }
    }


    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
    }

}

