package org.sxchinacourt.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.bean.AssignSubTaskBean;
import org.sxchinacourt.bean.AssignTaskBean;
import org.sxchinacourt.bean.DepartmentBean;
import org.sxchinacourt.bean.UserBean;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.dataformat.DataFormat;
import org.sxchinacourt.util.WebServiceUtil;
import org.sxchinacourt.widget.CustomActionBar;
import org.sxchinacourt.widget.CustomProgress;
import org.sxchinacourt.widget.SelectContactsDialog;

import static org.sxchinacourt.util.SysUtil.getStringDate;

/**
 * Created by baggio on 2017/2/21.
 */

public class CreateTaskActivity extends AppCompatActivity implements View.OnClickListener {
    private CustomActionBar mActionBarView;
    private CustomProgress mCustomProgress;
    private SelectContactsDialog mContactsDialog;
    private AlertDialog mExecutorsDialog;
    private TextView mJoinPersonView;
    private ViewGroup mSubTaskContainer;
    private ScrollView mScrollView;

    private CreateOAWorkTask mCreateOAWorkTask;


    private SelectContactsDialog.UsersSelectedListener mUsersSelectedListener;
    private View.OnClickListener mDialogBtnConfirmClickListener;
    private DialogInterface.OnDismissListener mDialogDismissListener;

    private AssignTaskBean mAssignTaskBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        initView();
        initListener();
        initData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_view: {
                finish();
                break;
            }
            case R.id.img_selectExecutors: {
                if (mContactsDialog == null) {
                    mContactsDialog = new SelectContactsDialog(this,
                            mDialogBtnConfirmClickListener, mDialogDismissListener, mUsersSelectedListener);
                }
                mContactsDialog.show();
                break;
            }
            case R.id.img_beginDate:
            case R.id.img_endDate:
            case R.id.img_remindDate: {
                setTaskTime(v);
                break;
            }
            case R.id.btn_send: {
                handleCreateTask();
                break;
            }
        }
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
        mActionBarView.setTitle("任务管理");
        mCustomProgress = (CustomProgress) findViewById(R.id.loading);
        mJoinPersonView = (TextView) findViewById(R.id.tv_executors);
        mSubTaskContainer = (ViewGroup) findViewById(R.id.subTaskContainer);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
    }

    private void initListener() {
        mActionBarView.getBackBtnView().setOnClickListener(this);
        findViewById(R.id.img_selectExecutors).setOnClickListener(this);
        findViewById(R.id.img_beginDate).setOnClickListener(this);
        findViewById(R.id.img_endDate).setOnClickListener(this);
        findViewById(R.id.img_remindDate).setOnClickListener(this);
        findViewById(R.id.img_addSubTask).setOnClickListener(this);
        findViewById(R.id.btn_send).setOnClickListener(this);
        mUsersSelectedListener = new SelectContactsDialog.UsersSelectedListener() {
            @Override
            public void userSelected(UserBean user) {
                handleCheckedExecutor(user);
            }

            @Override
            public void usersSelected(DepartmentBean department) {
                handleCheckedAllExecutors(department);
            }
        };

        mDialogBtnConfirmClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContactsDialog.dismiss();
            }
        };

        mDialogDismissListener = new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                StringBuffer strBuf = new StringBuffer();
                for (int i = 0; i < mAssignTaskBean.getSubTaskSize(); i++) {
                    AssignSubTaskBean subTask = mAssignTaskBean.getSubTask(i);
                    strBuf.append(subTask.getWsMan());
                    if (i < mAssignTaskBean.getSubTaskSize() - 1) {
                        strBuf.append("、");
                    }
                }
                mJoinPersonView.setText(strBuf.toString());
            }
        };
    }


    private void initData() {
        mAssignTaskBean = new AssignTaskBean();
        UserNewBean user = CApplication.getInstance().getCurrentUser();
        mAssignTaskBean.setWsMan(user.getUserName());
        mAssignTaskBean.setWsManId(user.getOrgid());
        mAssignTaskBean.setWsManOAId(Integer.getInteger(user.getOaid()));
    }

    private void handleCheckedAllExecutors(DepartmentBean department) {
        department.setSelectAll(!department.isSelectAll());
        for (UserBean user : department.getUsers()) {
            if (user.isChecked() ^ department.isSelectAll()) {
                user.setChecked(department.isSelectAll());
                if (user.isChecked()) {
                    addSubTaskView(user);
                } else {
                    removeSubTaskView(user);
                }
            }
        }
    }


    private void handleCheckedExecutor(UserBean user) {
        user.setChecked(!user.isChecked());
        if (user.isChecked()) {
            addSubTaskView(user);
        } else {
            removeSubTaskView(user);
        }
    }


    private void addSubTaskView(UserBean user) {
        final AssignSubTaskBean subTask = new AssignSubTaskBean();
        mAssignTaskBean.addSubTask(subTask);
        subTask.setWsMan(user.getUserName());
        subTask.setWsManOAId(user.getId());
        subTask.setWsManId(user.getCourtoaid());
//        subTask.setWsManId();
        final View subTaskView = LayoutInflater.from(this).inflate(R.layout.subtask_item, null);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                DateTimePickerDialog dialog = new DateTimePickerDialog(CreateTaskActivity.this,
                        System.currentTimeMillis());
                dialog.setOnDateTimeSetListener(new DateTimePickerDialog.OnDateTimeSetListener() {
                    public void OnDateTimeSet(AlertDialog dialog, long date) {
                        TextView tvDate = null;
                        String dateStr = getStringDate(date);
                        switch (v.getId()) {
                            case R.id.img_subTaskBeginDate:
                                tvDate = (TextView) subTaskView.findViewById(R.id.tv_subTaskBeginDate);
                                subTask.setSTime(dateStr);
                                break;
                            case R.id.img_subTaskEndDate:
                                tvDate = (TextView) subTaskView.findViewById(R.id.tv_subTaskEndDate);
                                subTask.setETime(dateStr);
                                break;
                        }
                        tvDate.setText(getStringDate(date));
                    }
                });
                dialog.show();
            }
        };
        subTaskView.findViewById(R.id.img_subTaskBeginDate).setOnClickListener(listener);
        subTaskView.findViewById(R.id.img_subTaskEndDate).setOnClickListener(listener);
        TextView subTaskExecutor = (TextView) subTaskView.findViewById(R.id
                .tv_subTaskExecutorName);
        subTaskExecutor.setText((mSubTaskContainer.getChildCount() + 1) + "." + subTask.getWsMan());
        subTaskView.setTag(subTask);
        mSubTaskContainer.addView(subTaskView);
    }

    private void removeSubTaskView(UserBean user) {
        int index = 0;
        for (int i = 0; i < mSubTaskContainer.getChildCount(); i++) {
            View subTaskView = mSubTaskContainer.getChildAt(i);
            AssignSubTaskBean subTask = (AssignSubTaskBean) subTaskView.getTag();
            if (subTask.getWsManOAId() == user.getId()) {
                index = i;
                subTaskView.setTag(null);
                mAssignTaskBean.removeSubTask(subTask);
            }
            if (i > index) {
                TextView subTaskExecutorView = (TextView) subTaskView
                        .findViewById(R.id.tv_subTaskExecutor);
                subTaskExecutorView.setText(i + "." + subTask.getWsMan());
            }
        }
        mSubTaskContainer.removeViewAt(index);
    }


    private void setTaskTime(final View v) {
        DateTimePickerDialog dialog = new DateTimePickerDialog(this, System.currentTimeMillis());
        dialog.setOnDateTimeSetListener(new DateTimePickerDialog.OnDateTimeSetListener() {
            public void OnDateTimeSet(AlertDialog dialog, long date) {
                TextView tvDate = null;
                String dateStr = getStringDate(date);
                switch (v.getId()) {
                    case R.id.img_beginDate:
                        tvDate = (TextView) findViewById(R.id.tv_beginDate);
                        mAssignTaskBean.setSTime(dateStr);
                        break;
                    case R.id.img_endDate:
                        tvDate = (TextView) findViewById(R.id.tv_endDate);
                        mAssignTaskBean.setETime(dateStr);
                        break;
                    case R.id.img_remindDate:
                        tvDate = (TextView) findViewById(R.id.tv_remindDate);
                        mAssignTaskBean.setAlertTime(dateStr);
                        break;
                }
                tvDate.setText(dateStr);
            }
        });
        dialog.show();
    }

    private void handleCreateTask() {
        if (mCreateOAWorkTask != null) {
            return;
        }
        EditText editText = (EditText) findViewById(R.id.et_taskTitle);
        mAssignTaskBean.setWTitle(editText.getText().toString());
        editText = (EditText) findViewById(R.id.et_taskContent);
        mAssignTaskBean.setWContent(editText.getText().toString());
        TextView executorsView = (TextView) findViewById(R.id.tv_executors);
        mAssignTaskBean.setNote(executorsView.getText().toString());
        for (int i = 0; i < mSubTaskContainer.getChildCount(); i++) {
            View subTaskView = mSubTaskContainer.getChildAt(i);
            AssignSubTaskBean subTask = (AssignSubTaskBean) subTaskView.getTag();
            editText = (EditText) findViewById(R.id.et_subTaskContent);
            subTask.setContent(editText.getText().toString());
        }
        mCreateOAWorkTask = new CreateOAWorkTask();
        mCreateOAWorkTask.execute();
    }


    private class CreateOAWorkTask extends AsyncTask<Void, Void, String> {
        DepartmentBean department;

        public CreateOAWorkTask() {

        }

        @Override
        protected void onPreExecute() {
            mCustomProgress.start();
        }

        @Override
        protected String doInBackground(Void... params) {

            String OID = null;
            try {
                OID = WebServiceUtil.getInstance().createTheOaWorkMng(DataFormat
                        .formatAssignTask(mAssignTaskBean).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return OID;
        }

        @Override
        protected void onPostExecute(String OID) {
            try {
                if (!TextUtils.isEmpty(OID)) {
                    Toast.makeText(CreateTaskActivity.this, "任务创建成功", Toast.LENGTH_LONG).show();
                    finish();
                }
            } finally {
                mCreateOAWorkTask = null;
                mCustomProgress.stop();
            }
        }

        @Override
        protected void onCancelled() {
            mCreateOAWorkTask = null;
        }
    }
}
