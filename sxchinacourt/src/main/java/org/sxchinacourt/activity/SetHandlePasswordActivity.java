package org.sxchinacourt.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wangnan.library.GestureLockView;
import com.wangnan.library.listener.OnGestureLockListener;

import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.util.WebServiceUtil;
import org.sxchinacourt.widget.CustomActionBar;

/**
 * @author lk
 */
public class SetHandlePasswordActivity extends AppCompatActivity {
    private CustomActionBar customActionBar;
    GestureLockView mGestureLockView;
    UserNewBean currentUser;
    TextView mRemind;
    String setHandlePwdOriginal ;
    private ChangeHandlePwdTask mAuthTask = null;
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_handle_password);
        getSupportActionBar().hide();
        customActionBar = (CustomActionBar) findViewById(R.id.customActionBar);
        customActionBar.setTitle("设置手势密码");
        customActionBar.setLogoViewVisible(View.INVISIBLE);
        currentUser = CApplication.getInstance().getCurrentUser();
        mRemind = (TextView)findViewById(R.id.tv_remind);
        mGestureLockView = (GestureLockView) findViewById(R.id.set_handle_pwd_glv);
        mGestureLockView.setPainter(new SignOnActivity.NiCaiFu360Painter());
        //设置手势监听器
        mGestureLockView.setGestureLockListener(new OnGestureLockListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(String progress) {

            }

            @Override
            public void onComplete(String result) {

                if (TextUtils.isEmpty(result)){
                    return;
                }else {
                    String userName = "gaohongwei";
//                    String remind = mRemind.getText().toString();
                    if ("请输入手势密码".equals(mRemind.getText().toString())){
                        setHandlePwdOriginal = result;
                        mRemind.setText("请再次输入手势密码");
                        mGestureLockView.clearView();
                    }else if ("请再次输入手势密码".equals(mRemind.getText().toString())){
                        if (setHandlePwdOriginal.equals(result)){
                            mAuthTask = new ChangeHandlePwdTask(currentUser.getUserName(), result);
                            mAuthTask.execute();

                        }else {
                            mRemind.setText("请输入手势密码");
                            Toast.makeText(SetHandlePasswordActivity.this,"两次密码不相同，请重新设置",Toast.LENGTH_SHORT).show();
                            mGestureLockView.clearView();

                        }
                    }

                    return;

                }
            }
        });
        customActionBar.getBackBtnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class ChangeHandlePwdTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mHandPassword;
        private String mMessage;

        ChangeHandlePwdTask(String username, String handPassword) {
            mUsername = username;
            mHandPassword = handPassword;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mUsername)) {
                    return pieces[1].equals(mHandPassword);
                }
            }
            UserNewBean user = new UserNewBean();
            String token = null;

            user.setUserName(mUsername);
            user.setHandPassword(mHandPassword);

            String resultStr = WebServiceUtil.getInstance().changeUserPWD(mUsername,mHandPassword);
            Log.e("resultStr",""+resultStr);
            if (resultStr!=null){
                if (!TextUtils.isEmpty(resultStr)) {
                    if ("true".equals(resultStr)){
                        mMessage = "设置成功";
                        return true;
                    }else {
                        mMessage = "设置失败";
                        return false;
                    }

                }
            }
            mMessage = "设置失败";
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
//            mProgressView.stop();
            if (!TextUtils.isEmpty(mMessage)) {
                Toast.makeText(SetHandlePasswordActivity.this, mMessage, Toast.LENGTH_LONG).show();
                mRemind.setText("手势密码设置完成");
//                mGestureLockView.clearView();
//                return;
            }
            if (success) {
                mGestureLockView.clearView();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
//            mProgressView.stop();
        }
    }


}
