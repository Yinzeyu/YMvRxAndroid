package com.aimyfun.android.sociallibrary.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;

public final class Utils {

  @SuppressLint("StaticFieldLeak")
  private static Application sApplication;

  static final ActivityLifecycleImpl ACTIVITY_LIFECYCLE = new ActivityLifecycleImpl();

  private Utils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }

  /**
   * Init utils.
   * <p>Init it in the class of Application.</p>
   *
   * @param context context
   */
  public static void init(@NonNull final Context context) {
    init((Application) context.getApplicationContext());
  }

  /**
   * Init utils.
   * <p>Init it in the class of Application.</p>
   *
   * @param app application
   */
  public static void init(@NonNull final Application app) {
    if (sApplication == null) {
      Utils.sApplication = app;
      Utils.sApplication.registerActivityLifecycleCallbacks(ACTIVITY_LIFECYCLE);
    }
  }

  /**
   * Return the context of Application object.
   *
   * @return the context of Application object
   */
  public static Application getApp() {
    if (sApplication != null) return sApplication;
    try {
      @SuppressLint("PrivateApi")
      Class<?> activityThread = Class.forName("android.app.ActivityThread");
      Object at = activityThread.getMethod("currentActivityThread").invoke(null);
      Object app = activityThread.getMethod("getApplication").invoke(at);
      if (app == null) {
        throw new NullPointerException("u should init first");
      }
      init((Application) app);
      return sApplication;
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    throw new NullPointerException("u should init first");
  }

  static class ActivityLifecycleImpl implements ActivityLifecycleCallbacks {

    final LinkedList<Activity> mActivityList = new LinkedList<>();
    final HashMap<Object, OnAppStatusChangedListener> mStatusListenerMap = new HashMap<>();

    private int mForegroundCount = 0;
    private int mConfigCount = 0;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
      setTopActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
      setTopActivity(activity);
      if (mForegroundCount <= 0) {
        postStatus(true);
      }
      if (mConfigCount < 0) {
        ++mConfigCount;
      } else {
        ++mForegroundCount;
      }
    }

    @Override
    public void onActivityResumed(Activity activity) {
      setTopActivity(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {/**/}

    @Override
    public void onActivityStopped(Activity activity) {
      if (activity.isChangingConfigurations()) {
        --mConfigCount;
      } else {
        --mForegroundCount;
        if (mForegroundCount <= 0) {
          postStatus(false);
        }
      }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {/**/}

    @Override
    public void onActivityDestroyed(Activity activity) {
      mActivityList.remove(activity);
    }

    private void postStatus(final boolean isForeground) {
      if (mStatusListenerMap.isEmpty()) return;
      for (OnAppStatusChangedListener onAppStatusChangedListener : mStatusListenerMap.values()) {
        if (onAppStatusChangedListener == null) return;
        if (isForeground) {
          onAppStatusChangedListener.onForeground();
        } else {
          onAppStatusChangedListener.onBackground();
        }
      }
    }

    private void setTopActivity(final Activity activity) {
      if (activity.getClass() == PermissionUtils.PermissionActivity.class) return;
      if (mActivityList.contains(activity)) {
        if (!mActivityList.getLast().equals(activity)) {
          mActivityList.remove(activity);
          mActivityList.addLast(activity);
        }
      } else {
        mActivityList.addLast(activity);
      }
    }
  }

  public interface OnAppStatusChangedListener {
    void onForeground();

    void onBackground();
  }
}
