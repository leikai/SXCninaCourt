package org.sxchinacourt.activity.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.adapter.InvestTwoAdapter;
import org.sxchinacourt.bean.DepartmentNewBean;
import org.sxchinacourt.util.WebServiceUtil;

import java.util.List;

public class InvestFragmentTwo extends ContactsManagerFragment {
    private Context context;
    private int resId;
    private ListView lv_section;
    public List<DepartmentNewBean> mDepartments;//联系人分组集合
    private RefreshGroupTask mRefreshGroupTask;
    private String token;


    @SuppressLint({"NewApi", "ValidFragment"})
    public InvestFragmentTwo() {

    }
    @SuppressLint({"NewApi", "ValidFragment"})
    public InvestFragmentTwo(Context context, int resId) {
        this.context = context;
        this.resId = resId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "部门";
        mShowBtnBack = View.INVISIBLE;
        mLogo = View.INVISIBLE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCreateView(inflater, R.layout.fragment_invest_two, container, savedInstanceState);


    }

    @Nullable
    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView(mRootView);
        initData();
    }

    private void initView(View v) {
        lv_section = (ListView) v.findViewById(R.id.lv_section);

    }

    protected void initData() {
        String str = bean.getCourt();
        token = CApplication.getInstance().getCurrentToken();

        Log.e("法院str",""+str);
        mRefreshGroupTask = new RefreshGroupTask(str);
        mRefreshGroupTask.execute();

    }


    private void initEvent() {
//        lv_section.setOnItemClickListener(mOnItemClickListener);
        lv_section.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String departmentId = null;
                String departmentName = null;
                if (i >= 0){
                    departmentId = mDepartments.get(i).getOid();
                    departmentName = mDepartments.get(i).getDeptName();
                    Log.e("部门名称：",""+departmentName);

//                    if (mFragmentTwoCallBack != null){
//                        mFragmentTwoCallBack.onClick(departmentId,departmentName);
                    mFragmentTwoCallBack.onClick(departmentId,departmentName);
                    Bundle bundle = new Bundle();

                    bundle.putInt(ContactsManagerFragment.PARAM_CHILD_TYPE, ContactsManagerFragment.CHILD_TYPE_CONTACTS_USER);
//                        bundle.putString("court","");
                    bean.setId(departmentId);
                    bean.setName(departmentName);
//                        showChildFragment(bundle);
                    if (mPreFragment == null){
                        mPreFragment = (BaseFragment) getParentFragment();

                    }
                    mPreFragment.showChildFragment(bundle);
//                        mPreFragment.showChildFragment(bundle);
//                    }
                }
            }
        });

    }
    private  void  setDateToView(){
        InvestTwoAdapter adapter = new InvestTwoAdapter(context,mDepartments);
        lv_section.setAdapter(adapter);
        initEvent();
    }


    private Handler mUpdateGroupsListHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            mDepartments = (List<DepartmentNewBean>) msg.obj;
            setDateToView();

        }
    };

    //请求联系人分组的数据
    private class RefreshGroupTask extends AsyncTask<Void, Void, List<DepartmentNewBean>> {
        String mDepartmentName;

        RefreshGroupTask(String department) {
            this.mDepartmentName = department;//法院Oid
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<DepartmentNewBean> doInBackground(Void... params) {
            Log.e("部门名称",""+mDepartmentName);
            List<DepartmentNewBean> departments = WebServiceUtil.getInstance().getDepartmentList(token,mDepartmentName);
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
    private FragmentTwoCallBack mFragmentTwoCallBack;
    public void setFragmentTwoCallBack(FragmentTwoCallBack mFragmentTwoCallBack){
        this.mFragmentTwoCallBack = mFragmentTwoCallBack;
    }
    public interface FragmentTwoCallBack{
        void onClick(String id, String name);
    }

}
