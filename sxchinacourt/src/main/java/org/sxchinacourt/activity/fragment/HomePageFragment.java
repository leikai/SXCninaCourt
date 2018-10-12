package org.sxchinacourt.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.activity.CabinetActivity;
import org.sxchinacourt.activity.LoginActivity;
import org.sxchinacourt.activity.MachineActivity;
import org.sxchinacourt.adapter.FruitAdapter;
import org.sxchinacourt.adapter.HomePageAdapter;
import org.sxchinacourt.bean.AppBean;
import org.sxchinacourt.bean.BannerBean;
import org.sxchinacourt.bean.Fruit;
import org.sxchinacourt.bean.NewsBean;
import org.sxchinacourt.bean.NewsParamsBean;
import org.sxchinacourt.bean.ParameterBean;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.common.Contstants;
import org.sxchinacourt.util.GsonUtil;
import org.sxchinacourt.util.SoapClient;
import org.sxchinacourt.util.SoapParams;
import org.sxchinacourt.util.WebServiceUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 殇冰无恨
 * @date 2017/12/9
 */

public class HomePageFragment extends BaseFragment {
    /**
     * 轮播图控件
     */
    private ConvenientBanner convenientBanner;
    /**
     * 轮播图图片位置集合
     */
    private ArrayList<Integer> localImages = new ArrayList<Integer>();
    /**
     * 微门户列表控件
     */
    private RecyclerView rvHomepageWeidoorRecycleView;
    /**
     * 云柜控件
     */
    private LinearLayout cabinet;
    /**
     * 留言机控件
     */
    private LinearLayout machine;
    /**
     * 机关工作人员请假
     */
    private LinearLayout leave;
    /**
     * 机关合同制工作人员请假
     */
    private LinearLayout leave_informal;
    private LinearLayout llWeiDoor;
    /**
     * 常见用用标题
     */
    private LinearLayout llCommonApps;
    /**
     * 常见应用内容
     */
    private LinearLayout llCommonAppsContent;
    /**
     * 晋中中院轮播图地址
     */
    private static String BASE_SERVER_URL_NEWS = "http://111.53.181.200:6688/mserver/upfile/webimg/";


    /**
     *功能控件
     */
    GridView mAppsGridView;
    HomePageAdapter mAdapter;
    /**
     * 待办、已办、通知公告、任务管理图片数组
     */
    int[] mResourceIdArray = {R.drawable.icon_dbsy, R.drawable.icon_ybsy, R.drawable
            .icon_wdlc};//, R.drawable.app_email_icon
    /**
     * 四大功能名称数组
     */
    String[] mAppNameArray = {"待办事宜", "已办事宜", "通知公告"};
    /**
     * 四大功能碎片
     */
    int[] mFragmentIndexArray = {AppBean.FRAGMENT_TO_DO_TASK, AppBean.FRAGMENT_HISTORY_TASK, AppBean
            .FRAGMENT_NOTICE_TASK, AppBean.FRAGMENT_MANAGER_TASK};
    /**
     * 微门户功能模块数据列表
     */
    private List<Fruit> fruitList = new ArrayList<>();
    private Context context;

    private UserNewBean user;
    private String rstJson;
    private List<BannerBean> bannerBeen_list;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = CApplication.getInstance().getCurrentUser();
        mTitle = user.getOrgname();
        mShowBtnBack = View.INVISIBLE;
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCreateView(inflater, R.layout.fragment_home_page, container, savedInstanceState);
    }

    @Nullable
    @Override
    public void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        llWeiDoor = (LinearLayout) mRootView.findViewById(R.id.ll_wei_door);
        rvHomepageWeidoorRecycleView = (RecyclerView) mRootView.findViewById(R.id.home_page_view_wei_door);

        convenientBanner = (ConvenientBanner) mRootView.findViewById(R.id.fragment_home_convenientBanner);
        cabinet = (LinearLayout) mRootView.findViewById(R.id.view_homepage_ll_cabinet);
        machine = (LinearLayout) mRootView.findViewById(R.id.view_homepage_ll_machine);
        leave = (LinearLayout) mRootView.findViewById(R.id.view_homepage_ll_leave);
        leave_informal = (LinearLayout) mRootView.findViewById(R.id.view_homepage_ll_tzgg);
        mAppsGridView = (GridView) mRootView.findViewById(R.id.apps_gridview);
        llCommonApps = (LinearLayout) mRootView.findViewById(R.id.ll_common_apps);
        llCommonAppsContent = (LinearLayout) mRootView.findViewById(R.id.ll_common_apps_content);



        ArrayList<AppBean> apps = new ArrayList<>();
        for (int i = 0; i < mResourceIdArray.length; i++) {
            AppBean app = new AppBean();
            app.setResourceId(mResourceIdArray[i]);
            app.setName(mAppNameArray[i]);
            app.setFragmentIndex(mFragmentIndexArray[i]);
            apps.add(app);
        }
        mAdapter = new HomePageAdapter(getContext(),this, apps);
        mAppsGridView.setAdapter(mAdapter);

        if (user.getOrgid() == null){
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }else if (user.getOrgid().equals(Contstants.OID_JINZHONG)){
            //获取本地图片
            getLocalImageJinzhong();
            llWeiDoor.setVisibility(View.VISIBLE);
            rvHomepageWeidoorRecycleView.setVisibility(View.VISIBLE);
            llCommonApps.setVisibility(View.VISIBLE);
            llCommonAppsContent.setVisibility(View.VISIBLE);

        }else if (user.getOrgid().equals(Contstants.OID_YUSHE)){
            //获取本地图片
            getLocalImageYuShe();
            llWeiDoor.setVisibility(View.GONE);
            rvHomepageWeidoorRecycleView.setVisibility(View.GONE);
            llCommonApps.setVisibility(View.VISIBLE);
            llCommonAppsContent.setVisibility(View.VISIBLE);
            cabinet.setVisibility(View.GONE);
            machine.setVisibility(View.VISIBLE);
            leave.setVisibility(View.VISIBLE);
            leave_informal.setVisibility(View.GONE);
        }else if (user.getOrgid().equals(Contstants.OID_LINGSHI)){
            //获取本地图片
            getLocalImageLingShi();
            llWeiDoor.setVisibility(View.GONE);
            rvHomepageWeidoorRecycleView.setVisibility(View.GONE);
            llCommonApps.setVisibility(View.GONE);
            llCommonAppsContent.setVisibility(View.GONE);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rvHomepageWeidoorRecycleView.setLayoutManager(layoutManager);
        initFruits();

        initEvent();

    }

    private void initEvent() {
        cabinet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jumptoCabinet = new Intent(getContext(), CabinetActivity.class);
                startActivity(jumptoCabinet);


            }
        });
        machine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MachineActivity.class));
            }
        });
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),Leave1Activity.class));
            }
        });
        leave_informal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),LeaveInformal1Activity.class));
            }
        });

    }


    private void getLocalImageYuShe() {
        //获取本地的图片
        for (int position = 10; position < 16; position++) {
            localImages.add(getResId("banner_" + position, R.mipmap.class));
        }
    }

    private void getLocalImageLingShi() {
        //获取本地的图片
        for (int position = 20; position < 23; position++) {
            localImages.add(getResId("banner_" + position, R.mipmap.class));
        }

    }




    @Override
    public void showChildFragment(Bundle bundle) {
        mPreFragment.showChildFragment(bundle);
    }
    private void initFruits() {

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

                        final List<NewsBean> resps=new ArrayList<NewsBean>(JSONArray.parseArray(response,NewsBean.class));
//                            CourtDataBean resp = JSON.parseObject(response, CourtDataBean.class);
                        Log.e("lk",""+resps.get(0).getOid());

                        Log.e("list",""+resps.get(0).getFtitle());
                        Log.e("list",""+resps.get(0).getKeyword());

//                            courts = new String[]{resp.get(0).getCourtName()};

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FruitAdapter fruitAdapter = new FruitAdapter(getContext(),resps);
                                rvHomepageWeidoorRecycleView.setAdapter(fruitAdapter);
                            }
                        });
                    }
                    @Override
                    public void onFailure(Exception e) {
//                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();

    }
    /**
     * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
     *
     * @param variableName
     * @param c
     * @return
     */
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    private void getLocalImageJinzhong() {
        ParameterBean bean = new ParameterBean();
        bean.setNums("5");
        Gson gson = new Gson();
        String pp = gson.toJson(bean);
        GetWebImageTask getWebImageTask = new GetWebImageTask(pp);
        getWebImageTask.execute();
    }

    /**
     * 任务：获取网站图片
     */
    private class GetWebImageTask extends AsyncTask<Void,Void,List> {
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
            convenientBanner.setPages(new CBViewHolderCreator<LocalImageHolderView>() {
                @Override
                public LocalImageHolderView createHolder() {
                    return new LocalImageHolderView();
                }
            }, list)
                    .setPointViewVisible(true)
                    .setPageIndicator(new int[]{R.drawable.circle_gray, R.drawable.circle_white})
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                    .startTurning(3000)
                    .setManualPageable(true);

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
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
