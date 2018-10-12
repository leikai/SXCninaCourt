package org.sxchinacourt.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.sxchinacourt.R;

/**
 * @author lk
 */
public class ActivitySendSelect extends FragmentActivity implements View.OnClickListener {

    private String TAG = "ActivitySendSelect";
    private ActivitySendSelect context;

    private TableLayout send_tab_one;
    private TableLayout send_tab_two;
    private LinearLayout send_one_dongtai;
    private LinearLayout send_one_zijin;
    private LinearLayout send_one_shebei;
    private LinearLayout send_one_rencai;
    private LinearLayout send_one_changdi;
    private LinearLayout send_one_juben;

    private LinearLayout send_two_qiu;
    private ImageView send_two_qiu_img;
    private TextView send_two_qiu_tv;
    private LinearLayout send_two_chu;
    private ImageView send_two_chu_img;
    private TextView send_two_chu_tv;

    private ImageView send_one_close_img;
    private ImageView send_two_close_img;
    private ImageView send_two_back_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_select);
        context = ActivitySendSelect.this;
        initView();
    }

    private void initView() {
        send_tab_one = (TableLayout) findViewById(R.id.send_tab_one);
        send_tab_two = (TableLayout) findViewById(R.id.send_tab_two);

        send_one_dongtai = (LinearLayout) findViewById(R.id.send_one_dongtai);
        send_one_zijin = (LinearLayout) findViewById(R.id.send_one_zijin);
        send_one_shebei = (LinearLayout) findViewById(R.id.send_one_shebei);
        send_one_rencai = (LinearLayout) findViewById(R.id.send_one_rencai);
        send_one_changdi = (LinearLayout) findViewById(R.id.send_one_changdi);
        send_one_juben = (LinearLayout) findViewById(R.id.send_one_juben);

        send_two_qiu = (LinearLayout) findViewById(R.id.send_two_qiu);
        send_two_qiu_img = (ImageView) findViewById(R.id.send_two_qiu_img);
        send_two_qiu_tv = (TextView) findViewById(R.id.send_two_qiu_tv);
        send_two_chu = (LinearLayout) findViewById(R.id.send_two_chu);
        send_two_chu_img = (ImageView) findViewById(R.id.send_two_chu_img);
        send_two_chu_tv = (TextView) findViewById(R.id.send_two_chu_tv);

        send_one_close_img = (ImageView) findViewById(R.id.send_one_close_img);
        send_two_close_img = (ImageView) findViewById(R.id.send_two_close_img);
        send_two_back_img = (ImageView) findViewById(R.id.send_two_back_img);
    }

    @Override
    public void onResume() {
        super.onResume();
        send_tab_one.setVisibility(View.VISIBLE);
        send_tab_one.setAnimation(moveToViewLocation());


        send_one_dongtai.setOnClickListener(this);
        send_one_zijin.setOnClickListener(this);

        send_one_shebei.setVisibility(View.INVISIBLE);
        send_one_shebei.setOnClickListener(this);
        send_one_rencai.setVisibility(View.INVISIBLE);
        send_one_rencai.setOnClickListener(this);
        send_one_changdi.setVisibility(View.INVISIBLE);
        send_one_changdi.setOnClickListener(this);
        send_one_juben.setVisibility(View.INVISIBLE);
        send_one_juben.setOnClickListener(this);

        send_one_close_img.setOnClickListener(this);
        send_two_close_img.setOnClickListener(this);
        send_two_back_img.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_one_close_img:
                finish();
                break;
            case R.id.send_two_close_img:
                finish();
                break;
            case R.id.send_two_back_img:
                send_tab_two.setVisibility(View.GONE);
                send_tab_two.setAnimation(moveToRightHide());
                send_tab_one.setVisibility(View.VISIBLE);
                send_tab_one.setAnimation(moveToRightShow());
                break;
            case R.id.send_one_dongtai:

                startActivity(new Intent(ActivitySendSelect.this,CabinetActivity.class));
//                Toast.makeText(context, "发布动态", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.send_one_zijin:
                startActivity(new Intent(ActivitySendSelect.this,MachineActivity.class));
//                Toast.makeText(context, "发布资金", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.send_one_shebei:
                send_two_qiu_img.setImageResource(R.mipmap.pic_fabu_shebei_zl);
                send_two_chu_img.setImageResource(R.mipmap.pic_fabu_shebei_cz);
                send_two_qiu_tv.setText("设备求租");
                send_two_chu_tv.setText("设备出租");
                send_tab_one.setVisibility(View.GONE);
                send_tab_one.setAnimation(moveToLeftHide());
                send_tab_two.setVisibility(View.VISIBLE);
                send_tab_two.setAnimation(moveToLeftShow());
                send_two_qiu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ActivitySendSelect.this, "设备出租", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                send_two_chu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ActivitySendSelect.this, "设备求租", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                break;
            case R.id.send_one_rencai:
                send_two_qiu_img.setImageResource(R.mipmap.pic_fabu_rencai_zm);
                send_two_chu_img.setImageResource(R.mipmap.pic_fabu_rencai_qz);
                send_two_qiu_tv.setText("人才招募");
                send_two_chu_tv.setText("人才求职");
                send_tab_one.setVisibility(View.GONE);
                send_tab_one.setAnimation(moveToLeftHide());
                send_tab_two.setVisibility(View.VISIBLE);
                send_tab_two.setAnimation(moveToLeftShow());
                send_two_qiu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ActivitySendSelect.this, "人才招募", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                send_two_chu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ActivitySendSelect.this, "人才求职", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                break;
            case R.id.send_one_changdi:
                send_two_qiu_img.setImageResource(R.mipmap.pic_fabu_changdi_zl);
                send_two_chu_img.setImageResource(R.mipmap.pic_fabu_changdi_cz);
                send_two_qiu_tv.setText("场地求租");
                send_two_chu_tv.setText("场地出租");
                send_tab_one.setVisibility(View.GONE);
                send_tab_one.setAnimation(moveToLeftHide());
                send_tab_two.setVisibility(View.VISIBLE);
                send_tab_two.setAnimation(moveToLeftShow());
                send_two_qiu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ActivitySendSelect.this, "场地求租", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                send_two_chu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ActivitySendSelect.this, "场地出租", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                break;
            case R.id.send_one_juben:
                send_two_qiu_img.setImageResource(R.mipmap.pic_fabu_juben_zj);
                send_two_chu_img.setImageResource(R.mipmap.pic_fabu_juben_cs);
                send_two_qiu_tv.setText("剧本征集");
                send_two_chu_tv.setText("剧本出售");
                send_tab_one.setVisibility(View.GONE);
                send_tab_one.setAnimation(moveToLeftHide());
                send_tab_two.setVisibility(View.VISIBLE);
                send_tab_two.setAnimation(moveToLeftShow());
                send_two_qiu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ActivitySendSelect.this, "剧本征集", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                send_two_chu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ActivitySendSelect.this, "剧本出售", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * 从控件所在位置移动到控件的底部
     */
    public static TranslateAnimation moveToViewBottom() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }


    /**
     * 从控件的底部移动到控件所在位置
     */
    public static TranslateAnimation moveToViewLocation() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }

    /**
     * 从控件所在位置移动到左侧
     */
    public static TranslateAnimation moveToLeftHide() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }

    /**
     * 从右侧移动到控件所在位置
     */
    public static TranslateAnimation moveToLeftShow() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }

    /**
     * 从控件所在位置移动到右侧
     */
    public static TranslateAnimation moveToRightHide() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }

    /**
     * 从左侧移动到控件所在位置
     */
    public static TranslateAnimation moveToRightShow() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
