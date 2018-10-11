package org.sxchinacourt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import org.sxchinacourt.R;
import org.sxchinacourt.bean.DepartmentNewBean;

import java.util.List;

public class DepartmentAdapter extends BaseAdapter {
    private Context context;
    private List<DepartmentNewBean> list;

    public DepartmentAdapter(Context context, List<DepartmentNewBean> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = convertView.inflate(context, R.layout.adapter_department,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_section = (TextView) convertView.findViewById(R.id.tv_section);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_section.setText(list.get(position).getDeptName());
        return convertView;
    }

    class ViewHolder{
        private TextView tv_section;
    }
}
