package org.sxchinacourt.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.sxchinacourt.R;
import org.sxchinacourt.adapter.TasksAdapter;
import org.sxchinacourt.bean.TaskBean;

/**
 * Created by baggio on 2017/2/7.
 */

public class NoticeTaskListFragment extends BaseTasksFragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "公告列表";
        mTaskType = 2;
        mShowBtnBack = View.VISIBLE;
        mShowSearchBar = View.GONE;
    }

    @Nullable
    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.initFragment(container, savedInstanceState);
        mRootView.findViewById(R.id.btn_selectgroup).setOnClickListener(this);
        mListView.setOnItemClickListener(this);
    }

    protected void initData() {
        mAdapter = new TasksAdapter(getContext());
        super.initData();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TaskBean task = (TaskBean) mAdapter.getItem(i - mListView.getRefreshableView().getHeaderViewsCount());
        Bundle bundle = new Bundle();
        bundle.putInt(BaseFragment.PARAM_CHILD_TYPE, AppsManagerFragment.CHILD_TYPE_NOTICE_DETAIL);
        bundle.putSerializable(NoticeDetailFragment.PARAM_TASK, task);
        mPreFragment.showChildFragment(bundle);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_selectgroup) {
//            showGroupPopUpWindow(mRootView.findViewById(R.id.groups_container));
        }
    }

    @Override
    public void showChildFragment(Bundle bundle) {
        mPreFragment.showChildFragment(bundle);
    }

}
