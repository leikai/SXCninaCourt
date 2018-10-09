package org.sxchinacourt.adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.sxchinacourt.R;
import org.sxchinacourt.bean.TaskBean;

/**
 * Created by baggio on 2017/2/10.
 */

public class TasksAdapter extends BaseTasksAdatper {


    public TasksAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.task_item, null);
            holder = new ViewHolder();
            holder.mTitleView = (TextView) view.findViewById(R.id.task_title);
            holder.mOwnerView = (TextView) view.findViewById(R.id.task_owner);
            holder.mTimeView = (TextView) view.findViewById(R.id.task_createTime);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        TaskBean task = mTasks.get(i);
//        String taskType = "";// "[" + task.getOwner() + "]";
        String taskTitle = task.getTitle();
        SpannableStringBuilder style = new SpannableStringBuilder(taskTitle);
//        style.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), 0, taskType.length(),
//                Spannable
//                        .SPAN_EXCLUSIVE_INCLUSIVE);
        holder.mTitleView.setText(taskTitle);
        holder.mOwnerView.setText(task.getOwnerName());
        holder.mTimeView.setText(task.getBeginTime());
        return view;
    }

    private class ViewHolder {
        TextView mTitleView;
        TextView mOwnerView;
        TextView mTimeView;
    }
}
