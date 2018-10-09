package org.sxchinacourt.activity.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.activity.LoginActivity;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.common.Contstants;
import org.sxchinacourt.util.file.FileAccessUtil;
import org.sxchinacourt.util.file.FileOpenHelper;
import org.sxchinacourt.widget.CustomProgress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Leave1Activity extends Activity {
    private Context context;
    private int resId;
    private String ceshi;
    private WebView webviewMsg;


    private CustomProgress mCustomProgress;

    private ProgressBar mProgressBar;
    private GetDownloadFileUrlTask mGetDownloadFileUrlTask;
    private String mFileUrl;

    private boolean mInterceptFlag = false;
    private int mProgress;

    private Thread mDownLoadThread;
    private Dialog mDownloadDialog;

    private String mFileName;
    private String pageLocationForH5 = "";//从页面中获取目前H5所在的界面
    private UserNewBean user;


    private static final int PROGRESS_UPDATE = 1;
    private static final int DOWNLOAD_FINISHED = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_leave1);
        mCustomProgress = (CustomProgress) findViewById(R.id.loading);
        webviewMsg = (WebView) findViewById(R.id.fg_webview_msg);
        user = CApplication.getInstance().getCurrentUser();
        ceshi  = CApplication.getInstance().getCurrentToken();
        webviewMsg.getSettings().setJavaScriptEnabled(true);//设置支持js
        webviewMsg.addJavascriptInterface(new TodoTaskListFragment.TestJavaScriptInterface(),"android");
        webviewMsg.getSettings().setDomStorageEnabled(true);//打开DOM存储API
        webviewMsg.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);//设置缓存模式：不使用缓存
        webviewMsg.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//                return super.onJsAlert(view, url, message, result);
                pageLocationForH5 = message;
                Log.e("pageLocationForH5",""+pageLocationForH5  );
                result.confirm();
                return true;
            }
        });
        webviewMsg.setWebViewClient(new WebViewClient());
        Log.e("取出来的token",""+ceshi);


        if (user.getOrgid() == null){
            startActivity(new Intent(Leave1Activity.this, LoginActivity.class));
            finish();
        }else if (user.getOrgid().equals(Contstants.OID_JINZHONG)){
            //-----------------------------------------------正式----------------------------------------------//
            webviewMsg.loadUrl("http://111.53.181.200:8087/mcourtoa/moffice/sign/doffice/bggl/jgqj/html/jgqj-daiban-add.html?token="+ceshi);
            //--------------------------------------------finish--------------------------------------//
        }else if (user.getOrgid().equals(Contstants.OID_YUSHE)){
            webviewMsg.loadUrl("http://111.53.181.200:8087/mcourtoa/moffice/sign/doffice/bggl/jgqj_ys/html/jgqj-daiban-add.html?token="+ceshi);
        }else if (user.getOrgid().equals(Contstants.OID_LINGSHI)){

        }




//        //-----------------------------------------------测试----------------------------------------------//
//        webviewMsg.loadUrl("http://192.168.1.114:8088/mcourtoa/moffice/sign/doffice/bggl/jgqj/html/jgqj-daiban-add.html?token="+ceshi);
//        //--------------------------------------------finish--------------------------------------//

        webviewMsg.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http"))
                {
                    mFileUrl = url;
                    mFileName = mFileUrl.substring(mFileUrl.length()-36);
                    showPromptDownloadDialog();

//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                }
            }
        });
    }



    private void showPromptDownloadDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    Leave1Activity.this);
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


    private class GetDownloadFileUrlTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            return mFileUrl;
        }

        @Override
        protected void onPreExecute() {
            mCustomProgress.start();
        }

        @Override
        protected void onPostExecute(String url) {
            try {
                if (!TextUtils.isEmpty(url)) {
                    mInterceptFlag = false;
                    downloadFile();
                    showDownloadDialog();
                } else {
                    Toast.makeText(Leave1Activity.this, "附件的下载地址无效", Toast.LENGTH_LONG).show();
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


    private void showDownloadDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(Leave1Activity.this);
            builder.setTitle("文件下载");
            final LayoutInflater inflater = LayoutInflater
                    .from(Leave1Activity.this);
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

                Log.e("lk",""+mFileUrl.substring(mFileUrl.length()-36));
                String filePath = "";
//                if (mFileName.endsWith(".doc") ) {
//                    filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+"1"+".doc") ;
//                }else if (mFileName.endsWith(".docx")){
//                    filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+"2"+".docx") ;
//
//                }else if (mFileName.endsWith(".xls") ) {
//                    filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+"3"+".xls") ;
//                }else if (mFileName.endsWith(".xlsx")){
//                    filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+"4"+".xlsx") ;
//
//                }else if (mFileName.endsWith(".txt")) {
//                    filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+"5"+".txt") ;
//                } else if (mFileName.endsWith(".pdf")) {
//                    filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+"6"+".pdf") ;
//                } else if (mFileName.endsWith(".jpg") ) {
//                    filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+"7"+".jpg") ;
//                }else if (mFileName.endsWith(".png")){
//                    filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+"8"+".png") ;
//
//                }
                filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName) ;

//                String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileUrl.substring(mFileUrl.length()-36)) ;
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
            AlertDialog.Builder builder = new AlertDialog.Builder(Leave1Activity.this);
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
                    Leave1Activity.this);
            builder.setTitle("下载完成");
            builder.setMessage("是否打开文件？");
            builder.setPositiveButton("打开",
                    new DialogInterface.OnClickListener() {
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

    private void openFile(int which) {
//        String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR) +
//                mComponent.getLabelValue();
//        File file = new File(filePath);
//        Intent intent = FileOpenHelper.getFileIntent(file);
//        if (intent != null) {
//            getContext().startActivity(intent);
//        }

        Intent intent = null;
        if (mFileName.endsWith(".doc")) {
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getWordFileIntent(Leave1Activity.this,file);
        }else if (mFileName.endsWith(".docx")){
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getWordFileIntent(Leave1Activity.this,file);
        }else if (mFileName.endsWith(".xls")) {
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getExcelFileIntent(Leave1Activity.this,file);
        }else if (mFileName.endsWith(".xlsx")){
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getExcelFileIntent(Leave1Activity.this,file);
        }else if (mFileName.endsWith(".txt")) {
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getTextFileIntent(Leave1Activity.this,file);
        } else if (mFileName.endsWith(".pdf")) {
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getPdfFileIntent(Leave1Activity.this,file);
        } else if (mFileName.endsWith(".jpg")  ) {
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getImageFileIntent(Leave1Activity.this,file);
        }else if (mFileName.endsWith(".png")){
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getImageFileIntent(Leave1Activity.this,file);
        }
        if (intent != null) {
            startActivity(intent);
        } else {
            showSelectFileTypeDialog();
        }
    }


    private void showSelectFileTypeDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    Leave1Activity.this);
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
