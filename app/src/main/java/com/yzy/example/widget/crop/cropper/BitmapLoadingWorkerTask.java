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

import android.net.Uri;
import android.os.AsyncTask;
import android.util.DisplayMetrics;

import com.blankj.utilcode.util.Utils;

/** Task to load bitmap asynchronously from the UI thread. */
public final class BitmapLoadingWorkerTask extends AsyncTask<Void, Void, ResultLoad> {

  // region: Fields and Consts

  /** Use a WeakReference to ensure the ImageView can be garbage collected */
  private final OnLoadFinishListener mLoadFinishListener;

  /** The Android URI of the image to load */
  private final Uri mUri;

  /** required width of the cropping image after density adjustment */
  private final int mWidth;

  /** required height of the cropping image after density adjustment */
  private final int mHeight;
  // endregion

  public BitmapLoadingWorkerTask(Uri uri, OnLoadFinishListener loadFinishListener) {
    mUri = uri;
    mLoadFinishListener = loadFinishListener;
    DisplayMetrics metrics = Utils.getApp().getResources().getDisplayMetrics();
    double densityAdj = metrics.density > 1 ? 1 / metrics.density : 1;
    mWidth = (int) (metrics.widthPixels * densityAdj);
    mHeight = (int) (metrics.heightPixels * densityAdj);
  }

  /** The Android URI that this task is currently loading. */
  public Uri getUri() {
    return mUri;
  }

  /**
   * Decode image in background.
   *
   * @param params ignored
   * @return the decoded bitmap data
   */
  @Override
  protected ResultLoad doInBackground(Void... params) {
    try {
      if (!isCancelled()) {

        BitmapUtils.BitmapSampled decodeResult =
            BitmapUtils.decodeSampledBitmap(Utils.getApp(), mUri, mWidth, mHeight);

        if (!isCancelled()) {

          BitmapUtils.RotateBitmapResult rotateResult =
              BitmapUtils.rotateBitmapByExif(decodeResult.bitmap, Utils.getApp(), mUri);

          return new ResultLoad(
              mUri, rotateResult.bitmap, decodeResult.sampleSize, rotateResult.degrees);
        }
      }
      return null;
    } catch (Exception e) {
      return new ResultLoad(mUri, e);
    }
  }

  /**
   * Once complete, see if ImageView is still around and set bitmap.
   *
   * @param result the result of bitmap loading
   */
  @Override
  protected void onPostExecute(ResultLoad result) {
    if (result != null) {
      boolean completeCalled = false;
      if (!isCancelled()) {
        if (mLoadFinishListener != null) {
          completeCalled = true;
          mLoadFinishListener.loadFinish(result);
        }
      }
      if (!completeCalled && result.bitmap != null) {
        // fast release of unused bitmap
        result.bitmap.recycle();
      }
    }
  }
}
