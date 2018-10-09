package org.sxchinacourt.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.adapter.UsersRelationAdapter;
import org.sxchinacourt.bean.TokenRoot;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.util.WebServiceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lk
 */
public class SwitchAccountActivity extends AppCompatActivity {

    /**
     * 返回按钮
     */
    Button btnBack;

    /**
     * logo
     */
    ImageView ivCourtoaIcon;
    /**
     * 操作提醒
     */
    TextView tvRemind;
    /**
     * 分割线
     */
    ImageView ivDecorate;
    /**
     * 角色列表
     * 部门相同显示姓名
     * 部门不同显示角色名
     */
    RecyclerView rvUsersRelation;
    /**
     * 清楚登录痕迹
     */
    TextView tvClearLogin;
    /**
     * 获取数据时的Handle
     */
    private Handler handlergetdata = null;
    /**
     * 相关角色数据列表
     */
    private List<UserNewBean> usersRelation;

    /**
     * 配置关联数据
     */
    public static final int SHOW_RESPONSE_USER_RELATION = 0;
    /**
     * 切换关联账号
     */
    public static final int SHOW_RESPONSE_RELATION_SWITCH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_account);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initView();
        initEvent();
        initData();


    }

    /**
     * 初始化界面
     */
    private void initView() {
        btnBack = (Button)findViewById(R.id.btn_back);
        ivCourtoaIcon = (ImageView)findViewById(R.id.iv_courtoa_icon);
        tvRemind = (TextView)findViewById(R.id.tv_remind);
        ivDecorate = (ImageView)findViewById(R.id.iv_decorate);
        rvUsersRelation = (RecyclerView)findViewById(R.id.rv_users_relation);
        tvClearLogin = (TextView)findViewById(R.id.tv_clear_login);
    }


    /**
     * 处理点击事件
     */
    private void initEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /**
     * 获取并处理数据
     */
    @SuppressLint("HandlerLeak")
    private void initData() {

        new Thread() {
            @Override
            public void run() {
                UserNewBean user = CApplication.getInstance().getCurrentUser();
                String relation = user.getRelation();
                String resp = WebServiceUtil.getInstance().getUsersByRelation(relation);
                List<UserNewBean> userList = JSON.parseArray(resp, UserNewBean.class);
                String ceshi = userList.get(0).getUserName();
                Message message = new Message();
                message.what = SHOW_RESPONSE_USER_RELATION;
                message.obj = userList;
                handlergetdata.sendMessage(message);

                Log.e("resp", "" + ceshi);
            }
        }.start();

        handlergetdata = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case SHOW_RESPONSE_USER_RELATION:
                        usersRelation = (ArrayList) msg.obj;
                        LinearLayoutManager layoutManager = new LinearLayoutManager(SwitchAccountActivity.this);
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        rvUsersRelation.setLayoutManager(layoutManager);
                        UsersRelationAdapter adapter = new UsersRelationAdapter(SwitchAccountActivity.this,usersRelation);
                        rvUsersRelation.setAdapter(adapter);

                        adapter.setOnItemClickListener(new UsersRelationAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClickListener(View view, int position) {
                                UserNewBean userNewBean = usersRelation.get(position);
                                ObtainNewRelationToken(userNewBean);
                            }
                        });
                        break;
                    case SHOW_RESPONSE_RELATION_SWITCH:
                        Intent intent = new Intent(SwitchAccountActivity.this, TabsActivity.class);
                        startActivity(intent);
                        Toast.makeText(SwitchAccountActivity.this,"跳微门户模块！",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };

    }

    /**
     * 获取新用户角色的token
     * @param user
     */
    private void ObtainNewRelationToken(final UserNewBean user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String token = null;
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
                                String ceshi = userInfo.getOrgid();
                                user.copyUserInfo(userInfo);
                                CApplication.getInstance().setUser(user);
                                CApplication.getInstance().setToken(token);
                                Message message = new Message();
                                message.what = SHOW_RESPONSE_RELATION_SWITCH;
                                message.obj = "1";
                                handlergetdata.sendMessage(message);

                            }


                        }else {

                        }

                    }
                }

            }
        }).start();
    }

}
