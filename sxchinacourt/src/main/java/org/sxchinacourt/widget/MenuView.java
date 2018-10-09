package org.sxchinacourt.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sxchinacourt.R;
import org.sxchinacourt.bean.MenuBean;

/**
 * Created by baggio on 2017/3/13.
 */

public class MenuView extends LinearLayout implements View.OnClickListener {
    ImageView mIconView;
    TextView mMenuNameVew;
    MenuBean mMenu;
    MenuLayout.SelectedListener mSelectedListener;

    public MenuView(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.menu_item, null);
        mIconView = (ImageView) view.findViewById(R.id.img_icon);
        mMenuNameVew = (TextView) view.findViewById(R.id.tv_menuName);
        view.setOnClickListener(this);
        addView(view);
    }

    public MenuView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void setMenu(MenuBean menu) {
        mMenu = menu;
        mMenuNameVew.setText(mMenu.getMenuName());
    }

    public void setSelectedListener(MenuLayout.SelectedListener mSelectedListener) {
        this.mSelectedListener = mSelectedListener;
    }

    public void setSelected(boolean selected) {
        mMenu.setChecked(selected);
        mIconView.setSelected(selected);
    }

    public MenuBean getMenu() {
        return mMenu;
    }

    @Override
    public void onClick(View v) {
        mSelectedListener.onSelected(this);
    }
}
