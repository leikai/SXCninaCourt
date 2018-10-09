package org.sxchinacourt.activity.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.sxchinacourt.R;
import org.sxchinacourt.adapter.ContactsAdapter;
import org.sxchinacourt.bean.DepartmentBean;
import org.sxchinacourt.bean.UserBean;
import org.sxchinacourt.util.PinYin.Characters;
import org.sxchinacourt.util.PinYin.PinYinUtil;
import org.sxchinacourt.util.WebServiceUtil;
import org.sxchinacourt.widget.CustomProgress;
import org.sxchinacourt.widget.SideBar;

import java.text.CollationKey;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by baggio on 2017/2/3.
 */

public class ContactsFragment extends BaseFragment implements PullToRefreshBase
        .OnRefreshListener,View.OnClickListener, AdapterView.OnItemClickListener {

    /*
文字图片的颜色值
*/
    public static final String[] ImageBgColor={
            "#568AAD",
            "#17c295",
            "#4DA9EB",
            "#F2725E",
            "#B38979",
            "#568AAD"
    };

    private PullToRefreshListView mContactsListView;     //自带刷新的listview
    private RefreshContactsTask mRefreshContactsTask;   //获取联系人的请求
    //private RefreshGroupTask mRefreshGroupTask;  //获取联系人分组的请求
    private ContactsAdapter mContactsAdapter;    //通讯录中间显示联系人的adapter
    private CustomProgress mCustomProgress;     //进度条
    private SideBar mSideBar;
    private TextView mSideBarTextView;
    private TextView mGroupNameView;
    private EditText mKeywordView;
    private List<DepartmentBean> mDepartments;//联系人分组
    private List<UserBean> mUsers;
    private List<UserBean> userList;
    public  ContactsFragment(){}
    @SuppressLint("ValidFragment")
    public ContactsFragment(List<DepartmentBean> lists){
        this.mDepartments = lists;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "通讯录";
        mShowBtnBack = View.INVISIBLE;
        mLogo = View.INVISIBLE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCreateView(inflater, R.layout.fragment_contacts, container, savedInstanceState);
    }

    @Nullable
    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContactsListView = (PullToRefreshListView) mRootView.findViewById(R.id.pull_refresh_list);
        mContactsListView.setOnItemClickListener(this);
        mCustomProgress = (CustomProgress) mRootView.findViewById(R.id.loading);
        mSideBar = (SideBar) mRootView.findViewById(R.id.sideBar);
        mSideBar.setListView(mContactsListView.getRefreshableView());
        mSideBarTextView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.contactlist_index, null);
        mSideBarTextView.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Activity.WINDOW_SERVICE);
        windowManager.addView(mSideBarTextView, lp);
        mSideBar.setTextView(mSideBarTextView);
        mRootView.findViewById(R.id.btn_selectgroup).setOnClickListener(this);
        mKeywordView = (EditText) mRootView.findViewById(R.id.et_keyword);
        mKeywordView.addTextChangedListener(mTextWatcher);
        mGroupNameView = (TextView) mRootView.findViewById(R.id.groupname_tv);
    }

    protected void initData() {
        mContactsAdapter = new ContactsAdapter(getContext());
        mContactsListView.getRefreshableView().setAdapter(mContactsAdapter);
        mRefreshContactsTask = new RefreshContactsTask(null, null, "全部分组");
        mRefreshContactsTask.execute();

//        mCustomProgress.start();
    }

    @Override
    public void onDestroy() {
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Activity.WINDOW_SERVICE);
        windowManager.removeViewImmediate(mSideBarTextView);
        super.onDestroy();
    }


    @Override
    public void onRefresh(PullToRefreshBase refreshView) {

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_selectgroup) {  //点击出现联系人分组的下拉按钮
            if (mDepartments == null) {//如果没有数据
//                mRefreshGroupTask = new RefreshGroupTask(null);
//                mRefreshGroupTask.execute();
                Toast.makeText(getContext(),"没有数据",Toast.LENGTH_SHORT).show();
            } else {//有数据  popupwindows显示
                showGroupPopUpWindow();
            }
        }
    }



    private void showGroupPopUpWindow() {
        String[] groupArray = new String[mDepartments.size() + 1];
        groupArray[0] = "全部分组";
        for (int i = 0; i < mDepartments.size(); i++) {
            groupArray[i + 1] = mDepartments.get(i).getDepartmentName();
        }
        showGroupPopUpWindow(mRootView.findViewById(R.id.groups_container), groupArray,
                -30, mGroupClickListener);
    }


    private AdapterView.OnItemClickListener mGroupClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String departmentId = null;
            String departmentName = "全部分组";
            if (position > 0) {
                departmentId = mDepartments.get(position - 1).getId();
                departmentName = mDepartments.get(position - 1).getDepartmentName();

            }

            try {
                mRefreshContactsTask = new RefreshContactsTask(null, departmentId,departmentName);   //点击联系人分组中的某一个条目请求炎黄数据
                mRefreshContactsTask.execute().get(1000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                mCustomProgress.stop();
            }
            hidePopUpWindow();//隐藏popupwindowa

        }
    };

//重要
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UserBean user = (UserBean) mContactsAdapter.getItem(position - mContactsListView
                .getRefreshableView()
                .getHeaderViewsCount());
        Bundle bundle = new Bundle();
        bundle.putInt(BaseFragment.PARAM_CHILD_TYPE, ContactsManagerFragment.CHILD_TYPE_USERINFO);
        bundle.putSerializable(UserDetailInfoFragment.PARAM_USER, user);
        mPreFragment.showChildFragment(bundle);

    }

    private class PinyinComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            String str1 = PinYinUtil.getPinYin(((UserBean) o1).getUserName())
                    .toUpperCase();
            String str2 = PinYinUtil.getPinYin(((UserBean) o2).getUserName())
                    .toUpperCase();
            return str1.compareTo(str2);
        }

    }

    private class AlphabetComparator implements Comparator<UserBean> {
        private RuleBasedCollator collator;

        public AlphabetComparator() {
            collator = (RuleBasedCollator) Collator
                    .getInstance(Locale.CHINA);
        }

        @Override
        public int compare(UserBean a1, UserBean a2) {
            CollationKey c1 = collator.getCollationKey(a1.getUserName());
            CollationKey c2 = collator.getCollationKey(a2.getUserName());
            return collator.compare(((CollationKey) c1).getSourceString(),
                    ((CollationKey) c2).getSourceString());
        }
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            try {
                if (mUsers != null && mUsers.size() > 0) {
                    List<UserBean> users = mUsers;
                    if (s.length() > 0) {
                        for (int i = 0; i < s.length(); i++) {
                            Characters characters1 = new Characters(s.toString().substring(i, i + 1));
                            char c = Character.toUpperCase(characters1.getFistChar());
                            List<UserBean> tUsers = new ArrayList<UserBean>();
                            for (UserBean user : users) {
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
                    mContactsAdapter.setContacts(users);
                    mContactsAdapter.notifyDataSetChanged();
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

    private Handler mUpdateUsersListHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            mUsers = (List<UserBean>) msg.obj;
            String departmentName = msg.getData().getString("departmentName");
            mGroupNameView.setText(departmentName);
            mContactsAdapter.setContacts(mUsers);
            mContactsAdapter.notifyDataSetChanged();
            mSideBar.setVisibility(View.VISIBLE);
        }
    };

//**    // 请求 所有联系人
    private class RefreshContactsTask extends AsyncTask<Void, Void, List<UserBean>> {

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
            mCustomProgress.start();
        }

        @Override
        protected List<UserBean> doInBackground(Void... params) {
            List<UserBean> users = WebServiceUtil.getInstance().getUserList(mUserName, mDepartmentId);
            if (users != null) {   //如果得到的数据不是空的
                Collections.sort(users, new PinyinComparator());   //联系人按照顺序排序
            }
            return users;
        }

        @Override
        protected void onPostExecute(List<UserBean> users) {
            mRefreshContactsTask = null;
            mCustomProgress.stop();
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
            mRefreshContactsTask = null;
        }
    }
   /* //请求联系人分组的数据
    private class RefreshGroupTask extends AsyncTask<Void, Void, List<DepartmentBean>> {
        String mDepartmentName;

        RefreshGroupTask(String department) {
            this.mDepartmentName = department;
        }

        @Override
        protected void onPreExecute() {
            mCustomProgress.start();
        }

        @Override
        protected List<DepartmentBean> doInBackground(Void... params) {
            List<DepartmentBean> departments = WebServiceUtil.getInstance().getDepartmentList(null);
            return departments;
        }

        @Override
        protected void onPostExecute(List<DepartmentBean> departments) {
            mRefreshContactsTask = null;
            mCustomProgress.stop();
            if (departments != null) {
                Message msg = mUpdateGroupsListHandler.obtainMessage();
                msg.obj = departments;
                mUpdateGroupsListHandler.sendMessage(msg);
            }
        }

        @Override
        protected void onCancelled() {
            mRefreshContactsTask = null;
        }
    }*/
}
