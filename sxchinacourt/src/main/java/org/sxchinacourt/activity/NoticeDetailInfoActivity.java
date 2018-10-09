package org.sxchinacourt.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.sxchinacourt.R;
import org.sxchinacourt.activity.fragment.NoticeDetailFragment;
import org.sxchinacourt.widget.CustomActionBar;

/**
 * Created by baggio on 2017/3/20.
 */

public class NoticeDetailInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private CustomActionBar mActionBarView;
    private NoticeDetailFragment mNoticeDetailFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticedetail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        mActionBarView = new CustomActionBar(this);
        actionBar.setCustomView(mActionBarView);
        mActionBarView.getBackBtnView().setOnClickListener(this);
        mNoticeDetailFragment = new NoticeDetailFragment();
        mNoticeDetailFragment.setArguments(getIntent().getExtras());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content, mNoticeDetailFragment);
        ft.show(mNoticeDetailFragment).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_view: {
                onBackPressed();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().beginTransaction().remove(mNoticeDetailFragment);
        mNoticeDetailFragment.onDestroy();
        super.onBackPressed();
    }
}
