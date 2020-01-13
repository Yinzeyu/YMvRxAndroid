package com.yzy.example.component.album

import android.util.SparseArray
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.yzy.example.utils.album.entity.LocalMedia

/**
 * Description: 大图列表展示的数据列表
 * @author: caiyoufei
 * @date: 19-5-17 下午7:21
 */
internal object PicPagerLiveData {
  private var localMediaList = MutableLiveData<SparseArray<MutableList<LocalMedia>>>()
  fun setPagerListData(
    index: Int = 0,
    info: MutableList<LocalMedia>
  ) {
    val map = SparseArray<MutableList<LocalMedia>>()
    //当前展示列表的位置和数据
    map.put(index, info)
    localMediaList.value = map
  }

  fun subscribeSeeSelect(
    owner: LifecycleOwner,
    observer: Observer<SparseArray<MutableList<LocalMedia>>>
  ) {
    localMediaList.observe(owner, observer)
  }

  fun unSubscribeSeeSelect(owner: LifecycleOwner) {
    localMediaList.removeObservers(owner)
  }

  fun clearAllData() {
    localMediaList.value = SparseArray()
  }
}