package org.sxchinacourt.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.sxchinacourt.R;
import org.sxchinacourt.activity.UserDetailInfodActivity;
import org.sxchinacourt.activity.fragment.BaseFragment;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.util.BitmapUtil;

import java.util.List;

/**
 * @author lk
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{
    private Context mcontext;
    private List<UserNewBean> mUsersList;
    private BaseFragment mPreFragment;

    public UsersAdapter(List<UserNewBean> mUsersList, BaseFragment mPreFragment) {
        this.mUsersList = mUsersList;
        this.mPreFragment= mPreFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mcontext == null){
            mcontext = parent.getContext();
        }
        View view = LayoutInflater.from(mcontext).inflate(R.layout.user_item,parent,false);


        final ViewHolder holder = new ViewHolder(view);



        holder.llUserItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                UserNewBean user = mUsersList.get(position);
                Intent intent = new Intent(mcontext, UserDetailInfodActivity.class);
                intent.putExtra("user",user);
                mcontext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserNewBean userNewBean = mUsersList.get(position);
        holder.userName.setText(userNewBean.getEmpname());
        Bitmap bp = BitmapUtil.GetUserImageByNickName(mcontext,userNewBean.getEmpname());
        holder.userImage.setImageBitmap(bp);
//        Glide.with(mcontext).load(userNewBean.getImageId()).into(holder.userImage);
    }

    @Override
    public int getItemCount() {
        return mUsersList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout llUserItem;
        ImageView userImage;
        TextView userName;
        public ViewHolder(View view){
            super(view);
            llUserItem = view.findViewById(R.id.ll_user_item);
            userImage = view.findViewById(R.id.iv_head);
            userName = view.findViewById(R.id.tv_username);
        }
    }
}
