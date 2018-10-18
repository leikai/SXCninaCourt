package org.sxchinacourt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.sxchinacourt.R;
import org.sxchinacourt.bean.DepartmentForCabinetBean;

import java.util.List;

/**
 *
 * @author 殇冰无恨
 * @date 2017/10/11
 */

public class DepartmentForCabinetAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    /**
     * 映射数据<泛型bean>
     */
    private List<DepartmentForCabinetBean> mDataList;

    public DepartmentForCabinetAdapter(Context context, List<DepartmentForCabinetBean> list) {
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
            view = mLayoutInflater.inflate(R.layout.department_item, null);
            holder.content = (TextView) view
                    .findViewById(R.id.tv_item_department);
            //用tag储存viewholder,从而使convertView与holder关联
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // 取出bean对象
        DepartmentForCabinetBean bean = mDataList.get(i);
        // 设置控件的数据
        holder.content.setText(bean.getDepartmentName());
        return view;
    }

    /**
     * ViewHolder用于缓存控件
     */
    class ViewHolder {
        public TextView content;
    }
}
