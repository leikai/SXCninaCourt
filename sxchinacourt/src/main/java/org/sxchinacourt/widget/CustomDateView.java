package org.sxchinacourt.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sxchinacourt.R;
import org.sxchinacourt.activity.DateTimePickerDialog;
import org.sxchinacourt.bean.Component;

import static org.sxchinacourt.util.SysUtil.getStringDate;

/**
 * Created by baggio on 2017/3/21.
 */

public class CustomDateView extends LinearLayout {
    Component mComponent;
    TextView mDateView;

    public CustomDateView(Context context, Component component) {
        super(context);
        this.mComponent = component;
        View dateView = LayoutInflater.from(context).inflate(R.layout.task_detailinfo_dateview,
                null);
        addView(dateView);
        initView();
    }

    public String getDate() {
        return mDateView.getText().toString();
    }

    public TextView getDateView() {
        return mDateView;
    }

    public void setDate(String dateStr) {
        mDateView.setText(dateStr);
    }

    private OnClickListener mChooseDateListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            DateTimePickerDialog dialog = new DateTimePickerDialog(getContext(), System.currentTimeMillis
                    ());
            dialog.setOnDateTimeSetListener(new DateTimePickerDialog.OnDateTimeSetListener() {
                public void OnDateTimeSet(AlertDialog dialog, long date) {
                    String dateStr = getStringDate(date);
                    mDateView.setText(dateStr);
                }
            });
            dialog.show();
        }
    };

    private void initView() {
        TextView labelView = (TextView) findViewById(R.id.tv_dateview_label);
        labelView.setText(mComponent.getText());
        mDateView = (TextView) findViewById(R.id.tv_date);
        findViewById(R.id.img_chooseDate).setOnClickListener(mChooseDateListener);
    }
}
