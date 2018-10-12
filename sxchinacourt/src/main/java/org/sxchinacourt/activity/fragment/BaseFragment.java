package org.sxchinacourt.activity.fragment;

import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.sxchinacourt.R;
import org.sxchinacourt.widget.CustomActionBar;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 *
 * @author baggio
 * @date 2017/2/7
 */

public class BaseFragment extends Fragment {
    public static final String PARAM_CHILD_TYPE = "childfragment_type";
    int mChildType;
    BaseFragment mPreFragment;
    View mRootView;
    CustomActionBar mActionBarView;
    String mTitle;
    int mLogo;
    int mShowBtnBack;
    int mShowSearchBar;
    PopupWindow mPopUpWindow;

    @Nullable
    public View onCreateView(LayoutInflater inflater, int layoutId, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(layoutId, null);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            mRootView.setLayoutParams(lp);
            mActionBarView = (CustomActionBar) ((AppCompatActivity) getActivity()).getSupportActionBar()
                    .getCustomView();
            initFragment(container, savedInstanceState);
            mActionBarView.setTitle(mTitle);
            mActionBarView.setBackBtnViewVisible(mShowBtnBack);
            mActionBarView.setLogoViewVisible(mLogo);
        }

        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        initData();
        return mRootView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mActionBarView.setTitle(mTitle);
            mActionBarView.setBackBtnViewVisible(mShowBtnBack);
            mActionBarView.setLogoViewVisible(mLogo);

        }
    }

    @Nullable
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    }

    ;

    protected void initData() {
    }

    public boolean onBackPressed() {
        if (mPreFragment != null) {
            getFragmentManager().beginTransaction().hide(this).commit();
            getFragmentManager().beginTransaction().show(mPreFragment).commit();
        }
        return false;
    }

    protected void showDialog(String title, String Message) {

    }

    public void setPreFragment(BaseFragment mParentFragment) {
        this.mPreFragment = mParentFragment;
    }

    public void showChildFragment(Bundle bundle) {
        if (bundle != null && bundle.containsKey(PARAM_CHILD_TYPE)) {
            mChildType = bundle.getInt(PARAM_CHILD_TYPE);
            Log.e("mChildType",""+mChildType);
        }
    }

    public void addChildFragment(Fragment fragment, int contentId) {
        if (!fragment.isAdded()) {
            Log.e("contentId",""+contentId);
            getChildFragmentManager().beginTransaction().add(contentId, fragment).commit();
        }
    }

    public void hideFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction().hide(fragment).commit();
    }

    public void showSingleChoiceDialog(String[] items, String title, int selected, DialogInterface
            .OnClickListener listener) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setTitle(title);
        alertBuilder.setSingleChoiceItems(items, selected, listener);
        alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });

        alertBuilder.create().show();
    }
    //显示popupUpWindow
    public void showGroupPopUpWindow(View anchorView, String[] data, int yOff, AdapterView
            .OnItemClickListener listener) {
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.group_popupwindow, null);
        ListView lsvMore = (ListView) popupView.findViewById(R.id.groups);  //展示示各个分组的Listview  如立案组 信息处
        lsvMore.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,
                data));
        lsvMore.setOnItemClickListener(listener);
        DisplayMetrics ds = getResources().getDisplayMetrics();
        int width = 3 * ds.widthPixels / 4;
        int height = 2 * ds.heightPixels / 3;
        mPopUpWindow = new PopupWindow(popupView, width, height);
        mPopUpWindow.setAnimationStyle(R.style.popup_window_anim);
        mPopUpWindow.setFocusable(true);
        mPopUpWindow.setOutsideTouchable(false);
//        setWindowAlpha(0.3f);
        mPopUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mPopUpWindow = null;
            }
        });
        popupView.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopUpWindow.dismiss();
            }
        });
        mPopUpWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopUpWindow.setOutsideTouchable(true);
        //设置监听
        mPopUpWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE && !mPopUpWindow.isFocusable()) {
                    return true;
                }
                return false;
            }
        });
        mPopUpWindow.update();
        mPopUpWindow.showAsDropDown(anchorView, ds.widthPixels / 8, yOff);
    }
    //隐藏popupUpWindow
    public void hidePopUpWindow() {
        if (mPopUpWindow != null) {
            mPopUpWindow.dismiss();
        }
    }

//    private void setWindowAlpha(float alpha) {
//        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//        lp.alpha = alpha;
//        getActivity().getWindow().setAttributes(lp);
//    }

}
