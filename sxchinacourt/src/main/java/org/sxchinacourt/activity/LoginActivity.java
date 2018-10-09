package org.sxchinacourt.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONException;
import org.json.JSONObject;
import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.bean.TokenRoot;
import org.sxchinacourt.bean.UserBean;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.common.Contstants;
import org.sxchinacourt.util.PinYin.SharedPreferencesUtil;
import org.sxchinacourt.util.WebServiceUtil;
import org.sxchinacourt.widget.CustomProgress;

import static android.Manifest.permission.READ_CONTACTS;
import static java.security.AccessController.getContext;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mUserNameView;
    private EditText mPasswordView;
    private CustomProgress mProgressView;
    private View mLoginFormView;
    private Context _context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        _context = LoginActivity.this;
        mUserNameView = (EditText) findViewById(R.id.et_username);
        mPasswordView = (EditText) findViewById(R.id.et_password);
        String name = SharedPreferencesUtil.getString(_context ,Contstants.KEY_SP_FILE, Contstants.KEY_SP_LoginName,null);
        mUserNameView.setText(name);
        String pass = SharedPreferencesUtil.getString(_context ,Contstants.KEY_SP_FILE, Contstants.KEY_SP_Password,null);
        mPasswordView.setText(pass);
        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = (CustomProgress) findViewById(R.id.login_progress);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mUserNameView,      R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String userName = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(userName)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mProgressView.start();
            mAuthTask = new UserLoginTask(userName, password);
            mAuthTask.execute();
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        private String mMessage;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mUsername)) {
                    return pieces[1].equals(mPassword);
                }
            }
            UserNewBean user = new UserNewBean();
            String token = null;

            user.setUserName(mUsername);
            user.setUserPassword(mPassword);

            String resultStr = WebServiceUtil.getInstance().createSession(user);
            Log.e("resultStr",""+resultStr);
            if (resultStr!=null){
                if (!TextUtils.isEmpty(resultStr)) {
                    TokenRoot resps = JSON.parseObject(resultStr,TokenRoot.class);
                    if (resps!=null){
                        Log.e("resps",""+resps.getMsginfo());
                        token = resps.getMsginfo().substring(10);
                        token = token.substring(0,token.length()-2);
                        Log.e("获取到的Token：",""+token);
                        if (resps.getOpresult()&&resps.getMsg().equals("登录成功!")){
                            UserNewBean userInfo = WebServiceUtil.getInstance().getUserInfo(user.getUserName(),null);

                            Contstants.titleName = userInfo.getOrgname();

                            user.copyUserInfo(userInfo);
                            CApplication.getInstance().setUser(user);
                            CApplication.getInstance().setToken(token);
                            return true;
                        }
                        mMessage = resps.getMsg();
                        return false;

                    }else {
                        mMessage = "网络问题稍后重试";
                        return false;
                    }

                }
            }
            mMessage = "网络问题稍后重试";
            return false;



/*

          //  Log.e("aoshdoasjdlasmdal;sdm;asmd;",resultStr.toString());
            try {
                if (!TextUtils.isEmpty(resultStr)) {
                    JSONObject json = new JSONObject(resultStr);

                    if (json.has("success") && json.getBoolean("success")) {
                        WebServiceUtil.getInstance().getUserInfo(user);
                        WebServiceUtil.getInstance().getUserDepartmentInfo(user);
                        WebServiceUtil.getInstance().getUserRoleInfo(user);
                        CApplication.getInstance().setUser(user);
                        return true;
                    }
                    //密码错误
                    mMessage = json.getString("msg");
                    return false;
                } else {
                    mMessage = "网络问题稍后重试";
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CApplication.getInstance().setUser(user);
            return true;
            */

//            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            mProgressView.stop();
            if (!TextUtils.isEmpty(mMessage)) {
                Toast.makeText(getBaseContext(), mMessage, Toast.LENGTH_LONG).show();
            }
            if (success) {
                SharedPreferencesUtil.setString(_context, Contstants.KEY_SP_FILE, Contstants.KEY_SP_LoginName, mUsername);
                SharedPreferencesUtil.setString(_context, Contstants.KEY_SP_FILE, Contstants.KEY_SP_Password, mPassword);
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), TabsActivity.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            mProgressView.stop();
        }
    }
}

