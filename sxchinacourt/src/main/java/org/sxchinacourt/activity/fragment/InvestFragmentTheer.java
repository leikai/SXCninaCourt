package org.sxchinacourt.activity.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.adapter.InvestTheerAdapter;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.util.PinYin.Characters;
import org.sxchinacourt.util.PinYin.PinYinUtil;
import org.sxchinacourt.util.WebServiceUtil;
import org.sxchinacourt.widget.SideBar;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InvestFragmentTheer extends ContactsManagerFragment implements PullToRefreshBase
        .OnRefreshListener,View.OnClickListener, AdapterView.OnItemClickListener  {

    private Context context;
    private int resId;
    private PullToRefreshListView mContactsListView;     //自带刷新的listview
    private RefreshContactsTask mRefreshContactsTask;
    private List<UserNewBean> list;
    private InvestTheerAdapter adapter;
    private SideBar mSideBar;
    private TextView mSideBarTextView;
    private String token;
    public static List<UserNewBean> mUsers; // 联系人集合

    @SuppressLint({"NewApi", "ValidFragment"})
    public InvestFragmentTheer(){

    }
    @SuppressLint({"NewApi", "ValidFragment"})
    public InvestFragmentTheer(Context context, int resId){
        this.context = context;
        this.resId = resId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "人员";
        mShowBtnBack = View.INVISIBLE;
        mLogo = View.INVISIBLE;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_invest_theer1,container,false);

        return onCreateView(inflater, R.layout.fragment_invest_theer, container, savedInstanceState);
//        return view;
    }

    @Nullable
    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView(mRootView);
        initData();
    }

    private void initView(View v) {
        mContactsListView = (PullToRefreshListView) v.findViewById(R.id.pull_refresh_list);
        mSideBar = (SideBar) v.findViewById(R.id.sideBar);
        mSideBarTextView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.contactlist_index, null);
        mSideBarTextView.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Activity.WINDOW_SERVICE);
        windowManager.addView(mSideBarTextView, lp);
        mSideBar.setTextView(mSideBarTextView);
    }

    protected void initData() {
        token = CApplication.getInstance().getCurrentToken();
        String id = bean.getId();
        String name = bean.getName();
        mRefreshContactsTask = new RefreshContactsTask(null,id,name);
        mRefreshContactsTask.execute();

    }
    private void initEvent() {
        mContactsListView.setOnItemClickListener(this);
    }
    private void setDataToView() {
        adapter = new InvestTheerAdapter(context,list);
        mContactsListView.getRefreshableView().setAdapter(adapter);
        mSideBar.setListView(mContactsListView.getRefreshableView());
        initEvent();
    }


    private Handler mUpdateUsersListHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            list = (List<UserNewBean>) msg.obj;
            setDataToView();
            String departmentName = msg.getData().getString("departmentName");
            adapter.setContacts(list);
            adapter.notifyDataSetChanged();
            mSideBar.setVisibility(View.VISIBLE);
        }
    };


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UserNewBean user = (UserNewBean) adapter.getItem(position - mContactsListView
                .getRefreshableView()
                .getHeaderViewsCount());
        Bundle bundle = new Bundle();
        bundle.putInt(BaseFragment.PARAM_CHILD_TYPE, ContactsManagerFragment.CHILD_TYPE_USERINFO);
        bundle.putSerializable(UserDetailInfoFragment.PARAM_USER, user);
        if (mPreFragment == null){
            mPreFragment = (BaseFragment) getParentFragment();

        }
        mPreFragment.showChildFragment(bundle);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {

    }

    //**    // 请求 所有联系人
    private class RefreshContactsTask extends AsyncTask<Void, Void, List<UserNewBean>> {

        String mUserName;
        String mDepartmentId;
        String mDepartmentName;

        //        long time;
        public RefreshContactsTask(String userName, String departmentId, String departmentName) {
            mUserName = userName;
            mDepartmentId = departmentId;
            mDepartmentName = departmentName;
        }

        @Override
        protected void onPreExecute() {
            // mCustomProgress.start();
        }

        @Override
        protected List<UserNewBean> doInBackground(Void... params) {
            List<UserNewBean> users = WebServiceUtil.getInstance().getNewUserList(token,mDepartmentId);
            if (users != null) {   //如果得到的数据不是空的
                Collections.sort(users, new PinyinComparator());   //联系人按照顺序排序
            }
            return users;
        }

        @Override
        protected void onPostExecute(List<UserNewBean> users) {
            // mRefreshContactsTask = null;
            // mCustomProgress.stop();
            if (users != null) {
                Message msg = mUpdateUsersListHandler.obtainMessage();
                msg.obj = users;
                Bundle bundle = new Bundle();
                bundle.putString("departmentName", mDepartmentName);
                msg.setData(bundle);
                mUpdateUsersListHandler.sendMessage(msg);
            }
        }

        @Override
        protected void onCancelled() {
            // mRefreshContactsTask = null;
        }
    }
    private class PinyinComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            String str1 = PinYinUtil.getPinYin(((UserNewBean) o1).getEmpname())
                    .toUpperCase();
            String str2 = PinYinUtil.getPinYin(((UserNewBean) o2).getEmpname())
                    .toUpperCase();
            return str1.compareTo(str2);
        }

    }
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            try {
                if (mUsers != null && mUsers.size() > 0) {
                    List<UserNewBean> users = mUsers;
                    if (s.length() > 0) {
                        for (int i = 0; i < s.length(); i++) {
                            Characters characters1 = new Characters(s.toString().substring(i, i + 1));
                            char c = Character.toUpperCase(characters1.getFistChar());
                            List<UserNewBean> tUsers = new ArrayList<UserNewBean>();
                            for (UserNewBean user : users) {
                                try {
                                    if (user.getUserName().length() >= s.length()) {
                                        Characters characters2 = new Characters(user.getUserName()
                                                .substring(i, i + 1));
                                        if (Character.toUpperCase(characters2
                                                .getFistChar()) == c) {
                                            tUsers.add(user);
                                        }
                                    }
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            }
                            users = tUsers;
                        }
                    }
                    adapter.setContacts(users);
                    adapter.notifyDataSetChanged();
                    mContactsListView.getRefreshableView().setSelection(0);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };


    public InvestFragmentOne.FragmentCallBack mCallBack;

    public void setFragmentCallBack(InvestFragmentOne.FragmentCallBack mCallBack){
        this.mCallBack = mCallBack;
    }

    public interface FragmentCallBack{
        void onClick(String str);
    }


}
