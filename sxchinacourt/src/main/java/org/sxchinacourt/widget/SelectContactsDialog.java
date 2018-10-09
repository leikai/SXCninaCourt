package org.sxchinacourt.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import org.sxchinacourt.R;
import org.sxchinacourt.adapter.JoinPersonAdapter;
import org.sxchinacourt.bean.DepartmentBean;
import org.sxchinacourt.bean.UserBean;
import org.sxchinacourt.util.WebServiceUtil;

import java.util.List;

/**
 * Created by baggio on 2017/3/7.
 */

public class SelectContactsDialog {
    private Dialog mContactsDialog;
    private Context mContext;
    private View.OnClickListener mDialogBtnConfirmClickListener;
    private DialogInterface.OnDismissListener mDialogDismissListener;
    private CustomProgress mRefreshUsersProgress;

    private List<DepartmentBean> mDepartments;
    private JoinPersonAdapter mAdapter;

    private ExpandUserListListener mExpandUserListListener;
    private UsersSelectedListener mUsersSelectedListener;

    private Handler mUpdateGroupsListHandler;
    private Handler mUpdateUsersListHandler;

    private RefreshGroupTask mRefreshGroupTask;
    private RefreshContactsTask mRefreshContactsTask;


    public SelectContactsDialog(Context context, View.OnClickListener btnConfirmClickListener,
                                DialogInterface.OnDismissListener dismissListener,
                                UsersSelectedListener usersSelectedListener) {
        this.mContext = context;
        this.mContactsDialog = new Dialog(mContext, R.style.contactsDialog);
        this.mDialogBtnConfirmClickListener = btnConfirmClickListener;
        this.mDialogDismissListener = dismissListener;
        this.mUsersSelectedListener = usersSelectedListener;
        initListener();
        initHandler();
        initDialog();
    }

    public interface ExpandUserListListener {
        public void expandUserList(DepartmentBean department);
    }


    public interface UsersSelectedListener {
        public void userSelected(UserBean user);

        public void usersSelected(DepartmentBean department);
    }

    public void show() {
        mContactsDialog.show();
        if (mRefreshGroupTask == null) {
            if (mDepartments == null || mDepartments.size() == 0) {
                mRefreshGroupTask = new RefreshGroupTask();
                mRefreshGroupTask.execute();
            }
        }
    }

    public void dismiss() {
        mContactsDialog.dismiss();
    }

    @Override
    protected void finalize() throws Throwable {
        mContactsDialog = null;
        mDepartments.clear();
        mDepartments = null;
        mUpdateGroupsListHandler = null;
        mUpdateUsersListHandler = null;
        super.finalize();
    }

    private void initDialog() {
        View content = LayoutInflater.from(mContext).inflate(
                R.layout.dialog_contacts, null);
        mContactsDialog.setContentView(content);
        Window window = mContactsDialog.getWindow();
        DisplayMetrics ds = mContext.getResources().getDisplayMetrics();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 1.0f;
        lp.dimAmount = 0.7f;
        lp.width = 6 * ds.widthPixels / 7;
        lp.height = 6 * ds.heightPixels / 7;
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        content.findViewById(R.id.btn_confirm).setOnClickListener(mDialogBtnConfirmClickListener);
        mContactsDialog.setOnDismissListener(mDialogDismissListener);
        mContactsDialog.setCanceledOnTouchOutside(false);
        mRefreshUsersProgress = (CustomProgress) content.findViewById(R.id.loading);
        ListView listView = (ListView) content.findViewById(R.id.list_contacts);
        mAdapter = new JoinPersonAdapter(mContext, mExpandUserListListener, mUsersSelectedListener);
        listView.setAdapter(mAdapter);
    }

    private void initListener() {
        mExpandUserListListener = new ExpandUserListListener() {
            @Override
            public void expandUserList(DepartmentBean department) {
                if (mRefreshContactsTask == null && department.getUsers() == null) {
                    mRefreshContactsTask = new RefreshContactsTask(department);
                    mRefreshContactsTask.execute();
                }
            }
        };
    }

    private void initHandler() {
        mUpdateGroupsListHandler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                mDepartments = (List<DepartmentBean>) msg.obj;
                mAdapter.setDepartments(mDepartments);
                mAdapter.notifyDataSetChanged();
            }
        };
        mUpdateUsersListHandler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                mAdapter.notifyDataSetChanged();
            }
        };
    }

    private class RefreshGroupTask extends AsyncTask<Void, Void, List<DepartmentBean>> {
        RefreshGroupTask() {
        }

        @Override
        protected void onPreExecute() {
            mRefreshUsersProgress.start();
        }

        @Override
        protected List<DepartmentBean> doInBackground(Void... params) {
            List<DepartmentBean> departments = WebServiceUtil.getInstance().getDepartmentList(null);
            return departments;
        }

        @Override
        protected void onPostExecute(List<DepartmentBean> departments) {
            try {
                if (departments != null) {
                    Message msg = mUpdateGroupsListHandler.obtainMessage();
                    msg.obj = departments;
                    mUpdateGroupsListHandler.sendMessage(msg);
                }
            } finally {
                mRefreshGroupTask = null;
            }
            mRefreshUsersProgress.stop();
        }

        @Override
        protected void onCancelled() {
            mRefreshGroupTask = null;
        }
    }

    private class RefreshContactsTask extends AsyncTask<Void, Void, List<UserBean>> {
        DepartmentBean department;

        //        long time;
        RefreshContactsTask(DepartmentBean department) {
            this.department = department;
        }

        @Override
        protected void onPreExecute() {
            mRefreshUsersProgress.start();
        }

        @Override
        protected List<UserBean> doInBackground(Void... params) {
            List<UserBean> users = WebServiceUtil.getInstance().getUserList(null,
                    department.getId());
            return users;
        }

        @Override
        protected void onPostExecute(List<UserBean> users) {
            try {
                if (users != null) {
                    department.setUsers(users);
                    department.setUserListIsExpanded(true);
                    Message msg = mUpdateUsersListHandler.obtainMessage();
                    mUpdateUsersListHandler.sendMessage(msg);
                }

            } finally {
                mRefreshContactsTask = null;
                mRefreshUsersProgress.stop();
            }
        }

        @Override
        protected void onCancelled() {
            mRefreshContactsTask = null;
        }
    }
}
