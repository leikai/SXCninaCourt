package org.sxchinacourt.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.sxchinacourt.R;
import org.sxchinacourt.adapter.FileAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by baggio on 2017/2/19.
 */

public class FileChooseDialog extends Dialog implements AdapterView.OnItemClickListener{
    private List<String> mItems;
    private List<String> mPaths;
    private String mRootPath = "/sdcard/";
    private String mCurPath = "/";
    private TextView mPath;
    private BaseAdapter mFileAdapter;
    private ListView mFileListView;
    private int mSelectedPosition =-1;
    private FileSelectedListener mListener;
    public FileChooseDialog(Context context,FileSelectedListener listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fileselect);
        mPath = (TextView) findViewById(R.id.mPath);
        mFileListView = (ListView)findViewById(R.id.fileList);
        mFileListView.setOnItemClickListener(this);
        Button buttonCancle = (Button) findViewById(R.id.buttonCancle);
        buttonCancle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                finish();
            }
        });
        getFileDir(mRootPath);

    }

    private void getFileDir(String filePath) {
        mPath.setText(filePath);
        mItems = new ArrayList<String>();
        mPaths = new ArrayList<String>();
        File f = new File(filePath);
        File[] files = f.listFiles();

        if (!filePath.equals(mRootPath)) {
            mItems.add("b1");
            mPaths.add(mRootPath);
            mItems.add("b2");
            mPaths.add(f.getParent());
        }
        if (files==null){

        }else{
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                mItems.add(file.getName());
                mPaths.add(file.getPath());
            }

        }
        mFileAdapter =new FileAdapter(getContext(), mItems, mPaths,mSelectedPosition);
        mFileListView.setAdapter(mFileAdapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("<<<<<<<<"+mSelectedPosition);
        try {
            File file = new File(mPaths.get(position));
            if (file.isDirectory()) {
                mCurPath = mPaths.get(position);
                view.setBackgroundColor(Color.WHITE);
                getFileDir(mPaths.get(position));
            } else{
                view.setSelected(true);
                mSelectedPosition=position;
                mCurPath = mPaths.get(position);
                System.out.println("<<<<<<<<"+position);
                mListener.getFilePath(mCurPath);
                this.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public interface FileSelectedListener{
        public void getFilePath(String path);
    }
}
