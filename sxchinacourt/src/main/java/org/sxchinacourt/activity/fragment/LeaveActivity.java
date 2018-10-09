package org.sxchinacourt.activity.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.activity.MachineActivity;
import org.sxchinacourt.activity.MessagemachineContentActivity;
import org.sxchinacourt.adapter.AnswerLeaveAdapter;
import org.sxchinacourt.adapter.AnswerPhoneAdapter;
import org.sxchinacourt.adapter.EmployeeAdapter;
import org.sxchinacourt.bean.AnswermachineOnceJsonBean;
import org.sxchinacourt.bean.EmployeeBean;
import org.sxchinacourt.bean.UserBean;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.dao.DBHelper;
import org.sxchinacourt.dao.Msgleavedb;
import org.sxchinacourt.dao.MsgleavedbDao;
import org.sxchinacourt.dao.Msgmachinedb;
import org.sxchinacourt.dao.MsgmachinedbDao;
import org.sxchinacourt.util.SoapClient;
import org.sxchinacourt.util.SoapParams;
import org.sxchinacourt.util.WebServiceUtil;
import org.sxchinacourt.wheelview.DateUtils;
import org.sxchinacourt.wheelview.JudgeDate;
import org.sxchinacourt.wheelview.ScreenInfo;
import org.sxchinacourt.wheelview.WheelWeekMain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static org.sxchinacourt.util.TimeUtils.getNowTime;
import static org.sxchinacourt.util.TimeUtils.getTimeString;
import static org.sxchinacourt.util.TimeUtils.times;

public class LeaveActivity extends Activity {
    private Button btn_back;
    private TextView tv_applicant_No;//编号
    private TextView et_applicant_name_No;//编号
    private TextView tv_applicant;//申请人
    private TextView tv_departmentName;//所属部门
    private TextView tv_applicant_data;//申请日期
    private TextView tv_leaveType;//请假类型
    private Spinner sp_leaveType;//请假类型
    private TextView et_leave_start;//开始时间
    private Button btn_leave_start;
    private TextView et_leave_end;//结束时间
    private Button btn_leave_end;
    private RadioGroup rg_leave_days;//请假天数
    private EditText et_leave_days;//请假天数
    private EditText et_leave_reasons;//请假原因
    private EditText et_leave_executor;//执行人
    private Button btn_leave_add_executor;//添加执行人按钮
    private TextView tv_beizhu;//备注
    private Button btn_leave_submit;//提交
    private UserNewBean user;
    private List<UserBean> users;
    private List<String> leaveTypes = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    private WheelWeekMain wheelWeekMainDate;
    private TextView tv_center;
    private String beginTime;
    private int No;//编号
    private String leaveDays;//请假天数

    private String resps;
    public static final int SHOW_RESPONSE_LEAVE_TABLE_NAME = 0;
    public static final int SHOW_RESPONSE_LEAVE = 1;
    private Handler handlerTwo;//判断申请是否成功
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE_LEAVE_TABLE_NAME:

                    resps = (String) msg.obj;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String json = get(resps);
                            Log.e("",json);
                            SoapParams soapParams = new SoapParams().put("jsonstr",json);
                            WebServiceUtil.getInstance().createInstances(soapParams, new SoapClient.ISoapUtilCallback() {
                                @Override
                                public void onSuccess(SoapSerializationEnvelope envelope) throws Exception {
                                    String response = envelope.getResponse().toString();
                                    Log.e("response",""+response);
                                    Message message = new Message();
                                    message.what = SHOW_RESPONSE_LEAVE;
                                    message.obj = response;
                                    handlerTwo.sendMessage(message);
                                }

                                @Override
                                public void onFailure(Exception e) {

                                }
                            });

                        }
                    }).start();

                    handlerTwo = new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            switch (msg.what){
                                case SHOW_RESPONSE_LEAVE:
                                    String respsTwo = (String) msg.obj;
                                    if ("0".equals(respsTwo)){
                                        Toast.makeText(getApplicationContext(),"申请失败，请检查",Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(getApplicationContext(),"申请成功",Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                    break;

                            }
                        }
                    };


                default:
                    break;
            }
        }

    };
    public static final int SHOW_RESPONSE_LEAVE_ADDEXECUTOR = 0;
    private Handler addExecutorhandler;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_leave);
        initView();
        initData();
        initListener();



    }

    private void initData() {
        user = CApplication.getInstance().getCurrentUser();
        tv_applicant.setText(user.getUserName());
        tv_departmentName.setText(user.getDeptname());


        tv_applicant_data.setText(getNowTime());
        tv_leaveType.setText("事假");
        et_leave_start.setText(getNowTime());
        et_leave_end.setText(getNowTime());
        et_leave_days.setText("1");
        et_leave_reasons.getText().toString().trim();
        tv_center = (TextView) findViewById(R.id.tv_based_pop);
        leaveTypes.add("病假");
        leaveTypes.add("事假");
        leaveTypes.add("其他");
        leaveTypes.add("请选择");
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, leaveTypes);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        sp_leaveType.setAdapter(adapter);





    }

    private void initListener() {
        rg_leave_days.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = (RadioButton) findViewById(i);
                leaveDays = rb.getText().toString().trim();
                Log.e("ceshi",""+leaveDays);
            }
        });
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
        sp_leaveType.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                /* 将所选mySpinner 的值带入myTextView 中*/
                tv_leaveType.setText( adapter.getItem(arg2));
                /* 将mySpinner 显示*/
                arg0.setVisibility(View.VISIBLE);
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                tv_leaveType.setText("NONE");
                arg0.setVisibility(View.VISIBLE);
            }
        });
        /*下拉菜单弹出的内容选项触屏事件处理*/
        sp_leaveType.setOnTouchListener(new Spinner.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                /**
                 *
                 */
                return false;
            }
        });
        /*下拉菜单弹出的内容选项焦点改变事件处理*/
        sp_leaveType.setOnFocusChangeListener(new Spinner.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

            }
        });
        btn_leave_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStartWeekBottoPopupWindow();
            }
        });
        btn_leave_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEndWeekBottoPopupWindow();
            }
        });

        btn_leave_add_executor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username=et_leave_executor.getText().toString().trim();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                         users = WebServiceUtil.getInstance().getUserList(username, null);
                        Message message = new Message();
                        message.what = SHOW_RESPONSE_LEAVE_ADDEXECUTOR;
                        message.obj = users;
                        addExecutorhandler.sendMessage(message);

                    }
                }).start();
                addExecutorhandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what) {
                            case SHOW_RESPONSE_LEAVE_ADDEXECUTOR:
                                btn_leave_add_executor.setText("已添加");
                            default:
                                break;
                        }

                    }
                };



            }
        });
        btn_leave_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SoapParams soapParams = new SoapParams().put("name","机关工作人员请假申请");//1414 是假数据，应该改成
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        WebServiceUtil.getInstance().getBoTableName(soapParams, new SoapClient.ISoapUtilCallback() {
                            @Override
                            public void onSuccess(SoapSerializationEnvelope envelope) throws Exception {
                                String response = envelope.getResponse().toString();
                                Log.e("response",""+response);
                                Message message = new Message();
                                message.what = SHOW_RESPONSE_LEAVE_TABLE_NAME;
                                message.obj = response;
                                handler.sendMessage(message);
                            }

                            @Override
                            public void onFailure(Exception e) {

                            }
                        });
                    }
                }).start();

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        tv_applicant_No = (TextView) findViewById(R.id.tv_applicant_No);
        et_applicant_name_No = (EditText)findViewById(R.id.et_applicant_name_No);
        btn_back = (Button) findViewById(R.id.btn_leave_content_back);
        tv_applicant = (TextView) findViewById(R.id.tv_applicant_name);
        tv_departmentName = (TextView) findViewById(R.id.tv_department_name);
        tv_applicant_data = (TextView) findViewById(R.id.tv_applicant_data_labvalue);
        tv_leaveType = (TextView) findViewById(R.id.tv_leave_type);
        sp_leaveType = (Spinner) findViewById(R.id.sp_leaveType);
        et_leave_start = (TextView) findViewById(R.id.et_applicant_time_start);
        et_leave_end = (TextView) findViewById(R.id.et_applicant_time_end);

        rg_leave_days = (RadioGroup) findViewById(R.id.rg_leave_days);
        et_leave_days = (EditText) findViewById(R.id.et_leavedays);
        et_leave_reasons = (EditText) findViewById(R.id.et_leave_reasons);
        et_leave_executor = (EditText) findViewById(R.id.et_executor);
        btn_leave_add_executor = (Button)findViewById(R.id.btn_add_executor);
        btn_leave_submit = (Button) findViewById(R.id.btn_submit);
        btn_leave_start = (Button) findViewById(R.id.btn_applicant_time_start);
        btn_leave_end = (Button) findViewById(R.id.btn_applicant_time_end);

        tv_beizhu = (TextView) findViewById(R.id.tv_beizhu);

    }
    private String get(String resps) {
        try {
            String  time_start= et_leave_start.getText().toString();
            String ceshi1  = time_start.substring(0,10);
            Log.e("ceshi1",""+ceshi1);
            String ceshi2  = time_start.substring(16);
            Log.e("ceshi2",""+ceshi2);


//            String starttime = time_start.substring(0,10)+time_start.substring(16);
//            Log.e("time_start",""+starttime);
            String starttime = time_start.substring(0,10);
            Log.e("time_start",""+starttime);
            String  time_end= et_leave_end.getText().toString();
//            String endtime = time_end.substring(0,10)+time_start.substring(16);
//            Log.e("time_end",""+endtime);
            String endtime = time_end.substring(0,10);
            Log.e("time_end",""+endtime);
            String tbrq = getNowTime();
            tbrq = tbrq.substring(0,10);
            Log.e("tbrq",""+tbrq);

            No = Integer.parseInt(et_applicant_name_No.getText().toString().trim());
            JSONArray array =new JSONArray();
            JSONObject object =new JSONObject();
            JSONObject object1 =new JSONObject();

            JSONObject obj= new JSONObject();
            object.put("BH",No);//编号
            object.put("XM",user.getUserName());//员工姓名
            object.put("SZBM",user.getDeptname());//部门名称

//            object.put("APPLYDATE",times(getNowTime()));//申请日期
            object.put("TBRQ",tbrq);//申请日期
//            object.put("HTYPE",tv_leaveType.getText().toString().trim());//请假类型
            object.put("KSSJ",starttime);//开始时间
            object.put("JSSJ",endtime);//结束时间
            object.put("QJTS",leaveDays);//请假天数
            object.put("QJSY",et_leave_reasons.getText().toString().trim());//请假原因
            object.put("BZ",tv_beizhu.getText().toString());//备注
            array.put(object);
            object1.put(resps,array);
            obj.put("boTableName","机关工作人员请假申请");
            obj.put("title",user.getUserName()+"机关工作人员请假申请");
            obj.put("userId",user.getUserName());
            obj.put("nextParticipants",users.get(0).getUserId());
            obj.put("boDatas",object1);
//            obj.put(resps,array);

//            System.out.println(obj.toString());
            return obj.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public void showStartWeekBottoPopupWindow() {
        WindowManager manager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = manager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        View menuView = LayoutInflater.from(this).inflate(R.layout.show_week_popup_window,null);
        final PopupWindow mPopupWindow = new PopupWindow(menuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
//		final PopupWindow mPopupWindow = new PopupWindow(menuView, (int)(width*0.8),
//				ActionBar.LayoutParams.WRAP_CONTENT);
        ScreenInfo screenInfoDate = new ScreenInfo(this);
        wheelWeekMainDate = new WheelWeekMain(menuView, true);
        wheelWeekMainDate.screenheight = screenInfoDate.getHeight();
        String time = DateUtils.currentMonth().toString();
        Calendar calendar = Calendar.getInstance();
        if (JudgeDate.isDate(time, "yyyy-MM-DD")) {
            try {
                calendar.setTime(new Date(time));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelWeekMainDate.initDateTimePicker(year, month, day, hours,minute);
        final String currentTime = wheelWeekMainDate.getTime().toString();
        mPopupWindow.setAnimationStyle(R.style.AnimationPreview);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.showAtLocation(tv_center, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new poponDismissListener());
        backgroundAlpha(0.6f);
        TextView tv_cancle = (TextView) menuView.findViewById(R.id.tv_cancle);
        TextView tv_ensure = (TextView) menuView.findViewById(R.id.tv_ensure);
        TextView tv_pop_title = (TextView) menuView.findViewById(R.id.tv_pop_title);
        tv_pop_title.setText("选择起始时间");
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mPopupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
        tv_ensure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                beginTime = wheelWeekMainDate.getTime().toString();
                et_leave_start.setText(DateUtils.formateStringH(beginTime,DateUtils.yyyyMMddHHmm));
                mPopupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
    }
    public void showEndWeekBottoPopupWindow() {
        WindowManager manager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = manager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        View menuView = LayoutInflater.from(this).inflate(R.layout.show_week_popup_window,null);

        final PopupWindow mPopupWindow = new PopupWindow(menuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);

//		final PopupWindow mPopupWindow = new PopupWindow(menuView, (int)(width*0.8),
//				ActionBar.LayoutParams.WRAP_CONTENT);
        ScreenInfo screenInfoDate = new ScreenInfo(this);
        wheelWeekMainDate = new WheelWeekMain(menuView, true);
        wheelWeekMainDate.screenheight = screenInfoDate.getHeight();
        String time = DateUtils.currentMonth().toString();
        Calendar calendar = Calendar.getInstance();
        if (JudgeDate.isDate(time, "yyyy-MM-DD")) {
            try {
                calendar.setTime(new Date(time));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelWeekMainDate.initDateTimePicker(year, month, day, hours,minute);
        final String currentTime = wheelWeekMainDate.getTime().toString();
        mPopupWindow.setAnimationStyle(R.style.AnimationPreview);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.showAtLocation(tv_center, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new poponDismissListener());
        backgroundAlpha(0.6f);
        TextView tv_cancle = (TextView) menuView.findViewById(R.id.tv_cancle);
        TextView tv_ensure = (TextView) menuView.findViewById(R.id.tv_ensure);
        TextView tv_pop_title = (TextView) menuView.findViewById(R.id.tv_pop_title);
        tv_pop_title.setText("选择结束时间");
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mPopupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
        tv_ensure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                beginTime = wheelWeekMainDate.getTime().toString();
                et_leave_end.setText(DateUtils.formateStringH(beginTime,DateUtils.yyyyMMddHHmm));
                mPopupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
    }
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }
    class poponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }

}
