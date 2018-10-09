package org.sxchinacourt.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.alibaba.fastjson.JSONArray;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.sxchinacourt.R;
import org.sxchinacourt.activity.NewsActivity;
import org.sxchinacourt.adapter.AppsAdapter;
import org.sxchinacourt.adapter.NewsAdapter;
import org.sxchinacourt.bean.AppBean;
import org.sxchinacourt.bean.BannerBean;
import org.sxchinacourt.bean.NewsBean;
import org.sxchinacourt.bean.NewsParamsBean;
import org.sxchinacourt.bean.ParameterBean;
import org.sxchinacourt.util.GsonUtil;
import org.sxchinacourt.util.PinYin.StringUtil;
import org.sxchinacourt.util.SoapClient;
import org.sxchinacourt.util.SoapParams;
import org.sxchinacourt.util.WebServiceUtil;
import org.sxchinacourt.widget.MyNestedScrollView;

import java.util.ArrayList;
import java.util.List;

import static org.sxchinacourt.util.GsonUtil.getBeanFromJson;

/**
 * Created by baggio on 2017/2/3.
 */

public class AppsFragment extends BaseFragment {
    private ConvenientBanner fragment_home_convenientBanner;
    private MaterialRefreshLayout refresh;
    private MyNestedScrollView myScrollView;
    private GetWebImageTask mGetWebImageTask;
    private String rstJson;
    private ListView lvNews;
    private static String BASE_SERVER_URL_NEWS = "http://111.53.181.200:6688/mserver/upfile/webimg/";
//    private static String BASE_SERVER_URL_NEWS = "http://10.0.0.84:6688/mserver/upfile/webimg/";
    private  List<BannerBean> bannerBeen_list;
    private List respss = null;
    public static final int SHOW_RESPONSE_NEWS = 0;
    public static final int SHOW_RESPONSE_NEWSS = 1;
    GridView mAppsGridView;
    int[] mResourceIdArray = {R.drawable.app_todo_icon, R.drawable.app_completed_icon, R.drawable
            .app_notice_icon, R.drawable.app_manager_icon,R.drawable.app_manager_icon,R.drawable.app_manager_icon};//, R.drawable.app_email_icon
    String[] mAppNameArray = {"待办", "已办查询", "公告", "任务管理","云柜","留言机"};//, "收发邮件"
    int[] mFragmentIndexArray = {AppBean.FRAGMENT_TO_DO_TASK, AppBean.FRAGMENT_HISTORY_TASK, AppBean
            .FRAGMENT_NOTICE_TASK, AppBean.FRAGMENT_MANAGER_TASK,AppBean.FRAGMENT_CABINET_TASK,AppBean.FRAGMENT_MESSAGE_MACHINE};//, AppBean.FRAGMENT_EMAIL_TASK
    AppsAdapter mAdapter;
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

                    newsAdapter = new NewsAdapter(getActivity().getApplicationContext(),resps);
                    lvNews.setAdapter(newsAdapter);

                default:
                    break;
            }
        }

    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "应用";
        mShowBtnBack = View.INVISIBLE;
        mLogo = View.INVISIBLE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCreateView(inflater, R.layout.fragment_apps, container, savedInstanceState);
    }

    @Nullable
    @Override
    public void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAppsGridView = (GridView) mRootView.findViewById(R.id.apps_gridview);
        fragment_home_convenientBanner = (ConvenientBanner) mRootView.findViewById(R.id.fragment_home_convenientBanner);
        refresh = (MaterialRefreshLayout) mRootView.findViewById(R.id.refresh);
        myScrollView = (MyNestedScrollView) mRootView.findViewById(R.id.scrollview);
        myScrollView.setFocusable(true);
        lvNews = (ListView) mRootView.findViewById(R.id.lv_application_news);
        initData();
        lvNews.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                myScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent jumptoNews = new Intent(getActivity().getApplicationContext(), NewsActivity.class);
                Bundle bundle = new Bundle();
                NewsBean newsData = (NewsBean)resps.get(i);
                bundle.putString("oid",newsData.getOid() );
                Log.e("oid",""+bundle);
                jumptoNews.putExtras(bundle);
                startActivity(jumptoNews);


            }
        });
    }

    protected void initData() {
        ArrayList<AppBean> apps = new ArrayList<>();
        for (int i = 0; i < mResourceIdArray.length; i++) {
            AppBean app = new AppBean();
            app.setResourceId(mResourceIdArray[i]);
            app.setName(mAppNameArray[i]);
            app.setFragmentIndex(mFragmentIndexArray[i]);
            apps.add(app);
        }
        mAdapter = new AppsAdapter(this, apps);
        mAppsGridView.setAdapter(mAdapter);
        ParameterBean bean = new ParameterBean();
        bean.setNums("5");
        Gson gson = new Gson();
        String pp = gson.toJson(bean);
        mGetWebImageTask = new GetWebImageTask(pp);
        mGetWebImageTask.execute();
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
        initEvent();
    }

    private void initEvent() {
        refresh.setMaterialRefreshListener(materialRefreshListener);
    }
    private MaterialRefreshListener materialRefreshListener = new MaterialRefreshListener() {
        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {

        }
    };

    @Override
    public void showChildFragment(Bundle bundle) {
        mPreFragment.showChildFragment(bundle);
    }

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
//            Glide.with(context).load(BASE_SERVER_URL_NEWS+bean.getAttname()).dontAnimate().fitCenter().crossFade().into(imageView);
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