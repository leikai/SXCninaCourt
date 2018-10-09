package org.sxchinacourt.activity;

import android.os.AsyncTask;

import org.sxchinacourt.bean.UserBean;
import org.sxchinacourt.util.WebServiceUtil;

import java.util.List;

/**
 * Created by baggio on 2017/3/30.
 */

public class ContactsManager {
    private static ContactsManager mInstance;
    List<UserBean>  mUser;

    public static ContactsManager getInstance() {
        if(mInstance == null){
            mInstance = new ContactsManager();
        }
        return mInstance;
    }

    private ContactsManager(){

    }

    private class RefreshContactsTask extends AsyncTask<Void, Void, List<UserBean>> {
        String mUserName;
        String mDepartmentId;
        String mDepartmentName;

        //        long time;
        RefreshContactsTask(String userName, String departmentId, String departmentName) {
            mUserName = userName;
            mDepartmentId = departmentId;
            mDepartmentName = departmentName;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected List<UserBean> doInBackground(Void... params) {

            List<UserBean> users = WebServiceUtil.getInstance().getUserList(mUserName,
                    mDepartmentId);
            if (users != null) {
//                long time = System.currentTimeMillis();
//                Collections.sort(users, new PinyinComparator());
//                mDepartmentName = "time is " + (System.currentTimeMillis() - time);
            }
            return users;
        }

        @Override
        protected void onPostExecute(List<UserBean> users) {
//            mRefreshContactsTask = null;
        }

        @Override
        protected void onCancelled() {

        }
    }
}
