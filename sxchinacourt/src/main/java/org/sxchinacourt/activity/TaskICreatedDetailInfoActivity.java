package org.sxchinacourt.activity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import org.sxchinacourt.R;
import org.sxchinacourt.bean.TaskICreatedBean;
import org.sxchinacourt.bean.ViewComponents;
import org.sxchinacourt.util.WebServiceUtil;
import org.sxchinacourt.widget.CustomActionBar;
import org.sxchinacourt.widget.CustomProgress;

/**
 *
 * @author baggio
 * @date 2017/2/28
 */

public class TaskICreatedDetailInfoActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String PARAM_TASK = "task";
    /**
     * 内容区域
     */
    private LinearLayout mContentView;
    private CustomActionBar mActionBarView;
    private CustomProgress mCustomProgress;
    private GetTaskInfoTask mGetTaskInfoTask;
    private TaskICreatedBean mTask;
    private ViewComponents mViewComponents;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskicreated_detailinfo);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        mActionBarView = new CustomActionBar(this);
        actionBar.setCustomView(mActionBarView);
        mActionBarView.setTitle("任务详情");
        mActionBarView.getBackBtnView().setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(PARAM_TASK)) {
                mTask = (TaskICreatedBean) bundle.getSerializable(PARAM_TASK);
            }
        }
        mCustomProgress = (CustomProgress) findViewById(R.id.loading);
        mContentView = (LinearLayout) findViewById(R.id.content);
        mGetTaskInfoTask = new GetTaskInfoTask();
        mGetTaskInfoTask.execute();
    }

    @SuppressLint("HandlerLeak")
    private Handler mUpdateUIHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            mViewComponents = (ViewComponents) msg.obj;
            mViewComponents.loadViewComponents(mContentView, mCustomProgress);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_view: {
                finish();
                break;
            }
            default:
                break;
        }
    }

    private class GetTaskInfoTask extends AsyncTask<Void, Void, ViewComponents> {
        @Override
        protected ViewComponents doInBackground(Void... params) {
            ViewComponents components = WebServiceUtil.getInstance().getTheOaWorkMng("工作管理"
                    , mTask.getOId());
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
                mGetTaskInfoTask = null;
                mCustomProgress.stop();
            }
        }

        @Override
        protected void onCancelled() {
            mGetTaskInfoTask = null;
            mCustomProgress.stop();
        }
    }
}
