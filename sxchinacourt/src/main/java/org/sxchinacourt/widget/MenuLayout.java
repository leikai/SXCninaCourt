package org.sxchinacourt.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import org.sxchinacourt.bean.MenuBean;

import java.util.List;

/**
 * Created by baggio on 2017/3/13.
 */

public class MenuLayout extends LinearLayout {
    private SelectedCallback mCallback;
    private MenuView mCurMenuView;

    public MenuLayout(Context context, SelectedCallback callback) {
        super(context, null);
        mCallback = callback;
    }

    public MenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    public void addMenus(List<MenuBean> menus) {
        for (MenuBean menu : menus) {
            MenuView menuView = new MenuView(getContext());
            menuView.setMenu(menu);
            menuView.setSelectedListener(mSelectedListener);
            addView(menuView);
        }

    }

    private SelectedListener mSelectedListener = new SelectedListener() {
        @Override
        public void onSelected(MenuView menuView) {
            if(menuView == mCurMenuView){
                return;
            }
            for (int i = 0; i < getChildCount(); i++) {
                MenuView view = (MenuView) getChildAt(i);
                if (view == menuView) {
                    view.setSelected(true);
                    mCurMenuView = view;
                    mCallback.onSelected(mCurMenuView.getMenu());
                } else {
                    view.setSelected(false);
                }
            }
        }
    };

    public MenuBean getCurMenu() {
        if (mCurMenuView != null) {
            return mCurMenuView.getMenu();
        }
        return null;
    }

    public interface SelectedListener {
        public void onSelected(MenuView view);
    }

    public interface SelectedCallback {
        public void onSelected(MenuBean menu);
    }

}
