package org.sxchinacourt.bean;

/**
 *
 * @author baggio
 * @date 2017/2/20
 */

public class MenuBean {
    /**
     * 菜单名称
     */
    private String mMenuName;
    /**
     * 一个 int 值，菜单的动作代码
     */
    private int mMenuType;
    /**
     * 是否选择
     */
    private boolean isChecked;
    /**
     * 留言内容
     */
    private String mOpinion;

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
