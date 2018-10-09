package org.sxchinacourt.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sxchinacourt.R;

/**
 * Created by baggio on 2017/2/7.
 */

public class CustomActionBar extends LinearLayout {
    ImageView mLogo;
    TextView mTitleView;
    TextView mActionBarLeftTextView;
    View mBackBtnView;

    public CustomActionBar(Context context) {
        super(context);
        initActionBar(context);
    }

    public CustomActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initActionBar(context);
    }

    public View getBackBtnView() {
        return mBackBtnView;
    }

    private void initActionBar(Context context) {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT);
        View actionBar = inflate(context, R.layout.tabs_actionbar, this);
        setLayoutParams(params);
        mLogo = (ImageView) actionBar.findViewById(R.id.iv_logo);
        mTitleView = (TextView) actionBar.findViewById(R.id.title);
        mActionBarLeftTextView = (TextView) actionBar.findViewById(R.id.left_textview);
        mBackBtnView = actionBar.findViewById(R.id.left_view);
//        addView(actionBar);
    }

    public void setLogoViewVisible(int visible){
        mLogo.setVisibility(visible);
    }
    public void setTitle(String title) {
        mTitleView.setText(title);
    }

    public void setLeftTextViewText(String text) {
        mActionBarLeftTextView.setText(text);
    }

    public void setBackBtnViewVisible(int visible) {
        mBackBtnView.setVisibility(visible);
    }
}
