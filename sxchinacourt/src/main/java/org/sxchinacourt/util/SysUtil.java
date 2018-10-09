package org.sxchinacourt.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import org.sxchinacourt.CApplication;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class SysUtil {

	public static String getExternalStoragePath() {

		// 获取SdCard状态

		String state = Environment.getExternalStorageState();

		// 判断SdCard是否存在并且是可用的

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			if (Environment.getExternalStorageDirectory().canWrite()) {
				return Environment.getExternalStorageDirectory()
						.getPath();
			}
		}
		return null;
	}

	public static String getAppStoragePath() {
		String path = getExternalStoragePath();
		if (path == null) {
			return null;
		}
		Application app = CApplication.getInstance();
		path += "/"
				+ app.getResources().getString(
						app.getApplicationInfo().labelRes);
		return path;
	}

	public static String createTempFile(String fileName) throws IOException {
		String filePath = getAppStoragePath();
		if (filePath == null) {
			throw new IOException();
		}
		filePath += "/temp";
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		filePath += "/" + fileName;
		file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();
		return filePath;
	}

	public static String getRealPathFromURI(ContentResolver resolver,
			Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		// 查询数据库
		try {
			Cursor cursor = resolver.query(contentUri, proj, null, null, null);
			if (cursor != null) {
				try {
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					String result = cursor.getString(column_index);
					return result;
				} finally {
					cursor.close();
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	public static Uri createCameraOutputUri(String fileName) {
		File rootsd = Environment.getExternalStorageDirectory();
		File dcim = new File(rootsd.getAbsolutePath() + "/DCIM");
		String folder = dcim.getAbsolutePath();
		Application app = CApplication.getInstance();
		String filePath = folder
				+ "/"
				+ app.getResources().getString(
						app.getApplicationInfo().labelRes) + "/" + fileName;
		File tmpFile = new File(filePath);
		Uri outputFileUri = Uri.fromFile(tmpFile);
		return outputFileUri;
		// ContentValues values = new ContentValues();
		// values.put(Images.Media.DATA, filePath);
		// values.put(Images.Media.TITLE, fileName);
		// values.put(Images.Media.DISPLAY_NAME, fileName);
		// values.put(Images.Media.MIME_TYPE, "image/jpeg");
		// app.getContentResolver().delete(
		// MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
		// Images.Media.TITLE + "='" + fileName + "'", null);
		// return app.getContentResolver().insert(
		// MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	}

	public static void hideSoftInputMethod(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (view == null) {
			return;
		}
		imm.hideSoftInputFromInputMethod(view.getWindowToken(), 0);
	}

	public static void hideSoftInputMethod(Activity context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		View view = context.getCurrentFocus();
		if (view == null) {
			return;
		}
		imm.hideSoftInputFromInputMethod(view.getWindowToken(), 0);
	}

	public static void showSoftInputMethod(Activity context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		View view = context.getCurrentFocus();
		if (view == null) {
			return;
		}
		imm.showSoftInput(view, 0);
	}

	public static void showSoftInputMethod(Activity context, View view) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (view == null) {
			return;
		}
		imm.showSoftInput(view, 0);
	}

	public static void alert(Context context, int message) {
		alert(context, context.getString(message));
	}

	public static void alert(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public static void showDialog(Context context, String title,
			String message, String okstr,
			DialogInterface.OnClickListener okListener, String noStr,
			DialogInterface.OnClickListener noListener,
			boolean canceledOnTouchOutside) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if (!TextUtils.isEmpty(title)) {
			builder.setTitle(title);
		}
		if (!TextUtils.isEmpty(message)) {
			builder.setMessage(message);
		}
		if (!TextUtils.isEmpty(okstr)) {
			builder.setPositiveButton(okstr, okListener);
		}
//		if (!TextUtils.isEmail(noStr)) {
//			builder.setNegativeButton(noStr, noListener);
//		}
		Dialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
		dialog.show();
	}

	public static void autoAdjustTextSize(TextView textview, int[] range,
			int width) {
		float textSize = textview.getTextSize();
		Paint paint = new Paint();
		paint.setTextSize(textSize);
		String tstr = "中";
		float charwidth = paint.measureText(tstr);
		while (width / charwidth > range[1]) {
			paint.setTextSize(++textSize);
			charwidth = paint.measureText(tstr);
		}
		while (width / charwidth < range[0]) {
			paint.setTextSize(--textSize);
			charwidth = paint.measureText(tstr);
		}
		textview.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
	}

	public static String getStringDate(Long date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss");
		String dateString = formatter.format(date);

		return dateString;
	}
}
