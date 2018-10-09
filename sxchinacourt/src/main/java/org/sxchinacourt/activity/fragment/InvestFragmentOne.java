package org.sxchinacourt.activity.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.sxchinacourt.R;
import org.sxchinacourt.common.Contstants;

public class InvestFragmentOne extends ContactsManagerFragment {
    private Context context;
    private int resId;
    private TextView tv_JinZhong, tv_ShouYang, tv_YuShe, tv_PingYao, tv_TaiGu, tv_HeShun, tv_YuCi, tv_LingShi, tv_ZuoQuan, tv_XiYang, tv_QiXian;
    private RelativeLayout rl_JinZhong, rl_ShouYang, rl_YuShe, rl_PingYao, rl_TaiGu, rl_HeShun, rl_YuCi, rl_LingShi, rl_ZuoQuan, rl_XiYang, rl_QiXian;
    private View view_JinZhong, view_ShouYang, view_YuShe, view_PingYao, view_TaiGu, view_HeShun, view_YuCi, view_LingShi, view_ZuoQuan, view_XiYang, view_QiXian;
    @SuppressLint({"NewApi", "ValidFragment"})
    public InvestFragmentOne() {

    }
    @SuppressLint({"NewApi", "ValidFragment"})
    public InvestFragmentOne(Context context, int resId) {
        this.context = context;
        this.resId = resId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "法院";
        mShowBtnBack = View.INVISIBLE;
        mLogo = View.INVISIBLE;
        //fm = getChildFragmentManager();
       /* fm  = getParentFragment().getFragmentManager();
        fm.findFragmentById(R.id.rl_content);*/
        /*if(fragment == null){
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }*/
        /*investFragmentOne = new InvestFragmentOne();
        investFragmentTwo = new InvestFragmentTwo();
        investFragmentTheer = new InvestFragmentTheer();*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(resId, container, false);
//        //tv = (TextView) view.findViewById(R.id.tv);
//        //String a = (String) tv.getText();
//     /*  FragmentManager fm =  getParentFragment().getFragmentManager();
//        Fragment fragment = fm.findFragmentById(R.id.rl_content);
//        if(fragment == null){
//            fragment = new InvestFragmentTwo();
//            fm.beginTransaction()
//                    .add(R.id.rl_content, fragment)
//                    .commit();
//        }*/
//        initView(view);
//        initData();
//        initEvent();
        return onCreateView(inflater, R.layout.fragment_invest_one, container, savedInstanceState);
    }

    @Nullable
    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        /*   RelativeLayout  findviewbyid  */
        rl_JinZhong = (RelativeLayout) mRootView.findViewById(R.id.rl_JinZhong);
        rl_ShouYang = (RelativeLayout) mRootView.findViewById(R.id.rl_ShouYang);
        rl_YuShe = (RelativeLayout) mRootView.findViewById(R.id.rl_YuShe);
        rl_PingYao = (RelativeLayout) mRootView.findViewById(R.id.rl_PingYao);
        rl_TaiGu = (RelativeLayout) mRootView.findViewById(R.id.rl_TaiGu);
        rl_HeShun = (RelativeLayout) mRootView.findViewById(R.id.rl_HeShun);
        rl_YuCi = (RelativeLayout) mRootView.findViewById(R.id.rl_YuCi);
        rl_LingShi = (RelativeLayout) mRootView.findViewById(R.id.rl_LingShi);
        rl_ZuoQuan = (RelativeLayout) mRootView.findViewById(R.id.rl_ZuoQuan);
        rl_XiYang = (RelativeLayout) mRootView.findViewById(R.id.rl_XiYang);
        rl_QiXian = (RelativeLayout) mRootView.findViewById(R.id.rl_QiXian);
        /*   TextView  findviewbyid  */
        tv_JinZhong = (TextView) mRootView.findViewById(R.id.tv_JinZhong);
        tv_ShouYang = (TextView) mRootView.findViewById(R.id.tv_ShouYang);
        tv_YuShe = (TextView) mRootView.findViewById(R.id.tv_YuShe);
        tv_PingYao = (TextView) mRootView.findViewById(R.id.tv_PingYao);
        tv_TaiGu = (TextView) mRootView.findViewById(R.id.tv_TaiGu);
        tv_HeShun = (TextView) mRootView.findViewById(R.id.tv_HeShun);
        tv_YuCi = (TextView) mRootView.findViewById(R.id.tv_YuCi);
        tv_LingShi = (TextView) mRootView.findViewById(R.id.tv_LingShi);
        tv_ZuoQuan = (TextView) mRootView.findViewById(R.id.tv_ZuoQuan);
        tv_XiYang = (TextView) mRootView.findViewById(R.id.tv_XiYang);
        tv_QiXian = (TextView) mRootView.findViewById(R.id.tv_QiXian);
        /*   View  findviewbyid  */
        view_JinZhong = mRootView.findViewById(R.id.view_JinZhong);
        view_ShouYang = mRootView.findViewById(R.id.view_ShouYang);
        view_YuShe = mRootView.findViewById(R.id.view_YuShe);
        view_PingYao = mRootView.findViewById(R.id.view_PingYao);
        view_TaiGu = mRootView.findViewById(R.id.view_TaiGu);
        view_HeShun = mRootView.findViewById(R.id.view_HeShun);
        view_YuCi = mRootView.findViewById(R.id.view_YuCi);
        view_LingShi = mRootView.findViewById(R.id.view_LingShi);
        view_ZuoQuan = mRootView.findViewById(R.id.view_ZuoQuan);
        view_XiYang = mRootView.findViewById(R.id.view_XiYang);
        view_QiXian = mRootView.findViewById(R.id.view_QiXian);
    }

    private void initEvent() {
        tv_JinZhong.setOnClickListener(mOnClickListener);
        tv_ShouYang.setOnClickListener(mOnClickListener);
        tv_YuShe.setOnClickListener(mOnClickListener);
        tv_PingYao.setOnClickListener(mOnClickListener);
        tv_TaiGu.setOnClickListener(mOnClickListener);
        tv_HeShun.setOnClickListener(mOnClickListener);
        tv_YuCi.setOnClickListener(mOnClickListener);
        tv_LingShi.setOnClickListener(mOnClickListener);
        tv_ZuoQuan.setOnClickListener(mOnClickListener);
        tv_XiYang.setOnClickListener(mOnClickListener);
        tv_QiXian.setOnClickListener(mOnClickListener);


        rl_JinZhong.setOnTouchListener(mOnTouchListener);
        rl_ShouYang.setOnTouchListener(mOnTouchListener);
        rl_YuShe.setOnTouchListener(mOnTouchListener);
        rl_PingYao.setOnTouchListener(mOnTouchListener);
        rl_TaiGu.setOnTouchListener(mOnTouchListener);
        rl_HeShun.setOnTouchListener(mOnTouchListener);
        rl_YuCi.setOnTouchListener(mOnTouchListener);
        rl_LingShi.setOnTouchListener(mOnTouchListener);
        rl_ZuoQuan.setOnTouchListener(mOnTouchListener);
        rl_XiYang.setOnTouchListener(mOnTouchListener);
        rl_QiXian.setOnTouchListener(mOnTouchListener);
    }

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (v.getId()) {
                case R.id.rl_JinZhong:
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        tv_JinZhong.setTextColor(getResources().getColor(R.color.black));
                        view_JinZhong.setBackgroundColor(getResources().getColor(R.color.gray));
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        tv_JinZhong.setTextColor(getResources().getColor(R.color.colorBlue));
                        view_JinZhong.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                    } else {
                        tv_JinZhong.setTextColor(getResources().getColor(R.color.black));
                        view_JinZhong.setBackgroundColor(getResources().getColor(R.color.gray));
                    }
                    break;
                case R.id.rl_ShouYang:
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        tv_ShouYang.setTextColor(getResources().getColor(R.color.black));
                        view_ShouYang.setBackgroundColor(getResources().getColor(R.color.gray));
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        tv_ShouYang.setTextColor(getResources().getColor(R.color.colorBlue));
                        view_ShouYang.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                    } else {
                        tv_ShouYang.setTextColor(getResources().getColor(R.color.black));
                        view_ShouYang.setBackgroundColor(getResources().getColor(R.color.gray));
                    }
                    break;
                case R.id.rl_YuShe:
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        tv_YuShe.setTextColor(getResources().getColor(R.color.black));
                        view_YuShe.setBackgroundColor(getResources().getColor(R.color.gray));
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        tv_YuShe.setTextColor(getResources().getColor(R.color.colorBlue));
                        view_YuShe.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                    } else {
                        tv_YuShe.setTextColor(getResources().getColor(R.color.black));
                        view_YuShe.setBackgroundColor(getResources().getColor(R.color.gray));
                    }
                    break;
                case R.id.rl_PingYao:
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        tv_PingYao.setTextColor(getResources().getColor(R.color.black));
                        view_PingYao.setBackgroundColor(getResources().getColor(R.color.gray));
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        tv_PingYao.setTextColor(getResources().getColor(R.color.colorBlue));
                        view_PingYao.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                    } else {
                        tv_PingYao.setTextColor(getResources().getColor(R.color.black));
                        view_PingYao.setBackgroundColor(getResources().getColor(R.color.gray));
                    }
                    break;
                case R.id.rl_TaiGu:
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        tv_TaiGu.setTextColor(getResources().getColor(R.color.black));
                        view_TaiGu.setBackgroundColor(getResources().getColor(R.color.gray));
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        tv_TaiGu.setTextColor(getResources().getColor(R.color.colorBlue));
                        view_TaiGu.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                    } else {
                        tv_TaiGu.setTextColor(getResources().getColor(R.color.black));
                        view_TaiGu.setBackgroundColor(getResources().getColor(R.color.gray));
                    }
                    break;
                case R.id.rl_HeShun:
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        tv_HeShun.setTextColor(getResources().getColor(R.color.black));
                        view_HeShun.setBackgroundColor(getResources().getColor(R.color.gray));
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        tv_HeShun.setTextColor(getResources().getColor(R.color.colorBlue));
                        view_HeShun.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                    } else {
                        tv_HeShun.setTextColor(getResources().getColor(R.color.black));
                        view_HeShun.setBackgroundColor(getResources().getColor(R.color.gray));
                    }
                    break;
                case R.id.rl_YuCi:
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        tv_YuCi.setTextColor(getResources().getColor(R.color.black));
                        view_YuCi.setBackgroundColor(getResources().getColor(R.color.gray));
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        tv_YuCi.setTextColor(getResources().getColor(R.color.colorBlue));
                        view_YuCi.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                    } else {
                        tv_YuCi.setTextColor(getResources().getColor(R.color.black));
                        view_YuCi.setBackgroundColor(getResources().getColor(R.color.gray));
                    }
                    break;
                case R.id.rl_LingShi:
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        tv_LingShi.setTextColor(getResources().getColor(R.color.black));
                        view_LingShi.setBackgroundColor(getResources().getColor(R.color.gray));
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        tv_LingShi.setTextColor(getResources().getColor(R.color.colorBlue));
                        view_LingShi.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                    } else {
                        tv_LingShi.setTextColor(getResources().getColor(R.color.black));
                        view_LingShi.setBackgroundColor(getResources().getColor(R.color.gray));
                    }
                    break;
                case R.id.rl_ZuoQuan:
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        tv_ZuoQuan.setTextColor(getResources().getColor(R.color.black));
                        view_ZuoQuan.setBackgroundColor(getResources().getColor(R.color.gray));
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        tv_ZuoQuan.setTextColor(getResources().getColor(R.color.colorBlue));
                        view_ZuoQuan.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                    } else {
                        tv_ZuoQuan.setTextColor(getResources().getColor(R.color.black));
                        view_ZuoQuan.setBackgroundColor(getResources().getColor(R.color.gray));
                    }
                    break;
                case R.id.rl_XiYang:
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        tv_XiYang.setTextColor(getResources().getColor(R.color.black));
                        view_XiYang.setBackgroundColor(getResources().getColor(R.color.gray));
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        tv_XiYang.setTextColor(getResources().getColor(R.color.colorBlue));
                        view_XiYang.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                    } else {
                        tv_XiYang.setTextColor(getResources().getColor(R.color.black));
                        view_XiYang.setBackgroundColor(getResources().getColor(R.color.gray));
                    }
                    break;
                case R.id.rl_QiXian:
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        tv_QiXian.setTextColor(getResources().getColor(R.color.black));
                        view_QiXian.setBackgroundColor(getResources().getColor(R.color.gray));
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        tv_QiXian.setTextColor(getResources().getColor(R.color.colorBlue));
                        view_QiXian.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                    } else {
                        tv_QiXian.setTextColor(getResources().getColor(R.color.black));
                        view_QiXian.setBackgroundColor(getResources().getColor(R.color.gray));
                    }
                    break;
            }
            return false;
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_JinZhong:
                    if (mCallBack != null) {
                        Log.e("TAG", "Fragment");
//                        mCallBack.onClick("");
                        mCallBack.onClick(Contstants.OID_JINZHONG);

                        Bundle bundle = new Bundle();

                        bundle.putInt(ContactsManagerFragment.PARAM_CHILD_TYPE, ContactsManagerFragment.CHILD_TYPE_CONTACTS_DEPART);
//                        bundle.putString("court","");
                        bean.setCourt(Contstants.OID_JINZHONG);
                        mPreFragment.showChildFragment(bundle);

                    } else {
                        Log.e("TAG", "null");
                    }
                    break;
                case R.id.tv_ShouYang:
                    if (mCallBack != null) {
                        Log.e("TAG", "Fragment");
//                        mCallBack.onClick("30");
                        mCallBack.onClick(Contstants.OID_SHOUYANG);

                        Bundle bundle = new Bundle();

                        bundle.putInt(ContactsManagerFragment.PARAM_CHILD_TYPE, ContactsManagerFragment.CHILD_TYPE_CONTACTS_DEPART);
                        bundle.putString("court",Contstants.OID_SHOUYANG);
                        bean.setCourt(Contstants.OID_SHOUYANG);
                        mPreFragment.showChildFragment(bundle);
                    } else {
                        Log.e("TAG", "null");
                    }
                    break;
                case R.id.tv_YuShe:
                    if (mCallBack != null) {
                        Log.e("TAG", "Fragment");
                        mCallBack.onClick(Contstants.OID_YUSHE);
                        Bundle bundle = new Bundle();

                        bundle.putInt(ContactsManagerFragment.PARAM_CHILD_TYPE, ContactsManagerFragment.CHILD_TYPE_CONTACTS_DEPART);
                        bundle.putString("court",Contstants.OID_YUSHE);
                        bean.setCourt(Contstants.OID_YUSHE);
                        mPreFragment.showChildFragment(bundle);
                    } else {
                        Log.e("TAG", "null");
                    }
                    break;
                case R.id.tv_PingYao:
                    if (mCallBack != null) {
                        Log.e("TAG", "Fragment");
                        mCallBack.onClick(Contstants.OID_PINGYAO);
                        Bundle bundle = new Bundle();

                        bundle.putInt(ContactsManagerFragment.PARAM_CHILD_TYPE, ContactsManagerFragment.CHILD_TYPE_CONTACTS_DEPART);
                        bundle.putString("court",Contstants.OID_PINGYAO);
                        bean.setCourt(Contstants.OID_PINGYAO);
                        mPreFragment.showChildFragment(bundle);
                    } else {
                        Log.e("TAG", "null");
                    }
                    break;
                case R.id.tv_TaiGu:
                    if (mCallBack != null) {
                        Log.e("TAG", "Fragment");
                        mCallBack.onClick(Contstants.OID_TAIGU);
                        Bundle bundle = new Bundle();

                        bundle.putInt(ContactsManagerFragment.PARAM_CHILD_TYPE, ContactsManagerFragment.CHILD_TYPE_CONTACTS_DEPART);
                        bundle.putString("court",Contstants.OID_TAIGU);
                        bean.setCourt(Contstants.OID_TAIGU);
                        mPreFragment.showChildFragment(bundle);
                    } else {
                        Log.e("TAG", "null");
                    }
                    break;
                case R.id.tv_HeShun:
                    if (mCallBack != null) {
                        Log.e("TAG", "Fragment");
                        mCallBack.onClick(Contstants.OID_HESHUN);
                        Bundle bundle = new Bundle();

                        bundle.putInt(ContactsManagerFragment.PARAM_CHILD_TYPE, ContactsManagerFragment.CHILD_TYPE_CONTACTS_DEPART);
                        bundle.putString("court",Contstants.OID_HESHUN);
                        bean.setCourt(Contstants.OID_HESHUN);
                        mPreFragment.showChildFragment(bundle);
                    } else {
                        Log.e("TAG", "null");
                    }
                    break;
                case R.id.tv_YuCi:
                    if (mCallBack != null) {
                        Log.e("TAG", "Fragment");
                        mCallBack.onClick(Contstants.OID_YUCI);
                        Bundle bundle = new Bundle();

                        bundle.putInt(ContactsManagerFragment.PARAM_CHILD_TYPE, ContactsManagerFragment.CHILD_TYPE_CONTACTS_DEPART);
                        bundle.putString("court",Contstants.OID_YUCI);
                        bean.setCourt(Contstants.OID_YUCI);
                        mPreFragment.showChildFragment(bundle);
                    } else {
                        Log.e("TAG", "null");
                    }
                    break;
                case R.id.tv_LingShi:
                    if (mCallBack != null) {
                        Log.e("TAG", "Fragment");
                        mCallBack.onClick(Contstants.OID_LINGSHI);
                        Bundle bundle = new Bundle();

                        bundle.putInt(ContactsManagerFragment.PARAM_CHILD_TYPE, ContactsManagerFragment.CHILD_TYPE_CONTACTS_DEPART);
                        bundle.putString("court",Contstants.OID_LINGSHI);
                        bean.setCourt(Contstants.OID_LINGSHI);
                        mPreFragment.showChildFragment(bundle);
                    } else {
                        Log.e("TAG", "null");
                    }
                    break;
                case R.id.tv_ZuoQuan:
                    if (mCallBack != null) {
                        Log.e("TAG", "Fragment");
                        mCallBack.onClick(Contstants.OID_ZUOQUAN);
                        Bundle bundle = new Bundle();

                        bundle.putInt(ContactsManagerFragment.PARAM_CHILD_TYPE, ContactsManagerFragment.CHILD_TYPE_CONTACTS_DEPART);
                        bundle.putString("court",Contstants.OID_ZUOQUAN);
                        bean.setCourt(Contstants.OID_ZUOQUAN);
                        mPreFragment.showChildFragment(bundle);
                    } else {
                        Log.e("TAG", "null");
                    }
                    break;
                case R.id.tv_XiYang:
                    if (mCallBack != null) {
                        Log.e("TAG", "Fragment");
                        mCallBack.onClick(Contstants.OID_XIYANG);
                        Bundle bundle = new Bundle();

                        bundle.putInt(ContactsManagerFragment.PARAM_CHILD_TYPE, ContactsManagerFragment.CHILD_TYPE_CONTACTS_DEPART);
                        bundle.putString("court",Contstants.OID_XIYANG);
                        bean.setCourt(Contstants.OID_XIYANG);
                        mPreFragment.showChildFragment(bundle);
                    } else {
                        Log.e("TAG", "null");
                    }
                    break;
                case R.id.tv_QiXian:
                    if (mCallBack != null) {
                        Log.e("TAG", "Fragment");
                        mCallBack.onClick(Contstants.OID_QIXIAN);
                        Bundle bundle = new Bundle();

                        bundle.putInt(ContactsManagerFragment.PARAM_CHILD_TYPE, ContactsManagerFragment.CHILD_TYPE_CONTACTS_DEPART);
                        bundle.putString("court",Contstants.OID_QIXIAN);
                        bean.setCourt(Contstants.OID_QIXIAN);
                        mPreFragment.showChildFragment(bundle);
                    } else {
                        Log.e("TAG", "null");
                    }
                    break;

            }

        }
    };

    protected void initData() {

    }

    public FragmentCallBack mCallBack;

    public void setFragmentCallBack(FragmentCallBack mCallBack){
        this.mCallBack = mCallBack;
    }

    public interface FragmentCallBack{
        void onClick(String str);
    }
}
