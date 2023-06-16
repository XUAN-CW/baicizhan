package com.example.baicizhan.util;

import android.content.Context;
import android.util.Log;

import com.example.baicizhan.entity.LearningRecord;

import java.io.File;

public class BaicizhanPathUtil {

    private static File wordResourceRootDir = null;
    private static File learningRecord = null;
    private static File databaseFile = null;



    public static File getWordResourceRootDir() {
        return wordResourceRootDir;
    }

    public static void init(Context context){
        if(wordResourceRootDir == null) {
            wordResourceRootDir = context.getExternalFilesDir("wordResourceRoot");
            databaseFile = new File(context.getExternalFilesDir("database"),"baicizhan.db");
        }
    }


    public static File getDatabaseFile() {
        return databaseFile;
    }
}
