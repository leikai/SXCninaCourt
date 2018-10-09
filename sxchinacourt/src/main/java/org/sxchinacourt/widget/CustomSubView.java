package org.sxchinacourt.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.sxchinacourt.R;
import org.sxchinacourt.activity.TaskDetailInfoActivity;
import org.sxchinacourt.bean.Component;
import org.sxchinacourt.bean.TaskBean;
import org.sxchinacourt.util.WebServiceUtil;

import java.io.File;
import java.io.IOException;
import java.io.PipedReader;

import static org.sxchinacourt.R.id.image;
import static org.sxchinacourt.R.id.mPathView;
import static org.sxchinacourt.activity.TaskDetailInfoActivity.activity;

/**
 * Created by DongZi on 2017/8/30.
 */

public class CustomSubView extends LinearLayout {
    private Component mComponent;
    private TextView tv_name;
    private ImageView iv_sign;
    private PopupWindow mPopupWindow;
    private LinePathView mPathView;
    private TaskBean mTask;

    public CustomSubView(final Context context , Component component) {
        super(context);
        mTask = new TaskBean();
        mComponent = component;
        ViewGroup subView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.task_detailinfo_subview,null);
        tv_name = (TextView) subView.findViewById(R.id.tv_name);
        iv_sign = (ImageView) subView.findViewById(R.id.iv_sign);
        addView(subView);
        tv_name.setText(component.getText()+":");
        iv_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TaskDetailInfoActivity.activity!=null){
                    //showPopupWindow(context,TaskDetailInfoActivity.activity);
                }

            }
        });
    }


}
