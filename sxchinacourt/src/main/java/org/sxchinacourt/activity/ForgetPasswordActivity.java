package org.sxchinacourt.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.util.WebServiceUtil;
import org.sxchinacourt.widget.CustomActionBar;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private CustomActionBar customActionBar;
    private EditText mOldPwdEditView;
    private EditText mNewPwdEditView1;
    private EditText mNewPwdEditView2;
    private LinearLayout confirm;
    private ModifyPwdTask mModifyPwdTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getSupportActionBar().hide();
        customActionBar = (CustomActionBar) findViewById(R.id.customActionBar);
        customActionBar.setTitle("修改密码");
        customActionBar.setLogoViewVisible(View.INVISIBLE);
        customActionBar.getBackBtnView().setOnClickListener(this);

        mOldPwdEditView = (EditText) findViewById(R.id.et_old_pwd);
        mNewPwdEditView1 = (EditText) findViewById(R.id.et_new_pwd1);
        mNewPwdEditView2 = (EditText) findViewById(R.id.et_new_pwd2);

        confirm = (LinearLayout) findViewById(R.id.btn_confirm);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_view:
                finish();
                break;
            case R.id.btn_confirm:
                String oldPwd = mOldPwdEditView.getText().toString();
                String newPwd1 = mNewPwdEditView1.getText().toString();
                String newPwd2 = mNewPwdEditView2.getText().toString();
                UserNewBean user = CApplication.getInstance().getCurrentUser();
                if (TextUtils.isEmpty(oldPwd)) {
                    Toast.makeText(this, "原密码不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!oldPwd.equals(user.getUserPassword())){
                    Toast.makeText(this, "输入的原密码不对", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(newPwd1)) {
                    Toast.makeText(this, "新密码不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if(newPwd1.equals(oldPwd)){
                    Toast.makeText(this, "新密码与原密码相同", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(newPwd1)) {
                    Toast.makeText(this, "确认密码不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!newPwd1.equals(newPwd2)) {
                    Toast.makeText(this, "两次输入的新密码不同", Toast.LENGTH_LONG).show();
                    return;
                }
                mModifyPwdTask = new ModifyPwdTask(oldPwd, newPwd1, newPwd2);
                mModifyPwdTask.execute();
                break;
            default:
                break;
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
                    mNewPwd1, "修改密码");
            return success;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean.booleanValue()) {
                Toast.makeText(ForgetPasswordActivity.this, "修改密码成功", Toast.LENGTH_LONG).show();
                UserNewBean user = CApplication.getInstance().getCurrentUser();
                user.setUserPassword(mNewPwd1);
                CApplication.getInstance().setUser(user);
            } else {
                Toast.makeText(ForgetPasswordActivity.this, "修改密码失败", Toast.LENGTH_LONG).show();
            }
            mModifyPwdTask = null;
        }
    }
}
