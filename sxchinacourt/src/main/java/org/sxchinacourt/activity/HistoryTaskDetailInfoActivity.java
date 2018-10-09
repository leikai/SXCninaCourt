package org.sxchinacourt.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.bean.ProcessOpinion;
import org.sxchinacourt.bean.TaskBean;
import org.sxchinacourt.bean.UserBean;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.bean.ViewComponents;
import org.sxchinacourt.util.WebServiceUtil;
import org.sxchinacourt.widget.CustomActionBar;
import org.sxchinacourt.widget.CustomProgress;

import java.util.List;

/**
 * Created by baggio on 2017/2/20.
 */

public class HistoryTaskDetailInfoActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String PARAM_TASK = "task";  //
    public static final String PARAM_TASK_TYPE = "task_type";
    private LinearLayout mContentView;//内容区域
    private CustomActionBar mActionBarView;
    private CustomProgress mCustomProgress;  //自定义等待框
    private GetTaskInfoTask mGetTaskInfoTask;
    private TaskBean mTask;
    private int mTaskType;
    private ViewComponents mViewComponents;
    private LinearLayout mProcessOpinionContainer;
    private TextView tvCheckMore;//点击拉开审批记录


    private Handler mUpdateUIHandler;
    private List<ProcessOpinion> mProcessOpinions;

    private UserBean user;
    private Handler handler = null;
    public static final int SHOW_RESPONSE_HISTORY_TASK_USER_INFO = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historytask_detailinfo);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        mActionBarView = new CustomActionBar(this);
        actionBar.setCustomView(mActionBarView);
        mActionBarView.getBackBtnView().setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(PARAM_TASK)) {
                mTask = (TaskBean) bundle.getSerializable(PARAM_TASK);
                String completeTitle = mTask.getTitle();
                String spStr[] = completeTitle.split("\\)");


                mActionBarView.setTitle(spStr[1]);
                mActionBarView.setLogoViewVisible(View.GONE);
            }
            mTaskType = bundle.getInt(PARAM_TASK_TYPE, 0);
        }
        mCustomProgress = (CustomProgress) findViewById(R.id.loading);
        mContentView = (LinearLayout) findViewById(R.id.content);
        mGetTaskInfoTask = new GetTaskInfoTask();
        mGetTaskInfoTask.execute();
        initHandler();
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

    private void initHandler() {
        mUpdateUIHandler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                mViewComponents = (ViewComponents) msg.obj;
                mViewComponents.loadViewComponents(mContentView, mCustomProgress);
                addProcessOpinionsViews();
            }
        };
    }

    private void addProcessOpinionsViews() {
        if (mProcessOpinions == null || mProcessOpinions.size() == 0) {
            return;
        }
        View view = LayoutInflater.from(this).inflate(R.layout
                .task_detailinfo_processopinions, null);
        mProcessOpinionContainer = (LinearLayout) view.findViewById(R.id
                .processopinion_container);
        tvCheckMore = (TextView)view.findViewById(R.id.tv_check_more);

        tvCheckMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("点击查看详情".equals(tvCheckMore.getText())){
                    mProcessOpinionContainer.setVisibility(View.VISIBLE);
                    int index = 1;
                    for (ProcessOpinion opinion : mProcessOpinions) {
                        View childView = LayoutInflater.from(HistoryTaskDetailInfoActivity.this).inflate
                                (R.layout
                                        .task_detailinfo_processopinion_item, null);
                        if (!TextUtils.isEmpty(opinion.getStepName())) {
                            TextView stepNameView = (TextView) childView.findViewById(R.id.tv_stepname);
                            stepNameView.setText(opinion.getStepName());
                        }
                        if (!TextUtils.isEmpty(opinion.getCreateUser())) {
                            TextView createUserView = (TextView) childView.findViewById(R.id
                                    .tv_createuser);
                            createUserView.setText(opinion.getCreateUser());
                        }
                        if (!TextUtils.isEmpty(opinion.getCreateDate())) {
                            TextView createDateView = (TextView) childView.findViewById(R.id
                                    .tv_createdate);
                            createDateView.setText(opinion.getCreateDate());
                        }
                        if (!TextUtils.isEmpty(opinion.getMenuName())) {
                            TextView menuNameView = (TextView) childView.findViewById(R.id.tv_menuname);
                            menuNameView.setText(opinion.getMenuName());
                        }
                        if (!TextUtils.isEmpty(opinion.getOpinion())) {
                            TextView opinionView = (TextView) childView.findViewById(R.id.tv_opinion);
                            opinionView.setText(opinion.getOpinion());
                        }
                        mProcessOpinionContainer.addView(childView);

                    }
                    tvCheckMore.setText("关闭");
                }else if ("关闭".equals(tvCheckMore.getText())){
                    mProcessOpinionContainer.setVisibility(View.GONE);
                    tvCheckMore.setText("点击查看详情");
                }

            }
        });
        mContentView.addView(view);
    }

    /**
     * 获取task详情
     */
    private class GetTaskInfoTask extends AsyncTask<Void, Void, ViewComponents> {
        @Override
        protected ViewComponents doInBackground(Void... params) {
            UserNewBean user = CApplication.getInstance().getCurrentUser();
            ViewComponents components = WebServiceUtil.getInstance().getBOData(user.getUserName(),
                    mTask.getTaskId(),
                    mTask
                            .getProcessInstanceId()
                    , mTask
                            .getProcessGroup(),true);
            mProcessOpinions = WebServiceUtil.getInstance().getProcessOpinionList(mTask
                    .getProcessInstanceId());
            if (mProcessOpinions != null){
                for (int i=0;i<mProcessOpinions.size();i++){
                    ProcessOpinion opinion = mProcessOpinions.get(i);
//                    UserBean userResult = WebServiceUtil.getInstance().getUserInfo(opinion.getCreateUser());
//                    opinion.setCreateUser(userResult.getUserName());
                }
            }
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
