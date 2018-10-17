
package org.sxchinacourt.activity.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.bean.TaskBean;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.bean.ViewComponents;
import org.sxchinacourt.util.WebServiceUtil;
import org.sxchinacourt.widget.CustomProgress;

/**
 *
 * @author baggio
 * @date 2017/2/10
 */

public class NoticeDetailFragment extends BaseFragment {
    public static final String PARAM_TASK = "task";
    private ViewComponents mViewComponents;
    private CustomProgress mCustomProgress;
    /**
     * 内容区域
     */
    private LinearLayout mContentView;

    private TaskBean mTask;
    private GetNoticeInfoTask mGetNoticeInfoTask;


    private Handler mUpdateUIHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "查看公告";
        mShowBtnBack = View.VISIBLE;
        mShowSearchBar = View.GONE;
        if (getArguments() != null && getArguments().containsKey(PARAM_TASK)) {
            mTask = (TaskBean) getArguments().getSerializable(PARAM_TASK);
        }
        initHandler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, R.layout.fragment_noticedetail, container,
                savedInstanceState);
    }

    @Nullable
    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.initFragment(container, savedInstanceState);
        mCustomProgress = (CustomProgress) mRootView.findViewById(R.id.loading);
        mContentView = (LinearLayout) mRootView.findViewById(R.id.content);
    }

    @Override
    protected void initData() {
        super.initData();
        mGetNoticeInfoTask = new GetNoticeInfoTask();
        mGetNoticeInfoTask.execute();
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        mUpdateUIHandler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                mViewComponents = (ViewComponents) msg.obj;
                mViewComponents.loadViewComponents(mContentView, mCustomProgress);
            }
        };
    }

    /**
     * 获取task详情
     */
    private class GetNoticeInfoTask extends AsyncTask<Void, Void, ViewComponents> {
        @Override
        protected ViewComponents doInBackground(Void... params) {
            UserNewBean user = CApplication.getInstance().getCurrentUser();
            ViewComponents components = WebServiceUtil.getInstance().getBOData(null,
                    -1,
                    mTask
                            .getProcessInstanceId()
                    , mTask
                            .getProcessGroup(),false);
            return components;
        }

        @Override
        protected void onPreExecute() {
            mCustomProgress.start();
        }

        @Override
        protected void onPostExecute(ViewComponents components) {
            try {
                if (components != null) {
                    Message msg = mUpdateUIHandler.obtainMessage();
                    msg.obj = components;
                    mUpdateUIHandler.sendMessage(msg);
                }
            } finally {
                mGetNoticeInfoTask = null;
                mCustomProgress.stop();
            }
        }

        @Override
        protected void onCancelled() {
            mGetNoticeInfoTask = null;
            mCustomProgress.stop();
        }
    }
}
