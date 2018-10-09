package org.sxchinacourt.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import org.sxchinacourt.R;
import org.sxchinacourt.bean.UserBean;
import org.sxchinacourt.util.PinYin.PinYinUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baggio on 2017/2/8.
 */

public class  ContactsAdapter extends BaseAdapter implements SectionIndexer {
    List<UserBean> mUsers;
    Context mContext;

    public ContactsAdapter(Context context) {
        this.mUsers = new ArrayList<UserBean>();
        this.mContext = context;
    }

    public void setContacts(List<UserBean> contacts) {
        this.mUsers = contacts;
    }

    @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mUsers.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.contact_item, null);
            holder = new ViewHolder();
            holder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
            holder.tv_common = (TextView) convertView
                    .findViewById(R.id.tv_common);
            holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            holder.tv_rolename = (TextView) convertView.findViewById(R.id.tv_rolename);
            convertView.setTag(holder);
            holder.tv_common.setVisibility(View.GONE);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        UserBean user = mUsers.get(position);
        String catalog = converterToFirstSpell(user.getUserName()).substring(
                0, 1).toUpperCase();
        if (position == 0) {
            holder.tv_common.setVisibility(View.VISIBLE);
            holder.tv_common.setText(catalog.toUpperCase());
        } else {
            String preCatalog = converterToFirstSpell(mUsers
                    .get(position - 1).getUserName().substring(0, 1)).toUpperCase();
            if (catalog.equals(preCatalog)) {
                holder.tv_common.setVisibility(View.GONE);
            } else {
                holder.tv_common.setVisibility(View.VISIBLE);
                holder.tv_common.setText(catalog.toUpperCase());
            }
        }
        if (!TextUtils.isEmpty(user.getUserName())) {
            holder.tv_username.setText(user.getUserName());
        }
        holder.tv_username.setText(user.getUserName());
        if (!TextUtils.isEmpty(user.getRoleName())) {
            holder.tv_rolename.setText(user.getRoleName());
        }
//        if (contancts.getPhoto() != null && contancts.getPhoto().length > 0) {
//            holder.iv_head.setImageBitmap(BitmapUtils
//                    .getRoundedBitmap(contancts.getPhoto()));
//            holder.tv_lastname.setVisibility(View.GONE);
//        } else {
//            Picasso.with(mContext)
//                    .load(mDefaultIconResource[position
//                            % mDefaultIconResource.length])
//                    .transform(new CircleTransform()).into(holder.iv_head);
//            holder.tv_lastname.setVisibility(View.VISIBLE);
//            holder.tv_lastname.setText(contancts.getName().substring(0, 1));
//        }
        return convertView;

    }

    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < mUsers.size(); i++) {
            String l = converterToFirstSpell(mUsers.get(i).getUserName())
                    .substring(0, 1);
            char firstChar = l.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Object[] getSections() {
        // TODO Auto-generated method stub
        return null;
    }

    public String converterToFirstSpell(String chines) {
        String pinyinName = "";
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    pinyinName += PinYinUtil.getPinYin(nameChar[i], i == 0).toUpperCase().charAt(0);
                } catch (Throwable e) {
                }
            } else {
                pinyinName += nameChar[i];
            }
        }
        return pinyinName;
    }

    private class ViewHolder {
        ImageView iv_head;
        TextView tv_common, tv_username, tv_department, tv_rolename;
    }

}
