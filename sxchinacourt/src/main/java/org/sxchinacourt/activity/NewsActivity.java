package org.sxchinacourt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.sxchinacourt.R;
import org.sxchinacourt.adapter.NewsAdapter;
import org.sxchinacourt.adapter.NewsDetailAdapter;
import org.sxchinacourt.bean.NewsBean;
import org.sxchinacourt.bean.NewsContentPattsRoot;
import org.sxchinacourt.bean.WebInfosBean;
import org.sxchinacourt.util.SoapClient;
import org.sxchinacourt.util.SoapParams;
import org.sxchinacourt.util.WebServiceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 殇冰无恨 on 2017/9/30.
 */

public class NewsActivity extends Activity{
    private TextView tvTitle;
    private TextView tvTime;
    private WebView webContent;
    private NewsDetailAdapter  newsDetailAdapter;
    private List listData = null;
    private NewsBean newsBean;
    private List resps = null;
    private Button btn_news_back;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE_NEWS_CONTENT:

                    resps = (ArrayList)msg.obj;
                    NewsContentPattsRoot newsContentPattsRoot = (NewsContentPattsRoot)resps.get(0);
                    tvTitle.setText(newsContentPattsRoot.getFtitle());
                    tvTime.setText(newsContentPattsRoot.getReleasetime());
                    webContent.loadDataWithBaseURL("about:blank",newsContentPattsRoot.getContent(),"text/html","utf-8",null);


                default:
                    break;
            }
        }

    };

    public static final int SHOW_RESPONSE_NEWS_CONTENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        initview();
        Intent intentGetData = getIntent();
        Bundle bundle = intentGetData.getExtras();
        String oid = bundle.getString("oid");
        Log.e("oid",""+oid);
        initData(oid);
        initEvent();

    }

    private void initEvent() {
        btn_news_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData(final String oid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebInfosBean webInfosBean = new WebInfosBean();
                webInfosBean.setOid(oid);
                Gson gson1 = new Gson();
                String jsonObjString1 = gson1.toJson(webInfosBean);
                Log.e("jsonObjString1",""+jsonObjString1);
//                WebServiceUtil.getInstance().getWebInfos(jsonObjString1);
                final SoapParams soapParams1 = new SoapParams().put("jsonstr",jsonObjString1);
                WebServiceUtil.getInstance().getWebInfos(soapParams1, new SoapClient.ISoapUtilCallback() {
                    @Override
                    public void onSuccess(SoapSerializationEnvelope envelope) throws Exception {
                        String response = envelope.getResponse().toString();

                        NewsContentPattsRoot resps = JSON.parseObject(response,NewsContentPattsRoot.class);
//                        List<NewsBean> resps=new ArrayList<NewsBean>(JSONArray.parseArray(response,NewsBean.class));
//                            CourtDataBean resp = JSON.parseObject(response, CourtDataBean.class);
                        List<NewsContentPattsRoot> resp = new ArrayList<NewsContentPattsRoot>();
                        resp.add(resps);
                        Log.e("lk",""+resps.getFtitle());



//                            courts = new String[]{resp.get(0).getCourtName()};

                        Message message = new Message();
                        message.what = SHOW_RESPONSE_NEWS_CONTENT;
                        message.obj = resp;
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });

            }
        }).start();


        listData = new ArrayList();

        newsBean = new NewsBean();
        newsBean.setFtitle("asdasdasda");
        newsBean.setContent("审管办考核");
        newsBean.setToptime("2017.9.14");
        listData.add(newsBean);
    }

    private void initview() {
        webContent = (WebView) findViewById(R.id.news_web_content);
        btn_news_back = (Button) findViewById(R.id.btn_news_back);
        tvTitle = (TextView) findViewById(R.id.news_tv_Title);
        tvTime = (TextView) findViewById(R.id.news_tv_time);
    }

}
