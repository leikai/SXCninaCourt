package org.sxchinacourt.activity.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.sxchinacourt.R;
import org.sxchinacourt.bean.CourtBean;
import org.sxchinacourt.bean.DepartmentBean;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.util.PinYin.PinYinUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class ContactsManagerFragment extends BaseFragment {
    public static final int CHILD_TYPE_CONTACTS = 0;
    public static final int CHILD_TYPE_USERINFO = 1;
    public static final int CHILD_TYPE_CONTACTS_COUTR = 2;//法院
    public static final int CHILD_TYPE_CONTACTS_DEPART = 3;//部门
    public static final int CHILD_TYPE_CONTACTS_USER = 4;//人员
    private ContactsFragment mContactsFragment;
    private InvestFragment mInvestFragment;
    private InvestFragmentOne mInvestFragmentOne;
    private InvestFragmentTwo mInvestFragmentTwo;
    private InvestFragmentTheer mInvestFragmentThree;
    private UserDetailInfoFragment mUserInfoFragment;
    private BaseFragment mCurChildFragment;
    private Vector<BaseFragment> mFragmentStack;


    private List<DepartmentBean> mDepartments;
    private List<UserNewBean> mUsers;
    public static CourtBean bean;
    private BottomNavigationView bnv_fragment;
    private int bottomNavVarPosition = 0;//获取顶部菜单栏目前选择的位置

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentStack = new Vector<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, R.layout.fragment_contactsmanager, container, savedInstanceState);
    }
//    public void initData(){
//        mRefreshContactsTask = new RefreshContactsTask(null, null, "全部分组");
//        mRefreshContactsTask.execute();
//    }




    @Nullable
    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView(mRootView);
        initData();
        initEvent();
        mChildType = CHILD_TYPE_CONTACTS_COUTR;
        showChildFragment(null);
    }

    private void initEvent() {
        bnv_fragment.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.invest_home:
                        item.setChecked(true);
                        hideAllFragment();
                        mChildType = CHILD_TYPE_CONTACTS_COUTR;
                        bnv_fragment.setSelected(true);
                        bottomNavVarPosition = bnv_fragment.getSelectedItemId();
                        Log.e("bottomNavVarPosition",""+bottomNavVarPosition);
                        showChildFragment(null);
                        break;
                    case R.id.invest_invest:
                        item.setChecked(true);
                        hideAllFragment();
                        mChildType = CHILD_TYPE_CONTACTS_DEPART;
                        bnv_fragment.setSelected(true);
                        bottomNavVarPosition = bnv_fragment.getSelectedItemId();
                        Log.e("bottomNavVarPosition",""+bottomNavVarPosition);
                        showChildFragment(null);
                        break;
                    case R.id.invest_user:
                        item.setChecked(true);
                        hideAllFragment();
                        mChildType = CHILD_TYPE_CONTACTS_USER;
                        bnv_fragment.setSelected(true);
                        bottomNavVarPosition = bnv_fragment.getSelectedItemId();
                        Log.e("bottomNavVarPosition",""+bottomNavVarPosition);
                        showChildFragment(null);
                        break;
                }
                return false;
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (mCurChildFragment!=null){
            mCurChildFragment.onHiddenChanged(hidden);
        }

    }

    @Override
    public void showChildFragment(Bundle bundle) {
        super.showChildFragment(bundle);
        switch (mChildType) {
            case CHILD_TYPE_CONTACTS:
                if (mInvestFragment == null) {
                    mInvestFragment = new InvestFragment();
                    mInvestFragment.setPreFragment(this);
                }
                addChildFragment(mInvestFragment, R.id.content);
                mCurChildFragment = mInvestFragment;
                break;
            case CHILD_TYPE_CONTACTS_COUTR:
//                if (mInvestFragmentOne == null) {
                mInvestFragmentOne = new InvestFragmentOne(getActivity(), R.layout.fragment_invest_one);
                bean = new CourtBean();
                mInvestFragmentOne.setFragmentCallBack(new InvestFragmentOne.FragmentCallBack() {
                    @Override
                    public void onClick(String str) {
                        bean.setCourt(str);
                    }
                });
                mInvestFragmentOne.setPreFragment(this);
//                }
                bnv_fragment.setVisibility(View.VISIBLE);
                bnv_fragment.getMenu().getItem(0).setChecked(true);

                addChildFragment(mInvestFragmentOne, R.id.content);
                mCurChildFragment = mInvestFragmentOne;
                break;
            case CHILD_TYPE_CONTACTS_DEPART:
                if (mInvestFragmentTwo == null) {
                    mInvestFragmentTwo = new InvestFragmentTwo(getContext(), R.layout.fragment_invest_two);
//                    bean = new CourtBean();
                    mInvestFragmentTwo.setFragmentTwoCallBack(new InvestFragmentTwo.FragmentTwoCallBack() {
                        @Override
                        public void onClick(String id, String name) {
//                            bnv_fragment.setSelectedItemId(R.id.invest_user);
                            bean.setId(id);
                            bean.setName(name);
                        }
                    });
                    mInvestFragmentTwo.setPreFragment(this);
                    mInvestFragmentTwo.setArguments(bundle);
                }else {
                    mInvestFragmentTwo = new InvestFragmentTwo(getContext(), R.layout.fragment_invest_two);
//                    bean = new CourtBean();
                    mInvestFragmentTwo.setFragmentTwoCallBack(new InvestFragmentTwo.FragmentTwoCallBack() {
                        @Override
                        public void onClick(String id, String name) {
//                            bnv_fragment.setSelectedItemId(R.id.invest_user);
                            bean.setId(id);
                            bean.setName(name);
                        }
                    });
                    mInvestFragmentTwo.setPreFragment(this);
                }
                bnv_fragment.setVisibility(View.VISIBLE);
                bnv_fragment.getMenu().getItem(1).setChecked(true);
                bnv_fragment.setSelected(true);
                addChildFragment(mInvestFragmentTwo, R.id.content);
                hideFragment(mInvestFragmentOne);
                mCurChildFragment = mInvestFragmentTwo;
                break;
            case CHILD_TYPE_CONTACTS_USER:
                if (mInvestFragmentThree == null) {
                    mInvestFragmentThree = new InvestFragmentTheer(getActivity(), R.layout.fragment_invest_theer);
                    mInvestFragmentThree.setPreFragment(this);
                    mInvestFragmentThree.setArguments(bundle);
                }else {
                    mInvestFragmentThree = new InvestFragmentTheer(getActivity(), R.layout.fragment_invest_theer);
                    mInvestFragmentThree.setPreFragment(this);
                }
                bnv_fragment.setVisibility(View.VISIBLE);
                bnv_fragment.getMenu().getItem(2).setChecked(true);
                bnv_fragment.setSelected(true);
                addChildFragment(mInvestFragmentThree,R.id.content);
                hideFragment(mInvestFragmentTwo);
                mCurChildFragment = mInvestFragmentThree;

                break;
            case CHILD_TYPE_USERINFO:
                if (mUserInfoFragment == null) {
                    mUserInfoFragment = new UserDetailInfoFragment();
                    mUserInfoFragment.setPreFragment(this);
                }else{
                    mUserInfoFragment = new UserDetailInfoFragment();
                    mUserInfoFragment.setPreFragment(this);
                }
                mUserInfoFragment.setArguments(bundle);
                addChildFragment(mUserInfoFragment, R.id.content);
                hideFragment(mInvestFragmentThree);
                bnv_fragment.setVisibility(View.GONE);
                mCurChildFragment = mUserInfoFragment;
                break;
        }
        mFragmentStack.add(mCurChildFragment);
    }

    @Override
    public boolean onBackPressed() {
        boolean handle = false;
        if (mFragmentStack.size() > 1) {
            handle = mCurChildFragment.onBackPressed();
            if (!handle) {
                if (mCurChildFragment instanceof InvestFragmentTwo||mCurChildFragment instanceof InvestFragmentOne) {
//                    mInvestFragmentTwo = null;
                    hideAllFragment();
                    mInvestFragmentOne = new InvestFragmentOne(getActivity(), R.layout.fragment_invest_one);
//                    bean = new CourtBean();
                    mInvestFragmentOne.setFragmentCallBack(new InvestFragmentOne.FragmentCallBack() {
                        @Override
                        public void onClick(String str) {

                            bean.setCourt(str);
                        }
                    });
                    mInvestFragmentOne.setPreFragment(this);
                    hideFragment(mInvestFragmentTwo);
                    addChildFragment(mInvestFragmentOne,R.id.content);
                    bnv_fragment.setVisibility(View.VISIBLE);
                    bnv_fragment.getMenu().getItem(0).setChecked(true);

                    mCurChildFragment = mInvestFragmentOne;
                }else if (mCurChildFragment instanceof InvestFragmentTheer) {
//                    mInvestFragmentThree = null;
                    hideAllFragment();
                    mInvestFragmentTwo = new InvestFragmentTwo(getActivity(), R.layout.fragment_invest_two);
//                    bean = new CourtBean();
                    mInvestFragmentTwo.setFragmentTwoCallBack(new InvestFragmentTwo.FragmentTwoCallBack() {
                        @Override
                        public void onClick(String id, String name) {
//                            bnv_fragment.setSelectedItemId(R.id.invest_user);
                            bean.setId(id);
                            bean.setName(name);
                        }
                    });
                    mInvestFragmentTwo.setPreFragment(this);
                    addChildFragment(mInvestFragmentTwo,R.id.content);
                    hideFragment(mInvestFragmentThree);
                    bnv_fragment.setVisibility(View.VISIBLE);
                    bnv_fragment.getMenu().getItem(1).setChecked(true);
                    mCurChildFragment = mInvestFragmentTwo;
                }else if (mCurChildFragment instanceof UserDetailInfoFragment) {
//                    mUserInfoFragment = null;
                    hideAllFragment();
                    mInvestFragmentThree = new InvestFragmentTheer(getActivity(), R.layout.fragment_invest_theer);
                    mInvestFragmentThree.setPreFragment(this);
                    addChildFragment(mInvestFragmentThree,R.id.content);
                    hideFragment(mCurChildFragment);
                    bnv_fragment.setVisibility(View.VISIBLE);
                    bnv_fragment.getMenu().getItem(2).setChecked(true);
                    mCurChildFragment = mInvestFragmentThree;
                }else {
                    onDestroy();
                }
//                mFragmentStack.remove(mCurChildFragment);
//                mCurChildFragment = mFragmentStack.get(mFragmentStack.size() - 1);
                return true;
            }
        }
        return handle;
    }
    /*隐藏所有fragment*/
    private void hideAllFragment() {
        if (mInvestFragmentOne!=null){
            hideFragment(mInvestFragmentOne);
        }
        if (mInvestFragmentTwo!=null){
            hideFragment(mInvestFragmentTwo);
        }
        if (mInvestFragmentThree!=null){
            hideFragment(mInvestFragmentThree);
        }


    }

    private void initView(View v) {
        bnv_fragment = (BottomNavigationView) v.findViewById(R.id.bnv_fragment);
    }
    private class PinyinComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            String str1 = PinYinUtil.getPinYin(((UserNewBean) o1).getEmpname())
                    .toUpperCase();
            String str2 = PinYinUtil.getPinYin(((UserNewBean) o2).getEmpname())
                    .toUpperCase();
            return str1.compareTo(str2);
        }

    }


}
