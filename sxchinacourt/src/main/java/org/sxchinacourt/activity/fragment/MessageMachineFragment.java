package org.sxchinacourt.activity.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

import com.alibaba.fastjson.JSONArray;
import com.geek.thread.GeekThreadManager;
import com.geek.thread.ThreadPriority;
import com.geek.thread.ThreadType;
import com.geek.thread.task.GeekRunnable;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.activity.MessagemachineContentActivity;
import org.sxchinacourt.adapter.AnswerPhoneAdapter;
import org.sxchinacourt.bean.AnswermachineOnceJsonBean;

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
 *
 * @author 殇冰无恨
 * @date 2017/11/29
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

    @SuppressLint("HandlerLeak")
    private void initFruits() {
        final UserNewBean user = CApplication.getInstance().getCurrentUser();
        String str = String.valueOf ( user.getOaid() );
        final SoapParams soapParams = new SoapParams().put("arg0",str);

        GeekThreadManager.getInstance().execute(new GeekRunnable(ThreadPriority.NORMAL) {
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
                //半分钟执行一次查询操作,访问一次后台
                timer.schedule(task,1000,30000);
            }
        },ThreadType.NORMAL_THREAD);
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
                        default:
                            break;
                }

            }
        };

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timer.cancel();
    }
}
