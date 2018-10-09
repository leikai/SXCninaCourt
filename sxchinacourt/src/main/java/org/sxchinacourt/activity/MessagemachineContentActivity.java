package org.sxchinacourt.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.sxchinacourt.R;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MessagemachineContentActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textContent;
    TextView voiceContent;
    TextView videoContent;
    Button play;
    Button pause;
    Button stop;
    Button videoPlay;
    Button videoPause;
    Button videoStop;
    VideoView videoView;
    LinearLayout viewTextContent;
    LinearLayout viewVoiceContent;
    LinearLayout viewVideoContent;
    TextView sbStart;
    TextView sbEnd;
    SeekBar sbVoice;
    TextView tvVoiceMachine;
    Button btnback;

    private String answerType;
    private String content;
    private MediaPlayer mediaPlayer = new MediaPlayer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagemachine_content);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        getIntentData();
        initView();

        bindData();
        initEvent();
        if (ContextCompat.checkSelfPermission(MessagemachineContentActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MessagemachineContentActivity.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },1);//检查应用的权限:是否含有读写SD卡的权限
        }else {
            initMediaPlayer();//如果有的话,初始化MediaPlayer
            initVideoPath(); //初始化 VideoView
        }
    }

    private void initView() {
        textContent = (TextView) findViewById(R.id.tv_text_content);
        voiceContent = (TextView) findViewById(R.id.tv_voice_content);
        videoContent = (TextView) findViewById(R.id.tv_video_content);
        play = (Button) findViewById(R.id.play);
        pause = (Button) findViewById(R.id.pause);
        stop = (Button) findViewById(R.id.stop);
        videoPlay = (Button) findViewById(R.id.video_play);
        videoPause = (Button) findViewById(R.id.video_pause);
        videoStop = (Button) findViewById(R.id.video_resume);
        videoView = (VideoView)findViewById(R.id.video_view);
        viewTextContent = (LinearLayout) findViewById(R.id.ll_view_textContent);
        viewVoiceContent = (LinearLayout) findViewById(R.id.ll_view_voiceContent);
        viewVideoContent = (LinearLayout) findViewById(R.id.ll_view_videoContent);
        sbStart = (TextView) findViewById(R.id.tv_start);
        sbEnd = (TextView) findViewById(R.id.tv_end);
        sbVoice = (SeekBar)findViewById(R.id.sb_voice);
        tvVoiceMachine = (TextView) findViewById(R.id.tv_voice_machine);
        btnback = (Button) findViewById(R.id.btn_messagemachine_content_back);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    initMediaPlayer();
                    initVideoPath();
                }else {
                    Toast.makeText(this,"拒绝权限将无法使用应用程序",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            default:
        }
    }
    private void initEvent() {
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(MessagemachineContentActivity.this,"播放完成",Toast.LENGTH_LONG).show();
            }
        });
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);
        videoPlay.setOnClickListener(this);
        videoPause.setOnClickListener(this);
        videoStop.setOnClickListener(this);
        sbVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b == true){
                    mediaPlayer.seekTo(i);
                    sbStart.setText(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    Handler handler = new Handler();
    Runnable updateThread = new Runnable(){
        public void run() {
            //获得歌曲现在播放位置并设置成播放进度条的值
            try {
                            sbVoice.setProgress(mediaPlayer.getCurrentPosition());
                //每次延迟100毫秒再启动线程
                handler.postDelayed(updateThread, 100);
            }catch (IllegalStateException e){
                e.printStackTrace();
            }

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play:
                if (!mediaPlayer.isPlaying()){
                    mediaPlayer.start();//开始播放
                    handler.post(updateThread);
                }
                break;
            case R.id.pause:
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();//暂停播放
                }
                break;
            case R.id.stop:
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();//停止播放
                }
                break;
            case R.id.video_play:
                if (!videoView.isPlaying()){
                    videoView.start();
                }
                break;
            case R.id.video_pause:
                if (videoView.isPlaying()){
                    videoView.pause();
                }
                break;
            case R.id.video_resume:
                if (videoView.isPlaying()){
                    videoView.resume();//重新播放
                }
                break;

            default:
                break;
        }

    }


    private void bindData() {
        if ("0".equals(answerType)){
            viewTextContent.setVisibility(View.VISIBLE);
            textContent.setText(content);
            viewVoiceContent.setVisibility(View.GONE);
            viewVideoContent.setVisibility(View.GONE);
        }
        if ("1".equals(answerType)){
//            viewTextContent.setVisibility(View.VISIBLE);
//            textContent.setText(content);
            viewTextContent.setVisibility(View.GONE);
            viewVoiceContent.setVisibility(View.VISIBLE);
//            tvVoiceMachine.setVisibility(View.GONE);
//            voiceContent.setVisibility(View.GONE);
            viewVideoContent.setVisibility(View.GONE);
        }
        if ("2".equals(answerType)){
            viewTextContent.setVisibility(View.GONE);
            viewVoiceContent.setVisibility(View.GONE);
            viewVideoContent.setVisibility(View.VISIBLE);
        }

    }

    private void getIntentData() {
        Intent intent = getIntent();
        answerType = intent.getStringExtra("answerType");
        Log.d("answerType", answerType);
        content = intent.getStringExtra("content");
        Log.d("content", content);
    }

    private void initMediaPlayer() {
        try {
            //-----------------播放应用的资源文件-------------------//
////            法1. 直接调用create函数实例化一个MediaPlayer对象，播放位于res/raw/test.mp3文件
//            MediaPlayer  mMediaPlayer = MediaPlayer.create(this, R.raw.test);
//
////            法2. test.mp3放在res/raw/目录下，使用setDataSource(Context context, Uri uri)
//            mp = new MediaPlayer();
//            Uri setDataSourceuri = Uri.parse("android.resource://com.android.sim/"+R.raw.test);
//            mp.setDataSource(this, uri);
//
////            说明：此种方法是通过res转换成uri然后调用setDataSource()方法，需要注意格式Uri.parse("android.resource://[应用程序包名Application package name]/"+R.raw.播放文件名);
////            例子中的包名为com.android.sim，播放文件名为：test;特别注意包名后的"/"。
//
////            法3. test.mp3文件放在assets目录下，使用setDataSource(FileDescriptor fd, long offset, long length)
//            AssetManager assetMg = this.getApplicationContext().getAssets();
//            AssetFileDescriptor fileDescriptor = assetMg.openFd("test.mp3");
//            mp.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            //-----------------end--------------------//
            //-----------------播放存储设备的资源文件-------------------//
//            MediaPlayer mediaPlayer = new MediaPlayer();
//            mediaPlayer.setDataSource("/mnt/sdcard/test.mp3");
            //-----------------end--------------------//

            //-----------------播放远程资源文件-------------------//
//            Uri uri = Uri.parse("http://**");
//            MediaPlayer mediaPlayer = new MediaPlayer();
//            mediaPlayer.setDataSource(Context, uri);
            //-----------------end--------------------//

//            File file = new File(Environment.getExternalStorageDirectory(),"李行亮-愿得一人心.flac");//寻找SD卡下的根目录中的指定文件

            //--------------------------------测试------------------------//
//            File file = new File(Environment.getExternalStorageDirectory(),"李行亮-愿得一人心.flac");//寻找SD卡下的根目录中的指定文件
//            mediaPlayer.setDataSource(file.getAbsolutePath());
            //---------------------------------完-------------------------//
//            //---------------------------正式-----------------------------//
            mediaPlayer.setDataSource(content);//指定音频文件的路径
//            //---------------------------完-----------------------------//
            mediaPlayer.prepare();//让mediaPlayer 进入到准备状态
            sbStart.setText("0:00");

            Date data = new Date(mediaPlayer.getDuration());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());

            sbVoice.setMax(mediaPlayer.getDuration());
            sbEnd.setText(simpleDateFormat.format(data));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initVideoPath() {
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoPath(Environment.getExternalStorageDirectory()+"/1513682210240.avi");//指定视频文件的路径
//        Uri uri = Uri.parse("http://111.53.181.200:8087/Cloud/uploadFile/1513682210240.avi");  //播放网上图片
//        videoView.setVideoURI(uri);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        try {
            if (videoView !=null){
                videoView.suspend();//将VideoView所占用的资源释放掉
            }
        }catch (IllegalStateException e){
            e.printStackTrace();
        }

    }
}
