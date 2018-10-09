package org.sxchinacourt.bean;

/**
 * Created by baggio on 2017/2/20.
 */

public class MenuBean {
    private String mMenuName;// 菜单名称
    private int mMenuType;//一个 int 值，菜单的动作代码
    private boolean isChecked;// 是否选择
    private String mOpinion;// 留言内容

    public String getMenuName() {
        return mMenuName;
    }

    public void setMenuName(String mMenuName) {
        this.mMenuName = mMenuName;
    }

    public int getMenuType() {
        return mMenuType;
    }

    public void setMenuType(int mMenuType) {
        this.mMenuType = mMenuType;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getOpinion() {
        return mOpinion;
    }

    public void setOpinion(String mOpinion) {
        this.mOpinion = mOpinion;
    }
}
