package org.sxchinacourt.widget;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.system.ErrnoException;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.sxchinacourt.R;
import org.sxchinacourt.bean.Component;
import org.sxchinacourt.util.WebServiceUtil;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by Baggio on 2017/6/29.
 *
 * @author Baggio.
 * @version 1.0
 * @date 2017/6/29
 */

public class CustomSignView extends LinearLayout {
    private Component mComponent;
    private String mSignImgUrl;
    private ImageView mSignImgView;
    private GetDownloadSignImgUrlTask mGetDownloadSignImgUrlTask;

    public CustomSignView(Context context, Component component) {
        super(context);
        mComponent = component;
        ViewGroup signView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.task_detailinfo_signview, null);
        mSignImgView = (ImageView) signView.findViewById(R.id.signImg);
        TextView textView = (TextView) signView.findViewById(R.id.textview);
        textView.setText(mComponent.getText());
        addView(signView);
        String value = mComponent.getLabelValue();
        mGetDownloadSignImgUrlTask = new GetDownloadSignImgUrlTask(value);
        mGetDownloadSignImgUrlTask.execute();
    }

    private class GetDownloadSignImgUrlTask extends AsyncTask<Void, Void, String> {
        public GetDownloadSignImgUrlTask(){

        }
         private  String value;
        public GetDownloadSignImgUrlTask (String avalue){
          this.value = avalue;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String url = WebServiceUtil.getInstance().downloadSignImg(value);
                return url;
            }catch (Exception e){
                e.printStackTrace();
            }

                return null;
        }

        @Override
        protected void onPostExecute(String url) {
            try {
                if (!TextUtils.isEmpty(url)) {
                    mSignImgUrl = WebServiceUtil.BASE_DOWNLOAD_URL + url;
                    Picasso.with(getContext()).load(mSignImgUrl).into(mSignImgView);
                } else {
                   //Toast.makeText(getContext(), "签名的下载地址无效", Toast.LENGTH_LONG).show();
                }
            } finally {
                //mGetDownloadSignImgUrlTask = null;
            }
        }

        @Override
        protected void onCancelled() {
            mGetDownloadSignImgUrlTask = null;
        }
    }
}
