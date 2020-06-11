package com.yzy.example.repository.model

import androidx.lifecycle.SavedStateHandle
import com.yzy.baselibrary.base.BaseRepository
import com.yzy.baselibrary.base.BaseViewModel

class MainViewModel(var state: SavedStateHandle) : BaseViewModel<BaseRepository>(){
    private val mainKey = "mainKey"
    fun setValue(value: Int) = state.set(mainKey, value)
    private fun getValue(): Int? = state.get(mainKey)
    fun loadPosition():Int{
        if (getValue() != null){
            return getValue()?:-1
        }
        return -1
    }
}