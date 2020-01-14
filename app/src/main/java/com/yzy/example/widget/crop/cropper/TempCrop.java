package com.yzy.example.widget.crop.cropper;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * description :
 *
 * @author : case
 * @date : 2018/8/22 16:09
 */
class TempCrop {
  /** The cropped bitmap */
  public final Bitmap bitmap;

  /** The saved cropped bitmap uri */
  public final Uri uri;

  /** The error that occurred during async bitmap cropping. */
  final Exception error;

  /** is the cropping request was to get a bitmap or to save it to uri */
  final boolean isSave;

  /** sample size used creating the crop bitmap to lower its size */
  final int sampleSize;

  TempCrop(Bitmap bitmap, int sampleSize) {
    this.bitmap = bitmap;
    this.uri = null;
    this.error = null;
    this.isSave = false;
    this.sampleSize = sampleSize;
  }

  TempCrop(Uri uri, int sampleSize) {
    this.bitmap = null;
    this.uri = uri;
    this.error = null;
    this.isSave = true;
    this.sampleSize = sampleSize;
  }

  TempCrop(Exception error, boolean isSave) {
    this.bitmap = null;
    this.uri = null;
    this.error = error;
    this.isSave = isSave;
    this.sampleSize = 1;
  }
}
