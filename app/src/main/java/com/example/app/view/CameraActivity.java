package com.example.app.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.app.AppConstant;
import com.example.app.R;
import com.example.app.smalllib.AutoVBRMode;
import com.example.app.smalllib.BaseMediaBitrateConfig;
import com.example.app.smalllib.MediaObject;
import com.example.app.smalllib.MediaRecorderBase;
import com.example.app.smalllib.MediaRecorderConfig;
import com.example.app.smalllib.MediaRecorderNative;
import com.example.app.smalllib.ProgressView;
import com.example.app.smalllib.VCamera;
import com.example.app.utils.BitmapUtils;
import com.example.app.utils.CameraUtil;
import com.example.app.utils.FileUtils;
import com.example.app.utils.GlideUtils;
import com.example.app.utils.LogUtils;
import com.example.app.utils.StringUtils;
import com.example.app.utils.SystemUtils;
import com.yixia.videoeditor.adapter.UtilityAdapter;

import java.io.File;
import java.io.IOException;

public class CameraActivity extends Activity
        implements SurfaceHolder.Callback,
        View.OnClickListener,
        MediaRecorderBase.OnErrorListener,
        MediaRecorderBase.OnEncodeListener,
        MediaRecorderBase.OnPreparedListener {
    private Camera mCamera;
    private SurfaceView surfaceView;
    private SurfaceHolder mHolder;
    private int mCameraId = 0;
    private Context context;

    //屏幕宽高
    private int screenWidth;
    private int screenHeight;
    private LinearLayout home_custom_top_relative;
    private ImageView camera_delay_time;
    private ImageView flash_light;
    private TextView camera_delay_time_text;//延迟拍照倒计时
    private int index;
    //底部高度 主要是计算切换正方形时的动画高度
    private int menuPopviewHeight;
    //动画高度
    private int animHeight;
    //闪光灯模式 0:关闭 1: 开启 2: 自动
    private int light_num = 0;
    //延迟时间
    private int delay_time;
    private int delay_time_temp;
    private boolean isview = false;
    private boolean is_camera_delay;
    private ImageView camera_frontback;
    private ImageView camera_close;
    private RelativeLayout homecamera_bottom_relative;
    private ImageView img_camera;
    private int picHeight;
    private RecyclerView recycler_pic;
    private MyAdapter mAdapterPic;

    //长按录制
    private MediaRecorderNative mMediaRecorder;
    private MediaObject mMediaObject;
    /**
     * 录制最长时间
     */
    private static int RECORD_TIME_MAX = 6 * 1000;
    /**
     * 是否是点击状态
     */
    private volatile boolean mPressedStatus;
    /**
     * 延迟拍摄停止
     */
    private static final int HANDLE_STOP_RECORD = 1;
    /**
     * 回删按钮、延时按钮、滤镜按钮
     */
    private TextView mRecordDelete;
    /**
     * 录制进度
     */
    private ProgressView mProgressView;
    /**
     * 录制最小时间
     */
    private static int RECORD_TIME_MIN = (int) (1.5f * 1000);
    /**
     * 刷新进度条
     */
    private static final int HANDLE_INVALIDATE_PROGRESS = 0;
    //录制完成点击确定
    private TextView mTitleNext;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        context = this;
        initView();
        initCameraData();
        initData();
        initMediaRecorder();
    }

    private void initData() {
        Intent intent = getIntent();
        MediaRecorderConfig mediaRecorderConfig = getMediaRecorderConfig();
        if (mediaRecorderConfig == null) {
            return;
        }
        RECORD_TIME_MAX = mediaRecorderConfig.getRecordTimeMax();
        RECORD_TIME_MIN = mediaRecorderConfig.getRecordTimeMin();
        MediaRecorderBase.MAX_FRAME_RATE = mediaRecorderConfig.getMaxFrameRate();
        MediaRecorderBase.MIN_FRAME_RATE = mediaRecorderConfig.getMinFrameRate();
        MediaRecorderBase.SMALL_VIDEO_HEIGHT = mediaRecorderConfig.getSmallVideoHeight();
        MediaRecorderBase.SMALL_VIDEO_WIDTH = mediaRecorderConfig.getSmallVideoWidth();
        MediaRecorderBase.mVideoBitrate = mediaRecorderConfig.getVideoBitrate();
        MediaRecorderBase.mediaRecorderConfig = mediaRecorderConfig.getMediaBitrateConfig();
        MediaRecorderBase.compressConfig = mediaRecorderConfig.getCompressConfig();
        MediaRecorderBase.CAPTURE_THUMBNAILS_TIME = mediaRecorderConfig.getCaptureThumbnailsTime();
        MediaRecorderBase.doH264Compress = mediaRecorderConfig.isDoH264Compress();
//        GO_HOME = mediaRecorderConfig.isGO_HOME();
    }

    private void initView() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mHolder = surfaceView.getHolder();
        mHolder.addCallback(this);
        img_camera = (ImageView) findViewById(R.id.img_camera);
        img_camera.setOnClickListener(this);
        img_camera.setOnTouchListener(mOnVideoControllerTouchListener);

        //关闭相机界面按钮
        camera_close = (ImageView) findViewById(R.id.camera_close);
        camera_close.setOnClickListener(this);

        //top 的view
        home_custom_top_relative = (LinearLayout) findViewById(R.id.home_custom_top_relative);
        home_custom_top_relative.setAlpha(0.5f);

        //前后摄像头切换
        camera_frontback = (ImageView) findViewById(R.id.camera_frontback);
        camera_frontback.setOnClickListener(this);

        //延迟拍照时间
        camera_delay_time = (ImageView) findViewById(R.id.camera_delay_time);
        camera_delay_time.setOnClickListener(this);

        //闪光灯
        flash_light = (ImageView) findViewById(R.id.flash_light);
        flash_light.setOnClickListener(this);
        camera_delay_time_text = (TextView) findViewById(R.id.camera_delay_time_text);
        homecamera_bottom_relative = (RelativeLayout) findViewById(R.id.homecamera_bottom_relative);

        //删除
        mRecordDelete = findViewById(R.id.record_delete);
        mRecordDelete.setOnClickListener(this);

        //录制进度条
        mProgressView = (ProgressView) findViewById(R.id.record_progress);

        mTitleNext = (TextView) findViewById(R.id.title_next);
        mTitleNext.setOnClickListener(this);
        mProgressView.setMaxDuration(RECORD_TIME_MAX);
        mProgressView.setMinTime(RECORD_TIME_MIN);

    }

    private void initCameraData() {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        menuPopviewHeight = screenHeight - screenWidth * 4 / 3;
        animHeight = (screenHeight - screenWidth - menuPopviewHeight - SystemUtils.dp2px(context, 44)) / 2;
        //这里相机取景框我这是为宽高比3:4 所以限制底部控件的高度是剩余部分
        RelativeLayout.LayoutParams bottomParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, menuPopviewHeight);
        bottomParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        homecamera_bottom_relative.setLayoutParams(bottomParam);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            switch (what) {
                case AppConstant.WHAT.SUCCESS:
                    if (delay_time > 0) {
                        camera_delay_time_text.setText("" + delay_time);
                    }

                    try {
                        if (delay_time == 0) {
                            captrue();
                            is_camera_delay = false;
                            camera_delay_time_text.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        return;
                    }

                    break;

                case AppConstant.WHAT.ERROR:
                    is_camera_delay = false;
                    break;

            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_camera:
                if (isview) {
                    if (delay_time == 0) {
                        switch (light_num) {
                            case 0:
                                //关闭
                                CameraUtil.getInstance().turnLightOff(mCamera);
                                break;
                            case 1:
                                CameraUtil.getInstance().turnLightOn(mCamera);
                                break;
                            case 2:
                                //自动
                                CameraUtil.getInstance().turnLightAuto(mCamera);
                                break;
                        }
                        captrue();
                    } else {
                        camera_delay_time_text.setVisibility(View.VISIBLE);
                        camera_delay_time_text.setText(String.valueOf(delay_time));
                        is_camera_delay = true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (delay_time > 0) {
                                    //按秒数倒计时
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        mHandler.sendEmptyMessage(AppConstant.WHAT.ERROR);
                                        return;
                                    }
                                    delay_time--;
                                    mHandler.sendEmptyMessage(AppConstant.WHAT.SUCCESS);
                                }
                            }
                        }).start();
                    }
                    isview = false;
                }
                break;
            //前后置摄像头拍照
            case R.id.camera_frontback:
                switchCamera();
                break;

            //退出相机界面 释放资源
            case R.id.camera_close:
                if (is_camera_delay) {
                    Toast.makeText(CameraActivity.this, "正在拍照请稍后...", Toast.LENGTH_SHORT).show();
                    return;
                }
                finish();
                break;

            //闪光灯
            case R.id.flash_light:
                if (mCameraId == 1) {
                    //前置
                    Toast.makeText(context, "请切换为后置摄像头开启闪光灯", Toast.LENGTH_SHORT).show();
                    return;
                }
                Camera.Parameters parameters = mCamera.getParameters();
                switch (light_num) {
                    case 0:
                        //打开
                        light_num = 1;
                        flash_light.setImageResource(R.drawable.btn_camera_flash_on);
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//开启
                        mCamera.setParameters(parameters);
                        break;
                    case 1:
                        //自动
                        light_num = 2;
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                        mCamera.setParameters(parameters);
                        flash_light.setImageResource(R.drawable.btn_camera_flash_auto);
                        break;
                    case 2:
                        //关闭
                        light_num = 0;
                        //关闭
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        mCamera.setParameters(parameters);
                        flash_light.setImageResource(R.drawable.btn_camera_flash_off);
                        break;
                }

                break;

            //延迟拍照时间
            case R.id.camera_delay_time:
                switch (delay_time) {
                    case 0:
                        delay_time = 3;
                        delay_time_temp = delay_time;
                        camera_delay_time.setImageResource(R.drawable.btn_camera_timing_3);
                        break;

                    case 3:
                        delay_time = 5;
                        delay_time_temp = delay_time;
                        camera_delay_time.setImageResource(R.drawable.btn_camera_timing_5);
                        break;

                    case 5:
                        delay_time = 10;
                        delay_time_temp = delay_time;
                        camera_delay_time.setImageResource(R.drawable.btn_camera_timing_10);
                        break;

                    case 10:
                        delay_time = 0;
                        delay_time_temp = delay_time;
                        camera_delay_time.setImageResource(R.drawable.btn_camera_timing_0);
                        break;

                }
                break;
            //录制视频删除
            case R.id.record_delete:
                if (mMediaObject != null) {
                    MediaObject.MediaPart part = mMediaObject.getCurrentPart();
                    if (part != null) {
                        if (part.remove) {
                            part.remove = false;
//                        mRecordDelete.setChecked(false);
                            if (mProgressView != null)
                                mProgressView.invalidate();
                        }
                    }
                }
                break;
            case R.id.title_next:
                mMediaRecorder.startEncoding();
                break;
        }
    }

    //切换前/后置摄像头
    public void switchCamera() {
        releaseCamera();
        mCameraId = (mCameraId + 1) % mCamera.getNumberOfCameras();
        mCamera = getCamera(mCameraId);
        if (mHolder != null) {
            startPreview(mCamera, mHolder);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = getCamera(mCameraId);
            if (mHolder != null) {
                startPreview(mCamera, mHolder);
            }
        }
        UtilityAdapter.freeFilterParser();
        UtilityAdapter.initFilterParser();
        if (mMediaRecorder == null) {
            initMediaRecorder();
        } else {
            mMediaRecorder.prepare();
            mProgressView.setData(mMediaObject);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();

        stopRecord();
        UtilityAdapter.freeFilterParser();
    }

    /**
     * 获取Camera实例
     *
     * @return
     */
    private Camera getCamera(int id) {
        Camera camera = null;
        try {
            camera = Camera.open(id);
        } catch (Exception e) {

        }
        return camera;
    }

    /**
     * 预览相机
     */
    private void startPreview(Camera camera, SurfaceHolder holder) {
        try {
            setupCamera(camera);
            camera.setPreviewDisplay(holder);
            //亲测的一个方法 基本覆盖所有手机 将预览矫正
            CameraUtil.getInstance().setCameraDisplayOrientation(this, mCameraId, camera);
//            camera.setDisplayOrientation(90);
            camera.startPreview();
            isview = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void captrue() {
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                isview = false;
                //将data 转换为位图 或者你也可以直接保存为文件使用 FileOutputStream
                //这里我相信大部分都有其他用处把 比如加个水印 后续再讲解
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Bitmap saveBitmap = CameraUtil.getInstance().setTakePicktrueOrientation(mCameraId, bitmap);
                saveBitmap = Bitmap.createScaledBitmap(saveBitmap, screenWidth, picHeight, true);
                if (index == 1) {
                    //正方形 animHeight(动画高度)
                    saveBitmap = Bitmap.createBitmap(saveBitmap, 0, animHeight + SystemUtils.dp2px(context, 44), screenWidth, screenWidth);
                } else {
                    //正方形 animHeight(动画高度)
                    saveBitmap = Bitmap.createBitmap(saveBitmap, 0, 0, screenWidth, screenWidth * 4 / 3);
                }
                String img_path = getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath() + File.separator + System.currentTimeMillis() + ".jpeg";
                BitmapUtils.saveJPGE_After(context, saveBitmap, img_path, 100);
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }
                if (!saveBitmap.isRecycled()) {
                    saveBitmap.recycle();
                }
//                Intent intent = new Intent();
//                intent.putExtra(AppConstant.KEY.IMG_PATH, img_path);
//                intent.putExtra(AppConstant.KEY.PIC_WIDTH, screenWidth);
//                intent.putExtra(AppConstant.KEY.PIC_HEIGHT, picHeight);
//                setResult(AppConstant.RESULT_CODE.RESULT_OK, intent);
//                finish();

                //这里打印宽高 就能看到 CameraUtil.getInstance().getPropPictureSize(parameters.getSupportedPictureSizes(), 200);
                // 这设置的最小宽度影响返回图片的大小 所以这里一般这是1000左右把我觉得
                Log.e("img_path==", img_path + "");
                Log.e("bitmapWidth==", bitmap.getWidth() + "");
                Log.e("bitmapHeight==", bitmap.getHeight() + "");

                notifyRecyclerView(img_path);

                //拍完一张可以继续拍
                releaseCamera();
                mCamera = getCamera(mCameraId);
                if (mHolder != null) {
                    startPreview(mCamera, mHolder);
                }
            }
        });
    }


    /**
     * 设置
     */
    private void setupCamera(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        if (parameters.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        //这里第三个参数为最小尺寸 getPropPreviewSize方法会对从最小尺寸开始升序排列 取出所有支持尺寸的最小尺寸
        Camera.Size previewSize = CameraUtil.getInstance().getPropSizeForHeight(parameters.getSupportedPreviewSizes(), 800);
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        Camera.Size pictrueSize = CameraUtil.getInstance().getPropSizeForHeight(parameters.getSupportedPictureSizes(), 800);
        parameters.setPictureSize(pictrueSize.width, pictrueSize.height);
        camera.setParameters(parameters);
        /**
         * 设置surfaceView的尺寸 因为camera默认是横屏，所以取得支持尺寸也都是横屏的尺寸
         * 我们在startPreview方法里面把它矫正了过来，但是这里我们设置设置surfaceView的尺寸的时候要注意 previewSize.height<previewSize.width
         * previewSize.width才是surfaceView的高度
         * 一般相机都是屏幕的宽度 这里设置为屏幕宽度 高度自适应 你也可以设置自己想要的大小
         *
         */
        picHeight = (screenWidth * pictrueSize.width) / pictrueSize.height;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(screenWidth, (screenWidth * pictrueSize.width) / pictrueSize.height);
        //这里当然可以设置拍照位置 比如居中 我这里就置顶了
        //params.gravity = Gravity.CENTER;
        surfaceView.setLayoutParams(params);
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startPreview(mCamera, holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        startPreview(mCamera, holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    private void notifyRecyclerView(String path) {
        if (recycler_pic == null) {
            recycler_pic = findViewById(R.id.recycler_pic);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recycler_pic.setLayoutManager(layoutManager);
            mAdapterPic = new MyAdapter(R.layout.item_shop_pic);
            recycler_pic.setAdapter(mAdapterPic);
        }
        mAdapterPic.addData(path);
    }

    @Override
    public void onVideoError(int what, int extra) {

    }

    @Override
    public void onAudioError(int what, String message) {

    }

    @Override
    public void onEncodeStart() {
        showProgress("", "压缩中", -1);
    }

    @Override
    public void onEncodeProgress(int progress) {
        //压缩或转码进度
        Log.e("TAGresponse", "" + progress);
    }

    @Override
    public void onEncodeComplete() {
        hideProgress();
        Intent intent = null;
//        try {
//            intent = new Intent(this, Class.forName(getIntent().getStringExtra(OVER_ACTIVITY_NAME)));
//            intent.putExtra(OUTPUT_DIRECTORY, mMediaObject.getOutputDirectory());
//            if (compressConfig != null) {
//                intent.putExtra(MediaRecorderActivity.VIDEO_URI, mMediaObject.getOutputTempTranscodingVideoPath());
//            } else {
//                intent.putExtra(MediaRecorderActivity.VIDEO_URI, mMediaObject.getOutputTempVideoPath());
//            }
//            intent.putExtra(MediaRecorderActivity.VIDEO_SCREENSHOT, mMediaObject.getOutputVideoThumbPath());
//            intent.putExtra("go_home", GO_HOME);
////            startActivity(intent);
//            this.setResult(0, intent);
//
//
//        } catch (ClassNotFoundException e) {
//            throw new IllegalArgumentException("需要传入录制完成后跳转的Activity的全类名");
//        }

        finish();
    }

    @Override
    public void onEncodeError() {
        hideProgress();
        Toast.makeText(this, "转码失败", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onPrepared() {
        releaseCamera();
        mCamera = getCamera(mCameraId);
        if (mHolder != null) {
            startPreview(mCamera, mHolder);
        }
    }

    class MyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        MyAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, final String item) {
            GlideUtils.INSTANCE.loadPic(mContext, item, (ImageView) helper.getView(R.id.iv));
            helper.getView(R.id.del).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(getData().indexOf(item));
                    notifyDataSetChanged();
                }
            });
        }
    }

    /**
     * 初始化拍摄SDK
     */
    private void initMediaRecorder() {
        try {
            mMediaRecorder = new MediaRecorderNative();

            mMediaRecorder.setOnErrorListener(this);
            mMediaRecorder.setOnEncodeListener(this);
            mMediaRecorder.setOnPreparedListener(this);

            File f = new File(VCamera.getVideoCachePath());
            if (!FileUtils.checkFile(f)) {
                f.mkdirs();
            }
            String key = String.valueOf(System.currentTimeMillis());
            mMediaObject = mMediaRecorder.setOutputDirectory(key,
                    VCamera.getVideoCachePath() + key);
            mMediaRecorder.setSurfaceHolder(surfaceView.getHolder());
            mMediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.INSTANCE.showLog(this, e.toString());
        }
    }
    //长按录制
    /**
     * 点击屏幕录制
     */
    private View.OnTouchListener mOnVideoControllerTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mMediaRecorder == null) {
                return false;
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 检测是否手动对焦
                    // 判断是否已经超时
                    if (mMediaObject.getDuration() >= RECORD_TIME_MAX) {
                        return true;
                    }
                    // 取消回删
                    if (cancelDelete())
                        return true;
                    startRecord();
                    break;
                case MotionEvent.ACTION_UP:
                    // 暂停
                    if (mPressedStatus) {
                        stopRecord();
                        // 检测是否已经完成
                        if (mMediaObject.getDuration() >= RECORD_TIME_MAX) {
                            mTitleNext.performClick();
                        }
                    }
                    break;
            }
            return true;
        }
    };

    /**
     * 停止录制
     */
    private void stopRecord() {
        mPressedStatus = false;
        img_camera.animate().scaleX(1).scaleY(1).setDuration(500).start();

        if (mMediaRecorder != null) {
            mMediaRecorder.stopRecord();
        }

        mRecordDelete.setVisibility(View.VISIBLE);
        flash_light.setEnabled(true);
        camera_frontback.setEnabled(true);


        mHandler.removeMessages(HANDLE_STOP_RECORD);
        mHandler.removeMessages(2);

        checkStatus();
    }

    /**
     * 检查录制时间，显示/隐藏下一步按钮
     */
    private int checkStatus() {
        int duration = 0;
        if (!isFinishing() && mMediaObject != null) {
            duration = mMediaObject.getDuration();
            if (duration < RECORD_TIME_MIN) {
                if (duration == 0) {
                    camera_frontback.setVisibility(View.VISIBLE);
                    mRecordDelete.setVisibility(View.GONE);
                }
                // 视频必须大于3秒
                if (mTitleNext.getVisibility() != View.INVISIBLE)
                    mTitleNext.setVisibility(View.INVISIBLE);
            } else {
                // 下一步
                if (mTitleNext.getVisibility() != View.VISIBLE) {
                    mTitleNext.setVisibility(View.VISIBLE);
                }
            }
        }
        return duration;
    }

    /**
     * 取消回删
     */
    private boolean cancelDelete() {
        if (mMediaObject != null) {
            MediaObject.MediaPart part = mMediaObject.getCurrentPart();
            if (part != null && part.remove) {
                part.remove = false;
//                mRecordDelete.setChecked(false);

                if (mProgressView != null)
                    mProgressView.invalidate();

                return true;
            }
        }
        return false;
    }

    /**
     * 开始录制
     */
    private void startRecord() {
        if (mMediaRecorder != null) {

            MediaObject.MediaPart part = mMediaRecorder.startRecord();
            if (part == null) {
                return;
            }
            // 如果使用MediaRecorderSystem，不能在中途切换前后摄像头，否则有问题
//            if (mMediaRecorder instanceof MediaRecorderSystem) {
//                camera_frontback.setVisibility(View.GONE);
//            }
            mProgressView.setData(mMediaObject);
        }

        mPressedStatus = true;
//		TODO 开始录制的图标
        img_camera.animate().scaleX(0.8f).scaleY(0.8f).setDuration(500).start();
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(2, 1000);
            mHandler.removeMessages(HANDLE_INVALIDATE_PROGRESS);
            mHandler.sendEmptyMessage(HANDLE_INVALIDATE_PROGRESS);
            mHandler.removeMessages(HANDLE_STOP_RECORD);
            mHandler.sendEmptyMessageDelayed(HANDLE_STOP_RECORD,
                    RECORD_TIME_MAX - mMediaObject.getDuration());
        }
        mRecordDelete.setVisibility(View.GONE);
        camera_frontback.setEnabled(false);
        flash_light.setEnabled(false);
    }

    public ProgressDialog showProgress(String title, String message, int theme) {

        if (mProgressDialog == null) {
            if (theme > 0)
                mProgressDialog = new ProgressDialog(this, theme);
            else
                mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.setCanceledOnTouchOutside(false);// 不能取消
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);// 设置进度条是否不明确
        }

        if (!StringUtils.INSTANCE.isEmpty(title))
            mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
        return mProgressDialog;
    }

    public void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }


    private MediaRecorderConfig getMediaRecorderConfig() {
        String width = "480";  //宽，，对应	mMediaRecorder.setVideoSize(640, 360);//after setVideoSource(),after setOutFormat()
        String height = "640";  //高
//        String width = "768";  //宽
//        String height = "1080";  //高
        String maxFramerate = "15"; //最大帧率
        String maxTime = "60000"; //最大录制时间
        String minTime = "1500";
        BaseMediaBitrateConfig recordMode;
        BaseMediaBitrateConfig compressMode = null;
        recordMode = new AutoVBRMode(Integer.valueOf("15"));  //第一次压缩比例
        recordMode.setVelocity("ultrafast");
        compressMode = new AutoVBRMode(Integer.valueOf("28"));
        compressMode.setVelocity("ultrafast");
        return new MediaRecorderConfig.Buidler()
                .doH264Compress(compressMode)
                .setMediaBitrateConfig(recordMode)
                .smallVideoWidth(Integer.valueOf(width))
                .smallVideoHeight(Integer.valueOf(height))
                .recordTimeMax(Integer.valueOf(maxTime))
                .maxFrameRate(Integer.valueOf(maxFramerate))
                .captureThumbnailsTime(1)
                .recordTimeMin(Integer.valueOf(minTime))
                .build();
    }
}