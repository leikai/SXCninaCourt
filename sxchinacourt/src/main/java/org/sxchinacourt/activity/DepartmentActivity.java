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

import com.alibaba.fastjson.JSONArray;
import com.geek.thread.GeekThreadManager;
import com.geek.thread.ThreadPriority;
import com.geek.thread.ThreadType;
import com.geek.thread.task.GeekRunnable;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.adapter.DepartmentForCabinetAdapter;
import org.sxchinacourt.bean.DepartmentForCabinetBean;
import org.sxchinacourt.util.SoapClient;
import org.sxchinacourt.util.SoapParams;
import org.sxchinacourt.util.WebServiceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 殇冰无恨
 * @date 2017/10/11
 */

public class DepartmentActivity extends Activity{
    private ListView dateDepartment;
    private static Boolean IsTure=true;
    private static  String[] courts = null;
    private DepartmentForCabinetAdapter departmentForCabinetAdapter;
    private List resps = null;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE_DEPARTMENT:

                    resps = (ArrayList)msg.obj;
                    departmentForCabinetAdapter = new DepartmentForCabinetAdapter(getApplicationContext(),resps);
                    dateDepartment.setAdapter(departmentForCabinetAdapter);
                    dateDepartment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent jumptoemployee = new Intent(getApplicationContext(),EmployeeActivity.class);
                            Bundle bundle = new Bundle();
                            DepartmentForCabinetBean departmentForCabinetBean = (DepartmentForCabinetBean)resps.get(i);
                            bundle.putString("departmentId",String.valueOf(departmentForCabinetBean.getDepartmentID()));
                            bundle.putString("SerialNo",SerialNo);
                            Log.e("departmentId",""+bundle);
                            jumptoemployee.putExtras(bundle);
                            startActivity(jumptoemployee);
                            finish();
                        }
                    });
                default:
                    break;
            }
        }

    };

    public static final int SHOW_RESPONSE_DEPARTMENT = 0;
    private Button backToCourt;
    public  String SerialNo = "";
    private String employeeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        initview();
        Intent intentGetDataForCourt = getIntent();
        Bundle bundle = intentGetDataForCourt.getExtras();
        String courtId = bundle.getString("courtId");
        SerialNo = bundle.getString("SerialNo");
        Log.e("courtId",""+courtId);
        employeeId = CApplication.getInstance().getCurrentEmployeeID();
        initdata(courtId);
        jump();

    }

    private void jump() {
        backToCourt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jumptoCourt = new Intent(getApplicationContext(),AssignActivity.class);
                startActivity(jumptoCourt);
                finish();
            }
        });
    }

    private void initdata(String courtId) {

        final SoapParams soapParams = new SoapParams().put("CourtID",courtId);

        GeekThreadManager.getInstance().execute(new GeekRunnable(ThreadPriority.NORMAL) {
            @Override
            public void run() {
                WebServiceUtil.getInstance().GetDepartment(soapParams, new SoapClient.ISoapUtilCallback() {
                    @Override
                    public void onSuccess(SoapSerializationEnvelope envelope) throws Exception {
                        String response = envelope.getResponse().toString();

                        List<DepartmentForCabinetBean> resps=new ArrayList<DepartmentForCabinetBean>(JSONArray.parseArray(response,DepartmentForCabinetBean.class));
                        Log.e("list",""+resps.get(0).getDepartmentName());

                        Log.e("courts",""+courts);
                        Message message = new Message();
                        message.what = SHOW_RESPONSE_DEPARTMENT;
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
        dateDepartment = (ListView) findViewById(R.id.data_department);
        backToCourt = (Button) findViewById(R.id.btn_department_back);

    }
}
