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
import androidx.navigation.fragment.findNavController
import com.yzy.baselibrary.base.BaseFragment
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.example.component.main.MainFragmentDirections
import com.yzy.example.extention.startNavigate
import kotlinx.android.synthetic.main.fragment_register.*


/**
 * Shows a register form to showcase UI state persistence. It has a button that goes to [Registered]
 */
class Register : BaseFragment<NoViewModel,ViewDataBinding>() {
    override val contentLayout: Int = R.layout.fragment_register

    override fun initView(root: View?) {
        signup_btn.setOnClickListener {
            startNavigate(view, RegisterDirections.actionRegisterToRegistered())

        }
    }

    override fun initData() {
    }
}
