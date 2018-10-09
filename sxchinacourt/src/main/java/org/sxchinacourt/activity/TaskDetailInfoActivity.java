package org.sxchinacourt.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.kinggrid.iapprevision_iwebrevision.FieldEntity;
import com.kinggrid.iapprevision_iwebrevision.iAppRevision;
import com.kinggrid.iapprevision_iwebrevision.iAppRevision_iWebRevision;
import org.json.JSONException;
import org.json.JSONObject;
import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.bean.Component;
import org.sxchinacourt.bean.DepartmentBean;
import org.sxchinacourt.bean.MenuBean;
import org.sxchinacourt.bean.ProcessOpinion;
import org.sxchinacourt.bean.SelectViewComponent;
import org.sxchinacourt.bean.SubViewComponent;
import org.sxchinacourt.bean.TaskBean;
import org.sxchinacourt.bean.UserBean;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.bean.ViewComponents;
import org.sxchinacourt.util.SharedPreferencesUtil;
import org.sxchinacourt.util.WebServiceUtil;
import org.sxchinacourt.widget.CustomActionBar;
import org.sxchinacourt.widget.CustomProgress;
import org.sxchinacourt.widget.MenuLayout;
import org.sxchinacourt.widget.SelectContactsDialog;
import java.util.ArrayList;
import java.util.List;
import org.sxchinacourt.common.Contstants;

/**
 * Created by baggio on 2017/2/20.
 */

public class TaskDetailInfoActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String PARAM_TASK = "task";
    public static final String PARAM_TASK_TYPE = "task_type";
    private LinearLayout mContentView;//内容区域
    private CustomActionBar mActionBarView;
    private CustomProgress mCustomProgress;
    private MenuLayout mMenuLayout;  //菜单容器
    private LinearLayout mProcessOpinionContainer;  //签批容器
    private TextView tvCheckMore;//点击拉开审批记录
    private TextView mParticipantsView;  //联系人
    private LinearLayout mParticipantsContainer;  //联系人控件容器
    private EditText mRemarkView;  //签批备注
    private GetTaskInfoTask mGetTaskInfoTask;  //获取task模版信息
    private CreateNextWorkflowTask mCreateNextWorkflowTask; //签批task
    private GetNextParticipantsTask mGetNextParticipantsTask; //获取下一步执行人task
    private static TaskBean mTask; //当前任务
    private int mTaskType;
    private ViewComponents mViewComponents; //模版控件
    public static String returnValue;
    private List<ProcessOpinion> mProcessOpinions;  //签批
    private List<MenuBean> mMenus;  // 菜单
    private int mNextStepNo;  //下一步
    private String mParticipants;//下一步执行人
    private SelectContactsDialog mContactsDialog; //联系人
    private SelectContactsDialog.UsersSelectedListener mUsersSelectedListener;//选中联系人回调
    private View.OnClickListener mDialogBtnConfirmClickListener;
    private DialogInterface.OnDismissListener mDialogDismissListener;
    private Handler mUpdateUIHandler;  //更新UI handler
    private Handler mUpdateParticipantHandler; //更新下一步执行人 handler
    private MenuLayout.SelectedCallback mSelectedCallback;
    private UserBean mExecutor;  //下一步执行人
    private List<UserBean> mParticipantList; //执行人列表
    public static Activity activity;
    private boolean mTaskFinished;  //标记是否签批成功，如果成功不能重复签批
    private static boolean result = false;
    private UserBean user;
    public static final int SHOW_RESPONSE_USER_INFO = 0;
    private Handler handler = null;

    //private final String copyRight = "SxD/phFsuhBWZSmMVtSjKZmm/c/3zSMrkV2Bbj5tznSkEVZmTwJv0wwMmH/+p6wLiUHbjadYueX9v51H9GgnjUhmNW1xPkB++KQqSv/VKLDsR8V6RvNmv0xyTLOrQoGzAT81iKFYb1SZ/Zera1cjGwQSq79AcI/N/6DgBIfpnlwiEiP2am/4w4+38lfUELaNFry8HbpbpTqV4sqXN1WpeJ7CHHwcDBnMVj8djMthFaapMFm/i6swvGEQ2JoygFU3CQHU1ScyOebPLnpsDlQDzCjYgmFZo8sqFMkNKOgywo7x6aD2yiupr6ji7hzsE6/QqGcC+eseQV1yrWJ/1FwxLD4Y1YsZxXwh2w5W4lqa1RyVWEbHWAH22+t7LdPt+jENUp0yRGw03l8UY1ryrlWCQGj7ISWDgc3YLh0bz4sFvgWSgCNRP4FpYjl8hG/IVrYXKlEsCbCtDWPSC8ObydILqOW8fXpxdRHfEuWC1PB9ruQ=";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detailinfo);
        activity = TaskDetailInfoActivity.this;
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
            if (bundle.containsKey(PARAM_TASK)) { //判断是否包含指定的键名
                mTask = (TaskBean) bundle.getSerializable(PARAM_TASK);
                String completeTitle = mTask.getTitle();
                String spStr[] = completeTitle.split("\\)");


                mActionBarView.setTitle(spStr[1]);
                mActionBarView.setLogoViewVisible(View.GONE);
//                mActionBarView.setTitle("待办详情");
            }
            mTaskType = bundle.getInt(PARAM_TASK_TYPE, 0);
        }

        mCustomProgress = (CustomProgress) findViewById(R.id.loading);
        mContentView = (LinearLayout) findViewById(R.id.content);
        mGetTaskInfoTask = new GetTaskInfoTask();
        mGetTaskInfoTask.execute();
        initListener();
        initHandler();
    }

    //返回键
    public void onBackPressed() {
        if (mTaskFinished) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(PARAM_TASK, mTask);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_view: {
                onBackPressed();
                break;
            }
            case R.id.img_selectExecutors: {
                if (mParticipantList.size() == 0) {
                    if (mContactsDialog == null) {
                        mContactsDialog = new SelectContactsDialog(this,
                                mDialogBtnConfirmClickListener, mDialogDismissListener,
                                mUsersSelectedListener);
                    }
                    mContactsDialog.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("选择执行人");
                    String[] names = new String[mParticipantList.size()];
                    int checkNum = 0;
                    for (int i = 0; i < names.length; i++) {
                        UserBean user = mParticipantList.get(i);
                        names[i] = user.getUserName();
                        if (mExecutor.getUserName().equals(names[i])) {
                            checkNum = i;
                        }
                    }
                    builder.setSingleChoiceItems(names, checkNum, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mExecutor = mParticipantList.get(which);
                        }
                    });

                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            mParticipantsView.setText(mExecutor.getUserName());
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
                break;
            }
            case R.id.btn_send: {

                //确认发送的按钮  跳入下一个节点的逻辑
                if (mTaskFinished) {
                    return;
                }
                if (mNextStepNo == 2) {
                    Toast.makeText(this, "当前节点时第一个节点，用户不能不能操作，请在PC端操作", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mCreateNextWorkflowTask == null) {
                    String menuName = null;
                    String recordData = null;
                    String remark = null;
                    int boId = -1;
                    int menuType = -1;
                    if (mNextStepNo > 0 && mExecutor == null) {
                        Toast.makeText(TaskDetailInfoActivity.this, "您还没有选择执行人", Toast
                                .LENGTH_LONG)
                                .show();
                        return;
                    }
                    if (mMenuLayout != null) {
                        if (mMenuLayout.getCurMenu() == null) {
                            Toast.makeText(TaskDetailInfoActivity.this, "您还有没做批示", Toast.LENGTH_LONG).show();
                            return;
                        }
                        menuName = mMenuLayout.getCurMenu().getMenuName();
                        menuType = mMenuLayout.getCurMenu().getMenuType();
                        remark = mRemarkView.getText().toString();

                    }
                    try {
                        JSONObject json = new JSONObject();
                        List<Component> components = mViewComponents.getSubViewComponents(); //拿到模板控件对应的标签名称
                        for (Component component : components) {  // 遍历控件常量
                            SubViewComponent subViewComponent = (SubViewComponent) component;
                            String viewName = subViewComponent.getViewName();
                            String name = subViewComponent.getName();

                            if (subViewComponent.getText().equals("自动编号")) { // 对hiddenview赋值
                                boId = Integer.parseInt(subViewComponent.getLabelValue());//String 转Int
                                continue;
                            }
                            if (viewName.equals(Component.VIEW_TYPE_EDITVIEW) || viewName.equals(Component.VIEW_TYPE_DATEVIEW)) {  //对edittext或者datetext进行赋值
                                json.put(name, ((TextView) subViewComponent.getView
                                        ()).getText().toString());
                            } else if (viewName.equals(Component.VIEW_TYPE_SELECTVIEW)) {//选中的selectView赋值，在下一个节点以文本的形式展示
                                json.put(name, ((SelectViewComponent) subViewComponent).getCurrentItem());
                            } else if(viewName.equals(Component.VIEW_TYPE_SIGNVIEW)){// signview进行赋值，在下一个节点以图片的形式展示
                                //String value = SharedPreferencesUtil.getString(activity,Contstants.KEY_SP_FILE,Contstants.KEY_SP_VALUE,null);
                                json.put(name,returnValue);
                                returnValue = null;
                            }else if(viewName.equals(Component.VIEW_TYPE_RADIOVIEW)){ //对radio进行赋值 在下一个节点以文本的形式展示
                                json.put(name, ViewComponents.radio);
                            } else if(viewName.equals(Component.VIEW_TYPE_IMAGEVIEW)){ //对imageView 进行赋值
                                json.put(name, subViewComponent.getLabelValue());
                            }else {
                                json.put(name, subViewComponent.getLabelValue());
                            }
                        }

                        if (json.length() > 0) {
                            recordData = json.toString();
                        }
                    } catch (JSONException e) {
                        Log.d(getClass().getSimpleName(), e.getMessage());
                    }


                    mCreateNextWorkflowTask = new CreateNextWorkflowTask(menuName, remark,
                            menuType, recordData, boId);
                    mCreateNextWorkflowTask.execute();
                }
                break;
            }
        }
    }

    private void initListener() {
        findViewById(R.id.btn_send).setOnClickListener(this);
        mUsersSelectedListener = new SelectContactsDialog.UsersSelectedListener() {
            @Override
            public void userSelected(UserBean user) {
                boolean checked = !user.isChecked();
                if (mExecutor == null) {
                    mExecutor = user;
                    mExecutor.setChecked(checked);
                } else {
                    if (user.equals(mExecutor)) {
                        mExecutor = null;
                        user.setChecked(checked);
                    } else {
                        mExecutor.setChecked(false);
                        mExecutor = user;
                        mExecutor.setChecked(checked);
                    }
                }
            }

            @Override
            public void usersSelected(DepartmentBean department) {
                Toast.makeText(TaskDetailInfoActivity.this, "只能选择一个执行人", Toast.LENGTH_LONG).show();
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
                if (mExecutor != null) {
                    mParticipantsView.setText(mExecutor.getUserName());
                } else {
                    mParticipantsView.setText("");
                }
            }
        };

        mSelectedCallback = new MenuLayout.SelectedCallback() {
            @Override
            public void onSelected(MenuBean menu) {
                hideParticipant();
                mGetNextParticipantsTask = new GetNextParticipantsTask(menu.getMenuName(), menu
                        .getOpinion(), menu.getMenuType());
                mGetNextParticipantsTask.execute();
            }
        };
    }

    public static boolean saveRevisionToNet(final String url, final String recordId, final String userName, final String fieldName, final Bitmap bitmap, final iAppRevision_iWebRevision apprevison){
        new Thread(new Runnable() {
            @Override
            public void run() {
                FieldEntity fieldEntity = null;
                iAppRevision.isDebug = true;
                result = apprevison.saveRevision(String.valueOf(mTask.getProcessInstanceId()), url, bitmap, fieldName, userName, fieldEntity, true);
                if(result){
                    // Toast.makeText(activity,"chenggong",Toast.LENGTH_LONG);

                } else {
                    //  Toast.makeText(activity,"chenggong",Toast.LENGTH_LONG);
                }
            }
        }).start();
        return true;
    }
    private void initHandler() {
        mUpdateUIHandler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                mViewComponents = (ViewComponents) msg.obj;
                mViewComponents.loadViewComponents(mContentView, mCustomProgress);
                addProcessOpinionsViews();
                addSelectExecutorsView();
                addMenusAndRemarkView();
            }
        };
        mUpdateParticipantHandler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                addSelectExecutorsView();
            }
        };

    }

    /**
     * 审批记录模块
     */
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
                        View childView = LayoutInflater.from(TaskDetailInfoActivity.this).inflate
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
     * 添加执行人模块
     */
    private void addSelectExecutorsView() {
        if (mParticipantsContainer == null) {
            mParticipantsContainer = (LinearLayout) LayoutInflater.from(this).inflate(R.layout
                    .task_detailinfo_selectexecutor, null);
            mParticipantsView = (TextView) mParticipantsContainer.findViewById(R.id.tv_executors);
            mContentView.addView(mParticipantsContainer);
        }
        if (mNextStepNo <= 0) {
            mParticipantsContainer.setVisibility(View.GONE);
            return;
        }
        mParticipantsContainer.setVisibility(View.VISIBLE);
        mParticipantList = new ArrayList<>();
        View img_selectExecutor = mParticipantsContainer.findViewById(R.id.img_selectExecutors);
        img_selectExecutor.setOnClickListener
                (TaskDetailInfoActivity.this);
        if (!TextUtils.isEmpty(mParticipants)) { //mParticipants 黄皓皓
            splitParticipants(mParticipants);
            mExecutor = mParticipantList.get(0);
            mParticipantsView.setText(mExecutor.getUserName());
            if (mParticipantList.size() > 1) {
                img_selectExecutor.setVisibility(View.VISIBLE);
            } else {
                img_selectExecutor.setVisibility(View.INVISIBLE);
            }
        } else {
            img_selectExecutor.setVisibility(View.VISIBLE);
        }
    }


    /*
    添加备注模块
     */
    private void addMenusAndRemarkView() {
        if (mMenus == null) {
            return;
        }


        mMenuLayout = new MenuLayout(this, mSelectedCallback);
        int paddingLeft = (int) getResources().getDimension(R.dimen.menuLayout_paddingLeft);
        int paddingTop = (int) getResources().getDimension(R.dimen.menuLayout_paddingTop);
        int paddingButtom = (int) getResources().getDimension(R.dimen.menuLayout_paddingBottom);
        mMenuLayout.setPadding(paddingLeft, paddingTop, paddingLeft,
                paddingButtom);
        mMenuLayout.addMenus(mMenus);
        mMenuLayout.setOrientation(LinearLayout.VERTICAL);
        View remarkView = LayoutInflater.from(this).inflate(R.layout.task_detailinfo_remark, null);
        mRemarkView = (EditText) remarkView.findViewById(R.id.et_remark);
        mContentView.addView(mMenuLayout);
        mContentView.addView(remarkView);
    }

    //请求网络
    private class GetTaskInfoTask extends AsyncTask<Void, Void, ViewComponents> {
        @Override
        protected ViewComponents doInBackground(Void... params) {
            UserNewBean user = CApplication.getInstance().getCurrentUser();
            ViewComponents components = WebServiceUtil.getInstance().getBOData(user.getUserName(),
                    mTask.getTaskId(),
                    mTask.getProcessInstanceId()
                    ,mTask.getProcessGroup(), false);

            mProcessOpinions = WebServiceUtil.getInstance().getProcessOpinionList(mTask.getProcessInstanceId());
            if (mProcessOpinions !=null){
                for (int i=0;i<mProcessOpinions.size();i++){
                    ProcessOpinion opinion = mProcessOpinions.get(i);
//                    UserBean userResult = WebServiceUtil.getInstance().getUserInfo(opinion.getCreateUser());
//                    opinion.setCreateUser(userResult.getUserName());
                }
            }


            mMenus = WebServiceUtil.getInstance().getAuditMenus(user.getUserName(), mTask.getTaskId());
            if (mMenus == null) {
                mNextStepNo = WebServiceUtil.getInstance().getNextStepNo(user.getUserName(), mTask.getProcessInstanceId(),
                        mTask.getTaskId(), null, null, Integer.MIN_VALUE);
                if (mNextStepNo > 0) {
                    mParticipants = WebServiceUtil.getInstance().getNextParticipants(user.getUserName(),
                            mTask.getProcessInstanceId(),
                            mTask.getTaskId(), null, null, Integer.MIN_VALUE);
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

    private class GetNextParticipantsTask extends AsyncTask<Void, Void, Void> {
        String menuName;
        String opinion;
        int menuType;

        private GetNextParticipantsTask(String menuName, String opinion, int menuType) {
            this.menuName = menuName;
            this.opinion = opinion;
            this.menuType = menuType;
        }


        @Override
        protected Void doInBackground(Void... params) {
            UserNewBean user = CApplication.getInstance().getCurrentUser();
            mNextStepNo = WebServiceUtil.getInstance().getNextStepNo(user.getUserName(), mTask.getProcessInstanceId(),
                    mTask.getTaskId(), menuName, opinion, menuType);
            if (mNextStepNo > 0) {
                mParticipants = WebServiceUtil.getInstance().getNextParticipants(user.getUserName(),
                        mTask.getProcessInstanceId(),
                        mTask.getTaskId(), menuName, opinion, menuType);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            mCustomProgress.start();
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (mNextStepNo > 0) {
                    Message msg = mUpdateParticipantHandler.obtainMessage();
                    mUpdateParticipantHandler.sendMessage(msg);
                }
            } finally {
                mGetNextParticipantsTask = null;
                mCustomProgress.stop();
            }
        }

        @Override
        protected void onCancelled() {
            mGetNextParticipantsTask = null;
            mCustomProgress.stop();
        }
    }
    public static class GetDataBasePhotoTask extends AsyncTask<Void,Void,Void>{
        private static int metaId;

        public GetDataBasePhotoTask(int metaId){
            this.metaId = metaId;
        }

        //while boolean if
        //初始值为false
        //进入while必须为true
        //进入循环体 如果取到值了 初始Boolean的值必须为！true  所以 boo=true 再次走到初始值 boo=true  进入while 现在为！true 跳出循环
        @Override
        protected Void doInBackground(Void... params) {
            returnValue = WebServiceUtil.getInstance().getDataBasePhote(mTask.getProcessInstanceId(),metaId,"");
            if (returnValue != null){
                //SharedPreferencesUtil.setString(activity,Contstants.KEY_SP_FILE,Contstants.KEY_SP_VALUE,returnValue);
            }else {
                boolean boo = false;
                while (!boo){
                    returnValue = WebServiceUtil.getInstance().getDataBasePhote(mTask.getProcessInstanceId(),metaId,"");
                    if (returnValue != null && !returnValue.equals("")){
                        // SharedPreferencesUtil.setString(activity,Contstants.KEY_SP_FILE,Contstants.KEY_SP_VALUE,returnValue);
                        boo = true;
                    }
                }
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }

    private class CreateNextWorkflowTask extends AsyncTask<Void, Void, String> {//请求的参数丶任务的进度丶获得的结果数据
        String menuName;
        String opinion;
        String recordData;
        int menuType;
        int boId;

        public CreateNextWorkflowTask(String menuName, String opinion, int menuType, String
                recordData, int boId) {
            this.menuName = menuName;
            this.opinion = opinion;
            this.menuType = menuType;
            this.recordData = recordData;
            this.boId = boId;
        }
        //耗时操作如请求网络
        @Override
        protected String doInBackground(Void... params) {
            UserNewBean user = CApplication.getInstance().getCurrentUser();
            String taskTitle = mTask.getTitle();
            if (mNextStepNo <= 0) {
                taskTitle = "最后";
            } else {
                taskTitle = mTask.getStepname() + taskTitle.substring(taskTitle.indexOf(")") + 1);
            }
            try {
                int resultNo = 1;
                if (!TextUtils.isEmpty(recordData)) {
                    resultNo = WebServiceUtil.getInstance().updateBOData(mTask.getProcessGroup(), boId, recordData);
                }
                if (resultNo > 0) {
                    return WebServiceUtil.getInstance().createNextWorkflowTask(user.getUserName()
                            , mTask
                                    .getProcessInstanceId(), mTask.getTaskId(), taskTitle, menuName,
                            opinion, menuType, mExecutor != null ? mExecutor
                                    .getUserId() : null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        //在UI线程里面调用，它在这个task执行后会立即调用
        @Override
        protected void onPreExecute() {
            mCustomProgress.start();
        }
        //当task结束后调用，它运行在UI线程
        @Override
        protected void onPostExecute(String result) {
            try {
                if (!TextUtils.isEmpty(result)) {
                    if (result.equals("true")) {
                        Toast.makeText(TaskDetailInfoActivity.this, "任务处理完成", Toast.LENGTH_LONG).show();
                        mTaskFinished = true;
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(TaskDetailInfoActivity.this, "任务处理失败", Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(TaskDetailInfoActivity.this, "任务处理失败", Toast.LENGTH_LONG)
                            .show();
                }
            } finally {
                mCreateNextWorkflowTask = null;
                mCustomProgress.stop();
            }
        }

        @Override
        protected void onCancelled() {
            mCreateNextWorkflowTask = null;
            mCustomProgress.stop();
        }
    }


    private void hideParticipant() {
        mParticipantsContainer.setVisibility(View.GONE);
        mParticipantsView.setText(null);
        mNextStepNo = -1;
        mParticipantList = null;
        mParticipants = null;
    }

    private void splitParticipants(String participants) {
        String[] users = participants.split(",");
        for (int i = 0; i < users.length; i++) {
            String user = users[i];
            UserBean userBean = new UserBean();
            userBean.setUserId(user);
            userBean.setUserName(user);
            mParticipantList.add(userBean);
        }
    }
}
