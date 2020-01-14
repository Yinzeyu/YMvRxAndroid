package com.yzy.example.widget.crop.cropper;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;

import com.blankj.utilcode.util.ScreenUtils;
import com.yzy.example.widget.crop.ucrop.model.ImageState;
import com.yzy.example.widget.crop.ucrop.view.CropImageView;

/**
 * description :
 *
 * @author : case
 * @date : 2018/8/22 19:34
 */
public class CropperUtils {
  /** 裁切图片 */
  public static void cropAndSaveImage(CropImageView cropImageView, Uri inputUri, Uri mOutputUri,
                                      Bitmap.CompressFormat format, int quality, OnCropFinishListener listener) {
    Bitmap bitmap = cropImageView.getViewBitmap();
    ImageState imageState = cropImageView.getImageState();
    //全部的范围
    RectF allRect = imageState.getCurrentImageRect();
    //裁切的范围
    RectF cropRect = imageState.getCropRect();
    //计算缩放比
    float ratio = 1;
    if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
      if (bitmap.getWidth() > bitmap.getHeight()) {
        //原本的缩放比
        ratio = bitmap.getHeight() * 1f / ScreenUtils.getScreenWidth();
        //缩放后的缩放比
        ratio = ratio / ((allRect.bottom - allRect.top) * 1f / ScreenUtils.getScreenWidth());
      } else {
        //原本的缩放比
        ratio = bitmap.getWidth() * 1f / ScreenUtils.getScreenWidth();
        //缩放后的缩放比
        ratio = ratio / ((allRect.right - allRect.left) * 1f / ScreenUtils.getScreenWidth());
      }
    }
    //ucrop拿到的是相对项目的位置,cropper需要的是相对图片的位置，所以自己重新计算
    float[] cropPoints = {
        Math.abs(cropRect.left - allRect.left) * ratio,
        Math.abs(cropRect.top - allRect.top) * ratio,

        Math.abs(cropRect.right - allRect.left) * ratio,
        Math.abs(cropRect.top - allRect.top) * ratio,

        Math.abs(cropRect.right - allRect.left) * ratio,
        Math.abs(cropRect.bottom - allRect.top) * ratio,

        Math.abs(cropRect.left - allRect.left) * ratio,
        Math.abs(cropRect.bottom - allRect.top) * ratio,
    };
    new BitmapCroppingWorkerTask(listener, bitmap, cropPoints, imageState.getCurrentImageRect(),
        imageState.getCropRect(), 0, true, 1,
        1, 0, 0, false, false,
        RequestSizeOptions.NONE, inputUri, mOutputUri, format, quality).execute();
  }
}