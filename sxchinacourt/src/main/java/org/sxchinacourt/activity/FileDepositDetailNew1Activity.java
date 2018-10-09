package org.sxchinacourt.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.adapter.SwipeMenuAdapter;
import org.sxchinacourt.bean.DepositDataBean;
import org.sxchinacourt.bean.DepositRootBean;
import org.sxchinacourt.bean.UserBean;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.util.NetworkUtils;
import org.sxchinacourt.util.SoapClient;
import org.sxchinacourt.util.SoapParams;
import org.sxchinacourt.util.TLog;
import org.sxchinacourt.util.WebServiceUtil;
import org.sxchinacourt.widget.SampleHeader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by 殇冰无恨 on 2017/11/4.
 */

public class FileDepositDetailNew1Activity extends Activity{
    private static final String TAG = "lzx";

    /**服务器端一共多少条数据*/
    private static final int TOTAL_COUNTER = 11;//如果服务器没有返回总数据或者总页数，这里设置为最大值比如10000，什么时候没有数据了根据接口返回判断

    /**每一页展示多少条数据*/
    private static final int REQUEST_COUNT = 10;

    /**已经获取到多少条数据了*/
    private static int mCurrentCounter = 0;
    private boolean isRefresh = false;

    private TextView tvNodata;
    private LRecyclerView mRecyclerView = null;

    private SwipeMenuAdapter mDataAdapter = null;

    private PreviewHandler mHandler = new PreviewHandler(this);
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    ArrayList<DepositDataBean> dataList = new ArrayList<>();
    ArrayList<DepositDataBean> newList = new ArrayList<>();
    ArrayList<DepositDataBean> waitassignList = new ArrayList<>();
    private Handler handler = null;
    private Handler handlerNext = null;

    private Button btnBack;

    public int index = 1;
    public static final int SHOW_RESPONSE_FILEREVERSEDETSIL = 0;
    public static final int SHOW_RESPONSE_FILEREVERSEDETSIL_NEXT = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.sample_ll_activity_deposit);
        tvNodata = (TextView)findViewById(R.id.tv_nodata);
        btnBack = (Button) findViewById(R.id.btn_file_deposit_detail_back);
        mRecyclerView = (LRecyclerView) findViewById(R.id.list);
        dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DepositDataBean depositDataBean = new DepositDataBean();
            depositDataBean.setInitiatorName("");
            depositDataBean.setAddress("");
            dataList.add(depositDataBean);
        }
        setAdapter();
    }

    private void setAdapter() {
        mDataAdapter = new SwipeMenuAdapter(getApplicationContext());
        mDataAdapter.setDataList(dataList);
        mDataAdapter.setOnDelListener(new SwipeMenuAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                Toast.makeText(FileDepositDetailNew1Activity.this, "删除:" + pos, Toast.LENGTH_SHORT).show();

                //RecyclerView关于notifyItemRemoved的那点小事 参考：http://blog.csdn.net/jdsjlzx/article/details/52131528
                mDataAdapter.getDataList().remove(pos);
                mDataAdapter.notifyItemRemoved(pos);//推荐用这个

                if(pos != (mDataAdapter.getDataList().size())){ // 如果移除的是最后一个，忽略 注意：这里的mDataAdapter.getDataList()不需要-1，因为上面已经-1了
                    mDataAdapter.notifyItemRangeChanged(pos, mDataAdapter.getDataList().size() - pos);
                }
                //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
            }

            @Override
            public void onTop(int pos) {//置顶功能有bug，后续解决
                TLog.error("onTop pos = " + pos);
                DepositDataBean itemModel = mDataAdapter.getDataList().get(pos);

                mDataAdapter.getDataList().remove(pos);
                mDataAdapter.notifyItemRemoved(pos);
                mDataAdapter.getDataList().add(0, itemModel);
                mDataAdapter.notifyItemInserted(0);


                if(pos != (mDataAdapter.getDataList().size())){ // 如果移除的是最后一个，忽略
                    mDataAdapter.notifyItemRangeChanged(0, mDataAdapter.getDataList().size() - 1,"jdsjlzx");
                }

                mRecyclerView.scrollToPosition(0);

            }
        });
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);

        final View header = LayoutInflater.from(this).inflate(R.layout.sample_header,(ViewGroup)findViewById(android.R.id.content), false);
        mLRecyclerViewAdapter.addHeaderView(new SampleHeader(this));
        mRecyclerView.setPullRefreshEnabled(true);//设置不能下拉刷新
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {//下拉刷新的操作
            @Override
            public void onRefresh() {
                mDataAdapter.clear();
                tvNodata.setVisibility(View.GONE);
                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                mCurrentCounter = 0;
                isRefresh = true;
                requestData();
            }
        });
        //是否禁用自动加载更多功能,false为禁用, 默认开启自动加载更多功能
        mRecyclerView.setLoadMoreEnabled(true);
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {//自动加载操作
            @Override
            public void onLoadMore() {

                if (newList.size() !=10) {//每页的数据设置的为10条,如果不是10条说明是最后一页
                    //the end
                    mRecyclerView.setNoMore(true);

                } else {
                    // loading more
                    requestData();

                }
            }
        });

        mRecyclerView.setLScrollListener(new LRecyclerView.LScrollListener() {

            @Override
            public void onScrollUp() {
            }

            @Override
            public void onScrollDown() {
            }


            @Override
            public void onScrolled(int distanceX, int distanceY) {
            }

            @Override
            public void onScrollStateChanged(int state) {

            }

        });

        //设置头部加载颜色
        mRecyclerView.setHeaderViewColor(R.color.colorAccent, R.color.dark ,android.R.color.white);
        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.colorAccent, R.color.dark ,android.R.color.white);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中","已经全部为你呈现了","网络不给力啊，点击再试一次吧");
        mRecyclerView.refresh();


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<DepositDataBean> list) {

        mDataAdapter.addAll(list);
        mCurrentCounter += list.size();

    }

    private class PreviewHandler extends Handler {

        private WeakReference<FileDepositDetailNew1Activity> ref;

        PreviewHandler(FileDepositDetailNew1Activity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final FileDepositDetailNew1Activity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {

                case -1:
                    if(activity.isRefresh){
                        activity.mDataAdapter.clear();
                        mCurrentCounter = 0;
                        index = 1;
                        startThread(index);

                    }else {
                        int currentSize = activity.mDataAdapter.getItemCount();
                        index = index + 1;
                        startThread(index);
                    }
                    //组装10个数据
                    handlerNext = new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            switch (msg.what){
                                case SHOW_RESPONSE_FILEREVERSEDETSIL_NEXT:
                                    newList = (ArrayList)msg.obj;
                                    if (newList.size()!=0){
                                        tvNodata.setVisibility(View.GONE);
                                        Log.e("newList",""+newList.size());
                                        activity.addItems(newList);
                                        activity.mRecyclerView.refreshComplete(REQUEST_COUNT);
                                        isRefresh = false;
                                    }else {
                                        tvNodata.setVisibility(View.VISIBLE);
                                        Log.e("newList",""+newList.size());
                                        activity.addItems(newList);
                                        activity.mRecyclerView.refreshComplete(REQUEST_COUNT);
                                        isRefresh = false;
                                    }

                                    break;

                            }
                        }
                    };


                    break;
                case -3:
                    activity.mRecyclerView.refreshComplete(REQUEST_COUNT);
                    activity.notifyDataSetChanged();
                    activity.mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                        @Override
                        public void reload() {
                            requestData();
                        }
                    });

                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 请求网络
     */
    private void requestData() {
        Log.d(TAG, "requestData");
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //模拟一下网络请求失败的情况
                if(NetworkUtils.isNetAvailable(getApplicationContext())) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_refresh) {
            mRecyclerView.forceToRefresh();
        }
        return true;
    }
    private void startThread(final int i) {
        UserNewBean user = CApplication.getInstance().getCurrentUser();
        String str = String.valueOf ( user.getOaid() );
//        Log.e("user",""+user.getCourtoaid()+""+user.getSessionId()+""+user.getUserNo()+""+user.getId()+user.getUserId());
        final SoapParams soapParamsDeposit = new SoapParams().put("arg0",String.valueOf ( user.getOaid() )).put("arg1",i);//1414 是假数据，应该改成

        new Thread(new Runnable() {
            @Override
            public void run() {
                WebServiceUtil.getInstance().GetEmployeeDeposit(soapParamsDeposit, new SoapClient.ISoapUtilCallback() {
                    @Override
                    public void onSuccess(SoapSerializationEnvelope envelope) throws Exception {
                        String response = envelope.getResponse().toString();
                        Log.e("response",response+"");
                        DepositRootBean resps = JSON.parseObject(response,DepositRootBean.class);
                        //对数据进行处理
                        for (int i = 0;i<resps.getData().size();i++){
                            if (resps.getData().get(i).getFileState() == 1){
                                waitassignList.add(resps.getData().get(i));
                            }
                        }



                        if (i ==1){

                            Message message = new Message();
                            message.what = SHOW_RESPONSE_FILEREVERSEDETSIL_NEXT;
//                            message.obj = resps.getData();
                            message.obj = waitassignList;
                            handlerNext.sendMessage(message);
                        }else {

                            Message message = new Message();
                            message.what = SHOW_RESPONSE_FILEREVERSEDETSIL_NEXT;
//                            message.obj = resps.getData();
                            message.obj = waitassignList;
                            handlerNext.sendMessage(message);
                        }
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

