//package com.example.baicizhan.util;
//
//import android.util.Log;
//
//import org.apache.commons.lang3.ArrayUtils;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Locale;
//import java.util.stream.Collectors;
//
//public class PathUtil {
//
//    private static File baicizhanResourceRootDir = null;
//    private static File wordResourceRootDir = null;
//    private static File databaseFile = null;
//    private static File gitRepositoryDir = null;
//
//    private static final String wordDataFile = "wordData.json";
//    private static final String usSpeechFile = "usSpeech.mp3";
//
//
//    public static void init(File dir){
//        if(baicizhanResourceRootDir == null){
//            baicizhanResourceRootDir = new File(dir, "baicizhanResourceRoot");
//            baicizhanResourceRootDir.mkdirs();
//            gitRepositoryDir =  new File(baicizhanResourceRootDir,"repository");
//            gitRepositoryDir.mkdirs();
//            wordResourceRootDir =  new File(gitRepositoryDir, "data-management/baicizhanResourceRoot/wordResourceRoot");
//            wordResourceRootDir.mkdirs();
//            databaseFile = new File(baicizhanResourceRootDir,"baicizhan.db");
//        }
//    }
//
//    public static File getWordResourceRootDir() {
//        return wordResourceRootDir;
//    }
//
//    public static File getDatabaseFile() {
//        return databaseFile;
//    }
//
//
//
//    public static File getUsSpeechFile(String word) {
//        return new File(getWordResourceRootDir().getAbsoluteFile() + File.separator + word + File.separator + PathUtil.usSpeechFile);
//    }
//
//    public static File getWordDataFile(String word) {
//        return new File(getWordResourceRootDir().getAbsoluteFile() + File.separator + word + File.separator + PathUtil.wordDataFile);
//    }
//}
