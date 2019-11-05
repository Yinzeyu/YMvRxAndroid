package com.yzy.example.utils;

import android.annotation.TargetApi;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static com.yzy.baselibrary.extention.RxSchedulersUtilsKt.applySchedulers;

@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
public class MediaHelper {
    private MediaRecorder mediaRecorder;
    private OnRecorderListener onRecorderStatusListener;
    private OnMaxAmplitudeListener mOnMaxAmplitudeListener;
    /**
     * 单位 0.1秒
     */
    private int duration;
    private boolean canLoop;
    private Disposable disposableMaxAmplitude;
    private Disposable disposableDuration;
    private int BASE = 1;
    private File filePath;

    public MediaHelper(OnRecorderListener onRecorderStatusListener) {
        this.onRecorderStatusListener = onRecorderStatusListener;
    }


    public void setOnMaxAmplitudeListener(
            OnMaxAmplitudeListener onMaxAmplitudeListener) {
        mOnMaxAmplitudeListener = onMaxAmplitudeListener;
    }


    public void startRecorder() {
        File dir = new File(Environment.getExternalStorageDirectory(), "VideoRecorder");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File soundFile = new File(dir, System.currentTimeMillis() + ".amr");
        if (!soundFile.exists()) {
            try {
                soundFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        filePath = soundFile;
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setOnErrorListener(new MediaRecorderOnErrorListener());
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setAudioChannels(1);

        //8000采样率音质
        mediaRecorder.setAudioSamplingRate(8000);
        mediaRecorder.setAudioEncodingBitRate(7950);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(filePath.getAbsolutePath());
        duration = 0;
        canLoop = true;
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (Exception e) {
            if (onRecorderStatusListener != null) {
                onRecorderStatusListener.onRecorder(MediaStatus.prepareError, duration, "准备中出错");
            }
        }
        startDuration();
        startMaxAmplitude();
        if (onRecorderStatusListener != null) {
            onRecorderStatusListener.onRecorder(MediaStatus.recordering, duration, "录音中");
        }
    }

    public void stopRecorder() {
        if (mediaRecorder == null) {
            return;
        }
        canLoop = false;
        try {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        } catch (Exception e) {
            if (onRecorderStatusListener != null) {
                onRecorderStatusListener.onRecorder(MediaStatus.stopError, duration, "停止出错");
            }
        }

        stopDuration();
        stopMaxAmplitude();
    }

    /**
     * 工作线程  每 0.1秒 迭代一次
     */
    private void startDuration() {
        disposableDuration = Observable.interval(0, 100, TimeUnit.MILLISECONDS)
                .compose(applySchedulers())
                .subscribe(aLong -> {
                    if (canLoop) {
                        duration++;
                    }
                });
    }

    private void stopDuration() {
        if (disposableDuration != null && !disposableDuration.isDisposed()) {
            disposableDuration.dispose();
        }
    }

    private void startMaxAmplitude() {
        disposableMaxAmplitude = Observable.interval(0, 100, TimeUnit.MILLISECONDS)
                .compose(applySchedulers())
                .subscribe(aLong -> {
                    if (mediaRecorder == null || !canLoop) {
                        stopMaxAmplitude();
                    }
                    double ratio = (double) mediaRecorder.getMaxAmplitude() / BASE;
                    // 分贝
                    int db = 0;
                    if (ratio > 1) {
                        db = (int) (20 * Math.log10(ratio));
                    }
                    Log.i("xpf", "set getMaxAmplitude" + mediaRecorder.getMaxAmplitude());
                    if (mOnMaxAmplitudeListener != null) {
                        mOnMaxAmplitudeListener.onMaxAmplitude(db);
                    }
                });
    }

    private void stopMaxAmplitude() {
        if (disposableMaxAmplitude != null && !disposableMaxAmplitude.isDisposed()) {
            disposableMaxAmplitude.dispose();
        }
    }

    /**
     * 单位秒
     */
    public int getDuration() {
        return duration / 10;
    }

    public File getVoiceFile() {
        return filePath;
    }

    public enum MediaStatus {
        /**
         * 准备过程中， 出错了
         */
        prepareError,
        /**
         * 录音中
         */
        recordering,
        /**
         * 录音中 出错
         */
        recorderError,
        /**
         * 停止出错
         */
        stopError,
    }

    public interface OnRecorderListener {
        void onRecorder(MediaStatus mediaStatus, int duration, String message);
    }

    public interface OnMaxAmplitudeListener {
        void onMaxAmplitude(int f);
    }

    private final class MediaRecorderOnErrorListener implements OnErrorListener {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            if (mr != null) {
                try {
                    mr.stop();
                    mr.release();
                    mr = null;
                } catch (Exception e) {
                    if (onRecorderStatusListener != null) {
                        onRecorderStatusListener.onRecorder(MediaStatus.recorderError, duration, "录音中出错");
                    }
                }
            }
            if (onRecorderStatusListener != null) {
                onRecorderStatusListener.onRecorder(MediaStatus.recorderError, duration, "录音中出错");
            }
        }
    }

}
