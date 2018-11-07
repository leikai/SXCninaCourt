package org.sxchinacourt.activity.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.adapter.DepartmentAdapter;
import org.sxchinacourt.adapter.UsersAdapter;
import org.sxchinacourt.bean.DepartmentNewBean;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.common.Contstants;
import org.sxchinacourt.util.WebServiceUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author baggio
 * @date 2017/2/3
 */

public class ContactsFragment extends BaseFragment{

    /**
     * 文字图片的颜色值
     */
    public static final String[] ImageBgColor={
            "#568AAD",
            "#17c295",
            "#4DA9EB",
            "#F2725E",
            "#B38979",
            "#568AAD"
    };

    private AppCompatSpinner spinnerCourtoa;
    private AppCompatSpinner spinnerDepartment;
    /**
     * 人员列表
     */
    private RecyclerView rvUsers;
    private RefreshGroupTask mRefreshGroupTask;
    private RefreshContactsTask mRefreshContactsTask;
    private ImageView ivSearch;
    private EditText etSearch;
    private TextView tvEmpty;

    /**
     * 当前登录人的个人信息
     */
    private UserNewBean user;
    private String token;
    /**
     * 法院ID
     */
    private String orgId;
    /**
     * 联系人分组集合
     */
    public List<DepartmentNewBean> mDepartments;
    /**
     * 联系人分组集合
     */
    public List<UserNewBean> mUsersList =  new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private UsersAdapter usersAdapter;
    private static final String TAG = "Constraints";

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
        spinnerCourtoa = mRootView.findViewById(R.id.spinner_courtoa);
        spinnerDepartment = mRootView.findViewById(R.id.spinner_department);
        tvEmpty = mRootView.findViewById(R.id.tv_empty);
        rvUsers = mRootView.findViewById(R.id.rv_user);
        ivSearch = mRootView.findViewById(R.id.iv_search);
        etSearch = mRootView.findViewById(R.id.et_search);

    }

    protected void initData() {
        token = CApplication.getInstance().getCurrentToken();
        user = CApplication.getInstance().getCurrentUser();



        Log.d(TAG, "initData: "+user.getEmpname());
        spinnerCourtoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String courtoaName = spinnerCourtoa.getItemAtPosition(position).toString();
                Log.e("courtoaName",""+courtoaName);
                if ("晋中市中级人民法院".equals(courtoaName)){
                    mRefreshGroupTask = new RefreshGroupTask(Contstants.OID_JINZHONG);
                }
                if ("寿阳县人民法院".equals(courtoaName)){
                    mRefreshGroupTask = new RefreshGroupTask(Contstants.OID_SHOUYANG);
                }
                if ("榆社县人民法院".equals(courtoaName)){
                    mRefreshGroupTask = new RefreshGroupTask(Contstants.OID_YUSHE);
                }
                if ("平遥县人民法院".equals(courtoaName)){
                    mRefreshGroupTask = new RefreshGroupTask(Contstants.OID_PINGYAO);
                }
                if ("太谷县人民法院".equals(courtoaName)){
                    mRefreshGroupTask = new RefreshGroupTask(Contstants.OID_TAIGU);
                }
                if ("和顺县人民法院".equals(courtoaName)){
                    mRefreshGroupTask = new RefreshGroupTask(Contstants.OID_HESHUN);
                }
                if ("榆次区人民法院".equals(courtoaName)){
                    mRefreshGroupTask = new RefreshGroupTask(Contstants.OID_YUCI);
                }
                if ("灵石县人民法院".equals(courtoaName)){
                    mRefreshGroupTask = new RefreshGroupTask(Contstants.OID_LINGSHI);
                }
                if ("左权县人民法院".equals(courtoaName)){
                    mRefreshGroupTask = new RefreshGroupTask(Contstants.OID_ZUOQUAN);
                }
                if ("昔阳县人民法院".equals(courtoaName)){
                    mRefreshGroupTask = new RefreshGroupTask(Contstants.OID_XIYANG);
                }
                if ("祁县人民法院".equals(courtoaName)){
                    mRefreshGroupTask = new RefreshGroupTask(Contstants.OID_QIXIAN);
                }
                if ("介休市人民法院".equals(courtoaName)){
                    mRefreshGroupTask = new RefreshGroupTask(Contstants.OID_JIEXIU);
                }
                mRefreshGroupTask.execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 主线程接收部门数据
     */
    private Handler mUpdateGroupsListHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            mDepartments = (List<DepartmentNewBean>) msg.obj;
            DepartmentNewBean departmentNewBean = new DepartmentNewBean();
            departmentNewBean.setDeptName("所有部门");
            departmentNewBean.setDeptPid("");
            orgId = mDepartments.get(1).getOrgid();
            mDepartments.add(0,departmentNewBean);
            setDateToView(mDepartments);

        }
    };

    /**
     * 将显示部门数据
     * @param mList
     */
    private  void  setDateToView(final List<DepartmentNewBean> mList){
        DepartmentAdapter departmentAdapter = new DepartmentAdapter(getActivity(), mList);
        //传入的参数分别为 Context , 未选中项的textview , 数据源List
        spinnerDepartment.setAdapter(departmentAdapter);
        departmentAdapter.notifyDataSetChanged();
        spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String departmentId = mList.get(position).getOid();
                String departmentName = mList.get(position).getDeptName();


                mRefreshContactsTask = new RefreshContactsTask(null,departmentId,departmentName,orgId);
                mRefreshContactsTask.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * 主线程接收人员数据
     */
    private Handler mUpdateUsersListHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            List<UserNewBean> lists = (List<UserNewBean>) msg.obj;
            mUsersList.clear();
            if (lists != null){
                mUsersList.addAll(lists);

            }

            setDataToView();
            String departmentName = msg.getData().getString("departmentName");
        }
    };

    /**
     * 将人员数据部署到页面
     */
    private void setDataToView() {
        if (mPreFragment == null){
            mPreFragment = (BaseFragment) getParentFragment();

        }
        if (mUsersList == null){
            rvUsers.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        }else {
            rvUsers.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
            if (layoutManager == null){
                layoutManager = new LinearLayoutManager(getActivity());
                rvUsers.setLayoutManager(layoutManager);
            }

            if (usersAdapter == null){
                usersAdapter = new UsersAdapter(mUsersList,mPreFragment);
                rvUsers.setAdapter(usersAdapter);
                rvUsers.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
            }

            usersAdapter.notifyDataSetChanged();
        }



        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ceshi  = etSearch.getText().toString().trim();
                Log.e("ceshi",""+ceshi);
                mRefreshContactsTask = new RefreshContactsTask(ceshi,null,null,orgId);
                mRefreshContactsTask.execute();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    //请求部门的数据
    private class RefreshGroupTask extends AsyncTask<Void, Void, List<DepartmentNewBean>> {
        String mCourtoaName;

        RefreshGroupTask(String courtoaOid) {
            this.mCourtoaName = courtoaOid;//法院Oid
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<DepartmentNewBean> doInBackground(Void... params) {
            Log.e("法院名称",""+mCourtoaName);
            List<DepartmentNewBean> departments = WebServiceUtil.getInstance().getDepartmentList(token,mCourtoaName);
            return departments;
        }

        @Override
        protected void onPostExecute(List<DepartmentNewBean> departments) {
            if (departments != null) {
                Message msg = mUpdateGroupsListHandler.obtainMessage();
                msg.obj = departments;
                mUpdateGroupsListHandler.sendMessage(msg);
            }
        }

        @Override
        protected void onCancelled() {
        }
    }

    //**    // 请求 联系人数据
    private class RefreshContactsTask extends AsyncTask<Void, Void, List<UserNewBean>> {

        String mUserName = "";
        String mDepartmentId = "";
        String mDepartmentName = "";
        String mCourtId = "";

        //        long time;
        public RefreshContactsTask(String userName, String departmentId, String departmentName, String orgId) {
            mUserName = userName;
            mDepartmentId = departmentId;
            mDepartmentName = departmentName;
            mCourtId = orgId;
        }

        @Override
        protected void onPreExecute() {
            // mCustomProgress.start();
        }

        @Override
        protected List<UserNewBean> doInBackground(Void... params) {
            List<UserNewBean> users;
            Log.e("mUserName",""+mUserName);
//            if (mUserName == null){
            users = WebServiceUtil.getInstance().getNewUserList(token,mCourtId,mDepartmentId,mUserName);
//            }else {
//                users = WebServiceUtil.getInstance().getNewUserList(token,mUserName,"");
//            }

            if (users != null) {   //如果得到的数据不是空的
//                Collections.sort(users, new PinyinComparator());   //联系人按照顺序排序
            }
            return users;
        }

        @Override
        protected void onPostExecute(List<UserNewBean> users) {
//            if (users != null) {
                Message msg = mUpdateUsersListHandler.obtainMessage();
                msg.obj = users;
                Bundle bundle = new Bundle();
                bundle.putString("departmentName", mDepartmentName);
                msg.setData(bundle);
                mUpdateUsersListHandler.sendMessage(msg);
//            }
        }

        @Override
        protected void onCancelled() {
            // mRefreshContactsTask = null;
        }
    }






    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
