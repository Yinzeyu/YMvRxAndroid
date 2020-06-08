package com.yzy.baselibrary.base

import androidx.lifecycle.SavedStateHandle

class NoViewModel(var savedStateHandle: SavedStateHandle) : BaseViewModel<BaseRepository>()