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

public class HomePageManagerFragment extends BaseFragment {
    public static final int CHILD_TYPE_APP = 0;
    public static final int CHILD_TYPE_TODOTASKLIST = 1;
    public static final int CHILD_TYPE_NOTICETASKLIST = 2;
    public static final int CHILD_TYPE_NOTICE_DETAIL = 3;
    public static final int CHILD_TYPE_HISTORYTASKLIST = 4;
    public static final int CHILD_TYPE_TASKMANAGER = 5;
    public static final int CHILD_TYPE_CABINETTASKLIST = 6;//云柜
    public static final int CHILD_TYPE_MESSAGEMACHINE = 7;//留言机
    private HomePageFragment mHomePageFragment;


    private TodoTaskListFragment mTodoTaskListFragment;
    private MsgManagerFragment msgManagerFragment;
    private TodaTaskListManagerFragment mTodaTaskListManagerFragment;//待办模块

    private HistoryTaskListManagerFragment mHistoryTaskListManagerFragment;//已办模块

    private NoticeDetailManagerFragment mNoticeDetailManagerFragment;//通知公告

    private HistoryTaskListFragment mHistoryTaskListFragment;
    private NoticeTaskListFragment mNoticeTaskListFragment;
    private NoticeDetailFragment mNoticeDetailFragment;
    private TaskManagerFragment mTaskManagerFragment;
    private BaseFragment mCurChildFragment;
    private Vector<BaseFragment> mFragmentStack;
    private CabinetTaskListFragment mCabinetTaskListFragment;//云柜
    private MessageMachineFragment mMessageMachineFragment;//留言机

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
                if (mHomePageFragment == null) {
                    mHomePageFragment = new HomePageFragment();
                    mHomePageFragment.setPreFragment(this);
                }
                addChildFragment(mHomePageFragment, R.id.content);
                mCurChildFragment = mHomePageFragment;
                break;

            case CHILD_TYPE_TODOTASKLIST:
//                if (mTodoTaskListFragment == null) {
//                    mTodoTaskListFragment = new TodoTaskListFragment();
//                    mTodoTaskListFragment.setPreFragment(mHomePageFragment);
//                }
//                mTodoTaskListFragment.setArguments(bundle);
//                addChildFragment(mTodoTaskListFragment, R.id.content);
//                hideFragment(mHomePageFragment);
//                mCurChildFragment = mTodoTaskListFragment;

//                if (msgManagerFragment == null) {
//                    msgManagerFragment = new MsgManagerFragment();
//                    msgManagerFragment.setPreFragment(mHomePageFragment);
//                }
//                msgManagerFragment.setArguments(bundle);
//                addChildFragment(msgManagerFragment, R.id.content);
//                hideFragment(mHomePageFragment);
//                mCurChildFragment = msgManagerFragment;


                if (mTodaTaskListManagerFragment == null) {
                    mTodaTaskListManagerFragment = new TodaTaskListManagerFragment();
                    mTodaTaskListManagerFragment.setPreFragment(mHomePageFragment);
                }
                mTodaTaskListManagerFragment.setArguments(bundle);
                addChildFragment(mTodaTaskListManagerFragment, R.id.content);
                hideFragment(mHomePageFragment);
                mCurChildFragment = mTodaTaskListManagerFragment;



                break;
            case CHILD_TYPE_HISTORYTASKLIST:
//                if (mHistoryTaskListFragment == null) {
//                    mHistoryTaskListFragment = new HistoryTaskListFragment();
//                    mHistoryTaskListFragment.setPreFragment(mHomePageFragment);
//                }
//                mHistoryTaskListFragment.setArguments(bundle);
//                addChildFragment(mHistoryTaskListFragment, R.id.content);
//                hideFragment(mHomePageFragment);
//                mCurChildFragment = mHistoryTaskListFragment;

                if (mHistoryTaskListManagerFragment == null) {
                    mHistoryTaskListManagerFragment = new HistoryTaskListManagerFragment();
                    mHistoryTaskListManagerFragment.setPreFragment(mHomePageFragment);
                }
                mHistoryTaskListManagerFragment.setArguments(bundle);
                addChildFragment(mHistoryTaskListManagerFragment, R.id.content);
                hideFragment(mHomePageFragment);
                mCurChildFragment = mHistoryTaskListManagerFragment;


                break;
            case CHILD_TYPE_NOTICETASKLIST:
//                if (mNoticeTaskListFragment == null) {
//                    mNoticeTaskListFragment = new NoticeTaskListFragment();
//                    mNoticeTaskListFragment.setPreFragment(mHomePageFragment);
//                }
//                mNoticeTaskListFragment.setArguments(bundle);
//                addChildFragment(mNoticeTaskListFragment, R.id.content);
//                hideFragment(mHomePageFragment);
//                mCurChildFragment = mNoticeTaskListFragment;


                if (mNoticeDetailManagerFragment == null) {
                    mNoticeDetailManagerFragment = new NoticeDetailManagerFragment();
                    mNoticeDetailManagerFragment.setPreFragment(mHomePageFragment);
                }
                mNoticeDetailManagerFragment.setArguments(bundle);
                addChildFragment(mNoticeDetailManagerFragment, R.id.content);
                hideFragment(mHomePageFragment);
                mCurChildFragment = mNoticeDetailManagerFragment;




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
                    mTaskManagerFragment.setPreFragment(mHomePageFragment);
                }
                addChildFragment(mTaskManagerFragment, R.id.content);
                hideFragment(mHomePageFragment);
                mCurChildFragment = mTaskManagerFragment;
                break;
            case CHILD_TYPE_CABINETTASKLIST:
                if (mCabinetTaskListFragment == null) {
                    mCabinetTaskListFragment = new CabinetTaskListFragment();
                    mCabinetTaskListFragment.setPreFragment(mHomePageFragment);
                }
                addChildFragment(mCabinetTaskListFragment, R.id.content);
                hideFragment(mHomePageFragment);
                mCurChildFragment = mCabinetTaskListFragment;
                break;
            case CHILD_TYPE_MESSAGEMACHINE:
                if (mMessageMachineFragment == null) {
                    mMessageMachineFragment = new MessageMachineFragment();
                    mMessageMachineFragment.setPreFragment(mHomePageFragment);
                }
                addChildFragment(mMessageMachineFragment, R.id.content);
                hideFragment(mHomePageFragment);
                mCurChildFragment = mMessageMachineFragment;
                break;
        }
        mFragmentStack.add(mCurChildFragment);
    }

    @Override
    public boolean onBackPressed() {
        boolean handle = false;
        if (mFragmentStack.size() > 1) {
            if (mCurChildFragment instanceof TodaTaskListManagerFragment) {
                mTodaTaskListManagerFragment = null;
            }
            handle = mCurChildFragment.onBackPressed();
            if (!handle) {
//                if (mCurChildFragment instanceof TodoTaskListFragment) {
//                    mTodoTaskListFragment = null;
//                }
                if (mCurChildFragment instanceof HistoryTaskListManagerFragment) {
                    mHistoryTaskListManagerFragment = null;
                } else if (mCurChildFragment instanceof NoticeDetailManagerFragment) {
                    mNoticeDetailManagerFragment = null;
                } else if (mCurChildFragment instanceof NoticeDetailFragment) {
                    mNoticeDetailFragment = null;
                } else if (mCurChildFragment instanceof TaskManagerFragment) {
                    mTaskManagerFragment = null;
                }else if (mCurChildFragment instanceof CabinetTaskListFragment) {
                    mCabinetTaskListFragment = null;
                }else if (mCurChildFragment instanceof MessageMachineFragment) {
                    mMessageMachineFragment = null;
                }
                mFragmentStack.remove(mCurChildFragment);
                mCurChildFragment = mFragmentStack.get(mFragmentStack.size() - 1);
                return true;
            }
        }
        return handle;
    }
}
