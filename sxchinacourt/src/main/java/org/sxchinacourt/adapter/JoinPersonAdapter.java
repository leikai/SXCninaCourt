package org.sxchinacourt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.sxchinacourt.R;
import org.sxchinacourt.bean.DepartmentBean;
import org.sxchinacourt.bean.UserBean;
import org.sxchinacourt.widget.SelectContactsDialog;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author baggio
 * @date 2017/2/21
 */

public class JoinPersonAdapter extends BaseAdapter {
    final int TYPE_DEPARTMENT = 0;
    final int TYPE_USER = 1;
    final int TYPE_COUNT = 2;
    List<DepartmentBean> mDepartments;
    Context mContext;
    SelectContactsDialog.ExpandUserListListener mExpandUserListener;
    SelectContactsDialog.UsersSelectedListener mUsersSelectedListener;

    public JoinPersonAdapter(Context context, SelectContactsDialog.ExpandUserListListener
            eListener, SelectContactsDialog.UsersSelectedListener usListener) {
        this.mContext = context;
        mDepartments = new ArrayList<>();
        mExpandUserListener = eListener;
        mUsersSelectedListener = usListener;
    }

    public void setDepartments(List<DepartmentBean> mDepartments) {
        this.mDepartments = mDepartments;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        int type = TYPE_DEPARTMENT;
        int i = 0;
        for (DepartmentBean department : mDepartments) {
            if (position == i) {
                type = TYPE_DEPARTMENT;
                break;
            }
            i++;
            if (department.isUserListIsExpanded()) {
                i += department.getUsers().size();
                if (position < i) {
                    type = TYPE_USER;
                    break;
                }
            }
        }
        return type;
    }

    @Override
    public int getCount() {
        int i = 0;
        for (DepartmentBean department : mDepartments) {
            i++;
            if (department.isUserListIsExpanded()) {
                i += department.getUsers().size();
            }
        }
        return i;
    }

    @Override
    public Object getItem(int position) {
        Object obj = null;
        int i = 0;
        for (DepartmentBean department : mDepartments) {
            if (position == i) {
                obj = department;
                break;
            }
            i++;
            if (department.isUserListIsExpanded()) {
                i += department.getUsers().size();
                if (position < i) {
                    int index = department.getUsers().size() - (i - position);
                    obj = department.getUsers().get(index);
                    break;
                }
            }
        }
        return obj;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            if (type == TYPE_DEPARTMENT) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout
                        .joinpersion_department_item, null);
                holder.tv_departmentName = (TextView) convertView.findViewById(R.id.tv_department);
                holder.checkBox = (ImageView) convertView.findViewById(R.id.cb_selectAll);
                holder.btnExpand = (ImageView) convertView.findViewById(R.id.btn_expand);
            } else if (type == TYPE_USER) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout
                        .joinpersion_user_item, null);
                holder.tv_userName = (TextView) convertView.findViewById(R.id.tv_userName);
                holder.checkBox = (ImageView) convertView.findViewById(R.id.cb_checked);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Object obj = getItem(position);
        if (obj instanceof DepartmentBean) {
            DepartmentBean department = (DepartmentBean) obj;
            holder.btnExpand.setTag(department);
            holder.btnExpand.setOnClickListener(mBinExpandClickListener);
            if (department.isUserListIsExpanded()) {
                convertView.setBackgroundResource(R.drawable.blue_rect_bg);
                holder.tv_departmentName.setTextColor(0xffffffff);
            } else {
                convertView.setBackgroundResource(R.drawable.white_rect_bg);
                holder.tv_departmentName.setTextColor(0xff1e1e1e);
            }
            holder.tv_departmentName.setText(department.getDepartmentName());
            holder.checkBox.setSelected(department.isSelectAll());
        } else if (obj instanceof UserBean) {
            UserBean user = (UserBean) obj;
            holder.tv_userName.setText(user.getUserName());
            holder.checkBox.setSelected(user.isChecked());
        }
        holder.checkBox.setTag(obj);
        holder.checkBox.setOnClickListener(mCheckBoxClickListener);
        return convertView;
    }


    private View.OnClickListener mBinExpandClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DepartmentBean departmentBean = (DepartmentBean) v.getTag();
            if (departmentBean.isUserListIsExpanded()) {
                departmentBean.setUserListIsExpanded(false);
                notifyDataSetChanged();
            } else if (departmentBean.getUsers() != null) {
                departmentBean.setUserListIsExpanded(true);
                notifyDataSetChanged();
            } else {
                mExpandUserListener.expandUserList(departmentBean);
            }
        }
    };

    private View.OnClickListener mCheckBoxClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object obj = v.getTag();
            if (obj instanceof DepartmentBean) {
                DepartmentBean department = (DepartmentBean) obj;
                if (department.getUsers() != null) {
                    mUsersSelectedListener.usersSelected(department);
                }
            } else if (obj instanceof UserBean) {
                UserBean user = (UserBean) obj;
                mUsersSelectedListener.userSelected(user);
            }
            notifyDataSetChanged();
        }
    };

    private class ViewHolder {
        TextView tv_departmentName;
        TextView tv_userName;
        ImageView checkBox;
        ImageView btnExpand;
    }
}
