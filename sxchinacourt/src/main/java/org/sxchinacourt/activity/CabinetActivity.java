package org.sxchinacourt.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.geek.thread.GeekThreadManager;
import com.geek.thread.ThreadPriority;
import com.geek.thread.ThreadType;
import com.geek.thread.task.GeekRunnable;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.bean.DepositDataBean;
import org.sxchinacourt.bean.DepositRootBean;
import org.sxchinacourt.bean.FileReverseDetailBean;
import org.sxchinacourt.bean.FileReverseDetailDataBean;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.util.QRCodeUtil;
import org.sxchinacourt.util.SoapClient;
import org.sxchinacourt.util.SoapParams;
import org.sxchinacourt.util.WebServiceUtil;

import java.io.File;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import zxing.EncodingHandler;

/**
 * @author lk
 */
public class CabinetActivity extends AppCompatActivity implements View.OnClickListener{
    String  erweima = null;
    private static Boolean IsTure=true;
    String  verificationErWeiMa;
    String  verificationErWeiMaResp;
    private ImageView iverweima;
    private TextView tvName;
    private TextView tvDepartment;
    private TextView tvWaitAssignAmount;
    private TextView tvWaitPickupAmount;
    private Button btnBack;

    private Bitmap mErWeiMaBitmap;
    private Handler handler = null;
    private Handler handler1 = null;
    private Handler handlerForDeposit = null;
    private Handler handlergetdata = null;
    private TextView tvErweimaName;
    private Button btnErweimaNext;
    private ImageView ivErwemaBgTop;
    private ImageView ivErwemaBgBottom;
    private ImageView ivErwemaBgLeft;
    private ImageView ivErwemaBgRight;

    private String filePath;
    private List respsDeposot = null;
    private List respsReverse = null;
    private String s = "";
    private UserNewBean user;
    private String employeeId;



    public static final int SHOW_RESPONSE_AUTHENTICATION = 0;
    public static final int SHOW_RESPONSE_FILEREVERSEDETSIL = 2;
    public static final int SHOW_RESPONSE_FILEDEPOSITDETSIL = 1;
    public static final int SHOW_CABINET_ERWEIMA_AUTHENTICATION= 11;
    public static final int SHOW_CABINET_ERWEIMA_DEPOSIT= 12;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cabinet);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        findViewById(R.id.depositecabinetTask);
        //待指派
        findViewById(R.id.btn_wait_assign).setOnClickListener(this);
        //存件记录列表
        findViewById(R.id.btn_deposit_file).setOnClickListener(this);
        //取件记录列表
        findViewById(R.id.btn_picked_up).setOnClickListener(this);
        //待取件
        findViewById(R.id.btn_wait_pickup).setOnClickListener(this);
        //存件
        findViewById(R.id.btn_deposit).setOnClickListener(this);
        findViewById(R.id.tv_scanning_Center).setOnClickListener(this);
        btnBack = (Button) findViewById(R.id.btn_messagemachine_content_back);
        tvName = (TextView)findViewById(R.id.tv_scanning_Center);
        tvDepartment = (TextView)findViewById(R.id.tv_department);
        tvWaitAssignAmount = (TextView) findViewById(R.id.tv_wait_assign_amount);
        tvWaitPickupAmount = (TextView)findViewById(R.id.tv_wait_pickup_amount);

        user = CApplication.getInstance().getCurrentUser();
        employeeId = CApplication.getInstance().getCurrentEmployeeID();
        tvName.setText(user.getEmpname());
        tvDepartment.setText("山西省晋中市人民法院--"+user.getDeptname());

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        new Thread(runGetData).start();

        File path1 = new File(Environment.getExternalStorageDirectory().getPath()+"/Json");
        if (!path1.exists()) {
            //若不存在，创建目录
            path1.mkdirs();
        }
        //创建的二维码地址
        filePath = Environment.getExternalStorageDirectory().getPath() + "/Json/" + "code_json.jpg";
        handlergetdata = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case SHOW_RESPONSE_FILEDEPOSITDETSIL:
                        respsDeposot = (ArrayList)msg.obj;
                        int waitAssignAmount = 0;
                        for (int i =0;i<respsDeposot.size();i++){
                            DepositDataBean depositDataBean = (DepositDataBean)respsDeposot.get(i);
                            if (depositDataBean.getFileState()==1){
                                waitAssignAmount = waitAssignAmount+1;
                            }
                        }
                        Log.e("waitAssignAmount",""+waitAssignAmount);
                        tvWaitAssignAmount.setText(String.valueOf(waitAssignAmount));
                        break;
                    case SHOW_RESPONSE_FILEREVERSEDETSIL:
                        respsReverse = (ArrayList)msg.obj;
                        int waitPickupAmount = 0;
                        for (int i =0;i<respsReverse.size();i++){
                            FileReverseDetailDataBean fileReverseDetailBean = (FileReverseDetailDataBean)respsReverse.get(i);
                            if (fileReverseDetailBean.getFileState() ==2){
                                waitPickupAmount = waitPickupAmount+1;
                            }
                        }
                        tvWaitPickupAmount.setText(String.valueOf(waitPickupAmount));
                        break;
                    default:
                        break;
                }
            }
        };

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    Runnable runGetData = new Runnable() {
        @Override
        public void run() {
            //

            final SoapParams soapParamsDeposit = new SoapParams().put("EmployeeID",employeeId).put("pageindex",1);
            final SoapParams soapParamsEmployeePickUp = new SoapParams().put("EmployeeID",employeeId ).put("pageindex",1);

            WebServiceUtil.getInstance().GetEmployeeDeposit(soapParamsDeposit, new SoapClient.ISoapUtilCallback() {
                @Override
                public void onSuccess(SoapSerializationEnvelope envelope) throws Exception {
                    String response = envelope.getResponse().toString();
                    DepositRootBean resps = JSON.parseObject(response,DepositRootBean.class);
                    Log.e("response",""+resps.getData().get(0));
                    Message message = new Message();
                    message.what = SHOW_RESPONSE_FILEDEPOSITDETSIL;
                    message.obj = resps.getData();
                    handlergetdata.sendMessage(message);
                }
                @Override
                public void onFailure(Exception e) {
                    Log.e("e",""+e);
                }
            });
            WebServiceUtil.getInstance().GetEmployeePickup(soapParamsEmployeePickUp, new SoapClient.ISoapUtilCallback() {
                @Override
                public void onSuccess(SoapSerializationEnvelope envelope) throws Exception {
                    String response = envelope.getResponse().toString();
                    FileReverseDetailBean resps = JSON.parseObject(response,FileReverseDetailBean.class);
                    Message message = new Message();
                    message.what = SHOW_RESPONSE_FILEREVERSEDETSIL;
                    message.obj = resps.getData();
                    handlergetdata.sendMessage(message);
                }
                @Override
                public void onFailure(Exception e) {
                    Log.e("e",""+e);
                }
            });
        }
    };
    Runnable runAddEwmImg = new Runnable() {
        @Override
        public void run() {
            //  BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.ic_launcher) 中间的logo图，
            //中间不添加图片 直接写null
            boolean isTF = QRCodeUtil.createQRImage(verificationErWeiMa, 400, 400,
                    BitmapFactory.decodeResource(getResources(), R.drawable.logo),
                    filePath);
            if (isTF) {
                Message message = new Message();
                message.what = SHOW_CABINET_ERWEIMA_AUTHENTICATION;
                message.arg1 = 5;
                handler1.sendMessage(message);
            }
        }
    };
    Runnable runAddEwmImgForDeposit = new Runnable() {
        @Override
        public void run() {
            //  BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.ic_launcher) 中间的logo图，
            //中间不添加图片 直接写null
            boolean isTF = QRCodeUtil.createQRImage(s, 400, 400,
                    BitmapFactory.decodeResource(getResources(), R.drawable.logo),
                    filePath);
            if (isTF) {
                Message message = new Message();
                message.what = SHOW_CABINET_ERWEIMA_DEPOSIT;
                message.arg1 = 5;
                handler1.sendMessage(message);
            }
        }
    };
    @SuppressLint("HandlerLeak")
    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
             /**
              * 待指派按钮
              */
            case R.id.btn_wait_assign: {
                Intent jumptoPickup = new Intent(CabinetActivity.this,FileDepositDetailNew1Activity.class);
                startActivity(jumptoPickup);

                break;
            }
             /**
              *: 存件记录列表按钮
              */
            case R.id.btn_deposit_file: {
                Intent jumptoPickup = new Intent(CabinetActivity.this,FileDepositDataActivity.class);
                startActivity(jumptoPickup);
                break;
            }
             /**
              * 待取件按钮
              */
            case R.id.btn_wait_pickup: {
                Intent jumptoWaitPickup = new Intent(CabinetActivity.this,FileReverseDetailNew1Activity.class);
                startActivity(jumptoWaitPickup);
                break;
            }

            /**
             * 取件记录列表
             */
            case R.id.btn_picked_up:
                Intent jumptoWaitPickup = new Intent(CabinetActivity.this,FileReverseDataActivity.class);
                startActivity(jumptoWaitPickup);
                break;
            case R.id.tv_department: {
                break;
            }
            case R.id.btn_deposit: {

                final SoapParams soapParams = new SoapParams().put("EmployeeID",employeeId );

                GeekThreadManager.getInstance().execute(new GeekRunnable(ThreadPriority.NORMAL) {
                    @Override
                    public void run() {
                        String dynamicData= WebServiceUtil.getInstance().GetQRCode(soapParams, new SoapClient.ISoapUtilCallback() {
                            @Override
                            public void onSuccess(SoapSerializationEnvelope envelope) throws Exception {
                                verificationErWeiMa  =  envelope.getResponse().toString();
                                Message message = new Message();
                                message.what = SHOW_RESPONSE_AUTHENTICATION;
                                message.obj = v;
                                handler.sendMessage(message);
                            }

                            @Override
                            public void onFailure(Exception e) {
                            }
                        });
                    }
                },ThreadType.NORMAL_THREAD);
                handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what) {
                            case SHOW_RESPONSE_AUTHENTICATION:
                                View v = (View) msg.obj;
                                Log.e("verificationErWeiMa",""+verificationErWeiMa);

                                if (verificationErWeiMa==null){
                                    break;
                                }
                                new Thread(runAddEwmImg).start();

                                Log.e("verificationErWeiMa",""+verificationErWeiMa);
                                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                                // 引入窗口配置文件
                                View view = inflater.inflate(R.layout.view_erweima_popuwindow, null);

                                tvErweimaName = (TextView) view.findViewById(R.id.tv_erwema_name);
                                iverweima = (ImageView) view.findViewById(R.id.iv_erweima);
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
                                            case SHOW_CABINET_ERWEIMA_AUTHENTICATION:
                                                tvErweimaName.setText("身份认证二维码");
                                                btnErweimaNext.setText("下一步");
                                                break;
                                            case SHOW_CABINET_ERWEIMA_DEPOSIT:
                                                tvErweimaName.setText("存件二维码");
                                                btnErweimaNext.setText("完成");
                                                default:
                                                    break;

                                        }
                                        iverweima.setImageBitmap(BitmapFactory.decodeFile(filePath));
                                    }
                                };
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
                                            //生成M+时期+四位随机数
                                            SimpleDateFormat format1    =   new    SimpleDateFormat ("yyyyMMddHHmmss");
                                            Date curDate    =   new    Date(System.currentTimeMillis());
                                            String    timeNow    =    format1.format(curDate);

                                            int intCount = 0;
                                            intCount = (new Random()).nextInt(9999);
                                            if (intCount < 1000) intCount += 1000;
                                            s = "M"+timeNow+intCount + "";
                                            Log.e("lk",s);
                                            new Thread(runAddEwmImgForDeposit).start();
                                        }

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
                                break;
                            default:
                                break;
                        }
                    }
                };
                break;

            }
            default:
                break;


        }

    }

    private Bitmap BC_2weima(String str) {
        try {
            String contentString = String.valueOf(str);
            String contentString2 = URLEncoder.encode(contentString.toString(), "utf-8");
            if (!contentString.equals("")) {
                // 根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
                Bitmap qrCodeBitmap = EncodingHandler.createQRCode(
                        contentString2, 1080);
                return qrCodeBitmap;
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
