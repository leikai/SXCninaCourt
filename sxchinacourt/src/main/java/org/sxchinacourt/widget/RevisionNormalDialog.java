package org.sxchinacourt.widget;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kinggrid.iapprevision.RevisionEntity;
import com.kinggrid.iapprevision.iAppRevisionView;

import org.sxchinacourt.R;
import org.sxchinacourt.bean.ViewComponents;

/**
 * 签名界面类
 */
public class RevisionNormalDialog implements OnClickListener{
	private  boolean isSelect = false;
	/**
	 * 显示对话框所在的Activity
	 */
	private Activity activity;
	/**
	 * 授权码
	 */
	private String copyRight;
	/**
	 * 控制按钮布局
	 */
	private LinearLayout manager_btn_layout;
	/**
	 * 控制按钮：设置、撤销、回退、清屏、保存、关闭等
	 */
	private Button pen_btn, clear_btn,undo_btn, redo_btn ,save_btn,close_btn;
	/**
	 * 完成签批、取消签批
	 */
	private Button demo_save_revision,demo_cancel_revision;
	/**
	 * 金格科技手写控件 
	 */
	private iAppRevisionView demo_revision_view;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 控件名称 
	 */
	private String fieldName;
	/**
	 * 手写签批dialog
	 */
	private Dialog revision_dialog;
	/**
	 * 设备管理类
	 */
	private DisplayMetrics dm = new DisplayMetrics();
	
	/**
	 * 构造方法
	 * @param context
	 * @param lic
	 * @param userName
	 * @param fieldName
	 * @param //scale
	 */
	public RevisionNormalDialog(Activity context, String lic, String userName, String fieldName){
		this.activity = context;
		copyRight = lic;
		this.userName = userName;
		this.fieldName = fieldName;
		
		initDialog();
	}



	/**
	 * 初始化Dialog
	 * @param //scale
	 */
	private void initDialog(){
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		revision_dialog = new Dialog(activity,android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
	    
		View dialog_view = LayoutInflater.from(activity).inflate(R.layout.revision_dialog, null);
		initDialogView(dialog_view);//初始化dialog控件
		initDialogListener();//初始化dialog控件监听
		
		revision_dialog.setContentView(dialog_view);
		WindowManager.LayoutParams lp = revision_dialog.getWindow().getAttributes();
	     //lp.height =  activity.getWindowManager().getDefaultDisplay().getHeight()/2;
		//lp.height = (int) (dm.heightPixels / 2.5); // 高度
//	    lp.height = (int)(dm.widthPixels * scale);
//	    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
	    revision_dialog.getWindow().setAttributes(lp);
	    revision_dialog.getWindow().setGravity(Gravity.BOTTOM); //设置在底部显示
	    revision_dialog.setCancelable(false);
	}
	
	/**
	 * 显示签批窗口
	 */
	public void showRevisionWindow(){
		if(revision_dialog != null && revision_dialog.isShowing()){
			return;
		}
		revision_dialog.show();
		manager_btn_layout.setVisibility(View.VISIBLE);
		demo_revision_view.useWriteSign();
	}
	
	/**
	 * 初始化对话框视图
	 * @param view
	 */
	private void initDialogView(View view) {
		
		//控制按钮：设置、撤销、回退、清屏、保存、关闭等
		manager_btn_layout = (LinearLayout) view.findViewById(R.id.manager_btn_layout);
		pen_btn = (Button) manager_btn_layout.findViewById(R.id.pen_btn);
		clear_btn = (Button) manager_btn_layout.findViewById(R.id.clear_btn);
		undo_btn = (Button) manager_btn_layout.findViewById(R.id.undo_btn);
		redo_btn = (Button) manager_btn_layout.findViewById(R.id.redo_btn);
		save_btn = (Button) manager_btn_layout.findViewById(R.id.save_btn);
		close_btn = (Button) manager_btn_layout.findViewById(R.id.close_btn);
		
		//确定保存或取消
		demo_save_revision = (Button) view.findViewById(R.id.demo_save_revision);
		demo_cancel_revision = (Button) view.findViewById(R.id.demo_cancel_revision);
		//金格控件
		demo_revision_view = (iAppRevisionView) view.findViewById(R.id.demo_revision_view);
	}
	
	private void initDialogListener() {
		initIAppRevisionView();
		
		//设置按钮点击事件监听
		pen_btn.setOnClickListener(this);
		clear_btn.setOnClickListener(this);
		undo_btn.setOnClickListener(this);
		redo_btn.setOnClickListener(this);
		save_btn.setOnClickListener(this);
		close_btn.setOnClickListener(this);
		
		demo_save_revision.setOnClickListener(this);
		demo_cancel_revision.setOnClickListener(this);
	}
	/**
	 * 初始化手写控件
	 */
	private void initIAppRevisionView(){
		//必设，设置授权码
		demo_revision_view.setCopyRight(activity,copyRight); 
		//设置手写笔的颜色、宽高、笔型
		demo_revision_view.configSign(Color.BLACK, 10 * 2, iAppRevisionView.TYPE_BALLPEN);
		//设置控件名称,需要和PC互通时需要保持一致
		demo_revision_view.setFieldName(fieldName); 
	}
	
	/**
	 * 生成签批结果
	 */
	private void create(){
		Bitmap valid_sign_bmp = null;
		valid_sign_bmp = demo_revision_view.saveValidSign();
		if (valid_sign_bmp != null) {
			demo_revision_view.showRevisionImage(valid_sign_bmp,false);
			manager_btn_layout.setVisibility(View.GONE);// 手写控制按钮
			demo_save_revision.setVisibility(View.VISIBLE);
			demo_cancel_revision.setVisibility(View.VISIBLE);
		} else {
			Toast.makeText(activity, "签批内容为空", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 保存最终签批结果
	 */
	private void save(){
		demo_revision_view.setBgIsWhite(true);
//		Bitmap bitmap = demo_revision_view.saveRevisionValidImage(userName,false); //保存有效区域签批内容
		Bitmap sign_bmp = demo_revision_view.saveValidSign();
		Bitmap bitmap = demo_revision_view.getStampTextBitmap(sign_bmp, userName);
		if (bitmap != null) {
				if (revision_dialog != null) {
					revision_dialog.dismiss();
				}
				if (finishListener != null) {
					finishListener.setOnFinish(demo_revision_view,bitmap, RevisionEntity.SIGN_FLAG);
				}

		} else {
			Toast.makeText(activity, "签批内容为空", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.demo_save_revision: // 完成签批
//			save();
			break;
		case R.id.demo_cancel_revision: // 取消签批
			if (revision_dialog != null) {
				revision_dialog.dismiss();
			}
			break;
		case R.id.pen_btn: //设置手写笔的颜色、笔型、粗细等
//			RevisionSettingDialog config = new RevisionSettingDialog(activity, new OnSignChangedListener() {
//				
//				@Override
//				public void changed(int color, int size, int type) {
//					demo_revision_view.configSign(color, size, type);
//				}
//			});
//			config.setProgress(30);
//			config.setKeyName(fieldName + "sign_color", fieldName + "sign_type", fieldName + "sign_size");
//			config.show();
			Toast.makeText(activity, "由于涉及资源较多，为了最快集成，暂未完善，请参考标准版Demo", Toast.LENGTH_SHORT).show();
			break;
		case R.id.clear_btn: //清屏
			demo_revision_view.clearSign();
			break;
		case R.id.undo_btn: //撤销
			demo_revision_view.undoSign();
			break;
		case R.id.redo_btn: //回退
			demo_revision_view.redoSign();
			break;
		case R.id.save_btn: //保存
//			create();
			save();
			break;
		case R.id.close_btn: //关闭
			if (revision_dialog != null) {
				revision_dialog.dismiss();
			}
			break;
		}
	}
	/**
	 * 定义接口：完成签批监听器
	 */
	public interface OnFinishListener {
		/**
		 * 保存签批完成
		 * @param revisionView
		 * @param bitmap
		 * @param sign_flag RevisionEntity.SIGN_FLAG - 手写 
		 *                  RevisionEntity.WORD_FLAG - 文字
		 */
		public void setOnFinish(View revisionView, Bitmap bitmap, String sign_flag);
	}
	/**
	 * 签批完成监听器
	 */
	private OnFinishListener finishListener;
	
	public void setOnFinishListener(OnFinishListener listener){
		finishListener = listener;
	}
}

