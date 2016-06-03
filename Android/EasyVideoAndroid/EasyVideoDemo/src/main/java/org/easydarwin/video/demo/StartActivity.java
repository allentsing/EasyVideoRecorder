package org.easydarwin.video.demo;

import org.easydarwin.video.common.OnErrorListener;
import org.easydarwin.video.common.ProgressDialogFactory;
import org.easydarwin.video.common.ToastFactory;
import org.easydarwin.video.recoder.conf.RecorderConfig;
import org.easydarwin.video.recoder.core.EasyVideoRecorder;
import org.easydarwin.video.render.conf.RenderConfig;
import org.easydarwin.video.render.core.EasyVideoRender;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class StartActivity extends Activity {

    Button startVideoRecord;
    boolean testRecord = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setRecorder();//设置 EasyVideoRecorder SDK 参数
        setRender();
        startVideoRecord = (Button) findViewById(R.id.startVideoRecord);
        initView();
        if (testRecord) {
            startVideoRecord.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    EasyVideoRecorder.getInstance().start();//启动拍摄
                }
            });
        } else {
            EasyVideoRender.getInstance().setInputVideo(Environment.getExternalStorageDirectory() + "/1/test2.mp4").start();
        }
    }

    private void setRecorder() {
        RecorderConfig config = RecorderConfig.create(this)//
                .setRecordTimeMax(15 * 1000)
                //设置拍摄的最大长度，单位毫秒
                .setRecordTimeMin(2 * 1000)
                .setPreviewSize(RecorderConfig.PREVIEW_SIZE_SMALL /*PREVIEW_SIZE_BIG PREVIEW_SIZE_SMALL*/)
                //设置预览页面大小
                .setProgressPosition(RecorderConfig.PROGRESS_POSITION_BOTTOM/*PROGRESS_POSITION_TOP PROGRESS_POSITION_BOTTOM*/)// 设置进度条位置 top bottom
                .setBaseDir(new File(this.getExternalFilesDir(null), "org.easydarwin.video").getAbsolutePath());

        EasyVideoRecorder.getInstance()//
                .setRecorderConfig(config)
                .setOnErrorListener(new OnErrorListener() { //设置出错回调函数
                    /**
                     * int code 错误码
                     * String message 错误信息
                     */
                    @Override
                    public void onError(int code, String message) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                })
                .setProgressDialogFactory(new ProgressDialogFactory() {
                    @Override
                    public ProgressDialog create(Context context, int type) {
                        if (type == 1) { // 拍摄界面点击下一步
                            ProgressDialog progressDialog = new ProgressDialog(context);
                            progressDialog.setMessage("正在合并视频");
                            return progressDialog;
                        }
                        if (type == 2) { // 视频界面点击图标预览
                            ProgressDialog progressDialog = new ProgressDialog(context);
                            progressDialog.setMessage("请稍等");
                            return progressDialog;
                        }
                        if (type == 3) { // 视频界面点击完成
                            ProgressDialog progressDialog = new ProgressDialog(context);
                            progressDialog.setMessage("正在合成视频");
                            return progressDialog;
                        }
                        return null;
                    }
                })
                .setToastFactory(new ToastFactory() {////设置toast 显示函数

                    @Override
                    public Toast create(Context arg0, String arg1) {
                        return Toast.makeText(arg0, arg1, Toast.LENGTH_LONG);
                    }
                })
                .setOnFinishListener(new EasyVideoRecorder.OnFinishListener() {// 设置拍摄结果回调函数
                    /**
                     * Activity activity 拍摄界面的activity
                     * String videoFile 拍摄后的视频文件地址，如果是null 则拍摄失败
                     */
                    @Override
                    public void onFinish(Activity activity, String videoFile) {
                        if (videoFile == null) {
                            Toast.makeText(getApplicationContext(), "拍摄失败", Toast.LENGTH_LONG).show();
                        } else {
                            EasyVideoRender.getInstance().setInputVideo(videoFile).start();
                        }
                    }
                });
    }

    private void setRender() {
        RenderConfig config = RenderConfig.create(this).setEndLogoShow(true);
        EasyVideoRender.getInstance()//
                .setRenderConfig(config)
                .setMoreRenderAction(EasyVideoRender.RENDER_TYPE_FILTER, "org.easydarwin.video.demo.DownloadFilterActivity")
                .setMoreRenderAction(EasyVideoRender.RENDER_TYPE_MUSIC, "org.easydarwin.video.demo.DownloadFilterActivity")
                .setOnFinishListener(new EasyVideoRender.OnFinishListener() {// 设置渲染结果回调函数
                    /**
                     * Activity activity 渲染界面的activity
                     * String videoFile 渲染后的视频文件地址，如果是null 则渲染失败
                     */
                    @Override
                    public void onFinish(Activity activity, String videoFile) {
                        if (videoFile == null) {
                            Toast.makeText(getApplicationContext(), "失败", Toast.LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent(getBaseContext(), VideoPlayActivity.class);
                            intent.putExtra("path", videoFile);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), videoFile, Toast.LENGTH_LONG).show();
                        }
                        // activity.finish();
                    }
                });
    }

    private void initView() {
        ObjectAnimator a1 = ObjectAnimator.ofFloat(startVideoRecord, "scaleX", 0.7f);
        a1.setDuration(1000).setRepeatCount(ValueAnimator.INFINITE);
        a1.setRepeatMode(ValueAnimator.REVERSE);
        a1.start();
        ObjectAnimator a2 = ObjectAnimator.ofFloat(startVideoRecord, "scaleY", 0.7f);
        a2.setDuration(1000).setRepeatCount(ValueAnimator.INFINITE);
        a2.setRepeatMode(ValueAnimator.REVERSE);
        a2.start();
    }
}
