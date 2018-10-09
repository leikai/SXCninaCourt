package org.sxchinacourt.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.sxchinacourt.R;
import org.sxchinacourt.bean.TaskBean;

/**
 * Created by baggio on 2017/2/15.
 */

public class MsgLeaveAdapter extends BaseTasksAdatper {

    public MsgLeaveAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.msg_item, null);
            holder = new ViewHolder();
            holder.mTypeView = (TextView) view.findViewById(R.id.tv_taskType);
            holder.mTitleView = (TextView) view.findViewById(R.id.tv_taskTitle);
            holder.mOwnerView = (TextView) view.findViewById(R.id.tv_taskOwner);
            holder.mTimeView = (TextView) view.findViewById(R.id.tv_taskCreateTime);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        TaskBean task = mTasks.get(i);
        String taskType = task.getWftitle();
        if (taskType.contains("公告")) {
            holder.mTypeView.setBackgroundResource(R.drawable.tv_gonggao_bg);
            taskType = "公告";
        } else if (taskType.contains("审批")) {
            holder.mTypeView.setBackgroundResource(R.drawable.tv_shenpi_bg);
            taskType = "审批";
        } else if (taskType.contains("邮件")) {
            holder.mTypeView.setBackgroundResource(R.drawable.tv_youjian_bg);
            taskType = "邮件";
        } else {
            holder.mTypeView.setBackgroundResource(R.drawable.tv_renwu_bg);
            taskType = "任务";
        }
        String ceshiName  = task.getProcessGroup();
        Log.e("ceshiName",""+ceshiName);
        holder.mTypeView.setText(taskType);
        holder.mTitleView.setText(task.getTitle());
        holder.mOwnerView.setText(task.getOwnerName());
        holder.mTimeView.setText(task.getBeginTime());
        return view;
    }

    private class ViewHolder {
        TextView mTypeView;
        TextView mTitleView;
        TextView mOwnerView;
        TextView mTimeView;
    }
}
