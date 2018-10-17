package org.sxchinacourt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.sxchinacourt.R;
import org.sxchinacourt.bean.NewsBean;

import java.util.List;

/**
 *
 * @author 殇冰无恨
 * @date 2017/9/28
 */

public class NewsAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    /**
     * 映射数据<泛型bean>
     */
    private List<NewsBean> mDataList;

    public NewsAdapter(Context context, List<NewsBean> list) {
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
            view = mLayoutInflater.inflate(R.layout.application_news_item, null);
            holder.title = (TextView) view
                    .findViewById(R.id.news_title);
            holder.content = (TextView) view
                    .findViewById(R.id.news_content);
            //用tag储存viewholder,从而使convertView与holder关联
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // 取出bean对象
        NewsBean bean = mDataList.get(i);
        // 设置控件的数据
        holder.title.setText(bean.getFtitle());
        holder.content.setText(bean.getKeyword());
        return view;
    }

    /**
     * ViewHolder用于缓存控件
     */
    class ViewHolder {
        public TextView title;
        public TextView content;
    }
}
