package org.sxchinacourt.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONException;
import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.adapter.TasksICreatedAdatper;
import org.sxchinacourt.bean.TaskICreatedBean;
import org.sxchinacourt.bean.UserBean;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.util.WebServiceUtil;
import org.sxchinacourt.widget.CustomActionBar;
import org.sxchinacourt.widget.CustomProgress;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by baggio on 2017/2/27.
 */

public class TasksICreatedActivity extends AppCompatActivity implements PullToRefreshBase
        .OnRefreshListener2, AdapterView.OnItemClickListener, View.OnClickListener {
    final int LIMIT = 20;
    private CustomActionBar mActionBarView;
    private CustomProgress mCustomProgress;
    PullToRefreshListView mListView;
    private RefreshTasksICreatedTask mRefreshTasksTask;
    private int mTasksCount;
    private TasksICreatedAdatper mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasksicreated);
        initView();
        initData();
    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        mActionBarView = new CustomActionBar(this);
        actionBar.setCustomView(mActionBarView);
        mActionBarView.setTitle("我布置的任务");
        mCustomProgress = (CustomProgress) findViewById(R.id.loading);
        mActionBarView.setBackBtnViewVisible(View.VISIBLE);
        mActionBarView.setLeftTextViewText("返回");
        mActionBarView.getBackBtnView().setOnClickListener(this);
        findViewById(R.id.groups_container).setVisibility(View.GONE);
        mListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        mListView.setOnRefreshListener(this);
        mListView.setOnItemClickListener(this);
    }

    private void initData() {
        mAdapter = new TasksICreatedAdatper(this);
        mListView.setAdapter(mAdapter);
        mRefreshTasksTask = new RefreshTasksICreatedTask(1, PullToRefreshBase.Mode
                .PULL_FROM_START.ordinal());
        mRefreshTasksTask.execute();
    }


    private Handler mUpdateTasksListHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            List<TaskICreatedBean> tasks = (List<TaskICreatedBean>) msg.obj;
            if (mListView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
                mAdapter.setTasks(tasks);
            } else {
                mAdapter.addTasks(tasks);
            }
            mListView.onRefreshComplete();
            if (mAdapter.getCount() == mTasksCount) {
                mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        if (mRefreshTasksTask == null) {
            mRefreshTasksTask = new RefreshTasksICreatedTask(1, PullToRefreshBase.Mode.PULL_FROM_START
                    .ordinal());
            mRefreshTasksTask.execute();
        }
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (mAdapter.getCount() < mTasksCount) {
            if (mRefreshTasksTask == null) {
                mRefreshTasksTask = new RefreshTasksICreatedTask(mAdapter.getCount(),
                        PullToRefreshBase.Mode
                                .PULL_FROM_END.ordinal());
                mRefreshTasksTask.execute();
            }
        } else {
            mListView.onRefreshComplete();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TaskICreatedBean task = (TaskICreatedBean) mAdapter.getItem(position - mListView.getRefreshableView()
                .getHeaderViewsCount());
        Intent intent = new Intent();
        intent.setClass(this, TaskICreatedDetailInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TaskICreatedDetailInfoActivity.PARAM_TASK, task);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_view: {
                finish();
                break;
            }
        }
    }

    private class RefreshTasksICreatedTask extends AsyncTask<Void, Void, Hashtable<String, Object>> {
        int mStart;
        int mCurrentMode;

        public RefreshTasksICreatedTask(int start, int mode) {
            mStart = start;
            mCurrentMode = mode;
        }

        @Override
        protected void onPreExecute() {
            mCustomProgress.start();
        }

        @Override
        protected Hashtable<String, Object> doInBackground(Void... params) {
            UserNewBean user = CApplication.getInstance().getCurrentUser();
            Hashtable<String, Object> hash = null;
            try {
                hash = WebServiceUtil.getInstance().getOaWorkMngs(user.getOrgid(), Integer.getInteger(user.getOaid()), "工作管理", mStart, LIMIT);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return hash;
        }

        @Override
        protected void onPostExecute(Hashtable<String, Object> hash) {
            try {
                if (hash.containsKey(WebServiceUtil.RESULT_PARAM_DATASCOUNT)) {
                    mTasksCount = ((Integer) hash.get(WebServiceUtil.RESULT_PARAM_DATASCOUNT))
                            .intValue();
                }
                if (hash.containsKey(WebServiceUtil.RESULT_PARAM_DATAS)) {
                    List<TaskICreatedBean> tasks = (List<TaskICreatedBean>) hash.get(WebServiceUtil
                            .RESULT_PARAM_DATAS);
                    Message msg = mUpdateTasksListHandler.obtainMessage();
                    msg.obj = tasks;
                    msg.arg1 = mCurrentMode;
                    mUpdateTasksListHandler.sendMessage(msg);
                }
            } finally {
                mRefreshTasksTask = null;
                mCustomProgress.stop();
            }
        }

        @Override
        protected void onCancelled() {
            mRefreshTasksTask = null;
        }
    }
}
