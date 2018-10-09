package org.sxchinacourt.activity;

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

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.adapter.CourtAdapter;
import org.sxchinacourt.bean.CourtDataBean;
import org.sxchinacourt.bean.UserBean;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.util.SoapClient;
import org.sxchinacourt.util.SoapParams;
import org.sxchinacourt.util.WebServiceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 殇冰无恨 on 2017/10/11.
 */

public class AssignActivity extends Activity{
    private ListView dateCourt;
    private static Boolean IsTure=true;
    private static  String[] courts = null;
    private CourtAdapter courtAdapter;
    private List resps = null;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE_COURT:

                    resps = (ArrayList)msg.obj;
                    courtAdapter = new CourtAdapter(getApplicationContext(),resps);
                    dateCourt.setAdapter(courtAdapter);
                    dateCourt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent jumptodepartment = new Intent(getApplicationContext(),DepartmentActivity.class);
                            Bundle bundle = new Bundle();
                            CourtDataBean courtDataBean = (CourtDataBean)resps.get(i);
                            bundle.putString("courtId",String.valueOf(courtDataBean.getCourtId())   );
                            bundle.putString("SerialNo",SerialNo);
                            Log.e("courtId",""+bundle);
                            jumptodepartment.putExtras(bundle);
                            startActivity(jumptodepartment);
                            finish();
                        }
                    });

                default:
                    break;
            }
        }

    };


    public static final int SHOW_RESPONSE_COURT = 0;
    private Button btnBacktoFileDeposit;
    public  String SerialNo = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign);
        initview();
        Intent intentGetDataForFileDepositDetail = getIntent();
        Bundle bundle = intentGetDataForFileDepositDetail.getExtras();
        SerialNo = bundle.getString("SerialNo");
        Log.e("SerialNo",""+SerialNo);
        initdata();
        jump();


    }

    private void jump() {
        btnBacktoFileDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent jumptoFileDeposit = new Intent(getApplicationContext(),FileDepositDetailActivity.class);
//                startActivity(jumptoFileDeposit);
                finish();
            }
        });
    }

    private void initdata() {
        startThread();

    }

    private void initview() {
        dateCourt = (ListView) findViewById(R.id.data_court);
        btnBacktoFileDeposit = (Button) findViewById(R.id.btn_court_back);
    }
    private void startThread() {
        UserNewBean user = CApplication.getInstance().getCurrentUser();
//        final SoapParams soapParams = new SoapParams().put("EmployeeID",user.getUserId());//1414 是假数据，应该改成
        final SoapParams soapParams = new SoapParams().put("arg0",String.valueOf ( user.getOaid()));//1414 是假数据，应该改成

        new Thread(new Runnable() {
            @Override
            public void run() {
                WebServiceUtil.getInstance().GetCourtInfo(soapParams, new SoapClient.ISoapUtilCallback() {
                    @Override
                    public void onSuccess(SoapSerializationEnvelope envelope) throws Exception {
                        String response = envelope.getResponse().toString();

                        List<CourtDataBean> resps=new ArrayList<CourtDataBean>(JSONArray.parseArray(response,CourtDataBean.class));
//                            CourtDataBean resp = JSON.parseObject(response, CourtDataBean.class);
                        Log.e("list",""+resps.get(0).getCourtName());

//                            courts = new String[]{resp.get(0).getCourtName()};
                        Log.e("courts",""+courts);
                        Message message = new Message();
                        message.what = SHOW_RESPONSE_COURT;
                        message.obj = resps;
                        handler.sendMessage(message);
                    }
                    @Override
                    public void onFailure(Exception e) {
                        Log.e("e",""+e);
                    }
                });
            }
        }).start();
    }

}
