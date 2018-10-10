package org.sxchinacourt.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.net.VpnService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.Thing;

import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.activity.fragment.AppsManagerFragment;
import org.sxchinacourt.activity.fragment.BaseFragment;
import org.sxchinacourt.activity.fragment.ContactsManagerFragment;
import org.sxchinacourt.activity.fragment.HomePageManagerFragment;
import org.sxchinacourt.activity.fragment.MsgFragment;
import org.sxchinacourt.activity.fragment.MsgManagerFragment;
import org.sxchinacourt.activity.fragment.SettingManagerFragment;
import org.sxchinacourt.bean.DepartmentBean;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.common.Contstants;
import org.sxchinacourt.service.NotificationService;
import org.sxchinacourt.util.ExampleUtil;
import org.sxchinacourt.util.IConstant;
import org.sxchinacourt.util.WebServiceUtil;
import org.sxchinacourt.util.file.FileAccessUtil;
import org.sxchinacourt.util.network.vpn.LocalVPNService;
import org.sxchinacourt.widget.CustomActionBar;

import java.util.List;
import java.util.Set;

import static org.sxchinacourt.R.id.bottom_nav_content;
import static org.sxchinacourt.activity.TagAliasOperatorHelper.ACTION_SET;
import static org.sxchinacourt.activity.TagAliasOperatorHelper.sequence;
import static org.sxchinacourt.activity.fragment.TodoTaskListFragment.pageLocationForH5;
import static org.sxchinacourt.util.IConstant.REVISION_FULL_SIGN;
import static org.sxchinacourt.util.IConstant.REVISION_GRID_SIGN;
import static org.sxchinacourt.util.IConstant.REVISION_SIGN;
import static org.sxchinacourt.util.IConstant.REVISION_WORD_SIGN;

/**
 * Created by baggio on 2017/2/3.
 */

public class TabsActivity extends AppCompatActivity implements BottomNavigationBar
        .OnTabSelectedListener, View.OnClickListener {
    /**
     * 获取存储权限
     */
    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    /**
     * 获取拨打电话权限
     */
    public static final int REQUEST_CALL_PHONE = 2;
    /**
     * 获取发送短信权限
     */
    public static final int REQUEST_SEND_SMS = 3;

    private static final int VPN_REQUEST_CODE = 0x0F;
    private boolean waitingForVPNStart;
    /**
     * 内容区域
     */
    private LinearLayout mBottomNavContent;
    /**
     * 底部导航栏
     */
    private BottomNavigationBar mBottomNavigationBarContainer;
    /**
     * 首页
     */
    private BottomNavigationItem mHomePage;
    /**
     * 消息tab
     */
    private BottomNavigationItem mMsgItem;
    /**
     * 应用tab
     */
    private BottomNavigationItem mAppsItem;
    /**
     * 联系人tab
     */
    private BottomNavigationItem mContactsItem;
    /**
     * 设置tab
     */
    private BottomNavigationItem mSettingItem;

    private BadgeItem badgeItem;
    /**
     * 首页碎片
     */
    private HomePageManagerFragment mHomePageManagerFragment;
    private MsgFragment mMsgFragment;
    private MsgManagerFragment mMsgManagerFragment;

    private AppsManagerFragment mAppsManagerFragment;
    private ContactsManagerFragment mContactsManagerFragment;
    private SettingManagerFragment mSettingManagerFragment;
    private BaseFragment mCurrentFragment;
    /**
     * 自定义actionBar
     */
    private CustomActionBar mActionBarView;
    private UpdateManager mUpdateManager;

    /**
     * 获取联系人分组的请求
     */
    private RefreshGroupTask mRefreshGroupTask;
    /**
     * 联系人分组集合
     */
    public static  List<DepartmentBean> mDepartments;
    /**
     * 联系人集合
     */
    public static List<UserNewBean> mUsers;
    //-----------------------推送-----------------------//
    String alias = null;
    boolean isAliasAction = false;
    int action = -1;
    public static boolean isForeground = false;
    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.pushtest1.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    /**
     * 获取底部菜单栏目前选择的位置
     */
    private int bottomNavVarPosition = 0;

    private String token;
    private UserNewBean user;
    private long exitTime = 0;
    /**
     * 从页面中获取目前H5所在的界面
     */
    private static String pageLocationForH5 = "2" ;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_tabs);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        mActionBarView = new CustomActionBar(this);
        actionBar.setCustomView(mActionBarView);
        user = CApplication.getInstance().getCurrentUser();


        initView();

        //-------推送-------------//
        onTagAliasAction();
        registerMessageReceiver();  // used for receive msg
        initData();
        initBottomNavBar();
        createRootDir();
        mUpdateManager = new UpdateManager(this);
    }

    private BroadcastReceiver vpnStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (LocalVPNService.BROADCAST_VPN_STATE.equals(intent.getAction())) {
                if (intent.getBooleanExtra("running", false)){
                    waitingForVPNStart = false;
                }

            }
        }
    };

    private void startVPN() {
        Intent vpnIntent = VpnService.prepare(this);
        if (vpnIntent != null)
            startActivityForResult(vpnIntent, VPN_REQUEST_CODE);
        else
            onActivityResult(VPN_REQUEST_CODE, RESULT_OK, null);
    }


    /**
     * 1：代表处于1级目录下，点击返回直接退出程序
     2：代表处于2级目录下，点击返回调用碎片管理器的返回键方法，返回1级目录
     3：代表处于2级目录下，点击返回调用碎片管理器的返回键方法，返回1级目录
     */
    @Override
    public void onBackPressed() {
        if (!mCurrentFragment.onBackPressed()) {
            super.onBackPressed();
        }
        Log.e("pageLocationForH5",""+pageLocationForH5);

    }
    private void initView() {
        mBottomNavContent = (LinearLayout) findViewById(bottom_nav_content);
        mBottomNavigationBarContainer = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar_container);
        mActionBarView.getBackBtnView().setOnClickListener(this);
    }
    private void initData(){

        token  = CApplication.getInstance().getCurrentToken();
        startService(new Intent(TabsActivity.this,NotificationService.class));
        Log.e("取出来的token",""+token);
        if (token ==null){
            AlertDialog.Builder dialog = new AlertDialog.Builder(TabsActivity.this);
            dialog.setTitle("");
            dialog.setMessage("您的身份验证令牌已过期，请点击确认，重新登录！");
            dialog.setCancelable(false);
            dialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            dialog.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(TabsActivity.this,LoginActivity.class));
                }
            });
            dialog.show();


        }
        mRefreshGroupTask = new RefreshGroupTask(null);
        mRefreshGroupTask.execute();

    }

    /**
     * 初始化底部导航栏
     */
    private void initBottomNavBar() {
        //自动隐藏
        mBottomNavigationBarContainer.setAutoHideEnabled(true);
        mBottomNavigationBarContainer.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBarContainer.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        //背景颜色
        mBottomNavigationBarContainer.setBarBackgroundColor("#ffffff");
        //角标
        badgeItem = new BadgeItem().setBackgroundColor(Color.RED).setText("99").setHideOnSelect(false);

        mHomePage = new BottomNavigationItem(R.drawable.icon_nav_home_selected,"首页");
        mAppsItem = new BottomNavigationItem(R.drawable.icon_nav_apply_selected, "应用");
        mContactsItem = new BottomNavigationItem(R.drawable.icon_nav_application_selected, "通讯录");
        mSettingItem = new BottomNavigationItem(R.drawable.icon_nav_mine_selected, "我");
        mHomePage.setInactiveIconResource(R.drawable.icon_nav_home_default);
        mAppsItem.setInactiveIconResource(R.drawable.icon_nav_apply_default);
        mContactsItem.setInactiveIconResource(R.drawable.icon_nav_application_default);
        mSettingItem.setInactiveIconResource(R.drawable.icon_nav_mine_default);
        mBottomNavigationBarContainer.addItem(mHomePage).addItem(mAppsItem).addItem(mContactsItem).addItem(mSettingItem);
        mBottomNavigationBarContainer.initialise();
        mBottomNavigationBarContainer.setTabSelectedListener(this);
        //显示默认的Frag
        setDefaultFragment();
    }


    @Override
    public void onTabSelected(int position) {
        switch (position) {
            case 0:
                hideAllFragment();
                if (mHomePageManagerFragment == null) {
                    mHomePageManagerFragment = new HomePageManagerFragment();
                }
                showFragment(mHomePageManagerFragment);
                bottomNavVarPosition = mBottomNavigationBarContainer.getCurrentSelectedPosition();
                break;
            case 1:
                if (user.getOrgid() == null){
                    startActivity(new Intent(TabsActivity.this, LoginActivity.class));
                    finish();
                }else if (user.getOrgid().equals(Contstants.OID_JINZHONG)){
                    //------------------------------修改------------------------------//
                    startActivity(new Intent(TabsActivity.this, ActivitySendSelect.class));
                    //-------------------------------完----------------------------//
                }else if (user.getOrgid().equals(Contstants.OID_YUSHE)){

                }else if (user.getOrgid().equals(Contstants.OID_LINGSHI)){
                }
                mBottomNavigationBarContainer.selectTab(bottomNavVarPosition);
                break;
            case 2:
                hideAllFragment();
                if (mContactsManagerFragment == null) {
                    mContactsManagerFragment = new ContactsManagerFragment();
                }
                showFragment(mContactsManagerFragment);
                bottomNavVarPosition = mBottomNavigationBarContainer.getCurrentSelectedPosition();
                break;
            case 3:
                hideAllFragment();
                if (mSettingManagerFragment == null) {
                    mSettingManagerFragment = new SettingManagerFragment();
                }
                showFragment(mSettingManagerFragment);
                bottomNavVarPosition = mBottomNavigationBarContainer.getCurrentSelectedPosition();
                break;
                default:
                    break;
        }
    }

    @Override
    public void onTabUnselected(int position) {


    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_view: {
                mCurrentFragment.onBackPressed();
                break;
            }
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mCurrentFragment != null) {
            mCurrentFragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createRootDir();
            }
        }
    }


    private void createRootDir() {
        if (android.os.Build.VERSION.SDK_INT < 23 || PackageManager.PERMISSION_GRANTED ==
                ContextCompat
                        .checkSelfPermission(this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            FileAccessUtil.createDir(FileAccessUtil.FILE_DIR);

            IConstant.Helper.setSignModeEnabled(REVISION_FULL_SIGN, REVISION_SIGN, REVISION_WORD_SIGN, REVISION_GRID_SIGN);
            Bitmap bitmap = null;
            String imageString = "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";
            try {

                byte[] bitmapArray;
                bitmapArray = Base64.decode(imageString, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
                System.out.println("haha");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String[] permissions = {Manifest.permission
                    .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest
                    .permission.CALL_PHONE, Manifest.permission.SEND_SMS};
            ActivityCompat.requestPermissions(this, permissions, REQUEST_EXTERNAL_STORAGE);
        }
    }

    /**
     * 设置默认Fragment
     */
    private void setDefaultFragment() {
        if (mHomePageManagerFragment == null) {
            mHomePageManagerFragment = new HomePageManagerFragment();
        }
        //默认显示msgFrag
        showFragment(mHomePageManagerFragment);

    }

    /**
     * 添加Frag
     * @param frag
     */
    private void addFragment(Fragment frag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (frag != null && !frag.isAdded()) {
            ft.add(bottom_nav_content, frag);
            ft.commit();
        }
    }

    /**
     * 隐藏所有fragment
     */
    private void hideAllFragment() {
        hideFragment(mHomePageManagerFragment);
        hideFragment(mMsgManagerFragment);
        hideFragment(mContactsManagerFragment);
        hideFragment(mSettingManagerFragment);
    }

    /**
     * 隐藏frag
     * @param frag
     */
    private void hideFragment(Fragment frag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (frag != null && frag.isAdded()) {
            ft.hide(frag);
            ft.commit();
        }
    }

    private void showFragment(BaseFragment fragment) {
        addFragment(fragment);
        getSupportFragmentManager().beginTransaction().show(fragment).commit();
        mCurrentFragment = fragment;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                // TODO: Define a title for the content shown.
                .setName("Tabs Page")
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }



    private Handler mUpdateGroupsListHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            mDepartments = (List<DepartmentBean>) msg.obj;
        }
    };

    /**
     * 请求联系人分组的数据
     */
    private class RefreshGroupTask extends AsyncTask<Void, Void, List<DepartmentBean>> {
        String mDepartmentName;

        RefreshGroupTask(String department) {
            this.mDepartmentName = department;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<DepartmentBean> doInBackground(Void... params) {
            List<DepartmentBean> departments = WebServiceUtil.getInstance().getDepartmentList(null);
            return departments;
        }

        @Override
        protected void onPostExecute(List<DepartmentBean> departments) {
            if (departments != null) {
                Message msg = mUpdateGroupsListHandler.obtainMessage();
                msg.obj = departments;
                mUpdateGroupsListHandler.sendMessage(msg);
            }
        }

        @Override
        protected void onCancelled() {
        }
    }
    @Override
    protected void onResume() {
        isForeground = true;

        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }
    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Log.e("intent.getAction()",""+intent.getAction().toString());
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    setCostomMsg(showMsg.toString());
                }
            } catch (Exception e){
            }
        }
    }
    private void setCostomMsg(String msg){
    }
    /**
     * 处理tag/alias相关操作的点击
     * */
    public void onTagAliasAction() {
        Set<String> tags = null;
        String alias = null;
        int action = -1;
        boolean isAliasAction = false;
        //设置alias
        alias = getInPutAlias();
        if(TextUtils.isEmpty(alias)){
            return;
        }
        isAliasAction = true;
        action = ACTION_SET;
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = action;
        sequence++;
        if(isAliasAction){
            tagAliasBean.alias = alias;
        }else{
            tagAliasBean.tags = tags;
        }
        tagAliasBean.isAliasAction = isAliasAction;
        TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(),sequence,tagAliasBean);
    }
    /**
     * 获取输入的alias
     * */
    private String getInPutAlias(){
        UserNewBean user = CApplication.getInstance().getCurrentUser();
        String alias =user.getUserName();
        if (TextUtils.isEmpty(alias)) {
            Toast.makeText(getApplicationContext(), R.string.error_alias_empty, Toast.LENGTH_SHORT).show();
            return null;
        }
        if (!ExampleUtil.isValidTagAndAlias(alias)) {
            Toast.makeText(getApplicationContext(), R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
            return null;
        }
        return alias;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     *  供H5页面调用的方法，目的是：获取现在H5页面处于几级页面下，原生处理返回键功能
     */
    public static class TestJavaScriptInterface {


        @JavascriptInterface
        public void getPageLocation(String location) {
            pageLocationForH5 = location;

            Log.e("location",""+location);

        }
    }

}
