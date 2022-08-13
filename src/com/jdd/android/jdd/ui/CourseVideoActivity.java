package com.jdd.android.jdd.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.jdd.android.jdd.R;
import com.jdd.android.jdd.controllers.CourseDetailController;
import com.jdd.android.jdd.entities.CourseEntity;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.jit.video.DensityUtil;
import com.jit.video.FullScreenVideoView;
import com.jit.video.LightnessController;
import com.jit.video.VolumeController;
import com.thinkive.adf.listeners.ListenerControllerAdapter;
import com.thinkive.android.app_engine.engine.TKActivity;
import com.thinkive.android.app_engine.utils.ScreenUtils;
import com.thinkive.android.app_engine.utils.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 描述：课程详细界面
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-03-04
 * @last_edit 2016-03-04
 * @since 1.0
 */
public class CourseVideoActivity extends TKActivity {
    public static final String KEY_COURSE_ENTITY = "ENTITY";
    // 自动隐藏顶部和底部View的时间
    private static final int HIDE_TIME = 5000;
    public boolean isPlayWithoutWifi = false;
    public Dialog dialogConfirmToPlay;
    //播放视频控件
    // 自定义VideoView
    public FullScreenVideoView video;
    public TextView tvAbout;
    public CourseEntity courseEntity;

    private TextView mTVActivityTitle;
    private ImageButton mBack;
    // 底部View
    private View mBottomView;
    private View mTopBar;
    // 视频播放拖动条
    private SeekBar mSeekBar;
    private ImageView mIVPlay;
    private TextView mTVPlayTime;
    private TextView mTVDurationTime;
    private RelativeLayout mRLVideo;
    private Button mBtnCancelPlay, mBtnConfirmPlay;
    private ImageButton mIBtnToPPT;
    private Button mBtnAbout;

    private CourseDetailController mController;

    // 音频管理器
    private AudioManager mAudioManager;
    // 声音调节Toast
    private VolumeController mVolumeController;
    // 屏幕宽高
    private float mWidth;
    private float mHeight;
    // 视频播放时间
    private int playTime;
    // 原始屏幕亮度
    private int mOriginalLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_video);

        findViews();
        setListeners();
        initData();
        initViews();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mWidth = DensityUtil.getWidthInPx(this);
        mHeight = DensityUtil.getHeightInPx(this);
        mThreshold = DensityUtil.dip2px(this, 18);
        mOriginalLight = LightnessController.getLightness(this);
        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mSVRoot.scrollTo(0, 0);
    }

    @Override
    protected void findViews() {
        mTVActivityTitle = (TextView) findViewById(R.id.tv_activity_title);
        mBack = (ImageButton) findViewById(R.id.ibtn_back);
        video = (FullScreenVideoView) findViewById(com.jit.video.R.id.videoview);
        mTVPlayTime = (TextView) findViewById(com.jit.video.R.id.tv_play_time);
        mTVDurationTime = (TextView) findViewById(com.jit.video.R.id.tv_total_time);
        mIVPlay = (ImageView) findViewById(com.jit.video.R.id.btn_play);
        mSeekBar = (SeekBar) findViewById(com.jit.video.R.id.seek_video);
        mBottomView = findViewById(com.jit.video.R.id.bottom_layout);
        mRLVideo = (RelativeLayout) findViewById(R.id.rl_video);
        dialogConfirmToPlay = new Dialog(this);
        dialogConfirmToPlay.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialogConfirmToPlay.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogConfirmToPlay.setContentView(R.layout.dialog_confirm);
        dialogConfirmToPlay.setCanceledOnTouchOutside(true);
        mBtnCancelPlay = (Button) dialogConfirmToPlay.findViewById(R.id.btn_cancel);
        mBtnConfirmPlay = (Button) dialogConfirmToPlay.findViewById(R.id.btn_confirm);
        mIBtnToPPT = (ImageButton) findViewById(R.id.ibtn_to_ppt);
        mBtnAbout = (Button) findViewById(R.id.btn_about);
        tvAbout = (TextView) findViewById(R.id.tv_video_about);
        mTopBar = findViewById(R.id.rl_top_bar);
    }

    @Override
    protected void setListeners() {
        mController = new CourseDetailController(this);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBack, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIVPlay, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnCancelPlay, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnConfirmPlay, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mIBtnToPPT, mController);
        registerListener(ListenerControllerAdapter.ON_CLICK, mBtnAbout, mController);
    }

    @Override
    protected void initData() {
        courseEntity = (CourseEntity) getIntent().getSerializableExtra(KEY_COURSE_ENTITY);
    }

    @Override
    protected void initViews() {
        mTVActivityTitle.setText("课程学习");
        String videoUrl = courseEntity.getVideoUrl().split(";")[0];
        videoUrl = ProjectUtil.getVideoFullUrl(videoUrl);
        video.setVideoPath(videoUrl);
        if (!StringUtils.isEmpty(courseEntity.getAbout())) {
            tvAbout.setText(courseEntity.getCourseName() + "\n" + courseEntity.getAbout());
        }
        if (StringUtils.isEmpty(courseEntity.getPptUrl())) {
            mIBtnToPPT.setVisibility(View.GONE);
        }

        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                video.setVideoWidth((int) ScreenUtils.getScreenWidth(CourseVideoActivity.this));
                video.setVideoHeight((int) ScreenUtils.getScreenHeight(CourseVideoActivity.this));
            }
        });
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mIVPlay.setImageResource(com.jit.video.R.drawable.btn_video_off);
                mTVPlayTime.setText("00:00");
                mTVDurationTime.setText(formatTime(video.getDuration()));
                mSeekBar.setProgress(0);
            }
        });
        video.setOnTouchListener(mTouchListener);
    }

    public void onPlay() {
        if (video.isPlaying()) {
            video.pause();
            mIVPlay.setImageResource(com.jit.video.R.drawable.btn_video_off);
        } else {
            if (!dialogConfirmToPlay.isShowing() && !isWifiConnected() && !isPlayWithoutWifi) {
                dialogConfirmToPlay.show();
                return;
            }
            mIVPlay.setImageResource(com.jit.video.R.drawable.btn_video_playing);
            video.start();
            if (playTime != 0) {
                video.seekTo(playTime);
            }

            mHandler.removeCallbacks(hideRunnable);
            mHandler.postDelayed(hideRunnable, HIDE_TIME);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    mHandler.sendEmptyMessage(1);
                }
            }, 0, 1000);
        }
    }

    public boolean isWifiConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiNetworkInfo && wifiNetworkInfo.isConnected()) {
            return true;
        }

        return false;
    }

    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mHandler.postDelayed(hideRunnable, HIDE_TIME);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mHandler.removeCallbacks(hideRunnable);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                int time = progress * video.getDuration() / 100;
                video.seekTo(time);
            }
        }
    };

    private void backward(float deltaX) {
        int current = video.getCurrentPosition();
        int backwardTime = (int) (deltaX / mWidth * video.getDuration());
        int currentTime = current - backwardTime;
        video.seekTo(currentTime);
        mSeekBar.setProgress(currentTime * 100 / video.getDuration());
        mTVPlayTime.setText(formatTime(currentTime));
    }

    private void forward(float deltaX) {
        int current = video.getCurrentPosition();
        int forwardTime = (int) (deltaX / mWidth * video.getDuration());
        int currentTime = current + forwardTime;
        video.seekTo(currentTime);
        mSeekBar.setProgress(currentTime * 100 / video.getDuration());
        mTVPlayTime.setText(formatTime(currentTime));
    }

    private void volumeDown(float delatY) {
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int down = (int) (delatY / mHeight * max * 3);
        int volume = Math.max(current - down, 0);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        int transformatVolume = volume * 100 / max;
        mVolumeController.show(transformatVolume);
    }

    private void volumeUp(float delatY) {
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int up = (int) ((delatY / mHeight) * max * 3);
        int volume = Math.min(current + up, max);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        int transformatVolume = volume * 100 / max;
        mVolumeController.show(transformatVolume);
    }

    private void lightDown(float delatY) {
        int down = (int) (delatY / mHeight * 255 * 3);
        int transformatLight = LightnessController.getLightness(this) - down;
        LightnessController.setLightness(this, transformatLight);
    }

    private void lightUp(float delatY) {
        int up = (int) (delatY / mHeight * 255 * 3);
        int transformatLight = LightnessController.getLightness(this) + up;
        LightnessController.setLightness(this, transformatLight);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
        mHandler.removeCallbacksAndMessages(null);
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (video.getCurrentPosition() > 0) {
                        mTVPlayTime.setText(formatTime(video.getCurrentPosition()));
                        int progress = video.getCurrentPosition() * 100 / video.getDuration();
                        mSeekBar.setProgress(progress);
                        if (video.getCurrentPosition() > video.getDuration() - 100) {
                            mTVPlayTime.setText("00:00");
                            mSeekBar.setProgress(0);
                        }
                        mSeekBar.setSecondaryProgress(video.getBufferPercentage());
                    } else {
                        mTVPlayTime.setText("00:00");
                        mSeekBar.setProgress(0);
                    }

                    break;
                case 2:
                    showOrHide();
                    break;

                default:
                    break;
            }
        }
    };

    private Runnable hideRunnable = new Runnable() {

        @Override
        public void run() {
            showOrHide();
        }
    };

    @SuppressLint("SimpleDateFormat")
    private String formatTime(long time) {
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(new Date(time));
    }

    private float mLastMotionX;
    private float mLastMotionY;
    private int mStartX;
    private int mStartY;
    private int mThreshold;
    private boolean mIsClick = true;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final float x = event.getX();
            final float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastMotionX = x;
                    mLastMotionY = y;
                    mStartX = (int) x;
                    mStartY = (int) y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaX = x - mLastMotionX;
                    float deltaY = y - mLastMotionY;
                    float absDeltaX = Math.abs(deltaX);
                    float absDeltaY = Math.abs(deltaY);
                    // 声音调节标识
                    boolean isAdjustAudio = false;
                    if (absDeltaX > mThreshold && absDeltaY > mThreshold) {
                        if (absDeltaX < absDeltaY) {
                            isAdjustAudio = true;
                        } else {
                            isAdjustAudio = false;
                        }
                    } else if (absDeltaX < mThreshold && absDeltaY > mThreshold) {
                        isAdjustAudio = true;
                    } else if (absDeltaX > mThreshold && absDeltaY < mThreshold) {
                        isAdjustAudio = false;
                    } else {
                        return true;
                    }
                    if (isAdjustAudio) {
                        if (x < mWidth / 2) {
                            if (deltaY > 0) {
                                lightDown(absDeltaY);
                            } else if (deltaY < 0) {
                                lightUp(absDeltaY);
                            }
                        } else {
                            //TODO 取消声音控制
//                            if (deltaY > 0) {
//                                volumeDown(absDeltaY);
//                            } else if (deltaY < 0) {
//                                volumeUp(absDeltaY);
//                            }
                        }

                    } else {
                        if (deltaX > 0) {
                            forward(absDeltaX);
                        } else if (deltaX < 0) {
                            backward(absDeltaX);
                        }
                    }
                    mLastMotionX = x;
                    mLastMotionY = y;
                    break;
                case MotionEvent.ACTION_UP:
                    if (Math.abs(x - mStartX) > mThreshold
                            || Math.abs(y - mStartY) > mThreshold) {
                        mIsClick = false;
                    }
                    mLastMotionX = 0;
                    mLastMotionY = 0;
                    mStartX = (int) 0;
                    if (mIsClick) {
                        showOrHide();
                    }
                    mIsClick = true;
                    break;

                default:
                    break;
            }
            return true;
        }

    };

    private void showOrHide() {
        if (mBottomView.getVisibility() == View.VISIBLE) {
            mTopBar.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(this,
                    com.jit.video.R.anim.option_leave_from_top);
            animation.setAnimationListener(new AnimationImp() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    mTopBar.setVisibility(View.GONE);
                }
            });
            mTopBar.startAnimation(animation);

            mBottomView.clearAnimation();
            Animation animation1 = AnimationUtils.loadAnimation(this,
                    com.jit.video.R.anim.option_leave_from_bottom);
            animation1.setAnimationListener(new AnimationImp() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    mBottomView.setVisibility(View.GONE);
                }
            });
            mBottomView.startAnimation(animation1);
            if (tvAbout.getVisibility() == View.VISIBLE) {
                tvAbout.setVisibility(View.GONE);
            }
        } else {
            mTopBar.setVisibility(View.VISIBLE);
            mTopBar.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(
                    this, com.jit.video.R.anim.option_entry_from_top
            );
            mTopBar.startAnimation(animation);

            mBottomView.setVisibility(View.VISIBLE);
            mBottomView.clearAnimation();
            Animation animation1 = AnimationUtils.loadAnimation(
                    this, com.jit.video.R.anim.option_entry_from_bottom
            );
            mBottomView.startAnimation(animation1);
            mHandler.removeCallbacks(hideRunnable);
            mHandler.postDelayed(hideRunnable, HIDE_TIME);
        }
    }

    private class AnimationImp implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mHeight = DensityUtil.getWidthInPx(this);
            mWidth = DensityUtil.getHeightInPx(this);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mWidth = DensityUtil.getWidthInPx(this);
            mHeight = DensityUtil.getHeightInPx(this);
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LightnessController.setLightness(this, mOriginalLight);
        if (video.isPlaying()) {
            mIVPlay.performClick();
        }
    }
}
