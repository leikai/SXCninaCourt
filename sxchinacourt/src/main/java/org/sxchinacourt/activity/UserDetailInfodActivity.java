package org.sxchinacourt.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.util.WebServiceUtil;
import org.sxchinacourt.widget.CustomActionBar;
import org.sxchinacourt.widget.CustomProgress;

import java.util.Vector;

/**
 * @author lk
 */
public class UserDetailInfodActivity extends AppCompatActivity {
    public static final String PARAM_USER = "user";
    private CustomActionBar customActionBar;
    UserNewBean mUser;
    RefreshUserInfoTask mRefreshUserInfoTask;
    CustomProgress mCustomProgress;
    LinearLayout mUserInfoContainer;
    TextView mUserNameView;
    TextView mDepartmentView;
    private String token;
    PopupWindow mPopUpWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_infod);
        receiveUserIntent();
        initView();
        initEvent();
        token = CApplication.getInstance().getCurrentToken();
        mRefreshUserInfoTask = new RefreshUserInfoTask();
        mRefreshUserInfoTask.execute();
    }

    private void initEvent() {
        findViewById(R.id.btn_tail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vector<String> nums = new Vector();
                if(!TextUtils.isEmpty(mUser.getEmpMobilephone())){
                    nums.add(mUser.getEmpMobilephone());
                }
                if(!TextUtils.isEmpty(mUser.getEmpMobilephone())){
                    nums.add(mUser.getEmpMobilephone());
                }
//                nums.add("18611976018");
                if(nums.size()>0){
                    String[] phoneNumArrsy = new String[nums.size()];
                    nums.toArray(phoneNumArrsy);
                    DisplayMetrics ds = getResources().getDisplayMetrics();
                    int height = 2 * ds.heightPixels / 3;
                    View anchorView = findViewById(R.id.tool_bar);
                    int yOff = -height;
                    if (height + anchorView.getHeight() > ds.heightPixels) {
                    }
                    showGroupPopUpWindow(anchorView, phoneNumArrsy, yOff,
                            mPhoneNumsOnClickListener);
                }
            }
        });
        findViewById(R.id.btn_sms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mUser.getEmpMobilephone())) {
                    if (android.os.Build.VERSION.SDK_INT < 23 || PackageManager.PERMISSION_GRANTED ==
                            ContextCompat
                                    .checkSelfPermission(UserDetailInfodActivity.this,
                                            Manifest.permission.SEND_SMS)) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("smsto:" + mUser.getEmpMobilephone()));
                        startActivity(intent);
                    } else {
                        String[] permissions = {Manifest
                                .permission.SEND_SMS};
                        ActivityCompat.requestPermissions(UserDetailInfodActivity.this, permissions,
                                TabsActivity.REQUEST_SEND_SMS);
                    }
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

    private void initView() {
        getSupportActionBar().hide();
        customActionBar = (CustomActionBar) findViewById(R.id.customActionBar);
        customActionBar.setTitle("人员信息");
        mUserInfoContainer = (LinearLayout) findViewById(R.id.userinfo_container);
        mCustomProgress = (CustomProgress) findViewById(R.id.loading);
        mUserNameView = (TextView) findViewById(R.id.tv_username);
        mDepartmentView = (TextView) findViewById(R.id.tv_department);
        if (!TextUtils.isEmpty(mUser.getEmpname())) {
            mUserNameView.setText(mUser.getEmpname());
        }
        if (!TextUtils.isEmpty(mUser.getDeptname())) {
            mDepartmentView.setText(mUser.getDeptname());
        }
    }

    private void receiveUserIntent() {
        mUser = (UserNewBean) getIntent().getSerializableExtra("user");
        Log.e("用户信息",""+mUser.getEmpname());
    }

    //通讯录中个人详情页面
    @SuppressLint("HandlerLeak")
    private Handler mUpdateUserInfoHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            mUser = (UserNewBean) msg.obj;
            if (!TextUtils.isEmpty(mUser.getEmpname())) {
                mUserNameView.setText(mUser.getEmpname());
            }
            if (!TextUtils.isEmpty(mUser.getDeptname())) {
                mDepartmentView.setText(mUser.getDeptname());
            }
            View childView = LayoutInflater.from(UserDetailInfodActivity.this).inflate(R.layout.user_info_item,
                    null);
            TextView labelView = (TextView) childView.findViewById(R.id.tv_label);
            TextView valueView = (TextView) childView.findViewById(R.id.tv_value);
            labelView.setText("手机");
            if (!TextUtils.isEmpty(mUser.getEmpMobilephone())) {
                valueView.setText(mUser.getEmpMobilephone());
            }
            mUserInfoContainer.addView(childView);

            childView = LayoutInflater.from(UserDetailInfodActivity.this).inflate(R.layout.user_info_item,
                    null);
            labelView = (TextView) childView.findViewById(R.id.tv_label);
            valueView = (TextView) childView.findViewById(R.id.tv_value);
            labelView.setText("电话");
            if (!TextUtils.isEmpty(mUser.getEmpMobilephone())) {
                valueView.setText(mUser.getEmpMobilephone());
            }
            mUserInfoContainer.addView(childView);
            // TODO: 2017/2/17

            childView = LayoutInflater.from(UserDetailInfodActivity.this).inflate(R.layout.user_info_item,
                    null);
            labelView = (TextView) childView.findViewById(R.id.tv_label);
            valueView = (TextView) childView.findViewById(R.id.tv_value);
            labelView.setText("邮箱");
            if (!TextUtils.isEmpty(mUser.getEmpEmail())) {
                valueView.setText(mUser.getEmpEmail());
            }
            mUserInfoContainer.addView(childView);

            childView = LayoutInflater.from(UserDetailInfodActivity.this).inflate(R.layout.user_info_item,
                    null);
            labelView = (TextView) childView.findViewById(R.id.tv_label);
            valueView = (TextView) childView.findViewById(R.id.tv_value);
            labelView.setText("部门");
            if (!TextUtils.isEmpty(mUser.getDeptname())) {
                valueView.setText(mUser.getDeptname());
            }
            mUserInfoContainer.addView(childView);

            childView = LayoutInflater.from(UserDetailInfodActivity.this).inflate(R.layout.user_info_item,
                    null);
            labelView = (TextView) childView.findViewById(R.id.tv_label);
            valueView = (TextView) childView.findViewById(R.id.tv_value);
            labelView.setText("法院");
            valueView.setText("");
            mUserInfoContainer.addView(childView);
        }
    };



    private AdapterView.OnItemClickListener mPhoneNumsOnClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (android.os.Build.VERSION.SDK_INT < 23 || PackageManager.PERMISSION_GRANTED ==
                    ContextCompat
                            .checkSelfPermission(UserDetailInfodActivity.this,
                                    Manifest.permission.CALL_PHONE)) {
                String phoneNum = parent.getAdapter().getItem(position).toString();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneNum));
                startActivity(intent);
            } else {
                String[] permissions = {Manifest
                        .permission.CALL_PHONE};
                ActivityCompat.requestPermissions(UserDetailInfodActivity.this, permissions,
                        TabsActivity.REQUEST_CALL_PHONE);
            }
        }
    };

    private class RefreshUserInfoTask extends AsyncTask<Void, Void, UserNewBean> {
        RefreshUserInfoTask() {
        }

        @Override
        protected void onPreExecute() {
            mCustomProgress.start();
        }

        @Override
        protected UserNewBean doInBackground(Void... params) {
            UserNewBean user = WebServiceUtil.getInstance().getUserInfo(null,mUser.getOid());
            return user;
        }

        @Override
        protected void onPostExecute(UserNewBean user) {
            try {
                if (user != null) {
                    Message msg = mUpdateUserInfoHandler.obtainMessage();
                    msg.obj = user;
                    mUpdateUserInfoHandler.sendMessage(msg);
                } else {
                    Toast.makeText(UserDetailInfodActivity.this, "获取用户信息失败", Toast.LENGTH_LONG).show();
                }
            } finally {
                mRefreshUserInfoTask = null;
                mCustomProgress.stop();
            }
        }

        @Override
        protected void onCancelled() {
            mRefreshUserInfoTask = null;
            mCustomProgress.stop();
        }
    }

    /**
     * 显示popupUpWindow
     * @param anchorView
     * @param data
     * @param yOff
     * @param listener
     */
    private void showGroupPopUpWindow(View anchorView, String[] data, int yOff, AdapterView
            .OnItemClickListener listener) {
        View popupView = LayoutInflater.from(UserDetailInfodActivity.this).inflate(R.layout.group_popupwindow, null);
        //展示示各个分组的Listview  如立案组 信息处
        ListView lsvMore = (ListView) popupView.findViewById(R.id.groups);
        lsvMore.setAdapter(new ArrayAdapter<String>(UserDetailInfodActivity.this, android.R.layout.simple_list_item_1,
                data));
        lsvMore.setOnItemClickListener(listener);
        DisplayMetrics ds = getResources().getDisplayMetrics();
        int width = 3 * ds.widthPixels / 4;
        int height = 2 * ds.heightPixels / 3;
        mPopUpWindow = new PopupWindow(popupView, width, height);
        mPopUpWindow.setAnimationStyle(R.style.popup_window_anim);
        mPopUpWindow.setFocusable(true);
        mPopUpWindow.setOutsideTouchable(false);
//        setWindowAlpha(0.3f);
        mPopUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mPopUpWindow = null;
            }
        });
        popupView.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopUpWindow.dismiss();
            }
        });
        mPopUpWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopUpWindow.setOutsideTouchable(true);
        //设置监听
        mPopUpWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE && !mPopUpWindow.isFocusable()) {
                    return true;
                }
                return false;
            }
        });
        mPopUpWindow.update();
        mPopUpWindow.showAsDropDown(anchorView, ds.widthPixels / 8, yOff);
    }



}
