package org.sxchinacourt.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.activity.MessagemachineContentActivity;
import org.sxchinacourt.adapter.AnswerPhoneAdapter;
import org.sxchinacourt.bean.AnswermachineOnceJsonBean;
import org.sxchinacourt.bean.AnswermachineTwiceJsonBean;
import org.sxchinacourt.bean.EmployeeBean;
import org.sxchinacourt.bean.MessagemachineBean;
import org.sxchinacourt.bean.UserBean;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.dao.DBHelper;
import org.sxchinacourt.dao.Msgmachinedb;
import org.sxchinacourt.dao.MsgmachinedbDao;
import org.sxchinacourt.util.SoapClient;
import org.sxchinacourt.util.SoapParams;
import org.sxchinacourt.util.WebServiceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 殇冰无恨 on 2017/11/29.
 */

public class MessageMachineFragment extends BaseFragment {
    private List<AnswermachineOnceJsonBean> mMessagemachineBeanList = new ArrayList<>();
    public static final int SHOW_RESPONSE_MESSAGEMACHINE= 0;
    private Handler handler = null;

    private Button button;

    private RecyclerView lvMessage;
    private Timer timer;
    private TimerTask task = null;



    private Msgmachinedb msgmachinedb;
    private MsgmachinedbDao msgmachinedbDao;
    private DBHelper dbHelper = DBHelper.getInstance();
    private String TABLE_NAME = "msgmachine.db";
    List<Msgmachinedb> msgmachinedbs= new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "留言机";
        mShowBtnBack = View.VISIBLE;

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, R.layout.fragment_message_machine, container,
                savedInstanceState);
    }

    @Nullable
    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.initFragment(container, savedInstanceState);
        initFruits();
        button = (Button) mRootView.findViewById(R.id.btn_voice);
        lvMessage = (RecyclerView) mRootView.findViewById(R.id.lv_message_machine);
        msgmachinedbDao = dbHelper.initDatabase(getContext(),TABLE_NAME).getMsgmachinedbDao();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MessagemachineContentActivity.class);
                intent.putExtra("answerType","1");
                intent.putExtra("content", "test");
                startActivity(intent);
            }
        });

    }

    private void initFruits() {
        final UserNewBean user = CApplication.getInstance().getCurrentUser();
        String str = String.valueOf ( user.getOaid() );
        final SoapParams soapParams = new SoapParams().put("arg0",str);
        new Thread(new Runnable() {
            @Override
            public void run() {
                timer = new Timer();

                task = new TimerTask() {
                    @Override

                    public void run() {



                        String dynamicData = WebServiceUtil.getInstance().getOaMessageList(soapParams, new SoapClient.ISoapUtilCallback() {
                            @Override
                            public void onSuccess(SoapSerializationEnvelope envelope) throws Exception {
                                String result = envelope.getResponse().toString();

//                                result = "[{\"answerId\":\"68d5c63963c042288277f25ad7c400c9\",\"answerTime\":\"2017-12-19 17:40:43\",\"answerType\":\"2\",\"del_flag\":\"0\",\"filePath\":\"/UpLoadFiles/vedio/史红波_201712191740052122.avi\",\"flag\":\"0\",\"idNumber\":\"142401199308261437\",\"judgeId\":\"402881e65905269201590574ea970027\",\"judgeName\":\"史红波\",\"oid\":\"04a48196606da21c01606e286b82013e\",\"personName\":\"王蒙\",\"phone_flag\":\"0\",\"textContent\":\"null\"}]";

                                Log.d(result, "onSuccess: " + result);

                                List<AnswermachineOnceJsonBean> resps = new ArrayList<AnswermachineOnceJsonBean>(JSONArray.parseArray(result, AnswermachineOnceJsonBean.class));

//                                AnswermachineOnceJsonBean onceResp = JSON.parseObject(result,AnswermachineOnceJsonBean.class);
//                                //得到需要二次解析的Json字符串
//                                String jsondata = onceResp.getJsonStr();
//                                // 声明中间变量进行处理中间的"/"
//                                String fly= jsondata.replace("\\", "");
//                                // 处理完成后赋值回去
//                                jsondata= fly.substring(1,fly.length() - 1);
//                                List<AnswermachineTwiceJsonBean> twiceResp=new ArrayList<AnswermachineTwiceJsonBean>(JSONArray.parseArray(jsondata,AnswermachineTwiceJsonBean.class));
                                Message message = new Message();
                                message.what = SHOW_RESPONSE_MESSAGEMACHINE;
                                message.obj = resps;
                                handler.sendMessage(message);


                            }

                            @Override
                            public void onFailure(Exception e) {
                            }
                        });
                    }
                };

                timer.schedule(task,1000,30000);//半分钟执行一次查询操作,访问一次后台
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
                        View view =  getView();
                        mMessagemachineBeanList = (ArrayList)msg.obj;
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
                            String[] a= test.split("=");
                            Log.e("testpath",""+a[1]);

                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        lvMessage.setLayoutManager(layoutManager);
                        AnswerPhoneAdapter answerPhoneAdapter = new AnswerPhoneAdapter(getContext(),view ,msgmachinedbs);
                        lvMessage.setAdapter(answerPhoneAdapter);
                        break;
                }

            }
        };
//        for (int i=0;i<2;i++){
//            MessagemachineBean user1 = new MessagemachineBean();
//            user1.setPersonName("user1");
//            user1.setAnswerType("文字留言");
//            user1.setTextContent("法官你好!请听我说");
//            user1.setAnswerTime("0:00");
//            mMessagemachineBeanList.add(user1);
//            MessagemachineBean user2 = new MessagemachineBean();
//            user2.setPersonName("user2");
//            user2.setAnswerType("语音留言");
//            user2.setTextContent("法官你好!请听我说");
//            user2.setAnswerTime("0:00");
//            mMessagemachineBeanList.add(user2);
//            MessagemachineBean user3 = new MessagemachineBean();
//            user3.setPersonName("user3");
//            user3.setAnswerType("文字留言");
//            user3.setTextContent("法官你好!请听我说");
//            user3.setAnswerTime("0:00");
//            mMessagemachineBeanList.add(user3);
//            MessagemachineBean user4 = new MessagemachineBean();
//            user4.setPersonName("user4");
//            user4.setAnswerType("视频留言");
//            user4.setTextContent("法官你好!请听我说");
//            user4.setAnswerTime("0:00");
//            mMessagemachineBeanList.add(user4);
//            MessagemachineBean user5 = new MessagemachineBean();
//            user5.setPersonName("user5");
//            user5.setAnswerType("文字留言");
//            user5.setTextContent("法官你好!请听我说");
//            user5.setAnswerTime("0:00");
//            mMessagemachineBeanList.add(user5);
//            MessagemachineBean user6 = new MessagemachineBean();
//            user6.setPersonName("user6");
//            user6.setAnswerType("语音留言");
//            user6.setTextContent("法官你好!请听我说");
//            user6.setAnswerTime("0:00");
//            mMessagemachineBeanList.add(user6);
//            MessagemachineBean user7 = new MessagemachineBean();
//            user7.setPersonName("user7");
//            user7.setAnswerType("视频留言");
//            user7.setTextContent("法官你好!请听我说");
//            user7.setAnswerTime("0:00");
//            mMessagemachineBeanList.add(user7);
//            MessagemachineBean user8 = new MessagemachineBean();
//            user8.setPersonName("user8");
//            user8.setAnswerType("文字留言");
//            user8.setTextContent("法官你好!请听我说");
//            user8.setAnswerTime("0:00");
//            mMessagemachineBeanList.add(user8);
//            MessagemachineBean user9 = new MessagemachineBean();
//            user9.setPersonName("user9");
//            user9.setAnswerType("语音留言");
//            user9.setTextContent("法官你好!请听我说");
//            user9.setAnswerTime("0:00");
//            mMessagemachineBeanList.add(user9);
//            MessagemachineBean user10 = new MessagemachineBean();
//            user10.setPersonName("user10");
//            user10.setAnswerType("文字留言");
//            user10.setTextContent("法官你好!请听我说");
//            user10.setAnswerTime("0:00");
//            mMessagemachineBeanList.add(user10);
//
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timer.cancel();
    }
}
