package com.yzy.example.component.playlist

import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.yzy.baselibrary.base.BaseEpoxyHolder
import com.yzy.baselibrary.base.BaseEpoxyModel
import com.yzy.baselibrary.extention.click
import com.yzy.example.R
import com.yzy.example.extention.load
import com.yzy.example.repository.bean.VideoBean
import kotlinx.android.synthetic.main.dkplayer_layout_prepare_view.view.thumb
import kotlinx.android.synthetic.main.item_list_video.view.*

/**
 * Description:
 * @author: caiyoufei
 * @date: 2019/12/12 12:12
 */
@EpoxyModelClass(layout = R.layout.item_list_video)
abstract class VideoListItem : BaseEpoxyModel<BaseEpoxyHolder>() {
  @EpoxyAttribute
  var videoBean: VideoBean? = null
  @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
  var onItemClick: ((videoBean: VideoBean) -> Unit)? = null
  @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
  var onContainerClick: ((videoBean: VideoBean) -> Unit)? = null

  override fun onBind(itemView: View) {
    videoBean?.let { data ->
      itemView.itemVideoListTitle.text = data.title
      itemView.itemVideoPrepareView.thumb.load(
        url = data.thumb,
        placeholderId = R.drawable.place_holder_video_16_9,
        errorRes = R.drawable.error_holder_video_16_9
      )
      itemView.click { onItemClick?.invoke(data) }
      itemView.itemVideoContainer.click { onContainerClick?.invoke(data) }
    }
  }
}