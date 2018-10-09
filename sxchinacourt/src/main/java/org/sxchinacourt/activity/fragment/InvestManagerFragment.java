package org.sxchinacourt.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sxchinacourt.R;

import java.util.Vector;

/**
 * Created by 殇冰无恨 on 2017/12/9.
 */

public class InvestManagerFragment extends BaseFragment {
    public static final int CHILD_TYPE_APP = 0;
    public static final int CHILD_TYPE_TODOTASKLIST = 1;
    public static final int CHILD_TYPE_NOTICETASKLIST = 2;
    public static final int CHILD_TYPE_NOTICE_DETAIL = 3;
    public static final int CHILD_TYPE_HISTORYTASKLIST = 4;
    public static final int CHILD_TYPE_TASKMANAGER = 5;

    private InvestFragment mInvestFragment;
    private InvestFragmentOne mInvestFragmentOne;
    private HistoryTaskListFragment mHistoryTaskListFragment;
    private NoticeTaskListFragment mNoticeTaskListFragment;
    private NoticeDetailFragment mNoticeDetailFragment;
    private TaskManagerFragment mTaskManagerFragment;
    private BaseFragment mCurChildFragment;
    private Vector<BaseFragment> mFragmentStack;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentStack = new Vector<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCreateView(inflater, R.layout.fragment_appsmanager, container, savedInstanceState);
    }

    @Nullable
    @Override
    public void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mChildType = CHILD_TYPE_APP;
        showChildFragment(null);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mCurChildFragment.onHiddenChanged(hidden);
    }

    @Override
    public void showChildFragment(Bundle bundle) {
        super.showChildFragment(bundle);
        switch (mChildType) {
            case CHILD_TYPE_APP:
                if (mInvestFragment == null) {
                    mInvestFragment = new InvestFragment();
                    mInvestFragment.setPreFragment(this);
                }
                addChildFragment(mInvestFragment, R.id.content);
                mCurChildFragment = mInvestFragment;
                break;

            case CHILD_TYPE_TODOTASKLIST:
//                if (mInvestFragmentOne == null) {
//                    mInvestFragmentOne = new InvestFragmentOne();
//                    mInvestFragmentOne.setPreFragment(mInvestFragment);
//                }
//                mInvestFragmentOne.setArguments(bundle);
//                addChildFragment(mInvestFragmentOne, R.id.content);
//                hideFragment(mInvestFragment);
//                mCurChildFragment = mInvestFragmentOne;
                break;
            case CHILD_TYPE_HISTORYTASKLIST:
                if (mHistoryTaskListFragment == null) {
                    mHistoryTaskListFragment = new HistoryTaskListFragment();
                    mHistoryTaskListFragment.setPreFragment(mInvestFragment);
                }
                mHistoryTaskListFragment.setArguments(bundle);
                addChildFragment(mHistoryTaskListFragment, R.id.content);
                hideFragment(mInvestFragment);
                mCurChildFragment = mHistoryTaskListFragment;
                break;
            case CHILD_TYPE_NOTICETASKLIST:
                if (mNoticeTaskListFragment == null) {
                    mNoticeTaskListFragment = new NoticeTaskListFragment();
                    mNoticeTaskListFragment.setPreFragment(mInvestFragment);
                }
                mNoticeTaskListFragment.setArguments(bundle);
                addChildFragment(mNoticeTaskListFragment, R.id.content);
                hideFragment(mInvestFragment);
                mCurChildFragment = mNoticeTaskListFragment;
                break;
            case CHILD_TYPE_NOTICE_DETAIL:
                if (mNoticeDetailFragment == null) {
                    mNoticeDetailFragment = new NoticeDetailFragment();
                    mNoticeDetailFragment.setPreFragment(mNoticeTaskListFragment);
                }
                mNoticeDetailFragment.setArguments(bundle);
                addChildFragment(mNoticeDetailFragment, R.id.content);
                hideFragment(mNoticeTaskListFragment);
                mCurChildFragment = mNoticeDetailFragment;
                break;
            case CHILD_TYPE_TASKMANAGER:
                if (mTaskManagerFragment == null) {
                    mTaskManagerFragment = new TaskManagerFragment();
                    mTaskManagerFragment.setPreFragment(mInvestFragment);
                }
                addChildFragment(mTaskManagerFragment, R.id.content);
                hideFragment(mInvestFragment);
                mCurChildFragment = mTaskManagerFragment;
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
                if (mCurChildFragment instanceof TodoTaskListFragment) {
                    mInvestFragmentOne = null;
                } else if (mCurChildFragment instanceof HistoryTaskListFragment) {
                    mHistoryTaskListFragment = null;
                } else if (mCurChildFragment instanceof NoticeTaskListFragment) {
                    mNoticeTaskListFragment = null;
                } else if (mCurChildFragment instanceof NoticeDetailFragment) {
                    mNoticeDetailFragment = null;
                } else if (mCurChildFragment instanceof TaskManagerFragment) {
                    mTaskManagerFragment = null;
                }
                mFragmentStack.remove(mCurChildFragment);
                mCurChildFragment = mFragmentStack.get(mFragmentStack.size() - 1);
                return true;
            }
        }
        return handle;
    }
}
