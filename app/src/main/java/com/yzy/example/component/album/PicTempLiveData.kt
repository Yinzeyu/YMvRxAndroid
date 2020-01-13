package com.yzy.example.component.album

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.yzy.example.utils.album.entity.LocalMedia

/**
 * Description: 临时选中的图片，还没有点击确认
 * @author: caiyoufei
 * @date: 19-5-18 下午4:05
 */
internal object PicTempLiveData {
  private var tempSelPicList = MutableLiveData<MutableList<LocalMedia>>()
  //图库grid列表和大图pager列表中的选中数据同步
  fun setSelectPicList(list: MutableList<LocalMedia>) {
    tempSelPicList.value = list
  }

  fun getSelectPicList(): MutableList<LocalMedia> {
    return tempSelPicList.value ?: mutableListOf()
  }

  //本来这个方法应该是放到PicSelectActivity去监听的，但是由于图片加载onResume会重新load，加上是异步的，担心无法判断到选中状态，所以直接调用了getSelectPicList
  fun subscribeSelect(
    owner: LifecycleOwner,
    observer: Observer<MutableList<LocalMedia>>
  ) {
    tempSelPicList.observe(owner, observer)
  }

  fun unSubscribeSelect(owner: LifecycleOwner) {
    tempSelPicList.removeObservers(owner)
  }

  fun clearAllData() {
    tempSelPicList.value = mutableListOf()
  }
}