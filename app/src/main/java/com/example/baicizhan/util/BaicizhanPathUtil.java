package com.example.baicizhan.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class BaicizhanPathUtil {

    private static File wordResourceRootDir = null;
    private static File gitRepositoryDir = null;
    public static final String cloneUrl = "https://github.com/XUAN-CW/baicizhan-data";
    private static File databaseFile = null;



    public static File getWordResourceRootDir() {
        return wordResourceRootDir;
    }

    public static void init(Context context){
        if(wordResourceRootDir == null) {
            wordResourceRootDir = context.getExternalFilesDir("wordResourceRoot");
            gitRepositoryDir = context.getExternalFilesDir("repository");
            databaseFile = new File(context.getExternalFilesDir("data"),"baicizhan.db");
        }
    }

    public static File getGitRepositoryDir() {
        return gitRepositoryDir;
    }

    public static File getDatabaseFile() {
        return databaseFile;
    }
}
