package org.sxchinacourt.activity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.sxchinacourt.R;
import org.sxchinacourt.bean.Component;
import org.sxchinacourt.bean.SelectViewComponent;
import org.sxchinacourt.bean.SubViewComponent;
import org.sxchinacourt.bean.TaskAssignedToMeBean;
import org.sxchinacourt.bean.ViewComponents;
import org.sxchinacourt.util.SysUtil;
import org.sxchinacourt.util.WebServiceUtil;
import org.sxchinacourt.widget.CustomActionBar;
import org.sxchinacourt.widget.CustomProgress;

import java.util.List;

/**
 *
 * @author baggio
 * @date 2017/2/28
 */

public class TaskAssignedToMeDetailInfoActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String PARAM_TASK = "task";
    /**
     * 内容区域
     */
    private LinearLayout mContentView;
    private CustomActionBar mActionBarView;
    private CustomProgress mCustomProgress;
    private GetTaskInfoTask mGetTaskInfoTask;
    private CreateTheOaWorkMngInfosTask mCreateOAWorkMngInfosTask;
    private TaskAssignedToMeBean mTask;
    private ViewComponents mViewComponents;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskassignedtome_detailinfo);
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
                mTask = (TaskAssignedToMeBean) bundle.getSerializable(PARAM_TASK);
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
            ViewGroup childView = (ViewGroup) LayoutInflater.from(TaskAssignedToMeDetailInfoActivity.this)
                    .inflate(R.layout.task_detailinfo_buttonview, null);
            childView.findViewById(R.id.btn_confirm).setOnClickListener
                    (TaskAssignedToMeDetailInfoActivity.this);
            mContentView.addView(childView);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_view:
                finish();
                break;
            case R.id.btn_confirm:
                if (mCreateOAWorkMngInfosTask == null) {
                    try {
                        JSONObject json = new JSONObject();
                        List<Component> components = mViewComponents.getSubViewComponents();
                        for (Component component : components) {
                            SubViewComponent subViewComponent = (SubViewComponent) component;
                            String viewName = subViewComponent.getViewName();
                            String name = subViewComponent.getName();
                            if (name.equals(SubViewComponent.INPUTTIME)) {
                                json.put(subViewComponent.getName(), SysUtil.getStringDate(System
                                        .currentTimeMillis()));
                            } else if (viewName.equals(Component.VIEW_TYPE_EDITVIEW)
                                    || viewName.equals(Component
                                    .VIEW_TYPE_DATEVIEW)) {
                                json.put(name, ((TextView) subViewComponent.getView
                                        ()).getText().toString());
                            } else if (viewName.equals(Component.VIEW_TYPE_SELECTVIEW)) {
                                json.put(name, ((SelectViewComponent) subViewComponent).getCurrentItem());
                            } else {
                                json.put(name, subViewComponent.getLabelValue());
                            }
                        }
                        mCreateOAWorkMngInfosTask = new CreateTheOaWorkMngInfosTask(json.toString
                                ());
                        mCreateOAWorkMngInfosTask.execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                break;
                default:
                    break;
        }
    }

    private class GetTaskInfoTask extends AsyncTask<Void, Void, ViewComponents> {
        @Override
        protected ViewComponents doInBackground(Void... params) {
            ViewComponents components = WebServiceUtil.getInstance().getTheOaWorkMngDetails("工作管理"
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

    private class CreateTheOaWorkMngInfosTask extends AsyncTask<Void, Void, String> {
        String mJsonStr;

        public CreateTheOaWorkMngInfosTask(String str) {
            mJsonStr = str;
        }

        @Override
        protected void onPreExecute() {
            mCustomProgress.start();
        }

        @Override
        protected String doInBackground(Void... params) {
            String OID = null;
            OID = WebServiceUtil.getInstance().createTheOaWorkMngInfos(mJsonStr);
            return OID;
        }

        @Override
        protected void onPostExecute(String OID) {
            try {
                if (!TextUtils.isEmpty(OID)) {
                    Toast.makeText(TaskAssignedToMeDetailInfoActivity.this, "回复任务成功", Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(TaskAssignedToMeDetailInfoActivity.this, "回复任务失败", Toast
                            .LENGTH_LONG).show();
                }
            } finally {
                mCreateOAWorkMngInfosTask = null;
                mCustomProgress.stop();
            }
        }

        @Override
        protected void onCancelled() {
            mCreateOAWorkMngInfosTask = null;
        }
    }
}
