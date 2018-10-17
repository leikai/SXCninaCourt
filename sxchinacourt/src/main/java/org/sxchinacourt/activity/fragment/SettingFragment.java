package org.sxchinacourt.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sxchinacourt.R;
import org.sxchinacourt.activity.LoginActivity;
import org.sxchinacourt.activity.SwitchAccountActivity;
import org.sxchinacourt.cache.CSharedPreferences;

/**
 *
 * @author baggio
 * @date 2017/2/3
 */

public class SettingFragment extends BaseFragment implements View.OnClickListener {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "设置";
        mShowBtnBack = View.INVISIBLE;
        mLogo = View.INVISIBLE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCreateView(inflater, R.layout.fragment_setting, container, savedInstanceState);
    }

    @Nullable
    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView.findViewById(R.id.btn_modify_pwd).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_set_handle_pwd).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_modify_handle_pwd).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_modify_username).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_modify_serverip).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_switch_account).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_logout).setOnClickListener(this);
    }

    @Override
    public void showChildFragment(Bundle bundle) {
        mPreFragment.showChildFragment(bundle);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_modify_pwd: {
                Bundle bundle = new Bundle();
                bundle.putInt(SettingManagerFragment.PARAM_CHILD_TYPE, SettingManagerFragment.CHILD_TYPE_MODIFYPWD);
                mPreFragment.showChildFragment(bundle);
                break;
            }

            case R.id.btn_set_handle_pwd: {
                Bundle bundle = new Bundle();
                bundle.putInt(SettingManagerFragment.PARAM_CHILD_TYPE, SettingManagerFragment.CHILD_TYPE_SETHANDLEPWD);
                mPreFragment.showChildFragment(bundle);
                break;
            }
            case R.id.btn_modify_handle_pwd: {
                Bundle bundle = new Bundle();
                bundle.putInt(SettingManagerFragment.PARAM_CHILD_TYPE, SettingManagerFragment.CHILD_TYPE_MODIFYHANDLEPWD);
                mPreFragment.showChildFragment(bundle);
                break;
            }

            case R.id.btn_modify_username: {
                Bundle bundle = new Bundle();
                bundle.putInt(SettingManagerFragment.PARAM_CHILD_TYPE, SettingManagerFragment
                        .CHILD_TYPE_MODIFYUSERNAME);
                mPreFragment.showChildFragment(bundle);
                break;
            }
            case R.id.btn_modify_serverip: {
                Bundle bundle = new Bundle();
                bundle.putInt(SettingManagerFragment.PARAM_CHILD_TYPE, SettingManagerFragment
                        .CHILD_TYPE_MODIFYSERVERIP);
                mPreFragment.showChildFragment(bundle);
                break;
            }
            case R.id.btn_switch_account: {
                Intent intent = new Intent();
                intent.setClass(getActivity(), SwitchAccountActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.btn_logout: {
                CSharedPreferences.getInstance().clear();
                Intent intent = new Intent();
                intent.setClass(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            }
            default:
                break;
        }
    }
}
