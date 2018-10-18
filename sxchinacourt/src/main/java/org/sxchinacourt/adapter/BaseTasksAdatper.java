package org.sxchinacourt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.sxchinacourt.bean.TaskBean;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author baggio
 * @date 2017/2/16
 */

public class BaseTasksAdatper extends BaseAdapter {
    List<TaskBean> mTasks;
    Context mContext;

    public BaseTasksAdatper(Context context) {
        this.mContext = context;
        mTasks = new ArrayList<>();
    }

    public void setTasks(List<TaskBean> mTasks) {
        this.mTasks = mTasks;
    }

    public void addTasks(List<TaskBean> tasks) {
        for (TaskBean task : tasks) {
            mTasks.add(task);
        }
    }

    public void removeTask(TaskBean _task) {
        for (TaskBean task : mTasks) {
            if (task.getTaskId() == _task.getTaskId()) {
                mTasks.remove(task);
                break;
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mTasks.size();
    }

    @Override
    public Object getItem(int position) {
        return mTasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
