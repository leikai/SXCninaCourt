package org.sxchinacourt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.adapter.AnswerPhoneAdapter;
import org.sxchinacourt.bean.AnswermachineOnceJsonBean;
import org.sxchinacourt.bean.UserBean;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.dao.DBHelper;
import org.sxchinacourt.dao.Msgmachinedb;
import org.sxchinacourt.dao.MsgmachinedbDao;
import org.sxchinacourt.util.SoapClient;
import org.sxchinacourt.util.SoapParams;
import org.sxchinacourt.util.WebServiceUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MachineActivity extends Activity {
    private List<AnswermachineOnceJsonBean> mMessagemachineBeanList = new ArrayList<>();
    public static final int SHOW_RESPONSE_MESSAGEMACHINE= 0;
    private Handler handler = null;

    private ImageView ivWait;
    private TextView tvNodata;
    private Button button;
    private Button btnback;
    private RecyclerView lvMessage;
    private AnswerPhoneAdapter answerPhoneAdapter;
    private Timer timer;
    private TimerTask task = null;



    private Msgmachinedb msgmachinedb;
    private MsgmachinedbDao msgmachinedbDao;
    private DBHelper dbHelper = DBHelper.getInstance();
    private String TABLE_NAME = "msgmachine.db";
    List<Msgmachinedb> msgmachinedbs= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_machine);
//        if (getSupportActionBar() != null){
//            getSupportActionBar().hide();
//        }
        initFruits();
        ivWait = (ImageView) findViewById(R.id.iv_view_wait);
        tvNodata = (TextView) findViewById(R.id.tv_nodata);
        btnback = (Button) findViewById(R.id.btn_messagemachine_content_back);
        button = (Button) findViewById(R.id.btn_voice);
        lvMessage = (RecyclerView) findViewById(R.id.lv_message_machine);
        msgmachinedbDao = dbHelper.initDatabase(getApplicationContext(),TABLE_NAME).getMsgmachinedbDao();

        Glide.with(MachineActivity.this).load("http://img.lanrentuku.com/img/allimg/1212/5-121204193Q9-50.gif")
                .into(ivWait);
        button.setVisibility(View.GONE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MachineActivity.this, MessagemachineContentActivity.class);
                intent.putExtra("answerType","1");
                intent.putExtra("content", "test");
                startActivity(intent);
            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initFruits() {
        final UserNewBean user = CApplication.getInstance().getCurrentUser();
        String str = String.valueOf ( user.getOaid() );
        final SoapParams soapParams = new SoapParams().put("arg0",str);
        final SoapParams soapParams1 = new SoapParams().put("judge_id", user.getOid()).put("del_flag","0");
        new Thread(new Runnable() {
            @Override
            public void run() {
                timer = new Timer();

                task = new TimerTask() {
                    @Override

                    public void run() {

                        UserNewBean user = CApplication.getInstance().getCurrentUser();

                        //-----------------------------------------------------------------------//
                        String judge_id = "{\"judge_id\":\"" + user.getOid()
                                + "\",\"del_flag\":\"0\" }";
                        String  result = WebServiceUtil.getInstance().getOaAnswerMessages(judge_id.toString());
                        Log.d(result, "onSuccess: " + result);
                        List<AnswermachineOnceJsonBean> resps = new ArrayList<AnswermachineOnceJsonBean>(JSONArray.parseArray(result, AnswermachineOnceJsonBean.class));

//                        String oid = resps.get(0).getOid();
//                        String  ceshi = WebServiceUtil.getInstance().getTheOaAnswerMessage(oid);




                        Message message = new Message();
                        message.what = SHOW_RESPONSE_MESSAGEMACHINE;
                        message.obj = resps;
                        handler.sendMessage(message);
                        //-----------------------------------------------------------------------//




//                        WebServiceUtil.getInstance().getOaAnswerMessages(soapParams1, new SoapClient.ISoapUtilCallback() {
//                            @Override
//                            public void onSuccess(SoapSerializationEnvelope envelope) throws Exception {
//
//                                String result = envelope.getResponse().toString();
//                                Log.d(result, "onSuccess: " + result);
//                            }
//
//                            @Override
//                            public void onFailure(Exception e) {
//
//                            }
//                        });

//                        String dynamicData = WebServiceUtil.getInstance().getOaMessageList(soapParams, new SoapClient.ISoapUtilCallback() {
//                            @Override
//                            public void onSuccess(SoapSerializationEnvelope envelope) throws Exception {
//                                String result = envelope.getResponse().toString();
//                                Log.d(result, "onSuccess: " + result);
//                                List<AnswermachineOnceJsonBean> resps = new ArrayList<AnswermachineOnceJsonBean>(JSONArray.parseArray(result, AnswermachineOnceJsonBean.class));
//                                Message message = new Message();
//                                message.what = SHOW_RESPONSE_MESSAGEMACHINE;
//                                message.obj = resps;
//                                handler.sendMessage(message);
//                            }
//
//                            @Override
//                            public void onFailure(Exception e) {
//                            }
//                        });
                    }
                };

                timer.schedule(task,1000,300000000);//半分钟执行一次查询操作,访问一次后台
            }
//                };
//            }
        }).start();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case SHOW_RESPONSE_MESSAGEMACHINE:
                        View view =  getCurrentFocus();
                        mMessagemachineBeanList = (ArrayList)msg.obj;
                        msgmachinedbDao.deleteAll();
                        for (int i = 0;i<mMessagemachineBeanList.size();i++){

                            //将数据添加到数据库
                            msgmachinedb = new Msgmachinedb();
                            msgmachinedb.setAnswerId(mMessagemachineBeanList.get(i).getAnswerId());
                            msgmachinedb.setAnswerTime(mMessagemachineBeanList.get(i).getAnswerTime());
                            msgmachinedb.setAnswerType(mMessagemachineBeanList.get(i).getAnswerType());
                            msgmachinedb.setDel_flag(mMessagemachineBeanList.get(i).getDel_flag());
                            msgmachinedb.setFilePath(mMessagemachineBeanList.get(i).getFilePath());
                            msgmachinedb.setFlag(mMessagemachineBeanList.get(i).getFlag());
                            msgmachinedb.setIdNumber(mMessagemachineBeanList.get(i).getIdNumber());
                            msgmachinedb.setJudgeId(mMessagemachineBeanList.get(i).getJudgeId());
                            msgmachinedb.setJudgeName(mMessagemachineBeanList.get(i).getJudgeName());
                            msgmachinedb.setOid(mMessagemachineBeanList.get(i).getOid());
                            msgmachinedb.setPersonName(mMessagemachineBeanList.get(i).getPersonName());
                            msgmachinedb.setPhone_flag(mMessagemachineBeanList.get(i).getPhone_flag());
                            msgmachinedb.setTextContent(mMessagemachineBeanList.get(i).getTextContent());
                            msgmachinedbDao.insert(msgmachinedb);
                        }
                        msgmachinedbs = msgmachinedbDao.loadAll();
                        if (msgmachinedbs.size() != 0){
                            String test =msgmachinedbs.get(0).getFilePath();
                            Log.e("test",""+test);
                            if (test!=null){
                                String[] a= test.split("=");
                                Log.e("testpath",""+a[1]);
                                ivWait.setVisibility(View.GONE);
                                tvNodata.setVisibility(View.GONE);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                lvMessage.setLayoutManager(layoutManager);
//                        initBind();
                                answerPhoneAdapter = new AnswerPhoneAdapter(MachineActivity.this,view ,msgmachinedbs);

                                lvMessage.setAdapter(answerPhoneAdapter);

                            }else{
                                ivWait.setVisibility(View.GONE);
                                tvNodata.setVisibility(View.GONE);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                lvMessage.setLayoutManager(layoutManager);
//                        initBind();
                                answerPhoneAdapter = new AnswerPhoneAdapter(MachineActivity.this,view ,msgmachinedbs);

                                lvMessage.setAdapter(answerPhoneAdapter);
                            }


                        }else {
                            ivWait.setVisibility(View.GONE);
                        }


                        break;
                }

            }
        };
    }
    private void initBind(){
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                //侧滑删除
                int swipeFlags = ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;

                //首先回调的方法 返回int 表示是否监听该方向
                int dragFlags = ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;

                return makeMovementFlags(dragFlags,swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //侧滑事件
                Collections.swap(msgmachinedbs,viewHolder.getAdapterPosition(),target.getAdapterPosition());
                answerPhoneAdapter.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());

                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //侧滑事件
                msgmachinedbs.remove(viewHolder.getAdapterPosition());
                msgmachinedbDao.delete(msgmachinedbs.get(viewHolder.getAdapterPosition()));
                answerPhoneAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

            }
        });
        helper.attachToRecyclerView(lvMessage);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

}
