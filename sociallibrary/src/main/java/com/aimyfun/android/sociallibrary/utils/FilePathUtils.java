package com.aimyfun.android.sociallibrary.utils;

import android.content.Context;
import java.io.File;

/**
 * description :
 *
 * @author : yinzeyu
 * @date : 2018/8/25 15:13
 */
public class FilePathUtils {
  /**
   * 公司文件夹名称
   */
  private static final String COMPANY_FOLDER = "aimyunion";
  /**
   * APP文件夹名称
   */
  private static final String APP_FOLDER = "aimyfun";
  /**
   * 图片
   */
  public static final String IMAGES = "images";

  /**
   * 获取自定义的app的主目录
   */
  public static String getAppPath(Context context) {
    return (SDCardUtils.Companion.isSDCardEnable() ? (SDCardUtils.Companion.getSdCardPaths().get(0)
        + File.separator)
        : context.getCacheDir().getPath() + File.separator)
        + COMPANY_FOLDER
        + File.separator
        + APP_FOLDER
        + File.separator;
  }
}
