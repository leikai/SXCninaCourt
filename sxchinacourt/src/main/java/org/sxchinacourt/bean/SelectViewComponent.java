package org.sxchinacourt.bean;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baggio on 2017/3/28.
 */

public class SelectViewComponent extends SubViewComponent {
    private List<String> mItems;
    private String mDefaultItem;
    private String mCurrentItem;

    public SelectViewComponent() {
        mItems = new ArrayList<>();
    }

    public List<String> getItems() {
        return mItems;
    }

    public void setItems(List<String> mItems) {
        this.mItems = mItems;
    }

    public String getDefaultItem() {
        return mDefaultItem;
    }

    public void setDefaultItem(String mDefaultItem) {
        this.mDefaultItem = mDefaultItem;
    }

    public String getCurrentItem() {
        return mCurrentItem;
    }

    public void setCurrentItem(String mCurrentItem) {
        this.mCurrentItem = mCurrentItem;
    }

    public void setSelection(int selection){
        this.mCurrentItem = mItems.get(selection);
    }

    @Override
    public void setLabelValue(String mLabelValue) {
        super.setLabelValue(mLabelValue);
        try {
            JSONArray array = new JSONArray(mLabelValue);
            for (int i = 0; i < array.length(); i++) {
                String item = array.getString(i);
                mItems.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
