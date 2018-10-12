package org.sxchinacourt.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.activity.NotificationActivity;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.util.WebServiceUtil;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author lk
 */
public class NotificationService extends Service {
    public static final String TAG = "NotificationService";
    String id = "我是通知";
    String name="我是通知名字";
    private Timer timer;
    private TimerTask task = null;
    private int beforeNo = 0;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE_NOTIFICATION_COUNT:

                    String resps = (String)msg.obj;
                    Intent intent = new Intent(NotificationService.this,NotificationActivity.class);
                    PendingIntent pi = PendingIntent.getActivity(NotificationService.this,0,intent,0);
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    Notification notification = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel mChannel = new NotificationChannel(id,name,NotificationManager.IMPORTANCE_LOW);
                        Log.i(TAG, mChannel.toString());
                        manager.createNotificationChannel(mChannel);

                        notification = new NotificationCompat.Builder(NotificationService.this)
                                .setChannelId(id)
                                .setContentTitle("通知:")
                                .setContentText("当前您有"+resps+"待办消息,请点击查看详情并尽快办理！")
//                              .setStyle(new NotificationCompat.BigTextStyle().bigText("通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容通知栏里面的内容"))
//                              .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.cabinet_bg)))//在通知栏里面显示一个大图片
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(R.mipmap.ic_launcher_updata)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_updata))
                                //设置点击通知栏跳转到该活动页面
                                .setContentIntent(pi)
                                //设置自动取消，即当点击该通知的时候自动取消
                                .setAutoCancel(true)
                                //设置播放音频
                                .setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Luna.ogg")))
                                //表示开始首先静止0s，震动1s，然后在静止1s，再震动1s  注意：这个功能需要申请权限
                                .setVibrate(new long[] {0,1000,1000,1000})
                                //设置手机LED灯，设置成一闪一闪的效果
                                .setLights(Color.GREEN,1000,1000)
//                                .setDefaults(NotificationCompat.DEFAULT_ALL)//将通知栏的属性设置成默认属性
                                //设置通知的重要性，最高的话可以在弹出一个横幅
                                .setPriority(NotificationCompat.PRIORITY_MAX)
                                .build();
                    }else {
                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(NotificationService.this)
                                .setContentTitle("通知:")
                                .setContentText("当前您有"+resps+"条消息,请点击查看详情并尽快办理！")
//                                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.cabinet_bg)))//在通知栏里面显示一个大图片
                                .setWhen(System.currentTimeMillis())
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_updata))
                                //设置点击通知栏跳转到该活动页面
                                .setContentIntent(pi)
                                //设置自动取消，即当点击该通知的时候自动取消
                                .setAutoCancel(true)
                                //设置播放音频
                                .setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Luna.ogg")))
                                //表示开始首先静止0s，震动1s，然后在静止1s，再震动1s  注意：这个功能需要申请权限
                                .setVibrate(new long[] {0,1000,1000,1000})
                                //设置手机LED灯，设置成一闪一闪的效果
                                .setLights(Color.GREEN,1000,1000)
//                                .setDefaults(NotificationCompat.DEFAULT_ALL)//将通知栏的属性设置成默认属性
                                //设置通知的重要性，最高的话可以在弹出一个横幅
                                .setPriority(NotificationCompat.PRIORITY_MAX)
                                .setSmallIcon(R.mipmap.ic_launcher_updata)
                                .setOngoing(true);
                        notification = notificationBuilder.build();
                    }
                    manager.notify(1,notification);


                default:
                    break;
            }
        }

    };

    public static final int SHOW_RESPONSE_NOTIFICATION_COUNT = 0;
    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                timer = new Timer();
                task = new TimerTask(){
                    @Override
                    public void run() {
                        String token  = CApplication.getInstance().getCurrentToken();
                        UserNewBean user = CApplication.getInstance().getCurrentUser();
                        String msgCountdaiban = WebServiceUtil.getInstance().getTaskCount(token);
                        Log.e("msgCountdaiban",""+msgCountdaiban);
                        int  a = Integer.parseInt(msgCountdaiban);
                        Log.e("a",""+a);

                        int ceshi = a - beforeNo ;
                        if (0 == ceshi){
                            beforeNo = a;
                            Log.e("ceshi",""+ceshi);

                        }else {
                            beforeNo = a;
                            Log.e("ceshi",""+ceshi);

                            Message message = new Message();
                            message.what = SHOW_RESPONSE_NOTIFICATION_COUNT;
                            message.obj = String.valueOf(ceshi);
                            handler.sendMessage(message);
                        }


                        String msgCount = WebServiceUtil.getInstance().getNoticeCount(token,"否");
                        String msgCountList = WebServiceUtil.getInstance().getNoticeList(token,"否");
                        Log.e("msgCountList",""+msgCountList);

                    }
                };
                //半分钟执行一次查询操作,访问一次后台
                timer.schedule(task,1000,60000);



            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
