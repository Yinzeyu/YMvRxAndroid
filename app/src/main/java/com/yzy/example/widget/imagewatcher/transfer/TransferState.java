package com.yzy.example.widget.imagewatcher.transfer;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;


import com.yzy.example.widget.imagewatcher.loader.ImageLoader;
import com.yzy.example.widget.imagewatcher.style.IProgressIndicator;
import com.yzy.example.widget.imagewatcher.view.image.TransferImage;




import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.ImageView.ScaleType.FIT_CENTER;

/**
 * 由于用户配置的参数不同 (例如 使用不同的 ImageLoader  / 是否指定了 thumbnailImageList 参数值) <br/>
 * 使得 Transferee 所表现的行为不同，所以采用一组策略算法来实现以下不同的功能：
 * <ul>
 * <li>1. 图片进入 Transferee 的过渡动画</li>
 * <li>2. 图片加载时不同的表现形式</li>
 * <li>3. 图片从 Transferee 中出去的过渡动画</li>
 * </ul>
 * Created by hitomi on 2017/5/4.
 * <p>
 * email: 196425254@qq.com
 */
abstract class TransferState {

    protected TransferLayout transfer;

    TransferState(TransferLayout transfer) {
        this.transfer = transfer;
    }

    /**
     * 由于 4.4 以下版本状态栏不可修改，所以兼容 4.4 以下版本的全屏模式时，要去除状态栏的高度
     *
     * @param oldY
     * @return
     */
    int getTransImageLocalY(int oldY) {
        return oldY;
    }

    /**
     * 获取 View 在屏幕坐标系中的坐标
     *
     * @param view 需要定位位置的 View
     * @return 坐标系数组
     */
    int[] getViewLocation(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return location;
    }

    /**
     * 依据 originImage 在屏幕中的坐标和宽高信息创建一个 TransferImage
     *
     * @param originImage 缩略图 ImageView
     * @return TransferImage
     */
    @NonNull
    TransferImage createTransferImage(ImageView originImage) {
        TransferConfig config = transfer.getTransConfig();
        int[] location = getViewLocation(originImage);

        TransferImage transImage = new TransferImage(transfer.getContext());
        transImage.setScaleType(FIT_CENTER);
        transImage.setOriginalInfo(location[0], getTransImageLocalY(location[1]),
                originImage.getWidth(), originImage.getHeight());
        transImage.setDuration(config.getDuration());
        transImage.setLayoutParams(new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        transImage.setOnTransferListener(transfer.transListener);

        return transImage;
    }


    /**
     * 图片加载完毕，开启预览
     *
     * @param targetImage 预览图片
     * @param imgUrl      图片url
     * @param config      设置
     * @param position    索引
     */
    void startPreview(TransferImage targetImage, String imgUrl, TransferConfig config, int position) {
        // 启用 TransferImage 的手势缩放功能
        targetImage.enable();
        transfer.bindOnOperationListener(targetImage, imgUrl, position);
    }
    /**
     * 当用户使用 justLoadHitImage 属
     * 性时，需要使用 prepareTransfer 方法提前让 ViewPager 对应
     * position 处的 TransferImage 剪裁并设置占位图
     *
     * @param transImage ViewPager 中 position 位置处的 TransferImage
     * @param position   当前点击的图片索引
     */
    public abstract void prepareTransfer(TransferImage transImage, final int position);

    /**
     * 创建一个 TransferImage 放置在 Transferee 中指定位置，并播放从缩略图到 Transferee 的过渡动画
     *
     * @param position 进入到 Transferee 之前，用户在图片列表中点击的图片的索引
     * @return 创建的 TransferImage
     */
    public abstract TransferImage createTransferIn(final int position);

    /**
     * 从网络或者从 {@link ImageLoader} 指定的缓存中加载 SourceImageList.get(position) 对应的图片
     *
     * @param position 原图片路径索引
     */
    public abstract void transferLoad(final int position);

    /**
     * 创建一个 TransferImage 放置在 Transferee 中指定位置，并播放从 Transferee 到 缩略图的过渡动画
     *
     * @param position 当前点击的图片索引
     * @return 创建的 TransferImage
     */
    public abstract TransferImage transferOut(final int position);

}
