package org.sxchinacourt.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.geek.thread.GeekThreadManager;
import com.geek.thread.ThreadPriority;
import com.geek.thread.ThreadType;
import com.geek.thread.task.GeekRunnable;


import org.sxchinacourt.R;

import org.sxchinacourt.activity.MessagemachineContentActivity;
import org.sxchinacourt.activity.PictureVideoPlayActivity;

import org.sxchinacourt.bean.CabinetDetailBean;

import org.sxchinacourt.dao.DBHelper;
import org.sxchinacourt.dao.Msgmachinedb;
import org.sxchinacourt.dao.MsgmachinedbDao;
import org.sxchinacourt.util.WebServiceUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * @author 殇冰无恨
 * @date 2017/12/5
 */

public class AnswerPhoneAdapter extends RecyclerView.Adapter<AnswerPhoneAdapter.ViewHolder> {
    private List<Msgmachinedb> mMessagemachineBeanList;
    private Context mContext;
    /**
     * 定义上传文件类型
     */
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");


    private Msgmachinedb msgmachinedb;
    private MsgmachinedbDao msgmachinedbDao;
    private DBHelper dbHelper = DBHelper.getInstance();
    private String TABLE_NAME = "msgmachine.db";


    private ImageView download;
    private View targetView;
    private PopupWindow popDownload;
    private static final int DOWN_COMPLETE_VOICE= 0;
    private static final int DOWN_COMPLETE_VOIDE= 0;

    public static final int SHOW_RESPONSE_MESSAGEMACHINE_DETAIL= 1;
    private Handler handler1 = null;
    private Handler handler2 = null;
    private Handler handler3 = null;
    private String pathServer;




    static class ViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout llViewMessageType;
        public LinearLayout llContent;
        public TextView tvMessagePersion;
        public TextView tvMessageType;
        public TextView tvMessageContent;
        public TextView tvMessageTime;


        public ViewHolder(View itemView) {
            super(itemView);
            llContent = (LinearLayout) itemView.findViewById(R.id.layout_content);
            llViewMessageType = (LinearLayout) itemView.findViewById(R.id.ll_view_message_type);
            tvMessagePersion = (TextView) itemView.findViewById(R.id.tv_message_person);
            tvMessageType = (TextView)itemView.findViewById(R.id.tv_message_type);
            tvMessageTime = (TextView)itemView.findViewById(R.id.tv_message_time);

        }
    }

    public AnswerPhoneAdapter(Context context,View view,List<Msgmachinedb> mMessagemachineBeanList) {
        mContext = context;
        targetView = view;
        this.mMessagemachineBeanList = mMessagemachineBeanList;
        msgmachinedbDao = dbHelper.initDatabase(mContext,TABLE_NAME).getMsgmachinedbDao();
    }
    /**
     * 创建ViewHolder的实例
     *    添加点击事件
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_machine,parent,false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }
    /**
     * 用于对RecyclerView子项的数据进行赋值
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Msgmachinedb messagemachineBean = mMessagemachineBeanList.get(position);
        //绑定数据
        holder.tvMessagePersion.setText(messagemachineBean.getPersonName());
        holder.tvMessageType.setText(messagemachineBean.getAnswerType());
        if ("0".equals(messagemachineBean.getAnswerType())){
            holder.tvMessageType.setText("文字留言");
        }else if ("1".equals(messagemachineBean.getAnswerType())){
            holder.tvMessageType.setText("语音留言");
        }else if ("2".equals(messagemachineBean.getAnswerType())){
            holder.tvMessageType.setText("视频留言");
        }
        holder.tvMessageTime.setText(messagemachineBean.getAnswerTime());
        holder.llContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("提醒");
                dialog.setMessage("是否删除此文件");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        msgmachinedbDao.delete(mMessagemachineBeanList.get(position));
                        mMessagemachineBeanList.remove(position);
                        notifyDataSetChanged();

                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();

                return true;
            }
        });
        holder.llViewMessageType.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onClick(View view) {
                Log.e("留言类型：",""+messagemachineBean.getAnswerType());
                if ("0".equals(messagemachineBean.getAnswerType())){
                    Intent intent = new Intent(mContext,MessagemachineContentActivity.class);
                    intent.putExtra("answerType",messagemachineBean.getAnswerType());
                    intent.putExtra("content",messagemachineBean.getTextContent());
                    mContext.startActivity(intent);
                }else {

                    GeekThreadManager.getInstance().execute(new GeekRunnable(ThreadPriority.NORMAL) {
                        @Override
                        public void run() {
                            String oid = messagemachineBean.getOid();
                            String  ceshi = WebServiceUtil.getInstance().getTheOaAnswerMessage(oid);
                            Log.e("留言机详情",""+ceshi);
                            CabinetDetailBean resps = JSON.parseObject(ceshi,CabinetDetailBean.class);
                            String ceshi1 =  resps.getFilePath();
                            Log.e("ceshi1",""+ceshi1);

                            Message message = new Message();
                            message.what = SHOW_RESPONSE_MESSAGEMACHINE_DETAIL;
                            message.obj = ceshi1;
                            handler3.sendMessage(message);
                        }
                    },ThreadType.NORMAL_THREAD);

                    handler3 = new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            switch (msg.what){
                                case SHOW_RESPONSE_MESSAGEMACHINE_DETAIL:
                                    pathServer = (String) msg.obj;
                                    Log.e("从数据库得到的地址：",""+pathServer);
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                                    dialog.setTitle("提醒");
                                    dialog.setMessage("是否确定下载此文件");
                                    dialog.setCancelable(false);
                                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            LayoutInflater inflater = LayoutInflater.from(mContext);
                                            // 引入窗口配置文件
                                            View downPop = inflater.inflate(R.layout.view_imageview_download, null);
                                            download = (ImageView) downPop.findViewById(R.id.iv_view_imageview);
                                            Glide.with(mContext).load("http://img.lanrentuku.com/img/allimg/1212/5-121204193Q9-50.gif").into(download);
                                            popDownload = new PopupWindow(downPop,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
                                            popDownload.setAnimationStyle(R.style.mypopwindow_anim_style);
                                            ColorDrawable dw = new ColorDrawable(0xb0000000);
                                            popDownload.setBackgroundDrawable(dw);
                                            // 需要设置一下此参数，点击外边可消失
                                            popDownload.setBackgroundDrawable(new BitmapDrawable());
                                            //设置点击窗口外边窗口消失
                                            popDownload.setOutsideTouchable(true);
                                            // 设置此参数获得焦点，否则无法点击
                                            popDownload.setFocusable(true);
                                            if(popDownload.isShowing()) {
                                                // 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
                                                popDownload.dismiss();
                                            } else {
                                                // 显示窗口
                                                popDownload.showAtLocation(targetView, Gravity.CENTER,0,0);

                                            }
                                            if ("1".equals(messagemachineBean.getAnswerType())){
                                                //下载文件
                                                String path = "";
                                                path = messagemachineBean.getFilePath();
                                                String [] a = path.split("=");
                                                final String  path1 = a[1];
                                                downAsynFile("1",path1);
                                                handler1 = new Handler(){
                                                    @Override
                                                    public void handleMessage(Message msg) {
                                                        super.handleMessage(msg);
                                                        switch (msg.what){
                                                            case DOWN_COMPLETE_VOICE:
                                                                popDownload.dismiss();

                                                                Intent intent = new Intent(mContext, MessagemachineContentActivity.class);
                                                                intent.putExtra("answerType","1");
                                                                intent.putExtra("content", Environment.getExternalStorageDirectory()+"/"+path1);
                                                                mContext.startActivity(intent);
                                                                break;
                                                        }
                                                    }
                                                };
                                            }else if ("2".equals(messagemachineBean.getAnswerType())){

                                                //下载文件
                                                String path = "";
                                                path = messagemachineBean.getFilePath();

                                                String ceshi2 = pathServer.substring(19);
                                                Log.e("ceshi2",""+ceshi2);
                                                String [] a = ceshi2.split("=");
                                                final String  path1 = a[1];

                                                downAsynFile("2",path1);
                                                handler2 = new Handler(){
                                                    @Override
                                                    public void handleMessage(Message msg) {
                                                        super.handleMessage(msg);
                                                        switch (msg.what){
                                                            case DOWN_COMPLETE_VOIDE:
                                                                popDownload.dismiss();
                                                                Intent intent = new Intent(mContext, PictureVideoPlayActivity.class);
                                                                intent.putExtra("video_path", Environment.getExternalStorageDirectory()+"/"+path1);
                                                                mContext.startActivity(intent);
                                                                break;
                                                                default:
                                                                    break;
                                                        }
                                                    }
                                                };
                                            }
                                        }
                                    });
                                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    dialog.show();
                                    break;
                                    default:
                                        break;
                            }

                        }
                    };








                }





            }
        });


    }

    private void downAsynFile(final String type, final String path) {



        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient client = builder.build();

        client.newBuilder().connectTimeout(10, TimeUnit.SECONDS);
        client.newBuilder().readTimeout(10,TimeUnit.SECONDS);
        client.newBuilder().writeTimeout(10,TimeUnit.SECONDS);


        String url = "http://111.53.181.200:8087/mserver/uploadFile/"+path;
        Log.e("下载地址",""+url);
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                File file = null;

                try {
                    String test = Environment.getExternalStorageDirectory().getAbsolutePath();
                    file = new File(test+"/"+path);
                    if (file.exists()){
                       Log.e("asda","asdasda");
                    }else {
                        fileOutputStream = new FileOutputStream(file);
                        byte[] buffer = new byte[2048];
                        int len = 0;
                        while ((len = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, len);
                        }
                        fileOutputStream.flush();
                    }
                } catch (IOException e) {
                    Log.i("wangshu", "IOException");
                    e.printStackTrace();
                }
                if ("1".equals(type)){
                    Message message = new Message();
                    message.what = DOWN_COMPLETE_VOICE;
                    message.obj = 1;
                    handler1.sendMessage(message);
                }else if ("2".equals(type)){
                    Message message = new Message();
                    message.what = DOWN_COMPLETE_VOIDE;
                    message.obj = 2;
                    handler2.sendMessage(message);
                }

                Log.e("lk","下载成功");


            }
        });
    }

    @Override
    public int getItemCount() {
        return mMessagemachineBeanList.size();
    }
}
