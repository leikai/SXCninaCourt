package org.sxchinacourt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.sxchinacourt.R;
import org.sxchinacourt.bean.NewsBean;
import org.sxchinacourt.bean.ProcessOpinion;

import java.util.List;

/**
 * Created by 殇冰无恨 on 2018/1/5.
 */

public class ApprovalAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    // 映射数据<泛型bean>
    private List<ProcessOpinion> mDataList;

    public ApprovalAdapter(Context context, List<ProcessOpinion> list) {
        mLayoutInflater = LayoutInflater.from(context);
        mDataList = list;
    }
    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //用viewholder 储存 findviewbyid的值，避免重复，提高了效率。
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            // 只将XML转化为View，不涉及具体布局，第二个参数设null
            view = mLayoutInflater.inflate(R.layout.task_detailinfo_processopinion_item, null);
            holder.tv_stepname = (TextView) view
                    .findViewById(R.id.tv_stepname);
            holder.tv_createuser = (TextView) view
                    .findViewById(R.id.tv_createuser);
            holder.tv_createdate = (TextView) view
                    .findViewById(R.id.tv_createdate);
            holder.tv_menuname = (TextView) view
                    .findViewById(R.id.tv_menuname);
            holder.tv_opinion = (TextView) view
                    .findViewById(R.id.tv_opinion);
            //用tag储存viewholder,从而使convertView与holder关联
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // 取出bean对象
        ProcessOpinion bean = mDataList.get(i);
        // 设置控件的数据
        holder.tv_stepname.setText(bean.getStepName());
        holder.tv_createuser.setText(bean.getCreateUser());
        holder.tv_createdate.setText(bean.getCreateDate());
        holder.tv_menuname.setText(bean.getMenuName());
        holder.tv_opinion.setText(bean.getOpinion());

        return view;
    }
    // ViewHolder用于缓存控件
    class ViewHolder {
        public TextView tv_stepname;
        public TextView tv_createuser;
        public TextView tv_createdate;
        public TextView tv_menuname;
        public TextView tv_opinion;
    }
}
