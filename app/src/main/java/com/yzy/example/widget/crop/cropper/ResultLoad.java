package com.yzy.example.widget.crop.cropper;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * description :
 *
 * @author : case
 * @date : 2018/8/22 16:05
 */
public class ResultLoad {

  /** The Android URI of the image to load */
  public final Uri uri;

  /** The loaded bitmap */
  public final Bitmap bitmap;

  /** The sample size used to load the given bitmap */
  public final int loadSampleSize;

  /** The degrees the image was rotated */
  public final int degreesRotated;

  /** The error that occurred during async bitmap loading. */
  public final Exception error;

  ResultLoad(Uri uri, Bitmap bitmap, int loadSampleSize, int degreesRotated) {
    this.uri = uri;
    this.bitmap = bitmap;
    this.loadSampleSize = loadSampleSize;
    this.degreesRotated = degreesRotated;
    this.error = null;
  }

  ResultLoad(Uri uri, Exception error) {
    this.uri = uri;
    this.bitmap = null;
    this.loadSampleSize = 0;
    this.degreesRotated = 0;
    this.error = error;
  }
}
