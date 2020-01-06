package com.yzy.example.utils

import com.yzy.baselibrary.utils.SharePreferencesUtils

/**
 * @author kuky.
 * @description
 */
object PreferencesHelper {
    private const val KEYBOARD_HEIGHT_PORTRAIT = "keyboard.height.portrait"

    fun getKeyboardHeightPortrait(): Int = SharePreferencesUtils.getInteger( KEYBOARD_HEIGHT_PORTRAIT)

    fun setKeyboardHeightPortrait(height: Int) = SharePreferencesUtils.saveInteger( KEYBOARD_HEIGHT_PORTRAIT, height)

}