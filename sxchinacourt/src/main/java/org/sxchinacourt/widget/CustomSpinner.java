package org.sxchinacourt.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.sxchinacourt.R;
import org.sxchinacourt.bean.SelectViewComponent;

/**
 * Created by baggio on 2017/3/28.
 */

public class CustomSpinner extends LinearLayout implements AdapterView.OnItemSelectedListener {
    SelectViewComponent mComponent;
    TextView mNameTextView;
    Spinner mSpinner;

    public CustomSpinner(Context context, SelectViewComponent component) {
        super(context);
        this.mComponent = component;
        View spinnerView = LayoutInflater.from(context).inflate(R.layout.task_detailinfo_spinner,
                null);
        addView(spinnerView);
        initView();
        initData();
    }

    private void initView() {
        mSpinner = (Spinner) findViewById(R.id.spinner);
        mNameTextView = (TextView) findViewById(R.id.tv_name);
        mSpinner.setOnItemSelectedListener(this);
    }

    private void initData() {
        if (!TextUtils.isEmpty(mComponent.getText())) {
            mNameTextView.setText(mComponent.getText() + "：");
        }
        if (mComponent.getItems().size() > 0) {
            ArrayAdapter arr_adapter = new ArrayAdapter<String>(getContext(), android.R.layout
                    .simple_spinner_item,
                    mComponent.getItems());
            arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner.setAdapter(arr_adapter);
            int selection = 0;
            if (!TextUtils.isEmpty(mComponent.getDefaultItem())) {
                for (int i = 0; i < mComponent.getItems().size(); i++) {
                    if (mComponent.getItems().get(i).equals(mComponent.getDefaultItem())) {
                        selection = i;
                        break;
                    }
                }
            }
            mSpinner.setSelection(selection);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mComponent.setSelection(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
