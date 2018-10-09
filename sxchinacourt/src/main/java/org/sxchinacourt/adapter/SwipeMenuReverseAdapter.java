package org.sxchinacourt.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.activity.AssignActivity;
import org.sxchinacourt.bean.FileReverseDetailDataBean;
import org.sxchinacourt.bean.UserBean;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.util.QRCodeUtil;
import org.sxchinacourt.util.SoapClient;
import org.sxchinacourt.util.SoapParams;
import org.sxchinacourt.util.WebServiceUtil;
import org.sxchinacourt.widget.SwipeMenuView;

import java.io.File;

public class SwipeMenuReverseAdapter extends ListBaseAdapter<FileReverseDetailDataBean> {
    private FileReverseDetailDataBean bean =null;
    private String serialNo ;
    private LayoutInflater mLayoutInflater;
    private ImageView iverweimaPop;
    private ImageView ivErwemaBgTop;
    private ImageView ivErwemaBgBottom;
    private ImageView ivErwemaBgLeft;
    private ImageView ivErwemaBgRight;
    private TextView  tvErweimaName;
    private Button btnErweimaNext;
    private Handler handler1 = null;
    private String filePath;
    private Context mmContext;
    String  verificationErWeiMa;
    private Handler handler = null;


    public static final int SHOW_RESPONSE_AUTHENTICATION = 11;
    public static final int SHOW_RESPONSE_ERWEIMA_AUTHENTICATION= 1;
    public static final int SHOW_RESPONSE_ERWEIMA_REVERSE= 2;


    public SwipeMenuReverseAdapter(Context context) {
        super(context);
        mLayoutInflater = LayoutInflater.from(context);
        mmContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_item_swipe;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        View contentView = holder.getView(R.id.rl_deposit_content_swipe);
        TextView InitiatorName = holder.getView(R.id.tv_InitiatorName_text_swipe);
        TextView RecipientName = holder.getView(R.id.tv_RecipientName_text_swipe);
        TextView address = holder.getView(R.id.tv_address_text_swipe);
        TextView fileState = holder.getView(R.id.tv_fileState_swipe);
        Button btnAssign = holder.getView(R.id.btnAssign);
        Button btnPickUp = holder.getView(R.id.btnPickUp);


        //这句话关掉IOS阻塞式交互效果 并依次打开左滑右滑
        ((SwipeMenuView)holder.itemView).setIos(false).setLeftSwipe( true);
//        ((SwipeMenuView)holder.itemView).setIos(false).setLeftSwipe(position % 2 == 0 ? true : false);
        String initiatorName = getDataList().get(position).getInitiatorName();
        Log.e("initiatorName",""+initiatorName);
        InitiatorName.setText(getDataList().get(position).getInitiatorName());
        RecipientName.setText(getDataList().get(position).getRecipientName());
        address.setText(getDataList().get(position).getAddress().substring(12));
        if (getDataList().get(position).getFileState()== 0){
            fileState.setText("未关门");
            fileState.setTextColor(mmContext.getResources().getColor(R.color.header));
            btnAssign.setVisibility(View.VISIBLE);
            btnPickUp.setVisibility(View.GONE);
        }else if (getDataList().get(position).getFileState()== 1){
            fileState.setText("待指派");
            fileState.setTextColor(mmContext.getResources().getColor(R.color.header));
            btnAssign.setVisibility(View.VISIBLE);
            btnPickUp.setVisibility(View.GONE);
        }else if (getDataList().get(position).getFileState()== 2){
            fileState.setText("流转中");
            fileState.setTextColor(mmContext.getResources().getColor(R.color.header));
            btnAssign.setVisibility(View.VISIBLE);
            btnPickUp.setVisibility(View.VISIBLE);
        }else if (getDataList().get(position).getFileState()== 3){
            fileState.setText("已取件");
            fileState.setTextColor(mmContext.getResources().getColor(R.color.fileState));
            //隐藏控件
            btnAssign.setVisibility(View.GONE);
            btnPickUp.setVisibility(View.GONE);

        }
        btnAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getDataList().get(position).getFileState()==0){
                    Toast.makeText(mmContext,"未关门",Toast.LENGTH_SHORT).show();
                }else if (getDataList().get(position).getFileState()==1){
                    Intent jumptoAssign = new Intent(mmContext,AssignActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("SerialNo",getDataList().get(position).getSerialNo());
                    Log.e("SerialNo",""+bundle);
                    jumptoAssign.putExtras(bundle);
                    jumptoAssign.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mmContext.startActivity(jumptoAssign);
                }else if (getDataList().get(position).getFileState()==2){
                    Intent jumptoAssign = new Intent(mmContext,AssignActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("SerialNo",getDataList().get(position).getSerialNo());
                    Log.e("SerialNo",""+bundle);
                    jumptoAssign.putExtras(bundle);
                    jumptoAssign.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mmContext.startActivity(jumptoAssign);
                }
                else if (getDataList().get(position).getFileState()==3){
                    Toast.makeText(mmContext,"已取件",Toast.LENGTH_SHORT).show();
                    Intent jumptoAssign = new Intent(mmContext,AssignActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("SerialNo",getDataList().get(position).getSerialNo());
                    Log.e("SerialNo",""+bundle);
                    jumptoAssign.putExtras(bundle);
                    jumptoAssign.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mmContext.startActivity(jumptoAssign);
                }
            }
        });
        File path1 = new File(Environment.getExternalStorageDirectory().getPath()+"/Json");
        if (!path1.exists()) {
            //若不存在，创建目录
            path1.mkdirs();
        }
        //创建的二维码地址
        filePath = Environment.getExternalStorageDirectory().getPath() + "/Json/" + "code_json.jpg";
        btnPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final UserNewBean user = CApplication.getInstance().getCurrentUser();
                String str = String.valueOf ( user.getOaid() );
//                Log.e("user","雷凱"+user.getCourtoaid()+"雷凱"+user.getSessionId()+"雷凱"+user.getUserNo()+"雷凱"+user.getId());
                final SoapParams soapParams = new SoapParams().put("arg0",str);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String dynamicData= WebServiceUtil.getInstance().GetQRCode(soapParams, new SoapClient.ISoapUtilCallback() {
                            @Override
                            public void onSuccess(SoapSerializationEnvelope envelope) throws Exception {
                                verificationErWeiMa  =  envelope.getResponse().toString();


                                Message message = new Message();
                                message.what = SHOW_RESPONSE_AUTHENTICATION;
//                                message.obj = verificationErWeiMa;
                                message.obj = view;

                                handler.sendMessage(message);


                            }

                            @Override
                            public void onFailure(Exception e) {
                            }
                        });
                    }
                }).start();
                handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what) {
                            case SHOW_RESPONSE_AUTHENTICATION:
//                                verificationErWeiMa = (String) msg.obj;
                                View v = (View) msg.obj;
                                Log.e("verificationErWeiMa",""+verificationErWeiMa);

                                if (verificationErWeiMa==null){
                                    break;
                                }
                                new Thread(runAddEwmImg).start();

//                                mErWeiMaBitmap = BC_2weima(verificationErWeiMa);
                                Log.e("verificationErWeiMa",""+verificationErWeiMa);
//                                LayoutInflater inflater = LayoutInflater.from(mmContext());
                                // 引入窗口配置文件
                                View view = mLayoutInflater.inflate(R.layout.view_erweima_popuwindow, null);

                                tvErweimaName = (TextView) view.findViewById(R.id.tv_erwema_name);
                                iverweimaPop = (ImageView) view.findViewById(R.id.iv_erweima);
                                btnErweimaNext = (Button) view.findViewById(R.id.btn_erweima_next);
                                ivErwemaBgTop = (ImageView) view.findViewById(R.id.iv_erweima_bgtop);
                                ivErwemaBgBottom = (ImageView) view.findViewById(R.id.iv_erweima_bgbottom);
                                ivErwemaBgLeft = (ImageView) view.findViewById(R.id.iv_erweima_bgleft);
                                ivErwemaBgRight = (ImageView) view.findViewById(R.id.iv_erweima_bgright);
                                handler1 = new Handler(){
                                    @Override
                                    public void handleMessage(Message msg) {
                                        super.handleMessage(msg);
                                        switch (msg.what) {
                                            case SHOW_RESPONSE_ERWEIMA_AUTHENTICATION:
                                                tvErweimaName.setText("身份认证二维码");
                                                btnErweimaNext.setText("下一步");
                                                break;
                                            case SHOW_RESPONSE_ERWEIMA_REVERSE:
                                                tvErweimaName.setText("取件二维码");
                                                btnErweimaNext.setText("完成");

                                        }
                                        iverweimaPop.setImageBitmap(BitmapFactory.decodeFile(filePath));
                                    }
                                };
//                                iverweima.setImageBitmap(mErWeiMaBitmap);

                                // 创建PopupWindow对象
                                final PopupWindow pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
                                pop.setAnimationStyle(R.style.mypopwindow_anim_style);
                                ColorDrawable dw = new ColorDrawable(0xb0000000);
                                pop.setBackgroundDrawable(dw);
                                // 需要设置一下此参数，点击外边可消失
                                pop.setBackgroundDrawable(new BitmapDrawable());
                                //设置点击窗口外边窗口消失
                                pop.setOutsideTouchable(true);
                                // 设置此参数获得焦点，否则无法点击
                                pop.setFocusable(true);
                                if(pop.isShowing()) {
                                    // 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
                                    pop.dismiss();
                                } else {
                                    // 显示窗口
                                    pop.showAtLocation(v, Gravity.CENTER,0,0);

                                }
                                btnErweimaNext.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (btnErweimaNext.getText().toString().equals("完成")){
                                            pop.dismiss();
                                        }else {
                                            bean = getDataList().get(position);
                                            serialNo =  bean.getSerialNo();
                                            new Thread(runAddEwmImgReverse).start();
                                        }
//                                        pop.dismiss();

                                    }
                                });
                                ivErwemaBgTop.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {
                                        pop.dismiss();
                                    }
                                });
                                ivErwemaBgBottom.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {
                                        pop.dismiss();
                                    }
                                });
                                ivErwemaBgLeft.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {
                                        pop.dismiss();
                                    }
                                });
                                ivErwemaBgRight.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {
                                        pop.dismiss();
                                    }
                                });


                            default:
                                break;
                        }
                    }
                };


            }
        });
        
        //注意事项，设置item点击，不能对整个holder.itemView设置咯，只能对第一个子View，即原来的content设置，这算是局限性吧。
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppToast.makeShortToast(mContext, getDataList().get(position).title);
                Log.d("TAG", "onClick() called with: v = [" + v + "]");
            }
        });
    }

    Runnable runAddEwmImg = new Runnable() {
        @Override
        public void run() {
            //  BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.ic_launcher) 中间的logo图，
            //中间不添加图片 直接写null
            Log.e("serialNo",""+serialNo);
            if (true){
                boolean isTF = QRCodeUtil.createQRImage(verificationErWeiMa, 400, 400,
                        BitmapFactory.decodeResource(mmContext.getResources(), R.drawable.logo),
                        filePath);
                if (isTF) {
                    Message message = new Message();
                    message.what = SHOW_RESPONSE_ERWEIMA_AUTHENTICATION;
                    message.arg1 = 5;
                    handler1.sendMessage(message);
                }
            }
        }
    };
    Runnable runAddEwmImgReverse = new Runnable() {
        @Override
        public void run() {
            //  BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.ic_launcher) 中间的logo图，
            //中间不添加图片 直接写null
            Log.e("serialNo",""+serialNo);
                boolean isTF = QRCodeUtil.createQRImage(serialNo, 400, 400,
                        BitmapFactory.decodeResource(mmContext.getResources(), R.drawable.logo),
                        filePath);
                if (isTF) {
                    Message message = new Message();
                    message.what = SHOW_RESPONSE_ERWEIMA_REVERSE;
                    message.arg1 = 5;
                    handler1.sendMessage(message);
                }
        }
    };

    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);

        void onTop(int pos);
    }

    private onSwipeListener mOnSwipeListener;

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }


}

