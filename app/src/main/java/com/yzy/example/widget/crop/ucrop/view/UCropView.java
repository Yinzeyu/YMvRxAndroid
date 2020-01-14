package com.yzy.example.widget.crop.ucrop.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.yzy.example.R;
import com.yzy.example.widget.crop.cropper.CropperUtils;
import com.yzy.example.widget.crop.cropper.OnCropFinishListener;
import com.yzy.example.widget.crop.ucrop.callback.CropBoundsChangeListener;
import com.yzy.example.widget.crop.ucrop.callback.OverlayViewChangeListener;

public class UCropView extends FrameLayout {

  private GestureCropImageView mCropImageView;
  private final OverlayView mViewOverlay;
  private Uri mInputUri, mOutputUri;

  public UCropView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public UCropView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    LayoutInflater.from(context).inflate(R.layout.ucrop_view, this, true);
    mCropImageView = (GestureCropImageView) findViewById(R.id.image_view_crop);
    mViewOverlay = (OverlayView) findViewById(R.id.view_overlay);

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ucrop_UCropView);
    mViewOverlay.processStyledAttributes(a);
    mCropImageView.processStyledAttributes(a);
    a.recycle();

    setListenersToViews();
  }

  private void setListenersToViews() {
    mCropImageView.setCropBoundsChangeListener(new CropBoundsChangeListener() {
      @Override
      public void onCropAspectRatioChanged(float cropRatio) {
        mViewOverlay.setTargetAspectRatio(cropRatio);
      }
    });
    mViewOverlay.setOverlayViewChangeListener(new OverlayViewChangeListener() {
      @Override
      public void onCropRectUpdated(RectF cropRect) {
        mCropImageView.setCropRect(cropRect);
      }
    });
  }

  @Override
  public boolean shouldDelayChildPressedState() {
    return false;
  }

  @NonNull
  public GestureCropImageView getCropImageView() {
    return mCropImageView;
  }

  @NonNull
  public OverlayView getOverlayView() {
    return mViewOverlay;
  }

  /**
   * Method for reset state for UCropImageView such as rotation, scale, translation.
   * Be careful: this method recreate UCropImageView instance and reattach it to layout.
   */
  public void resetCropImageView() {
    removeView(mCropImageView);
    mCropImageView = new GestureCropImageView(getContext());
    setListenersToViews();
    mCropImageView.setCropRect(getOverlayView().getCropViewRect());
    addView(mCropImageView, 0);
  }

  public void setImageUri(@NonNull Uri inputUri, @Nullable Uri outUri) throws Exception {
    mInputUri = inputUri;
    mOutputUri = outUri;
    mCropImageView.setImageUri(inputUri, outUri);
  }

  public void cropAndSaveImage(Bitmap.CompressFormat format, int quality,
                               OnCropFinishListener listener) {
    CropperUtils.cropAndSaveImage(mCropImageView, null, mOutputUri, format, quality, listener);
  }

  public void cropImage(Bitmap.CompressFormat format, int quality,
                        OnCropFinishListener listener) {
    CropperUtils.cropAndSaveImage(mCropImageView, mInputUri, mOutputUri, format, quality, listener);
  }
}