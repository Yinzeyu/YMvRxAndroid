/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yzy.example

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.yzy.baselibrary.base.BaseFragment
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.baselibrary.extention.click
import kotlinx.android.synthetic.main.fragment_registered.*

/**
 * Shows "Done".
 */
class Registered : BaseFragment<NoViewModel,ViewDataBinding>() {
    override val contentLayout: Int = R.layout.fragment_registered

    override fun initView(root: View?) {
        ttttt.click {
            Navigation.findNavController(root!!).popBackStack(R.id.register,true)
        }

    }

    override fun initData() {
    }
}
