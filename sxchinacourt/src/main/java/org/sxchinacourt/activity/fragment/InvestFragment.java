package org.sxchinacourt.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.sxchinacourt.R;
import org.sxchinacourt.adapter.InvestTwoAdapter;
import org.sxchinacourt.bean.CourtBean;

public class InvestFragment extends BaseFragment {

    private BottomNavigationView bnv_fragment;
    private Context context;
    private FragmentTransaction ft;
    private InvestFragmentTheer investFragmentTheer;
    private InvestFragmentOne investFragmentOne;
    private InvestFragmentTwo investFragmentTwo;
    private InvestTwoAdapter adapter;
    public static CourtBean bean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "通讯录";
        mShowBtnBack = View.INVISIBLE;
        mLogo = View.INVISIBLE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = inflater.getContext();
        return onCreateView(inflater, R.layout.fragment_invest, container, savedInstanceState);

    }

    @Nullable
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView(mRootView);
        initData();
        initEvent();
    }

    private void initEvent() {
        bnv_fragment.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.invest_home:
                    ft = getChildFragmentManager().beginTransaction();
                    ft.replace(R.id.rl_content, investFragmentOne).commit();
                    break;
                case R.id.invest_invest:
                    ft = getChildFragmentManager().beginTransaction();
                    ft.replace(R.id.rl_content, investFragmentTwo).commit();
                    break;
                case R.id.invest_user:
                    ft = getChildFragmentManager().beginTransaction();
                    ft.replace(R.id.rl_content, investFragmentTheer).commit();
                    break;
            }
            return true;
        }
    };

    protected void initData() {
        bean = new CourtBean();
        investFragmentOne = new InvestFragmentOne(context, R.layout.fragment_invest_one);
        investFragmentTwo = new InvestFragmentTwo(context, R.layout.fragment_invest_two);
        investFragmentTheer = new InvestFragmentTheer(context, R.layout.fragment_invest_theer);
        investFragmentTheer.setPreFragment(this);
        ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.rl_content, investFragmentOne);
        ft.add(R.id.rl_content, investFragmentTwo);
        ft.add(R.id.rl_content, investFragmentTheer);
        ft.replace(R.id.rl_content, investFragmentOne).commit();
        investFragmentOne.setFragmentCallBack(new InvestFragmentOne.FragmentCallBack() {
            @Override
            public void onClick(String str) {
                bnv_fragment.setSelectedItemId(R.id.invest_invest);
                bean.setCourt(str);
            }
        });
        investFragmentTwo.setFragmentTwoCallBack(new InvestFragmentTwo.FragmentTwoCallBack() {
            @Override
            public void onClick(String id, String name) {
                bnv_fragment.setSelectedItemId(R.id.invest_user);
                bean.setId(id);
                bean.setName(name);
            }
        });
    }

    protected void initView(View v) {
        bnv_fragment = (BottomNavigationView) v.findViewById(R.id.bnv_fragment);
    }

    @Override
    public void showChildFragment(Bundle bundle) {
        if (mPreFragment != null) {
            mPreFragment.showChildFragment(bundle);
        }
    }

    @Override
    public boolean onBackPressed() {
        getFragmentManager().popBackStack();
        return true;
    }
}
