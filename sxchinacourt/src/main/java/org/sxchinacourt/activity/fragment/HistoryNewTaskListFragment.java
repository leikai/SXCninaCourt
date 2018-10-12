package org.sxchinacourt.activity.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;
import org.sxchinacourt.util.file.FileAccessUtil;
import org.sxchinacourt.util.file.FileOpenHelper;
import org.sxchinacourt.widget.CustomProgress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author baggio
 * @date 2017/2/7
 */

public class HistoryNewTaskListFragment extends BaseFragment {
    private Context context;
    private int resId;
    /**
     * 获取当前的token
     */
    private String ceshi;
    /**
     * 已办事项显示的webView
     */
    private WebView webviewMsg;
    /**
     * 等待中。。。图标
     */
    private CustomProgress mCustomProgress;
    /**
     * 网页加载，显示的进度条
     */
    private ProgressBar mProgressBar;
    /**
     * 获取附件下载地址的任务
     */
    private GetDownloadFileUrlTask mGetDownloadFileUrlTask;
    /**
     * 附件下载地址
     */
    private String mFileUrl;
    /**
     * 下载时的截获标志，
     */
    private boolean mInterceptFlag = false;
    private int mProgress;
    /**
     * 下载线程
     */
    private Thread mDownLoadThread;
    /**
     * 下载附件的弹窗
     */
    private Dialog mDownloadDialog;
    /**
     * 文件名
     */
    private String mFileName;
    /**
     * 下载中，更新进度的任务
     */
    private static final int PROGRESS_UPDATE = 1;
    /**
     * 处理下载完成后的任务
     */
    private static final int DOWNLOAD_FINISHED = 2;
    /**
     * 从页面中获取目前H5所在的界面
     */
    public static String pageLocationForH5 = "2" ;

    @SuppressLint({"NewApi", "ValidFragment"})
    public HistoryNewTaskListFragment() {

    }
    @SuppressLint({"NewApi", "ValidFragment"})
    public HistoryNewTaskListFragment(Context context, int resId) {
        this.context = context;
        this.resId = resId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = "已办列表";
        mLogo = View.INVISIBLE;
        mShowBtnBack = View.INVISIBLE;
        mShowSearchBar = View.GONE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, R.layout.fragment_msg, container,
                savedInstanceState);
    }

    @Nullable
    @Override
    protected void initFragment(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.initFragment(container, savedInstanceState);
        mCustomProgress = (CustomProgress) mRootView.findViewById(R.id.loading);
        webviewMsg = (WebView) mRootView.findViewById(R.id.fg_webview_msg);
        ceshi  = CApplication.getInstance().getCurrentToken();
        webviewMsg.getSettings().setJavaScriptEnabled(true);//设置支持js
        webviewMsg.addJavascriptInterface(new TestJavaScriptInterface(),"android");
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
//        //-------------------------------------------------待办---------------------------------------------- //
//        webviewMsg.loadUrl("http://192.168.3.61:8080/mcourtoa/moffice/sign/list_daiban.html?token="+ceshi);
//        //-----------------------------------------------------------------------//
        //--------------------------------------------------已办---------------------------------------------//
        webviewMsg.loadUrl("http://111.53.181.200:8087/mcourtoa/moffice/sign/list_yiban.html?token="+ceshi);
        //-----------------------------------------------------------------------------------------------------//

//        //-------------------------------------------------------通知公告--------------------------------------------//
//        webviewMsg.loadUrl("http://192.168.3.61:8080/mcourtoa/moffice/sign/list_yiban.html?token="+ceshi+"&flowname=tzgg");
//        //-----------------------------------------------------------------------------------------------------------//

//        //--------------------------------------------ceshi--------------------------------------//
//        webviewMsg.loadUrl("http://192.168.3.95:8080/mcourtoa/moffice/sign/list_daiban.html?token="+ceshi);
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


    /**
     * 显示下载弹窗
     */
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

    /**
     * 任务：获取附件下载地址
     */
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

    /**
     * 显示下载文件弹窗
     */
    private void showDownloadDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
    /**
     * 下载
     */
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
                filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName) ;
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
                    // 点击取消就停止下载.
                } while (!mInterceptFlag);
                fos.close();
                is.close();
            } catch (Throwable e) {
                mInterceptFlag = true;
                mDownloadError.sendEmptyMessage(0);
            }
        }
    };

    @SuppressLint("HandlerLeak")
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
    /**
     * 下载失败时，进行的处理
     */
    @SuppressLint("HandlerLeak")
    private Handler mDownloadError = new Handler() {
        public void handleMessage(Message msg) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
    /**
     * 弹窗：下载完成后，是否打开文件
     */
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
    /**
     * 打开文件
     * @param which
     */
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
            intent = FileOpenHelper.getWordFileIntent(getActivity(),file);
        }else if (mFileName.endsWith(".docx")){
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getWordFileIntent(getActivity(),file);
        }else if (mFileName.endsWith(".xls")) {
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getExcelFileIntent(getActivity(),file);
        }else if (mFileName.endsWith(".xlsx")){
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getExcelFileIntent(getActivity(),file);
        }else if (mFileName.endsWith(".txt")) {
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getTextFileIntent(getActivity(),file);
        } else if (mFileName.endsWith(".pdf")) {
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getPdfFileIntent(getActivity(),file);
        } else if (mFileName.endsWith(".jpg")  ) {
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getImageFileIntent(getActivity(),file);
        }else if (mFileName.endsWith(".png")){
            String filePath = FileAccessUtil.getDirBasePath(FileAccessUtil.FILE_DIR+mFileName);
            File file = new File(filePath);
            intent = FileOpenHelper.getImageFileIntent(getActivity(),file);
        }
        if (intent != null) {
            getActivity().startActivity(intent);
        } else {
            showSelectFileTypeDialog();
        }
    }

    /**
     * 弹窗：选择打开文件的类型
     */
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
    /**
     * 供H5页面调用的方法，目的是：获取现在H5页面处于几级页面下，原生处理返回键功能
     */
    public static class TestJavaScriptInterface {


        @JavascriptInterface
        public void getPageLocation(String location) {
            pageLocationForH5 = location;

            Log.e("location",""+location);

        }
    }
}
