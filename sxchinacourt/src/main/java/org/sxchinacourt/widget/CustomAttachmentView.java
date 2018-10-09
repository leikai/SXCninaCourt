package org.sxchinacourt.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.bean.Component;
import org.sxchinacourt.util.WebServiceUtil;
import org.sxchinacourt.util.file.FileAccessUtil;
import org.sxchinacourt.util.file.FileOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by baggio on 2017/3/20.
 */

public class CustomAttachmentView extends LinearLayout {
    private static final int PROGRESS_UPDATE = 1;
    private static final int DOWNLOAD_FINISHED = 2;
    private Component mComponent;
    private String mFileName;
    private String mFileUrl;
    private ProgressBar mProgressBar;
    private Dialog mDownloadDialog;
    private Thread mDownLoadThread;
    private boolean mInterceptFlag = false;
    private int mProgress;
    private CustomProgress mCustomProgress;
    private GetDownloadFileUrlTask mGetDownloadFileUrlTask;
    private String mTableName;
    private int mBoid;

    public CustomAttachmentView(Context context, Component component,
                                CustomProgress customProgress) {
        super(context);
        mComponent = component;
        mCustomProgress = customProgress;
        String labelValue = component.getLabelValue();
        if (!labelValue.equals("") ){
            mTableName = labelValue.substring(0, labelValue.indexOf("-"));
            int start = labelValue.indexOf("-") + 1;
            int end = labelValue.indexOf("-", start);
            mBoid = Integer.parseInt(labelValue.substring(start, end));
            mFileName = labelValue.substring(end + 1);
        }
        ViewGroup attachmentView = (ViewGroup) LayoutInflater.from(context)
                .inflate(R.layout.task_detailinfo_attachment, null);
        TextView textView = (TextView) attachmentView.findViewById(R.id.textview);
        textView.setText(component.getText() + "：" + mFileName);
        attachmentView.setOnClickListener(mClickListener);
        addView(attachmentView);
    }

    private View.OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (FileAccessUtil.exists(mComponent.getLabelValue())) {
                openFile();
            } else {
                showPromptDownloadDialog();
            }
        }
    };

    private class GetDownloadFileUrlTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String url = WebServiceUtil.getInstance().downloadFileByFiled(mTableName
                    , mFileName, mBoid);
            return url;
        }

        @Override
        protected void onPreExecute() {
            mCustomProgress.start();
        }

        @Override
        protected void onPostExecute(String url) {
            try {
                if (!TextUtils.isEmpty(url)) {
                    mFileUrl = WebServiceUtil.BASE_DOWNLOAD_URL + url;
                    mInterceptFlag = false;
                    downloadFile();
                    showDownloadDialog();
                } else {
                    Toast.makeText(getContext(), "附件的下载地址无效", Toast.LENGTH_LONG).show();
                }
            } finally {
                mGetDownloadFileUrlTask = null;
                mCustomProgress.stop();
            }
        }

        @Override
        protected void onCancelled() {
            mCustomProgress.stop();
            mGetDownloadFileUrlTask = null;
        }
    }

    private void showPromptDownloadDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    getContext());
            builder.setTitle("下载");
            builder.setMessage("确认要下载附件吗？");
            builder.setPositiveButton("下载",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mGetDownloadFileUrlTask = new GetDownloadFileUrlTask();
                            mGetDownloadFileUrlTask.execute();
                            dialog.dismiss();
                        }
                    });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
            builder.create().show();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void showDownloadDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("文件下载");
            final LayoutInflater inflater = LayoutInflater
                    .from(getContext());
            View v = inflater.inflate(R.layout.download_attachment_progress, null);
            mProgressBar = (ProgressBar) v.findViewById(R.id.progress);
            builder.setView(v);
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mInterceptFlag = true;
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
            mDownloadDialog = builder.create();
            mDownloadDialog.show();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void downloadFile() {
        mDownLoadThread = new Thread(mDownFileRunnable);
        mDownLoadThread.start();
    }

    private void openFile() {
//        String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR) +
//                mComponent.getLabelValue();
//        File file = new File(filePath);
//        Intent intent = FileOpenHelper.getFileIntent(file);
//        if (intent != null) {
//            getContext().startActivity(intent);
//        }
//        String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR) +
//                mComponent.getLabelValue();
//        File file = new File(filePath);
//        Intent intent = null;
//        if (mFileName.endsWith(".doc") || mFileName.endsWith(".docx")) {
//            intent = FileOpenHelper.getWordFileIntent(getActivity(), file);
//        } else if (mFileName.endsWith(".xls") || mFileName.endsWith(".xlsx")) {
//            intent = FileOpenHelper.getExcelFileIntent(getActivity(), file);
//        } else if (mFileName.endsWith(".txt")) {
//            intent = FileOpenHelper.getTextFileIntent(file);
//        } else if (mFileName.endsWith(".pdf")) {
//            intent = FileOpenHelper.getPdfFileIntent(file);
//        } else if (mFileName.endsWith(".jpg") || mFileName.endsWith(".png")) {
//            intent = FileOpenHelper.getImageFileIntent(file);
//        }
//        if (intent != null) {
//            getContext().startActivity(intent);
//        } else {
//            showSelectFileTypeDialog();
//        }
    }

    private void openFile(int type) {
        Intent intent = null;
        String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR) +
                mComponent.getLabelValue();
        File file = new File(filePath);
        switch (type) {
            case 0:
//                intent = FileOpenHelper.getTextFileIntent(file);
                break;
            case 1:
                intent = FileOpenHelper.getAudioFileIntent(file);
                break;
            case 2:
                intent = FileOpenHelper.getVideoFileIntent(file);
                break;
            case 3:
//                intent = FileOpenHelper.getImageFileIntent(file);
                break;
            case 4:
                intent = FileOpenHelper.getApkFileIntent(file);
                break;
            case 5:
                intent = FileOpenHelper.getPPTFileIntent(file);
                break;
        }
        if (intent != null) {
            getContext().startActivity(intent);
        }
    }

    private Runnable mDownFileRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(mFileUrl);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();
                String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR) +
                        mComponent.getLabelValue();
                File file = new File(filePath);
                file.createNewFile();
                file.setWritable(true);
                FileOutputStream fos = new FileOutputStream(file);
                int count = 0;
                byte buf[] = new byte[1024];
                int preProgress = mProgress;
                do {
                    int numread = is.read(buf);
                    count += numread;
                    mProgress = (int) (((float) count / length) * 100);
                    if (preProgress != mProgress) {
                        // 更新进度
                        mHandler.sendEmptyMessage(PROGRESS_UPDATE);
                        preProgress = mProgress;
                    }
                    fos.write(buf, 0, numread);
                    if (count == length) {
                        mHandler.sendEmptyMessage(DOWNLOAD_FINISHED);
                        break;
                    }
                } while (!mInterceptFlag);// 点击取消就停止下载.
                fos.close();
                is.close();
            } catch (Throwable e) {
                mInterceptFlag = true;
                mDownloadError.sendEmptyMessage(0);
            }
        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROGRESS_UPDATE:
                    mProgressBar.setProgress(mProgress);
                    break;
                case DOWNLOAD_FINISHED:
                    mDownloadDialog.dismiss();
                    showPromptOpenFileDialog();
                    break;
                default:
                    break;
            }
        }
    };

    private Handler mDownloadError = new Handler() {
        public void handleMessage(Message msg) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CApplication.getInstance());
            builder.setTitle("下载失败");
            builder.setMessage("网络不给力，再试下呗");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    downloadFile();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    };

    private void showPromptOpenFileDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    getContext());
            builder.setTitle("下载完成");
            builder.setMessage("是否打开文件？");
            builder.setPositiveButton("打开",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            openFile();
                        }
                    });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
            builder.create().show();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void showSelectFileTypeDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    getContext());
            builder.setTitle("打开为");
            builder.setItems(new String[]{"文本", "音频", "视频", "图片", "应用", ""}, new DialogInterface
                    .OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    openFile(which);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
            builder.create().show();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
