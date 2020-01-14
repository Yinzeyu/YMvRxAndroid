package com.yzy.example.widget.crop.cropper;

/** Possible options for handling requested width/height for cropping. */
public enum RequestSizeOptions {

  /** No resize/sampling is done unless required for memory management (OOM). */
  NONE,

  /**
   * Only sample the image during loading (if image set using URI) so the smallest of the image
   * dimensions will be between the requested size and x2 requested size.<br>
   * NOTE: resulting image will not be exactly requested width/height see: <a
   * href="http://developer.android.com/training/displaying-bitmaps/load-bitmap.html">Loading
   * Large Bitmaps Efficiently</a>.
   */
  SAMPLING,

  /**
   * Resize the image uniformly (maintain the image's aspect ratio) so that both dimensions (width
   * and height) of the image will be equal to or <b>less</b> than the corresponding requested
   * dimension.<br>
   * If the image is smaller than the requested size it will NOT change.
   */
  RESIZE_INSIDE,

  /**
   * Resize the image uniformly (maintain the image's aspect ratio) to fit in the given
   * width/height.<br>
   * The largest dimension will be equals to the requested and the second dimension will be
   * smaller.<br>
   * If the image is smaller than the requested size it will enlarge it.
   */
  RESIZE_FIT,

  /**
   * Resize the image to fit exactly in the given width/height.<br>
   * This resize method does NOT preserve aspect ratio.<br>
   * If the image is smaller than the requested size it will enlarge it.
   */
  RESIZE_EXACT
}