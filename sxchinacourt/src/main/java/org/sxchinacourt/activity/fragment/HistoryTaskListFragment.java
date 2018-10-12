package org.sxchinacourt.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.sxchinacourt.R;
import org.sxchinacourt.activity.HistoryTaskDetailInfoActivity;
import org.sxchinacourt.activity.TaskDetailInfoActivity;
import org.sxchinacourt.adapter.TasksAdapter;
import org.sxchinacourt.bean.TaskBean;

/**
 *
 * @author baggio
 * @date 2017/2/7
 */

public class HistoryTaskListFragment extends BaseTasksFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "已办列表";
        mShowBtnBack = View.VISIBLE;
        mShowSearchBar = View.GONE;
        mTaskType = 0;
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
        bundle.putSerializable(TaskDetailInfoActivity.PARAM_TASK, task);
        Intent intent = new Intent();
        intent.setClass(getContext(), HistoryTaskDetailInfoActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_selectgroup) {
//            showGroupPopUpWindow(mRootView.findViewById(R.id.groups_container));
        }
    }
}
