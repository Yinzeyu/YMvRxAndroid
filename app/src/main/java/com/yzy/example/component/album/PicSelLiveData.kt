package com.yzy.example.component.album

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.yzy.example.utils.album.entity.LocalMedia

/**
 * Description: 确认选中的图片
 * @author: yzy
 * @date: 19-5-14 下午8:23
 */
internal object PicSelLiveData {
  private var selectPicList = MutableLiveData<MutableList<LocalMedia>>()
  fun setSelectPicList(list: MutableList<LocalMedia>) {
    selectPicList.value = list
  }

  fun getSelectPicList(): MutableList<LocalMedia> {
    val list = selectPicList.value ?: mutableListOf()
    setSelectPicList(mutableListOf())
    return list
  }

  fun subscribeSelect(
    owner: LifecycleOwner,
    observer: Observer<MutableList<LocalMedia>>
  ) {
    selectPicList.observe(owner, observer)
  }

  fun unSubscribeSelect(owner: LifecycleOwner) {
    selectPicList.removeObservers(owner)
  }

  fun clearCurrentData() {
    selectPicList.value = mutableListOf()
    clearTempData()
  }

  fun clearTempData() {
    PicTempLiveData.clearAllData()
    PicPagerLiveData.clearAllData()
  }
}