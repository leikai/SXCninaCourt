package org.sxchinacourt.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.sxchinacourt.R;
import org.sxchinacourt.activity.AssignActivity;
import org.sxchinacourt.bean.DepositDataBean;
import org.sxchinacourt.widget.SwipeMenuView;

/**
 * @author lk
 */
public class SwipeMenuAdapter extends ListBaseAdapter<DepositDataBean> {

    public SwipeMenuAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_deposit_item_swipe;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        View contentView = holder.getView(R.id.rl_deposit_content_swipe);
        TextView InitiatorName = holder.getView(R.id.tv_InitiatorName_text_swipe);
        TextView RecipientName = holder.getView(R.id.tv_RecipientName_text_swipe);
        TextView address = holder.getView(R.id.tv_address_text_swipe);
        TextView fileState = holder.getView(R.id.tv_fileState_swipe);
        Button btnAssign = holder.getView(R.id.btnAssign);
        Button btnPickUp = holder.getView(R.id.btnPickUp);


        //这句话关掉IOS阻塞式交互效果 并依次打开左滑右滑
        ((SwipeMenuView)holder.itemView).setIos(false).setLeftSwipe( true);
        String initiatorName = getDataList().get(position).getInitiatorName();
        Log.e("initiatorName",""+initiatorName);
        InitiatorName.setText(getDataList().get(position).getInitiatorName());
        RecipientName.setText(getDataList().get(position).getFileNo());
        address.setText(getDataList().get(position).getAddress().substring(12));
        if (getDataList().get(position).getFileState()== 0){
            fileState.setText("未关门");
            fileState.setTextColor(mContext.getResources().getColor(R.color.header));
            btnAssign.setVisibility(View.VISIBLE);
            btnPickUp.setVisibility(View.GONE);
        }else if (getDataList().get(position).getFileState()== 1){
            fileState.setText("待指派");
            fileState.setTextColor(mContext.getResources().getColor(R.color.header));
            btnAssign.setVisibility(View.VISIBLE);
            btnPickUp.setVisibility(View.GONE);
        }else if (getDataList().get(position).getFileState()== 2){
            fileState.setText("流转中");
            fileState.setTextColor(mContext.getResources().getColor(R.color.header));
            btnAssign.setVisibility(View.VISIBLE);
            btnPickUp.setVisibility(View.GONE);
        }else if (getDataList().get(position).getFileState()== 3){
            fileState.setText("已取件");
            fileState.setTextColor(mContext.getResources().getColor(R.color.fileState));
            //隐藏控件
            btnAssign.setVisibility(View.VISIBLE);
            btnPickUp.setVisibility(View.GONE);

        }
        btnAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getDataList().get(position).getFileState()==0){
                    Toast.makeText(mContext,"未关门",Toast.LENGTH_SHORT).show();
                }else if (getDataList().get(position).getFileState()==1){
                    Intent jumptoAssign = new Intent(mContext,AssignActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("SerialNo",getDataList().get(position).getSerialNo());
                    Log.e("SerialNo",""+bundle);
                    jumptoAssign.putExtras(bundle);
                    jumptoAssign.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(jumptoAssign);
                }else if (getDataList().get(position).getFileState()==2){
                    Intent jumptoAssign = new Intent(mContext,AssignActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("SerialNo",getDataList().get(position).getSerialNo());
                    Log.e("SerialNo",""+bundle);
                    jumptoAssign.putExtras(bundle);
                    jumptoAssign.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(jumptoAssign);
                }
                else if (getDataList().get(position).getFileState()==3){
                    Toast.makeText(mContext,"已取件",Toast.LENGTH_SHORT).show();
                    Intent jumptoAssign = new Intent(mContext,AssignActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("SerialNo",getDataList().get(position).getSerialNo());
                    Log.e("SerialNo",""+bundle);
                    jumptoAssign.putExtras(bundle);
                    jumptoAssign.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(jumptoAssign);
                }
            }
        });

        //注意事项，设置item点击，不能对整个holder.itemView设置咯，只能对第一个子View，即原来的content设置，这算是局限性吧。
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick() called with: v = [" + v + "]");
            }
        });
    }
    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);

        void onTop(int pos);
    }

    private onSwipeListener mOnSwipeListener;

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }


}

