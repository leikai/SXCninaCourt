package org.sxchinacourt.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sxchinacourt.R;
import org.sxchinacourt.activity.CreateTaskActivity;
import org.sxchinacourt.activity.TasksAssignedToMeActivity;
import org.sxchinacourt.activity.TasksICreatedActivity;

/**
 *
 * @author baggio
 * @date 2017/2/25
 */

public class TaskManagerFragment extends BaseFragment implements View.OnClickListener {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "任务管理";
        mShowBtnBack = View.VISIBLE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, R.layout.fragment_taskmanager, container,
                savedInstanceState);
    }

    @Nullable
    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.initFragment(container, savedInstanceState);
        mRootView.findViewById(R.id.createTask).setOnClickListener(this);
        mRootView.findViewById(R.id.taskICreated).setOnClickListener(this);
        mRootView.findViewById(R.id.taskAssignedToMe).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createTask: {
                Intent intent = new Intent();
                intent.setClass(getContext(), CreateTaskActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.taskICreated: {
                Intent intent = new Intent();
                intent.setClass(getContext(), TasksICreatedActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.taskAssignedToMe: {
                Intent intent = new Intent();
                intent.setClass(getContext(), TasksAssignedToMeActivity.class);
                startActivity(intent);
                break;
            }
            default:
                break;
        }

    }
}
