package com.yzy.example.widget.imagewatcher.transfer;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.yzy.example.widget.imagewatcher.loader.ImageLoader;
import com.yzy.example.widget.imagewatcher.style.IProgressIndicator;
import com.yzy.example.widget.imagewatcher.view.image.TransferImage;

import java.util.List;

/**
 * 用户指定了缩略图路径，使用该路径加载缩略图，
 * 并使用 {@link TransferImage#CATE_ANIMA_TOGETHER} 动画类型展示图片
 * <p>
 * Created by hitomi on 2017/5/4.
 * <p>
 * email: 196425254@qq.com
 */
class RemoteThumbState extends TransferState {

    RemoteThumbState(TransferLayout transfer) {
        super(transfer);
    }

    @Override
    public void prepareTransfer(final TransferImage transImage, int position) {
        final TransferConfig config = transfer.getTransConfig();

        ImageLoader imageLoader = config.getImageLoader();
        String imgUrl = config.getThumbnailImageList().get(position);

//        if (imageLoader.isLoaded(imgUrl)) {
            imageLoader.showImage(imgUrl, transImage, config.getMissDrawable(transfer.getContext()), null);
//        } else {
//            transImage.setImageDrawable(config.getMissDrawable(transfer.getContext()));
//        }
    }

    @Override
    public TransferImage createTransferIn(final int position) {
        TransferConfig config = transfer.getTransConfig();

        TransferImage transImage = createTransferImage(config.getOriginImageList().get(position));
        loadSourceImage(transImage.getDrawable(),position,transImage);
//        if (position <= originImageList.size() - 1 && originImageList.get(position) != null) {
//            transImage = createTransferImage(originImageList.get(position));
//
//        transformThumbnail(config.getThumbnailImageList().get(position), transImage, true);
        transfer.addView(transImage, 1);

        return transImage;
    }

    @Override
    public void transferLoad(final int position) {
        final TransferImage targetImage = transfer.transAdapter.getImageItem(position);
        loadSourceImage(targetImage.getDrawable(), position, targetImage);


//        if (config.isJustLoadHitImage()) {
//            // 如果用户设置了 JustLoadHitImage 属性，说明在 prepareTransfer 中已经
//            // 对 TransferImage 裁剪且设置了占位图， 所以这里直接加载原图即可
//
//        } else {
//            String thumbUrl = config.getThumbnailImageList().get(position);
//
////            if (imageLoader.isLoaded(thumbUrl)) {
//                imageLoader.loadImageAsync(thumbUrl,targetImage, drawable -> {
//                    if (drawable == null)
//
//
//                    loadSourceImage(drawable, position, targetImage);
//                });
////            } else {
////                loadSourceImage(config.getMissDrawable(transfer.getContext()),
////                        position, targetImage);
////            }
//        }
    }

    private void loadSourceImage(Drawable drawable, final int position, final TransferImage targetImage) {
        final TransferConfig config = transfer.getTransConfig();
        final ImageLoader imageLoader = config.getImageLoader();
        final String sourceUrl = config.getSourceImageList().get(position);
        final IProgressIndicator progressIndicator = config.getProgressIndicator();
        progressIndicator.attach(position, transfer.transAdapter.getParentItem(position));
        imageLoader.showImage(sourceUrl, targetImage, drawable, new ImageLoader.SourceCallback() {

            @Override
            public void onProgress(int progress) {
                progressIndicator.onProgress(position, progress);
            }

            @Override
            public void onDelivered(int status) {
                progressIndicator.onFinish(position); // onFinish 只是说明下载完毕，并没更新图像
                switch (status) {
                    case ImageLoader.STATUS_DISPLAY_SUCCESS:
                        // 启用 TransferImage 的手势缩放功能
                        targetImage.enable();
                        // 绑定点击关闭 Transferee
                        transfer.bindOnOperationListener(targetImage, sourceUrl, position);
                        break;
                    case ImageLoader.STATUS_DISPLAY_FAILED:  // 加载失败，显示加载错误的占位图
                        targetImage.setImageDrawable(config.getErrorDrawable(transfer.getContext()));
                        break;
                }
            }
        });
    }

    @Override
    public TransferImage transferOut(final int position) {
        TransferImage transImage = null;

        TransferConfig config = transfer.getTransConfig();
        List<ImageView> originImageList = config.getOriginImageList();
        final TransferImage targetImage = transfer.transAdapter.getImageItem(position);
        if (position <= originImageList.size() - 1 && originImageList.get(position) != null) {
            transImage = createTransferImage(originImageList.get(position));
            loadSourceImage(targetImage.getDrawable(),position,targetImage);
//            transformThumbnail(config.getThumbnailImageList().get(position), transImage, false);

            transfer.addView(transImage, 1);
        }

        return transImage;
    }
}