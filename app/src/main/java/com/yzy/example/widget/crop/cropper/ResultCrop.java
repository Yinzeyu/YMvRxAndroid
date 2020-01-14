package com.yzy.example.widget.crop.cropper;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;

/**
 * description :
 *
 * @author : case
 * @date : 2018/8/22 16:25
 */
public class ResultCrop {
  /**
   * The image bitmap of the original image loaded for cropping.<br>
   * Null if uri used to load image or activity result is used.
   */
  private final Bitmap mOriginalBitmap;

  /**
   * The Android uri of the original image loaded for cropping.<br>
   * Null if bitmap was used to load image.
   */
  private final Uri mOriginalUri;

  /**
   * The cropped image bitmap result.<br>
   * Null if save cropped image was executed, no output requested or failure.
   */
  private final Bitmap mBitmap;

  /**
   * The Android uri of the saved cropped image result.<br>
   * Null if get cropped image was executed, no output requested or failure.
   */
  private final Uri mUri;

  /** The error that failed the loading/cropping (null if successful) */
  private final Exception mError;

  /** The 4 points of the cropping window in the source image */
  private final float[] mCropPoints;

  /** The rectangle of the cropping window in the source image */
  private final RectF mCropRect;

  /** The rectangle of the source image dimensions */
  private final RectF mWholeImageRect;

  /** The final rotation of the cropped image relative to source */
  private final int mRotation;

  /** sample size used creating the crop bitmap to lower its size */
  private final int mSampleSize;

  ResultCrop(
      Bitmap originalBitmap,
      Uri originalUri,
      Bitmap bitmap,
      Uri uri,
      Exception error,
      float[] cropPoints,
      RectF cropRect,
      RectF wholeImageRect,
      int rotation,
      int sampleSize) {
    mOriginalBitmap = originalBitmap;
    mOriginalUri = originalUri;
    mBitmap = bitmap;
    mUri = uri;
    mError = error;
    mCropPoints = cropPoints;
    mCropRect = cropRect;
    mWholeImageRect = wholeImageRect;
    mRotation = rotation;
    mSampleSize = sampleSize;
  }

  /**
   * The image bitmap of the original image loaded for cropping.<br>
   * Null if uri used to load image or activity result is used.
   */
  public Bitmap getOriginalBitmap() {
    return mOriginalBitmap;
  }

  /**
   * The Android uri of the original image loaded for cropping.<br>
   * Null if bitmap was used to load image.
   */
  public Uri getOriginalUri() {
    return mOriginalUri;
  }

  /** Is the result is success or error. */
  public boolean isSuccessful() {
    return mError == null;
  }

  /**
   * The cropped image bitmap result.<br>
   * Null if save cropped image was executed, no output requested or failure.
   */
  public Bitmap getBitmap() {
    return mBitmap;
  }

  /**
   * The Android uri of the saved cropped image result Null if get cropped image was executed, no
   * output requested or failure.
   */
  public Uri getUri() {
    return mUri;
  }

  /** The error that failed the loading/cropping (null if successful) */
  public Exception getError() {
    return mError;
  }

  /** The 4 points of the cropping window in the source image */
  public float[] getCropPoints() {
    return mCropPoints;
  }

  /** The rectangle of the cropping window in the source image */
  public RectF getCropRect() {
    return mCropRect;
  }

  /** The rectangle of the source image dimensions */
  public RectF getWholeImageRect() {
    return mWholeImageRect;
  }

  /** The final rotation of the cropped image relative to source */
  public int getRotation() {
    return mRotation;
  }

  /** sample size used creating the crop bitmap to lower its size */
  public int getSampleSize() {
    return mSampleSize;
  }
}
