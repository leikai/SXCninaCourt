package org.sxchinacourt.activity;

import android.annotation.SuppressLint;
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
import com.geek.thread.GeekThreadManager;
import com.geek.thread.ThreadPriority;
import com.geek.thread.ThreadType;
import com.geek.thread.task.GeekRunnable;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.adapter.SwipeMenuReverseAdapter;
import org.sxchinacourt.bean.FileReverseDetailBean;
import org.sxchinacourt.bean.FileReverseDetailDataBean;
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
 *
 * @author 殇冰无恨
 * @date 2017/11/9
 */

public class FileReverseDetailNew1Activity extends Activity{
    private static final String TAG = "lzx";

    /**
     * 如果服务器没有返回总数据或者总页数，这里设置为最大值比如10000，什么时候没有数据了根据接口返回判断
     * 服务器端一共多少条数据
     */
    private static final int TOTAL_COUNTER = 11;

    /**
     * 每一页展示多少条数据
     */
    private static final int REQUEST_COUNT = 10;

    /**
     * 已经获取到多少条数据了
     */
    private static int mCurrentCounter = 0;
    private boolean isRefresh = false;

    private LRecyclerView mRecyclerView = null;

    private SwipeMenuReverseAdapter mDataAdapter = null;

    private PreviewHandler mHandler = new PreviewHandler(this);
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    ArrayList<FileReverseDetailDataBean> dataList = new ArrayList<>();
    ArrayList<FileReverseDetailDataBean> newList = new ArrayList<>();
    ArrayList<FileReverseDetailDataBean> waitpickupList = new ArrayList<>();
    private Handler handler = null;
    private Handler handlerNext = null;

    private TextView tvNodata;
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
        setContentView(R.layout.sample_ll_activity_reverse);
        tvNodata = (TextView) findViewById(R.id.tv_nodata);
        btnBack = (Button) findViewById(R.id.btn_file_reverse_detail_back);
        mRecyclerView = (LRecyclerView) findViewById(R.id.list);
        dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            FileReverseDetailDataBean fileReverseDetailDataBean = new FileReverseDetailDataBean();
            fileReverseDetailDataBean.setRecipientName("");
            fileReverseDetailDataBean.setInitiatorName("");
            fileReverseDetailDataBean.setAddress("");
            dataList.add(fileReverseDetailDataBean);
        }
        setAdapter();
    }

    private void setAdapter() {
        mDataAdapter = new SwipeMenuReverseAdapter(getApplicationContext());
        mDataAdapter.setDataList(dataList);
        mDataAdapter.setOnDelListener(new SwipeMenuReverseAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                Toast.makeText(FileReverseDetailNew1Activity.this, "删除:" + pos, Toast.LENGTH_SHORT).show();

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
                FileReverseDetailDataBean itemModel = mDataAdapter.getDataList().get(pos);

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
        //设置下拉刷新
        mRecyclerView.setPullRefreshEnabled(true);
        //下拉刷新的操作
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
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
        //自动加载操作
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //每页的数据设置的为10条,如果不是10条说明是最后一页
                if (newList.size() !=10) {
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

    private void addItems(ArrayList<FileReverseDetailDataBean> list) {
        mDataAdapter.addAll(list);
        mCurrentCounter += list.size();
    }

    private class PreviewHandler extends Handler {

        private WeakReference<FileReverseDetailNew1Activity> ref;

        PreviewHandler(FileReverseDetailNew1Activity activity) {
            ref = new WeakReference<>(activity);
        }

        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            final FileReverseDetailNew1Activity activity = ref.get();
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
                                    if (newList.size() !=0 ){
                                        tvNodata.setVisibility(View.GONE);
                                    }else {
                                        tvNodata.setVisibility(View.VISIBLE);
                                    }
                                    Log.e("newList",""+newList.size());
                                    activity.addItems(newList);
                                    activity.mRecyclerView.refreshComplete(REQUEST_COUNT);
                                    isRefresh = false;
                                    break;
                                    default:
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
        GeekThreadManager.getInstance().execute(new GeekRunnable(ThreadPriority.NORMAL) {
            @Override
            public void run() {
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
        },ThreadType.NORMAL_THREAD);
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
    private void startThread(int i) {
        UserNewBean user = CApplication.getInstance().getCurrentUser();
        final SoapParams soapParams = new SoapParams().put("SerialNo","17091215332720");
        final SoapParams soapParamsEmployeePickUp = new SoapParams().put("arg0", String.valueOf ( user.getOaid() )).put("arg1",i);

        GeekThreadManager.getInstance().execute(new GeekRunnable(ThreadPriority.NORMAL) {
            @Override
            public void run() {
                WebServiceUtil.getInstance().GetEmployeePickup(soapParamsEmployeePickUp, new SoapClient.ISoapUtilCallback() {
                    @Override
                    public void onSuccess(SoapSerializationEnvelope envelope) throws Exception {
                        String response = envelope.getResponse().toString();
                        FileReverseDetailBean resps = JSON.parseObject(response,FileReverseDetailBean.class);
                        //对数据进行处理,将待指派的数据过滤出去
                        for (int i = 0;i<resps.getData().size();i++){
                            if (resps.getData().get(i).getFileState() == 2){
                                waitpickupList.add(resps.getData().get(i));
                            }
                        }
                        Message message = new Message();
                        message.what = SHOW_RESPONSE_FILEREVERSEDETSIL_NEXT;
                        message.obj = waitpickupList;
                        handlerNext.sendMessage(message);
                    }
                    @Override
                    public void onFailure(Exception e) {
                        Log.e("e",""+e);
                    }
                });
            }
        },ThreadType.NORMAL_THREAD);
    }

}
