package org.sxchinacourt.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.geek.thread.GeekThreadManager;
import com.geek.thread.ThreadPriority;
import com.geek.thread.ThreadType;
import com.geek.thread.task.GeekRunnable;
import com.google.gson.Gson;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.adapter.EmployeeAdapter;
import org.sxchinacourt.bean.CeShiBean;
import org.sxchinacourt.bean.EmployeeBean;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.util.SoapClient;
import org.sxchinacourt.util.SoapParams;
import org.sxchinacourt.util.WebServiceUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author 殇冰无恨
 * @date 2017/10/11
 */

public class EmployeeActivity extends Activity {
    private ListView dateEmployee;
    private static Boolean IsTure=true;
    private static  String[] courts = null;
    private EmployeeAdapter employeeAdapter;
    private List resps = null;
    private Button btnAssign;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE_EMPLOYEE:
                    resps = (ArrayList)msg.obj;
                    employeeAdapter = new EmployeeAdapter(getApplicationContext(),resps);
                    dateEmployee.setAdapter(employeeAdapter);
                    dateEmployee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                            GeekThreadManager.getInstance().execute(new GeekRunnable(ThreadPriority.NORMAL) {
                                @Override
                                public void run() {
                                    EmployeeBean Recipient = (EmployeeBean)resps.get(i);
                                    final UserNewBean user = CApplication.getInstance().getCurrentUser();
                                    final SoapParams soapParams = new SoapParams().put("SerialNo",SerialNo).put("InitiatorID",employeeId).put("RecipientID",Recipient.getEmployeeID());
                                    //指派
                                    WebServiceUtil.getInstance().FileStateInReverse(soapParams, new SoapClient.ISoapUtilCallback() {
                                        @Override
                                        public void onSuccess(SoapSerializationEnvelope envelope) throws Exception {
                                            String result = envelope.toString();
                                            Log.e("result",""+result);
                                            if(result!=null){
                                            }
                                        }
                                        @Override
                                        public void onFailure(Exception e) {
                                        }
                                    });
                                }
                            },ThreadType.NORMAL_THREAD);
                            final UserNewBean user = CApplication.getInstance().getCurrentUser();
                            EmployeeBean RecipientGetAddress = (EmployeeBean)resps.get(i);

                            GeekThreadManager.getInstance().execute(new GeekRunnable(ThreadPriority.NORMAL) {
                                @Override
                                public void run() {
                                    final EmployeeBean Recipient = (EmployeeBean)resps.get(i);
                                    final UserNewBean user = CApplication.getInstance().getCurrentUser();
                                    final SoapParams soapParams = new SoapParams().put("arg0",Recipient.getTDHyhid());
                                    WebServiceUtil.getInstance().GetTelByEmployeeID(soapParams, new SoapClient.ISoapUtilCallback() {
                                        @Override
                                        public void onSuccess(SoapSerializationEnvelope envelope) throws Exception {
                                            Log.e("employeeTel",""+envelope.getResponse().toString());
                                            String employeeTel = envelope.getResponse().toString();
                                            if(employeeTel!=null){
                                                //推送消息
                                                sendMseeage();
                                                //发短信
                                                sendMseeage(Recipient.getName(),employeeTel);
                                            }
                                        }
                                        @Override
                                        public void onFailure(Exception e) {

                                        }
                                    });
                                }
                            },ThreadType.NORMAL_THREAD);
                            Toast.makeText(getApplicationContext(),"指派成功",Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                default:
                    break;
            }
        }
    };

    public void sendMseeage(String Recipient,String employeeTel) {
        CeShiBean ceShiBean = new CeShiBean();
        ceShiBean.setMobiles("18335182938");
        SimpleDateFormat format1    =   new    SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Date curDate    =   new    Date(System.currentTimeMillis());
        String    timeNow    =    format1.format(curDate);
        final UserNewBean user = CApplication.getInstance().getCurrentUser();
        ceShiBean.setSmsContent(user.getUserName()+"于"+timeNow +"给您指派了一份案件材料，请及时取件【晋中中院云柜】");
        ceShiBean.setMans(Recipient);
        Gson gson1 = new Gson();
        String jsonObjString1 = gson1.toJson(ceShiBean);
        Log.e("jsonObjString1",""+jsonObjString1);
        final SoapParams soapParams1 = new SoapParams().put("jsonstr",jsonObjString1);
        GeekThreadManager.getInstance().execute(new GeekRunnable(ThreadPriority.NORMAL) {
            @Override
            public void run() {
                //发消息
                WebServiceUtil.getInstance().sendSmsInfo(soapParams1, new SoapClient.ISoapUtilCallback() {
                    @Override
                    public void onSuccess(SoapSerializationEnvelope envelope) throws Exception {
                        String result  =  envelope.getResponse().toString();
                        Log.e("tel",""+result);
                    }
                    @Override
                    public void onFailure(Exception e) {
                    }
                });
            }
        },ThreadType.NORMAL_THREAD);
    }

    /**
     * 推送消息
     */
    public void sendMseeage() {
        SimpleDateFormat format1    =   new    SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Date curDate    =   new    Date(System.currentTimeMillis());
        String    timeNow    =    format1.format(curDate);
        final UserNewBean user = CApplication.getInstance().getCurrentUser();
        final SoapParams soapParams1 = new SoapParams().put("arg0",user.getUserName()+"于"+timeNow +"给您指派了一份案件材料，请及时取件【晋中中院云柜】").put("arg1","这是一个信息").put("arg2",user.getUserName());
        GeekThreadManager.getInstance().execute(new GeekRunnable(ThreadPriority.NORMAL) {
            @Override
            public void run() {
                //发消息
                WebServiceUtil.getInstance().sendAndrNotByAlias(soapParams1, new SoapClient.ISoapUtilCallback() {
                    @Override
                    public void onSuccess(SoapSerializationEnvelope envelope) throws Exception {
                        String result  =  envelope.getResponse().toString();
                        Log.e("lk推送消息",""+result);
                    }
                    @Override
                    public void onFailure(Exception e) {
                    }
                });
            }
        },ThreadType.NORMAL_THREAD);
    }

    public static final int SHOW_RESPONSE_EMPLOYEE = 0;
    private  Button btnBacktoDepartment;
    public  String SerialNo = "";
    private String employeeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        initview();
        Intent intentGetDataForDepartment= getIntent();
        Bundle bundle = intentGetDataForDepartment.getExtras();
        String departmentId = bundle.getString("departmentId");
        Log.e("departmentId",""+departmentId);
        SerialNo = bundle.getString("SerialNo");
        Log.e("SerialNo",""+SerialNo);
        employeeId = CApplication.getInstance().getCurrentEmployeeID();
        initdata(departmentId);
        btnBacktoDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jumpToDepartment = new Intent(getApplicationContext(),DepartmentActivity.class);
                startActivity(jumpToDepartment);
                finish();
            }
        });

    }

    /**
     * 获取人员数据
     * @param departmentId
     */
    private void initdata(String departmentId) {
        UserNewBean user = CApplication.getInstance().getCurrentUser();
        final SoapParams soapParams = new SoapParams().put("Departmentid",departmentId);
        GeekThreadManager.getInstance().execute(new GeekRunnable(ThreadPriority.NORMAL) {
            @Override
            public void run() {
                WebServiceUtil.getInstance().GetEmployee(soapParams, new SoapClient.ISoapUtilCallback() {
                    @Override
                    public void onSuccess(SoapSerializationEnvelope envelope) throws Exception {
                        String response = envelope.getResponse().toString();
                        List<EmployeeBean> resps=new ArrayList<EmployeeBean>(JSONArray.parseArray(response,EmployeeBean.class));
                        Log.e("list",""+resps.get(0).getName());
                        Log.e("courts",""+courts);
                        Message message = new Message();
                        message.what = SHOW_RESPONSE_EMPLOYEE;
                        message.obj = resps;
                        handler.sendMessage(message);
                    }
                    @Override
                    public void onFailure(Exception e) {
                        Log.e("e",""+e);
                    }
                });
            }
        },ThreadType.NORMAL_THREAD);
    }

    private void initview() {
        dateEmployee = (ListView) findViewById(R.id.data_employee);
        btnAssign = (Button) findViewById(R.id.btn_assign_sure);
        btnBacktoDepartment = (Button) findViewById(R.id.btn_employee_back);
    }
}
