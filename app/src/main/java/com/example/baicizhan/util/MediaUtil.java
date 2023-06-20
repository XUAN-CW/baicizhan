package com.example.baicizhan.util;

import java.io.File;

public class MediaUtil {
    public static Boolean isImage(String url){
        File file = new File(url);
        return file.isFile()
                && (file.getName().toLowerCase().endsWith(".jpg")
                || (file.getName().toLowerCase().endsWith(".jpeg")
                || file.getName().toLowerCase().endsWith(".png"))
                || file.getName().toLowerCase().endsWith(".gif"));
    }

    public static Boolean isVideo(String url){
        File file = new File(url);
        return file.isFile()
                && (file.getName().toLowerCase().endsWith(".mp4"));
    }

    public static Boolean isMedia(String url){
        return isVideo(url) || isImage(url);
    }
}
