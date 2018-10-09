package org.sxchinacourt.util.file;

import android.os.Environment;

import org.sxchinacourt.CApplication;
import org.sxchinacourt.util.SysUtil;

import java.io.File;

public abstract class FileAccessUtil {
    public static final String FILE_DIR = "下载/";
    public static boolean hasSdcard() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    public static String getDirBasePath(String dirName) {
        String dir = "/"
                + CApplication
                .getInstance()
                .getResources()
                .getString(
                        CApplication.getInstance()
                                .getApplicationInfo().labelRes) + "/"
                + dirName;
        if (FileAccessUtil.hasSdcard()) {
            String cachePath = SysUtil.getExternalStoragePath();
            return cachePath + dir;
        } else {
            return dir;
        }
    }

    public static String createExportDir(String dir) {
        String fileDir;
        if (FileAccessUtil.hasSdcard()) {
            String cachePath = SysUtil.getExternalStoragePath();
            fileDir = cachePath + "/" + dir;
        } else {
            fileDir = "/" + dir;
        }
        File f = new File(fileDir);
        if (!f.exists()) {
            f.mkdir();
        }
        return fileDir;
    }


    public static String createDir(String dir) {
        String fullPath = getDirBasePath(dir);
        File f = new File(fullPath);
        if (!f.exists()) {
            f.mkdirs();
        }
        return fullPath;
    }

    public static File getFile(String fileFullPath) {
        File f = new File(fileFullPath);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (Exception e) {
                return null;
            }
        }
        return f;
    }

    public static String decodeFileName(String rawName) {
        String fileName = rawName;
        if (fileName.contains("\\")) {
            fileName = fileName.substring(fileName.lastIndexOf("\\"));
        }
        return fileName;
    }

    public static File[] getFillList(String dirPath) {
        File file = new File(dirPath);
        return file.listFiles();
    }

    public static boolean checkEndsWithInStringArray(String checkItsEnd,
                                                     String[] fileEndings) {
        for (String aEnd : fileEndings) {
            if (checkItsEnd.endsWith(aEnd))
                return true;
        }
        return false;
    }

    public static boolean exists(String fileName) {
        boolean exists = false;
        fileName = getDirBasePath(FILE_DIR) + fileName;
        exists = new File(fileName).exists();
        return exists;
    }
}
