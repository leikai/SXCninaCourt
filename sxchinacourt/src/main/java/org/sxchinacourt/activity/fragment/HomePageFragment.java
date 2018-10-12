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
 * Created by 殇冰无恨 on 2017/12/9.
 */

public class HomePageFragment extends BaseFragment {
    private ConvenientBanner convenientBanner;
    private ArrayList<Integer> localImages = new ArrayList<Integer>();
    private RecyclerView rvHomepageWeidoorRecycleView;
    private LinearLayout cabinet;
    private LinearLayout machine;
    private LinearLayout leave;
    private LinearLayout leave_informal;
    private LinearLayout llWeiDoor;
    //常见用用标题
    private LinearLayout llCommonApps;
    //常见应用内容
    private LinearLayout llCommonAppsContent;
    private static String BASE_SERVER_URL_NEWS = "http://111.53.181.200:6688/mserver/upfile/webimg/";



    GridView mAppsGridView;
    HomePageAdapter mAdapter;
    int[] mResourceIdArray = {R.drawable.icon_dbsy, R.drawable.icon_ybsy, R.drawable
            .icon_wdlc};//, R.drawable.app_email_icon
    String[] mAppNameArray = {"待办事宜", "已办事宜", "通知公告"};//, "收发邮件"
    int[] mFragmentIndexArray = {AppBean.FRAGMENT_TO_DO_TASK, AppBean.FRAGMENT_HISTORY_TASK, AppBean
            .FRAGMENT_NOTICE_TASK, AppBean.FRAGMENT_MANAGER_TASK};//, AppBean.FRAGMENT_EMAIL_TASK

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


//    //为了方便改写，来实现复杂布局的切换
//    private class LocalImageHolderView implements Holder<Integer> {
//        private ImageView imageView;
//
//        @Override
//        public View createView(Context context) {
//            //你可以通过layout文件来创建，不一定是Image，任何控件都可以进行翻页
//            imageView = new ImageView(context);
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            return imageView;
//        }
//
//        @Override
//        public void UpdateUI(Context context, int position, Integer data) {
//            imageView.setImageResource(data);
//        }
//    }



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

//        for (int i=0;i<1;i++){
//            Fruit apple = new Fruit("晋中中院院长史红波带队在最高院考察学习信息化建设工作", R.drawable.weidoor_1);
//            fruitList.add(apple);
//            Fruit banana = new Fruit("晋中市龙泉生态牧林庄园和魏某某等34名被告人集资诈骗、非法吸收公众存款、掩饰隐瞒犯罪所得一案一审宣判", R.drawable.weidoor_2);
//            fruitList.add(banana);
//            Fruit orange = new Fruit("榆次区人民法院开发区人民法庭正式挂牌运行", R.drawable.weidoor_3);
//            fruitList.add(orange);
//            Fruit watermelon = new Fruit("新年伊始，晋中市中级人民法院迎来了“开门红”", R.drawable.weidoor_4);
//            fruitList.add(watermelon);
//            Fruit pear = new Fruit("晋中全市法院党建工作会议在榆社召开", R.drawable.weidoor_5);
//            fruitList.add(pear);
//
//        }

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
