package org.sxchinacourt.activity.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.adapter.BaseTasksAdatper;
import org.sxchinacourt.bean.TaskBean;
import org.sxchinacourt.util.WebServiceUtil;
import org.sxchinacourt.widget.CustomProgress;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by baggio on 2017/2/16.
 */

public class BaseTasksFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2 {
    final int LIMIT = 20;
    protected int mTaskType;
    int mTasksCount = 0;
    PullToRefreshListView mListView;
    CustomProgress mCustomProgress;
    EditText mKeywordETView;
    BaseTasksAdatper mAdapter;
    RefreshTasksTask mRefreshTasksTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, R.layout.fragment_list, container, savedInstanceState);
    }

    @Nullable
    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mKeywordETView = (EditText) mRootView.findViewById(R.id.et_keyword);
        mCustomProgress = (CustomProgress) mRootView.findViewById(R.id.loading);
        mListView = (PullToRefreshListView) mRootView.findViewById(R.id.pull_refresh_list);
        mListView.setOnRefreshListener(this);
        View searchBar = mRootView.findViewById(R.id.searchbar_container);
        searchBar.setVisibility(mShowSearchBar);
    }


    @Override
    protected void initData() {
        mListView.setAdapter(mAdapter);
        mRefreshTasksTask = new RefreshTasksTask(1, PullToRefreshBase.Mode
                .PULL_FROM_START.ordinal());
        mRefreshTasksTask.execute();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        if (mRefreshTasksTask == null) {
            mRefreshTasksTask = new RefreshTasksTask(1, PullToRefreshBase.Mode.PULL_FROM_START
                    .ordinal());
            mRefreshTasksTask.execute();
        }
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (mAdapter.getCount() < mTasksCount) {
            if (mRefreshTasksTask == null) {
                mRefreshTasksTask = new RefreshTasksTask(mAdapter.getCount(), PullToRefreshBase.Mode
                        .PULL_FROM_END.ordinal());
                mRefreshTasksTask.execute();
            }
        } else {
            mListView.onRefreshComplete();
        }
    }

    private Handler mUpdateTasksListHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            List<TaskBean> tasks = (List<TaskBean>) msg.obj;
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


    private class RefreshTasksTask extends AsyncTask<Void, Void, Hashtable<String, Object>> {
        int mStart;
        int mCurrentMode;

        RefreshTasksTask(int start, int mode) {
            mStart = start;
            mCurrentMode = mode;
        }

        @Override
        protected void onPreExecute() {
            mCustomProgress.start();
        }

        @Override
        protected Hashtable<String, Object> doInBackground(Void... params) {
            Hashtable<String, Object> hash = null;
            String userId = CApplication.getInstance().getCurrentUser().getUserName();
            if (BaseTasksFragment.this instanceof HistoryTaskListFragment) {
                hash = WebServiceUtil.getInstance().getHistoryTaskList(userId,
                        mTaskType, null,
                        null, mStart, LIMIT);
            } else {
                hash = WebServiceUtil.getInstance().getTaskList(userId,
                        mTaskType, null,
                        null, mStart, LIMIT);
            }
            return hash;
        }

        @Override
        protected void onPostExecute(Hashtable<String, Object> hash) {
            try {
                if (hash.containsKey(WebServiceUtil.RESULT_PARAM_TASKSCOUNT)) {
                    mTasksCount = ((Integer) hash.get(WebServiceUtil.RESULT_PARAM_TASKSCOUNT))
                            .intValue();
                }
                if (hash.containsKey(WebServiceUtil.RESULT_PARAM_TASKS)) {
                    List<TaskBean> tasks = (List<TaskBean>) hash.get(WebServiceUtil.RESULT_PARAM_TASKS);
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
            mCustomProgress.stop();
        }
    }
}
