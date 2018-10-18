package org.sxchinacourt.bean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.kinggrid.iapprevision_iwebrevision.iAppRevision_iWebRevision;
import org.sxchinacourt.R;
import org.sxchinacourt.activity.TaskDetailInfoActivity;
import org.sxchinacourt.common.Contstants;
import org.sxchinacourt.util.PinYin.SharedPreferencesUtil;
import org.sxchinacourt.util.WebServiceUtil;
import org.sxchinacourt.widget.CustomAttachmentView;
import org.sxchinacourt.widget.CustomDateView;
import org.sxchinacourt.widget.CustomProgress;
import org.sxchinacourt.widget.CustomSignView;
import org.sxchinacourt.widget.CustomSpinner;
import org.sxchinacourt.widget.FlowRadioGroup;
import org.sxchinacourt.widget.RevisionNormalDialog;

import java.util.ArrayList;
import java.util.List;

import static org.sxchinacourt.R.id.tv_value;
import static org.sxchinacourt.activity.TaskDetailInfoActivity.activity;

/**
 *
 * @author baggio
 * @date 2017/3/2
 */

public class ViewComponents{
    List<Component> mComponents = new ArrayList<>();
    List<Component> mSubViewComponents = new ArrayList<>();
    List<Component> mHiddenComponent = new ArrayList<>();
    private  RadioButton radioButton;
    /**
     * 和iWebRevision交互的对象
     */
    private iAppRevision_iWebRevision apprevison;
    public static String radio;
    private TextView tv_name;
    private String fieldName;
    private ImageView iv_sign;
    private SubViewComponent subViewComponent;
    private  String user_name;
    public  String imetaId;
    private final String copyRight = "SxD/phFsuhBWZSmMVtSjKZmm/c/3zSMrkV2Bbj5tznSkEVZmTwJv0wwMmH/+p6wLBSSVwmUpRfendwEawLj0VLJ6vDthIEAOcf4QTRWI/ShtZhuMjbvpgCAqbKKMHJWgpW1Gr8XXPYJ4HhGdpGi52DxjDOb5tag0qxTJVdrxXbre7ScPph+uyVo0lj5RYMthslzcF8aMKcf7kC8oWy6JNGF7KKeL4hJgLscmq65tlNSgSOimMBZXAWJyoNec+zKVpsIbmNxqKyvhX5tNT5jWlstVFd1auzF3hfElMKqI/DhfjgZng3BAkQ0sSjCWwWV9BpEVGh+pLlRbzYy0JKBajR97b07zZk7J3N9w9HyrB6pbyicI7UCAwAPZKTF8lcz+31TdZ5LxM6ZF9VY0A81SIZKEWu5GE6csEuLnbina4reniVn7rtDzO6a7c0CwCVewb44+oWA6yuS47Uqtqvl76Q==";
    public void addComponent(Component component) {
        if (component.getViewName().equals(Component.VIEW_TYPE_HIDDENVIEW)) {
            mHiddenComponent.add(component);
        } else {
            mComponents.add(component);
        }
    }

    public void addSubViewComponent(Component component) {
        mSubViewComponents.add(component);
    }

    public List<Component> getComponents() {
        return mComponents;
    }

    public List<Component> getSubViewComponents() {
        return mSubViewComponents;
    }


    public void addHiddenComponent(Component component) {
        mHiddenComponent.add(component);
    }

    public List<Component> getHiddenComponent() {
        return mHiddenComponent;
    }

    /**
     * 待办详情页面动态摆放控件   根据标签名称 view
     * @param contentView
     * @param customProgress
     */
    public void loadViewComponents(ViewGroup contentView, CustomProgress customProgress) {
        final Context context = contentView.getContext();
        subViewComponent = new SubViewComponent();
        for (Component component : getComponents()) {
            //满足这两个条件一定是图片签批
            if (component.getViewName().equals(Component.VIEW_TYPE_SIGNVIEW) ) {
                initIAppRevisionInstance();
                    imetaId = component.getLabelValue();
                    fieldName= imetaId;
                    user_name = SharedPreferencesUtil.getString(activity , Contstants.KEY_SP_FILE, Contstants.KEY_SP_LoginName,null);

                    //摆放控件
                    final ViewGroup subView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.task_detailinfo_subview,null);
                    tv_name = (TextView) subView.findViewById(R.id.tv_name);
                    iv_sign = (ImageView) subView.findViewById(R.id.iv_sign);
                    contentView.addView(subView);
                    tv_name.setText(component.getText()+":");
                    iv_sign.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(activity!=null){
                                //showPopupWindow(context,TaskDetailInfoActivity.activity,metaId);
                                showRevisionDialog();
                            }

                        }
                    });
            }
                //满足这个判断的一个条件就是文本签批
               else if (component.getViewName().equals(Component.VIEW_TYPE_TEXTVIEW) ) {
                    ViewGroup childView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.task_detailinfo_textview, null);
                    TextView tv_nameView = (TextView) childView.findViewById(R.id.tv_name);
                    tv_nameView.setText(component.getText() + "：");
                    TextView tv_valueView = (TextView) childView.findViewById(tv_value);
                    tv_valueView.setText(component.getLabelValue());
                    component.setView(tv_valueView);
                    contentView.addView(childView);
                }
                //editview
                else if (component.getViewName().equals(Component.VIEW_TYPE_EDITVIEW)) {
                    ViewGroup childView = (ViewGroup) LayoutInflater.from(context)
                            .inflate(R.layout.task_detailinfo_editview, null);
                    TextView textView = (TextView) childView.findViewById(R.id.tv_name);
                    textView.setText(component.getText() + "：");
                    EditText editText = (EditText) childView.findViewById(R.id.et_value);
                    if (!TextUtils.isEmpty(component.getHintText())) {
                        editText.setText(component.getHintText());
                    }
                    component.setView(editText);
                    contentView.addView(childView);
                }
                //attcahmentview
                else if (component.getViewName().equals(Component.VIEW_TYPE_ATTACHMENTVIEW)) {
                    CustomAttachmentView attachmentView = new CustomAttachmentView(context, component, customProgress);
                    contentView.addView(attachmentView);
                }
                //dateview
                else if (component.getViewName().equals(Component.VIEW_TYPE_DATEVIEW)) {
                    CustomDateView dateView = new CustomDateView(context, component);
                    if (!TextUtils.isEmpty(component.getHintText())) {
                        dateView.setDate(component.getHintText());
                    }
                    component.setView(dateView.getDateView());
                    contentView.addView(dateView);
                }
                //selectview
                else if (component.getViewName().equals(Component.VIEW_TYPE_SELECTVIEW)) {
                    CustomSpinner spinnerView = new CustomSpinner(context, (SelectViewComponent) component);
                    contentView.addView(spinnerView);
                }
                //radioview
                else if (component.getViewName().equals(Component.VIEW_TYPE_RADIOVIEW)) {
                    LinearLayout layout = new LinearLayout(context);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 10, 30, 0);
                    layout.setOrientation(LinearLayout.HORIZONTAL);
                    TextView textView = new TextView(context);
                    textView.setTextColor(Color.BLACK);
                    textView.setTextSize(16);
                    textView.setPadding(0,20,0,0);
                    textView.setText(component.getText()+"：");
                    FlowRadioGroup radioGroup = new FlowRadioGroup(context);
                    radioGroup.setOrientation(LinearLayout.HORIZONTAL);
                    String labelvalue = component.getLabelValue();
                    String ReplaceValue = labelvalue.replace("[","")
                            .replace("]","")
                            .replace("'","")
                            .replace("'","");
                    String [] city = ReplaceValue.split(",");
                    for (int j = 0; j < city.length ; j++) {
                        radioButton = new RadioButton(context);
                        radioButton.setText(city[j]);
                        radioGroup.addView(radioButton);
                    }
                    radioGroup.setLayoutParams(lp);
                    layout.addView(textView);
                    layout.addView(radioGroup);
                    ((RadioButton)radioGroup.getChildAt(0)).setChecked(true);
                    contentView.addView(layout);
                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                            RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                            radio = radioButton.getText().toString();
                        }
                    });
                }else if (component.getViewName().equals(Component.VIEW_TYPE_IMAGEVIEW)){
                    CustomSignView signView = new CustomSignView(context, component);
                    contentView.addView(signView);

                }
        }
    }
    private void showRevisionDialog() {
        RevisionNormalDialog revisionNormalDialog = new RevisionNormalDialog(activity, copyRight, user_name, fieldName);
        revisionNormalDialog.showRevisionWindow();
        revisionNormalDialog.setOnFinishListener(new RevisionNormalDialog.OnFinishListener() {

            @Override
            public void setOnFinish(View revisionView,
                                    Bitmap bitmap, String sign_flag) {
                if (bitmap != null) {
                    iv_sign.setImageBitmap(bitmap);
                   boolean boo =  TaskDetailInfoActivity.saveRevisionToNet(WebServiceUtil.BASE_DOWNLOAD_URL+"/webRevision", "", user_name, fieldName, bitmap,apprevison);
                    if (boo){
                        if (!imetaId.equals("")){
                            int metaId = Integer.parseInt(imetaId);
                            TaskDetailInfoActivity.GetDataBasePhotoTask mGetDataBasePhotoTask
                                    = new TaskDetailInfoActivity.GetDataBasePhotoTask(metaId);
                            //保存签批至服务器
                            mGetDataBasePhotoTask.execute();
                        }else {
                        }

                    }else {
                    }



                } else {
                    Toast.makeText(activity, "没有获取到签批数据", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initIAppRevisionInstance() {
        // 获取与iWebRevision进行交互的对象
        if (apprevison == null) {
            apprevison = new iAppRevision_iWebRevision();
            apprevison.setCopyRight(activity, copyRight, "");
        }
    }
}
