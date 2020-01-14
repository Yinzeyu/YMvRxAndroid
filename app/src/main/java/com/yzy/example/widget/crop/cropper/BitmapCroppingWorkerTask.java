// "Therefore those skilled at the unorthodox
// are infinite as heaven and earth,
// inexhaustible as the great rivers.
// When they come to an end,
// they begin again,
// like the days and months;
// they die and are reborn,
// like the four seasons."
//
// - Sun Tsu,
// "The Art of War"

package com.yzy.example.widget.crop.cropper;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;

import com.blankj.utilcode.util.Utils;

/** Task to crop bitmap asynchronously from the UI thread. */
public final class BitmapCroppingWorkerTask extends AsyncTask<Void, Void, TempCrop> {

  // region: Fields and Consts

  /** Use a WeakReference to ensure the ImageView can be garbage collected */
  private final OnCropFinishListener mOnCropFinishListener;

  /** the bitmap to crop */
  private final Bitmap mBitmap;

  /** The Android URI of the image to load */
  private final Uri mUri;

  /** Required cropping 4 points (x0,y0,x1,y1,x2,y2,x3,y3) */
  private final float[] mCropPoints;

  /** Degrees the image was rotated after loading */
  private final int mDegreesRotated;

  /** the original width of the image to be cropped (for image loaded from URI) */
  private final int mOrgWidth;

  /** the original height of the image to be cropped (for image loaded from URI) */
  private final int mOrgHeight;

  /** is there is fixed aspect ratio for the crop rectangle */
  private final boolean mFixAspectRatio;

  /** the X aspect ration of the crop rectangle */
  private final int mAspectRatioX;

  /** the Y aspect ration of the crop rectangle */
  private final int mAspectRatioY;

  /** required width of the cropping image */
  private final int mReqWidth;

  /** required height of the cropping image */
  private final int mReqHeight;

  /** is the image flipped horizontally */
  private final boolean mFlipHorizontally;

  /** is the image flipped vertically */
  private final boolean mFlipVertically;

  /** The option to handle requested width/height */
  private final RequestSizeOptions mReqSizeOptions;

  /** the Android Uri to save the cropped image to */
  private final Uri mSaveUri;

  /** the compression format to use when writing the image */
  private final Bitmap.CompressFormat mSaveCompressFormat;

  /** the quality (if applicable) to use when writing the image (0 - 100) */
  private final int mSaveCompressQuality;
  /** The rectangle of the cropping window in the source image */
  private final RectF mCropRect;

  /** The rectangle of the source image dimensions */
  private final RectF mWholeImageRect;

  public BitmapCroppingWorkerTask(
      OnCropFinishListener cropFinishListener,
      Bitmap bitmap,
      float[] cropPoints,
      RectF cropRect,
      RectF wholeImageRect,
      int degreesRotated,
      boolean fixAspectRatio,
      int aspectRatioX,
      int aspectRatioY,
      int reqWidth,
      int reqHeight,
      boolean flipHorizontally,
      boolean flipVertically,
      RequestSizeOptions options,
      Uri inputUri,
      Uri saveUri,
      Bitmap.CompressFormat saveCompressFormat,
      int saveCompressQuality) {
    mUri = inputUri;
    mOnCropFinishListener = cropFinishListener;
    mBitmap = bitmap;
    mCropPoints = cropPoints;
    mDegreesRotated = degreesRotated;
    mCropRect = cropRect;
    mWholeImageRect = wholeImageRect;
    mFixAspectRatio = fixAspectRatio;
    mAspectRatioX = aspectRatioX;
    mAspectRatioY = aspectRatioY;
    mReqWidth = reqWidth;
    mReqHeight = reqHeight;
    mFlipHorizontally = flipHorizontally;
    mFlipVertically = flipVertically;
    mReqSizeOptions = options;
    mSaveUri = saveUri;
    mSaveCompressFormat = saveCompressFormat;
    mSaveCompressQuality = saveCompressQuality;
    mOrgWidth = 0;
    mOrgHeight = 0;
  }

  /** The Android URI that this task is currently loading. */
  public Uri getUri() {
    return mUri;
  }

  /**
   * Crop image in background.
   *
   * @param params ignored
   * @return the decoded bitmap data
   */
  @Override
  protected TempCrop doInBackground(Void... params) {
    try {
      if (!isCancelled()) {

        BitmapUtils.BitmapSampled bitmapSampled;
        if (mUri != null) {
          bitmapSampled =
              BitmapUtils.cropBitmap(
                  Utils.getApp(),
                  mUri,
                  mCropPoints,
                  mDegreesRotated,
                  mOrgWidth,
                  mOrgHeight,
                  mFixAspectRatio,
                  mAspectRatioX,
                  mAspectRatioY,
                  mReqWidth,
                  mReqHeight,
                  mFlipHorizontally,
                  mFlipVertically);
        } else if (mBitmap != null) {
          bitmapSampled =
              BitmapUtils.cropBitmapObjectHandleOOM(
                  mBitmap,
                  mCropPoints,
                  mDegreesRotated,
                  mFixAspectRatio,
                  mAspectRatioX,
                  mAspectRatioY,
                  mFlipHorizontally,
                  mFlipVertically);
        } else {
          return new TempCrop((Bitmap) null, 1);
        }

        Bitmap bitmap =
            BitmapUtils.resizeBitmap(bitmapSampled.bitmap, mReqWidth, mReqHeight, mReqSizeOptions);

        if (mSaveUri == null) {
          return new TempCrop(bitmap, bitmapSampled.sampleSize);
        } else {
          BitmapUtils.writeBitmapToUri(
              Utils.getApp(), bitmap, mSaveUri, mSaveCompressFormat, mSaveCompressQuality);
          if (bitmap != null) {
            bitmap.recycle();
          }
          return new TempCrop(mSaveUri, bitmapSampled.sampleSize);
        }
      }
      return null;
    } catch (Exception e) {
      return new TempCrop(e, mSaveUri != null);
    }
  }

  /**
   * Once complete, see if ImageView is still around and set bitmap.
   *
   * @param result the result of bitmap cropping
   */
  @Override
  protected void onPostExecute(TempCrop result) {
    if (result != null) {
      boolean completeCalled = false;
      if (!isCancelled()) {
        if (mOnCropFinishListener != null) {
          completeCalled = true;
          ResultCrop resultCrop = new ResultCrop(
              mBitmap,
              mUri,
              result.bitmap,
              result.uri,
              result.error,
              mCropPoints,
              mCropRect,
              mWholeImageRect,
              mDegreesRotated,
              result.sampleSize);
          mOnCropFinishListener.cropFinish(resultCrop);
        }
      }
      if (!completeCalled && result.bitmap != null) {
        // fast release of unused bitmap
        result.bitmap.recycle();
      }
    }
  }
}
