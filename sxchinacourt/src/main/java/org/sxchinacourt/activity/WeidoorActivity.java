package org.sxchinacourt.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.sxchinacourt.R;
import org.sxchinacourt.adapter.NewsAdapter;
import org.sxchinacourt.bean.AppBean;
import org.sxchinacourt.bean.BannerBean;
import org.sxchinacourt.bean.NewsBean;
import org.sxchinacourt.bean.NewsParamsBean;
import org.sxchinacourt.bean.ParameterBean;
import org.sxchinacourt.util.GsonUtil;
import org.sxchinacourt.util.SoapClient;
import org.sxchinacourt.util.SoapParams;
import org.sxchinacourt.util.WebServiceUtil;

import java.util.ArrayList;
import java.util.List;

public class WeidoorActivity extends AppCompatActivity {
    private ConvenientBanner fragment_home_convenientBanner;
    private MaterialRefreshLayout refresh;
    private GetWebImageTask mGetWebImageTask;
    private String rstJson;
    private ListView lvNews;
    private Button back;
    private static String BASE_SERVER_URL_NEWS = "http://111.53.181.200:6688/mserver/upfile/webimg/";
    private  List<BannerBean> bannerBeen_list;
    private List respss = null;
    public static final int SHOW_RESPONSE_NEWS = 0;
    public static final int SHOW_RESPONSE_NEWSS = 1;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE_NEWS:

                    respss = (ArrayList)msg.obj;
                    fragment_home_convenientBanner.setPages(new CBViewHolderCreator<LocalImageHolderView>() {
                        @Override
                        public LocalImageHolderView createHolder() {
                            return new LocalImageHolderView();
                        }
                    }, respss)
                            .setPointViewVisible(true)
                            .setPageIndicator(new int[]{R.drawable.circle_gray, R.drawable.circle_white})
                            .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                            .startTurning(3000)
                            .setManualPageable(true);
                default:
                    break;
            }
        }

    };
    private List resps = null;
    private NewsAdapter newsAdapter;
    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE_NEWSS:

                    resps = (ArrayList)msg.obj;

                    newsAdapter = new NewsAdapter(getApplicationContext(),resps);
                    lvNews.setAdapter(newsAdapter);
                    ArrayList<AppBean> apps = new ArrayList<>();
                    ParameterBean bean = new ParameterBean();
                    bean.setNums("5");
                    Gson gson = new Gson();
                    String pp = gson.toJson(bean);
                    mGetWebImageTask = new GetWebImageTask(pp);
                    mGetWebImageTask.execute();
                    initEvent();

                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weidoor);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        initView();
        initData();
        lvNews.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                myScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent jumptoNews = new Intent(getApplicationContext(), NewsActivity.class);
                Bundle bundle = new Bundle();
                NewsBean newsData = (NewsBean)resps.get(i);
                bundle.putString("oid",newsData.getOid() );
                Log.e("oid",""+bundle);
                jumptoNews.putExtras(bundle);
                startActivity(jumptoNews);


            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    private void initView() {
        fragment_home_convenientBanner = (ConvenientBanner) findViewById(R.id.activity_weidoor_convenientBanner);
        refresh = (MaterialRefreshLayout) findViewById(R.id.weodoor_refresh);
        lvNews = (ListView) findViewById(R.id.lv_weidoor_news);
        back = (Button) findViewById(R.id.btn_news_back);
    }

    protected void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NewsParamsBean user = new NewsParamsBean();

                user.setNums("5");
                user.setTypename("法院要闻");
                Gson gson = new Gson();
                String jsonObjString = gson.toJson(user);
                final SoapParams soapParams = new SoapParams().put("jsonstr",jsonObjString);
                WebServiceUtil.getInstance().getWebInfos1(soapParams, new SoapClient.ISoapUtilCallback() {
                    @Override
                    public void onSuccess(SoapSerializationEnvelope envelope) throws Exception {
                        String response = envelope.getResponse().toString();

                        List<NewsBean> resps=new ArrayList<NewsBean>(JSONArray.parseArray(response,NewsBean.class));
//                            CourtDataBean resp = JSON.parseObject(response, CourtDataBean.class);
                        Log.e("lk",""+resps.get(0).getOid());

                        Log.e("list",""+resps.get(0).getFtitle());
                        Log.e("list",""+resps.get(0).getKeyword());

//                            courts = new String[]{resp.get(0).getCourtName()};

                        Message message = new Message();
                        message.what = SHOW_RESPONSE_NEWSS;
                        message.obj = resps;
                        handler1.sendMessage(message);
                    }
                    @Override
                    public void onFailure(Exception e) {
                        Log.e("e",""+e);
                    }
                });
            }
        }).start();

    }

    private void initEvent() {
        refresh.setMaterialRefreshListener(materialRefreshListener);
    }
    private MaterialRefreshListener materialRefreshListener = new MaterialRefreshListener() {
        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {

        }
    };

    private class GetWebImageTask extends AsyncTask<Void,Void,List>{
        private String nums;
        public GetWebImageTask(String nums){
            this.nums = nums;
        }

        @Override
        protected List doInBackground(Void... params) {
            rstJson  =  WebServiceUtil.getInstance().getWebImgs(nums);
            if (rstJson != null){
                bannerBeen_list = GsonUtil.getListFromJson(rstJson, new TypeToken<List<BannerBean>>(){});
            }
            return bannerBeen_list;
        }

        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            Message message = new Message();
            message.what = SHOW_RESPONSE_NEWS;
            respss = list;
            message.obj = respss;
            handler.sendMessage(message);
        }
    }

    public class LocalImageHolderView implements Holder<BannerBean> {

        private ImageView imageView;

        @Override
        public View createView(Context context) {
            View view = View.inflate(context, R.layout.view_imageview, null);
            imageView = (ImageView) view.findViewById(R.id.iv_view_imageview);
            return view;
        }

        @Override
        public void UpdateUI(final Context context, int position, final BannerBean bean) {
            Glide.with(context).load(BASE_SERVER_URL_NEWS+bean.getAttname()).into(imageView);
            refresh.finishRefresh();
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
