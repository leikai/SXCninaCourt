package org.sxchinacourt.activity.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.bean.UserNewBean;

/**
 *
 * @author baggio
 * @date 2017/2/9
 */

public class ModifyUserNameFragment extends BaseFragment implements View.OnClickListener {
    ModifyUserNameTask mModifyUserNameTask;
    EditText mOldUserNameEditView;
    EditText mNewUserNameEditView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "修改用户名";
        mShowBtnBack = View.VISIBLE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCreateView(inflater, R.layout.fragment_modify_username, container, savedInstanceState);
    }


    @Nullable
    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView.findViewById(R.id.btn_confirm).setOnClickListener(this);
        mOldUserNameEditView = (EditText) mRootView.findViewById(R.id.et_old_username);
        mNewUserNameEditView = (EditText) mRootView.findViewById(R.id.et_new_username);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_confirm) {
            String oldUserName = mOldUserNameEditView.getText().toString();
            String newUserName = mNewUserNameEditView.getText().toString();
            if (TextUtils.isEmpty(oldUserName)) {
                Toast.makeText(getContext(), "请输入原用户名", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(newUserName)) {
                Toast.makeText(getContext(), "请输入新用户名", Toast.LENGTH_LONG).show();
                return;
            }
            mModifyUserNameTask.execute();
        }
    }

    private class ModifyUserNameTask extends AsyncTask<Void, Void, Boolean> {
        String mOldUserName;
        String mNewUserName;

        private ModifyUserNameTask(String oldUserName, String newUserName) {
            mOldUserName = oldUserName;
            mNewUserName = newUserName;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            UserNewBean user = CApplication.getInstance().getCurrentUser();
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            mModifyUserNameTask = null;
        }
    }
}
