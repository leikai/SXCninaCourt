package org.sxchinacourt.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.sxchinacourt.R;
import org.sxchinacourt.activity.NewsActivity;
import org.sxchinacourt.bean.NewsBean;

import java.util.List;

/**
 * 新闻列表适配器
 * @author 殇冰无恨
 * @date 2017/12/4
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {
    private List<NewsBean> mFruitList;
    private Context context;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        private final TextView info;

        public ViewHolder(View itemView) {
            super(itemView);
            title =  itemView.findViewById(R.id.news_title);
            info = itemView.findViewById(R.id.news_content);
        }
    }

    public NewsListAdapter(Context context, List<NewsBean> mFruitList) {
        this.context = context;
        this.mFruitList = mFruitList;
    }

    /**
     *  创建ViewHolder的实例
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_news_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent jumptoNews = new Intent(context, NewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("oid",mFruitList.get((int) holder.itemView.getTag()).getOid());
                jumptoNews.putExtras(bundle);
                context.startActivity(jumptoNews);
            }
        });
        return holder;
    }
    /**
     * 用于对RecyclerView子项的数据进行赋值
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(mFruitList.get(position).getFtitle());
        holder.info.setText(mFruitList.get(position).getKeyword());
        holder.itemView.setTag(position);
    }


    @Override
    public int getItemCount() {
        return mFruitList.size();
    }
}
