package org.sxchinacourt.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.sxchinacourt.R;
import org.sxchinacourt.activity.AlreadyWebActivity;
import org.sxchinacourt.activity.NoticeWebActivity;
import org.sxchinacourt.activity.TotoWebActivity;
import org.sxchinacourt.activity.fragment.BaseFragment;
import org.sxchinacourt.activity.fragment.HomePageFragment;
import org.sxchinacourt.activity.fragment.HomePageManagerFragment;
import org.sxchinacourt.bean.AppBean;

import java.util.List;

/**
 *
 * @author baggio
 * @date 2017/2/3
 */

public class HomePageAdapter extends BaseAdapter {
    private final int TYPE_LEFT_ITEM = 0;
    private final int TYPE_RIGHT_ITEM = 1;
    private final int TYPE_COUNT = 2;
    private HomePageFragment mFragment;
    List<AppBean> mApps;
    private  Context context;
    public HomePageAdapter(Context context,HomePageFragment fragment, List<AppBean> apps) {
        this.context = context;
        this.mFragment = fragment;
        this.mApps = apps;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            int app_type = 0;
            AppBean app = (AppBean) view.getTag();
            switch (app.getFragmentIndex()) {
                case AppBean.FRAGMENT_TO_DO_TASK: {
                    app_type = HomePageManagerFragment.CHILD_TYPE_TODOTASKLIST;
                    context.startActivity(new Intent(context, TotoWebActivity.class));
                    break;
                }
                case AppBean.FRAGMENT_HISTORY_TASK: {
                    app_type = HomePageManagerFragment.CHILD_TYPE_HISTORYTASKLIST;
                    context.startActivity(new Intent(context, AlreadyWebActivity.class));
                    break;
                }
                case AppBean.FRAGMENT_NOTICE_TASK: {
                    app_type = HomePageManagerFragment.CHILD_TYPE_NOTICETASKLIST;
                    context.startActivity(new Intent(context, NoticeWebActivity.class));
                    break;
                }
                case AppBean.FRAGMENT_MANAGER_TASK: {
                    app_type = HomePageManagerFragment.CHILD_TYPE_TASKMANAGER;
                    bundle.putInt(BaseFragment.PARAM_CHILD_TYPE, app_type);
                    mFragment.showChildFragment(bundle);
                    break;
                }
                case AppBean.FRAGMENT_CABINET_TASK: {
                    app_type = HomePageManagerFragment.CHILD_TYPE_CABINETTASKLIST;
                    bundle.putInt(BaseFragment.PARAM_CHILD_TYPE, app_type);
                    mFragment.showChildFragment(bundle);
                    break;
                }
                case AppBean.FRAGMENT_MESSAGE_MACHINE: {
                    app_type = HomePageManagerFragment.CHILD_TYPE_MESSAGEMACHINE;
                    bundle.putInt(BaseFragment.PARAM_CHILD_TYPE, app_type);
                    mFragment.showChildFragment(bundle);
                    break;
                }
                default: {
                    Toast.makeText(mFragment.getContext(), "暂不支持该功能", Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }
    };

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return TYPE_LEFT_ITEM;
        }
        return TYPE_RIGHT_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getCount() {
        return mApps.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        AppViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mFragment.getContext()).inflate(R.layout.app_homepage_item, null,
                    false);
            holder = new AppViewHolder();
            holder.mContainerView = view.findViewById(R.id.home_page_view_ll_dbsy);
            holder.mAppNameView = (TextView) view.findViewById(R.id.tv_todo);
            holder.mAppIconView = (TextView) view.findViewById(R.id.tv_click);
            view.setTag(holder);
        } else {
            holder = (AppViewHolder) view.getTag();
        }
        AppBean app = mApps.get(i);
        holder.mContainerView.setTag(app);
        holder.mContainerView.setOnClickListener(mOnClickListener);
        holder.mAppNameView.setText(app.getName());
        holder.mContainerView.setBackgroundResource(app.getResourceId());
        return view;
    }

    class AppViewHolder {
        private View mContainerView;
        private TextView mAppNameView;
        private TextView mAppIconView;
    }
}
