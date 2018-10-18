package org.sxchinacourt.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.util.BitmapUtil;

import java.util.List;

/**
 * 角色切换适配器
 * @author lk
 */
public class UsersRelationAdapter extends RecyclerView.Adapter<UsersRelationAdapter.ViewHolder> {
    /**
     * 角色数据列表
     */
    private List<UserNewBean> mRelationList;
    /**
     * 获取SwitchAccountActivity的上下文
     */
    private Context context;
    static class ViewHolder extends RecyclerView.ViewHolder{
        /**
         * RecyclerView的Item整体
         */
        ConstraintLayout llItemRelation;
        /**
         * 角色头像（或人员头像）
         */
        ImageView relationImage;
        /**
         * 角色信息（或人员信息）
         */
        CheckedTextView relationName;
        public ViewHolder(View itemView) {
            super(itemView);
            llItemRelation = (ConstraintLayout) itemView.findViewById(R.id.view_item_relation);
            relationImage = (ImageView) itemView.findViewById(R.id.item_weidoor_imageview);
            relationName = (CheckedTextView) itemView.findViewById(R.id.item_weidoor_title);
        }
    }

    public UsersRelationAdapter(Context context, List<UserNewBean> mRelationList) {
        this.context = context;
        this.mRelationList = mRelationList;
    }
    /**
     * Item的回调接口
     *
     */
    public interface OnItemClickListener {
        /**
         *RecyclerView的Item点击事件的接口
         * @param view
         * @param position
         */
        void onItemClickListener(View view, int position);
    }

    /**
     * 点击Item的回调对象
     */
    private OnItemClickListener listener;

    /**
     * 设置回调监听
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     *创建ViewHolder的实例
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users_relation,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    /**
     * 用于对RecyclerView子项的数据进行赋值
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
               UserNewBean userCurrent = mRelationList.get(position);

        Bitmap bp1 = BitmapUtil.GetUserImageByNickName(context, userCurrent.getUserName());
        holder.relationImage.setImageBitmap(bp1);
        UserNewBean currentUser = CApplication.getInstance().getCurrentUser();
        //如果有与主角色（当前登录人的角色）相同的部门的话，显示登录人的名字，如果部门名称不相同的话显示部门名称，并在当前登录人后面打√
        if (currentUser.getDeptname().equals(userCurrent.getDeptname())){
            holder.relationName.setText(userCurrent.getEmpname());
        }else {
            holder.relationName.setText(userCurrent.getDeptname());
        }
        if (currentUser.getOid().equals(userCurrent.getOid())){
            holder.relationName.setChecked(true);
        }else {
            holder.relationName.setChecked(false);
        }

        if (listener != null) {
            holder.llItemRelation.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onItemClickListener(v, position);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mRelationList.size();
    }
}
