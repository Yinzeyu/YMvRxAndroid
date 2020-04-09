package com.yzy.example.component.main.item



/**
 * Description:
 * @author: YZY
 * @date: 2019/10/13 19:11
 */
//@EpoxyModelClass(layout = R.layout.item_banner)
//abstract class BannerItem : BaseEpoxyModel<BaseEpoxyHolder>() {
//    //数据源
//    @EpoxyAttribute
//    var dataList: MutableList<BannerBean>? = null
//
//    override fun onBind(itemView: View) {
//        //随机横竖屏切换
//        dataList?.let { data ->
//            //防止每次滑出屏幕再滑入后重新创建
//            val tag = data.hashCode() + data.size
//            if (itemView.tag == tag) return@let
//            itemView.tag = tag
//            val itemBanner = itemView.itemBanner
//            itemBanner.listSize = data.size
//            val bannerAdapter = ViewPagerAdapter(data)
//            itemBanner.setAdapter(bannerAdapter)
//            itemBanner.mViewPager2?.setPageTransformer(CompositePageTransformer())
//            itemBanner.setAutoTurning(3000L)
//        }
//    }


//}


