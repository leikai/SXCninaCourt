package org.sxchinacourt.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sxchinacourt.R;

import java.util.Vector;

import static org.sxchinacourt.activity.fragment.TodoTaskListFragment.pageLocationForH5;

//import static org.sxchinacourt.activity.TabsActivity.pageLocationForH5;

public class HistoryTaskListManagerFragment extends BaseFragment {
    private MsgFragment mMsgFragment;

    private HistoryNewTaskListFragment mHistoryTaskListFragment;
    private HomePageManagerFragment mHomePageManagerFragment;

    private BaseFragment mCurChildFragment;
    private Vector<BaseFragment> mFragmentStack;
    public static final int CHILD_TYPE_MSG_TODO = 12;//消息

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentStack = new Vector<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, R.layout.fragment_msgmanager, container, savedInstanceState);
    }

    @Nullable
    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView(mRootView);
        initData();
        mChildType = CHILD_TYPE_MSG_TODO;
        showChildFragment(null);
    }
    @Override
    public void showChildFragment(Bundle bundle) {
        super.showChildFragment(bundle);
        switch (mChildType) {
            case CHILD_TYPE_MSG_TODO:
//                mMsgFragment = new MsgFragment(getActivity(), R.layout.fragment_msg);
//                mMsgFragment.setPreFragment(this);
//                addChildFragment(mMsgFragment, R.id.content);
//                mCurChildFragment = mMsgFragment;

                mHistoryTaskListFragment = new HistoryNewTaskListFragment(getActivity(), R.layout.fragment_msg);
                mHistoryTaskListFragment.setPreFragment(this);
                addChildFragment(mHistoryTaskListFragment, R.id.content);
                mCurChildFragment = mHistoryTaskListFragment;

                break;

        }
        mFragmentStack.add(mCurChildFragment);
    }


    @Override
    public boolean onBackPressed() {
        boolean handle = false;
        if (mFragmentStack.size() > 0) {
            handle = mCurChildFragment.onBackPressed();
            if (!handle) {
                if (mCurChildFragment instanceof HistoryNewTaskListFragment) {
                    hideAllFragment();
                    mHistoryTaskListFragment = null;
                    mFragmentStack.remove(mHistoryTaskListFragment);
                    mCurChildFragment = mFragmentStack.get(mFragmentStack.size() - 1);
                    if ("2".equals(pageLocationForH5)){
                        mHistoryTaskListFragment = new HistoryNewTaskListFragment(getActivity(), R.layout.fragment_invest_one);
                        mHistoryTaskListFragment.setPreFragment(this);
                        addChildFragment(mHistoryTaskListFragment,R.id.content);
                        mCurChildFragment = mHistoryTaskListFragment;
                        pageLocationForH5 = "1";
                    }else {
                        mHomePageManagerFragment = new HomePageManagerFragment();
                        mHomePageManagerFragment.setPreFragment(this);
                        addChildFragment(mHomePageManagerFragment,R.id.content);
                        mCurChildFragment = mHomePageManagerFragment;
                        pageLocationForH5 = "2";
                    }


                }
                return true;
            }
        }
        return handle;
    }


    /*当Fragment由可见状态变为不可见状态时保存Fragment的页面显示状态，以备再次加载时使用*/
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mCurChildFragment.onHiddenChanged(hidden);
    }

    /*隐藏所有fragment*/
    private void hideAllFragment() {
        if (mHistoryTaskListFragment!=null){
            hideFragment(mHistoryTaskListFragment);
        }
    }
    private void initView(View mRootView) {

    }


}
