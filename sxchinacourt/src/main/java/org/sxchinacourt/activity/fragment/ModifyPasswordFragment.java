package org.sxchinacourt.activity.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.util.WebServiceUtil;

/**
 *
 * @author baggio
 * @date 2017/2/9
 */

public class ModifyPasswordFragment extends BaseFragment implements View.OnClickListener {
    ModifyPwdTask mModifyPwdTask;
    EditText mOldPwdEditView;
    EditText mNewPwdEditView1;
    EditText mNewPwdEditView2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "修改密码";
        mShowBtnBack = View.VISIBLE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCreateView(inflater, R.layout.fragment_modify_pwd, container, savedInstanceState);
    }


    @Nullable
    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView.findViewById(R.id.btn_confirm).setOnClickListener(this);
        mOldPwdEditView = (EditText) mRootView.findViewById(R.id.et_old_pwd);
        mNewPwdEditView1 = (EditText) mRootView.findViewById(R.id.et_new_pwd1);
        mNewPwdEditView2 = (EditText) mRootView.findViewById(R.id.et_new_pwd2);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_confirm) {
            String oldPwd = mOldPwdEditView.getText().toString();
            String newPwd1 = mNewPwdEditView1.getText().toString();
            String newPwd2 = mNewPwdEditView2.getText().toString();
            UserNewBean user = CApplication.getInstance().getCurrentUser();
            if (TextUtils.isEmpty(oldPwd)) {
                Toast.makeText(getContext(), "原密码不能为空", Toast.LENGTH_LONG).show();
                return;
            }
            if(!oldPwd.equals(user.getUserPassword())){
                Toast.makeText(getContext(), "输入的原密码不对", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(newPwd1)) {
                Toast.makeText(getContext(), "新密码不能为空", Toast.LENGTH_LONG).show();
                return;
            }
            if(newPwd1.equals(oldPwd)){
                Toast.makeText(getContext(), "新密码与原密码相同", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(newPwd1)) {
                Toast.makeText(getContext(), "确认密码不能为空", Toast.LENGTH_LONG).show();
                return;
            }
            if (!newPwd1.equals(newPwd2)) {
                Toast.makeText(getContext(), "两次输入的新密码不同", Toast.LENGTH_LONG).show();
                return;
            }
            mModifyPwdTask = new ModifyPwdTask(oldPwd, newPwd1, newPwd2);
            mModifyPwdTask.execute();
        }
    }

    private class ModifyPwdTask extends AsyncTask<Void, Void, Boolean> {
        String mOldPwd;
        String mNewPwd1;
        String mNewPwd2;

        private ModifyPwdTask(String oldPwd, String newPwd1, String newPwd2) {
            mOldPwd = oldPwd;
            mNewPwd1 = newPwd1;
            mNewPwd2 = newPwd2;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            UserNewBean user = CApplication.getInstance().getCurrentUser();
            boolean success = WebServiceUtil.getInstance().changeUserPWD(user.getUserName(),
                    mOldPwd, mNewPwd1);
            return success;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean.booleanValue()) {
                Toast.makeText(getContext(), "修改密码成功", Toast.LENGTH_LONG).show();
                UserNewBean user = CApplication.getInstance().getCurrentUser();
                user.setUserPassword(mNewPwd1);
                CApplication.getInstance().setUser(user);
            } else {
                Toast.makeText(getContext(), "修改密码失败", Toast.LENGTH_LONG).show();
            }
            mModifyPwdTask = null;
        }
    }
}
