package org.sxchinacourt.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sxchinacourt.R;
import org.sxchinacourt.activity.WeidoorActivity;
import org.sxchinacourt.bean.Fruit;

import java.util.List;

/**
 * Created by 殇冰无恨 on 2017/12/4.
 */

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {
    private List<Fruit> mFruitList;
    private Context context;
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout llItemWeidoor;
        ImageView fruitImage;
        TextView fruitName;
        public ViewHolder(View itemView) {
            super(itemView);
            llItemWeidoor = (LinearLayout) itemView.findViewById(R.id.view_item_weidoor);
            fruitImage = (ImageView) itemView.findViewById(R.id.item_weidoor_imageview);
            fruitName = (TextView) itemView.findViewById(R.id.item_weidoor_title);
        }
    }

    public FruitAdapter(Context context, List<Fruit> mFruitList) {
        this.context = context;
        this.mFruitList = mFruitList;
    }

    /*
    创建ViewHolder的实例
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weidoor,parent,false);
        ViewHolder holder = new ViewHolder(view);
        holder.llItemWeidoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WeidoorActivity.class);
                context.startActivity(intent);
            }
        });
        return holder;
    }
    /*
    用于对RecyclerView子项的数据进行赋值
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Fruit fruit = mFruitList.get(position);
        holder.fruitImage.setImageResource(fruit.getImageId());
        holder.fruitName.setText(fruit.getName());
    }


    @Override
    public int getItemCount() {
        return mFruitList.size();
    }
}
