package org.sxchinacourt.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.sxchinacourt.R;
import org.sxchinacourt.bean.TaskICreatedBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baggio on 2017/2/16.
 */

public class TasksICreatedAdatper extends BaseAdapter {
    List<TaskICreatedBean> mTasks;
    Context mContext;

    public TasksICreatedAdatper(Context context) {
        this.mContext = context;
        mTasks = new ArrayList<>();
    }

    public void setTasks(List<TaskICreatedBean> mTasks) {
        this.mTasks = mTasks;
    }

    public void addTasks(List<TaskICreatedBean> tasks) {
        for (TaskICreatedBean task : tasks) {
            mTasks.add(task);
        }
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
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.taskicrated_item, null);
            holder = new ViewHolder();
            holder.mTitleView = (TextView) view.findViewById(R.id.task_title);
            holder.mOwnerView = (TextView) view.findViewById(R.id.task_owner);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        TaskICreatedBean task = mTasks.get(position);
        String taskType = "";// "[" + task.getOwner() + "]";
        String taskTitle = taskType + task.getTitle();
        SpannableStringBuilder style = new SpannableStringBuilder(taskTitle);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), 0, taskType.length(),
                Spannable
                        .SPAN_EXCLUSIVE_INCLUSIVE);
        holder.mTitleView.setText(style);
        holder.mOwnerView.setText("执行人：" + task.getNote());
        return view;
    }

    private class ViewHolder {
        TextView mTitleView;
        TextView mOwnerView;
    }
}
