package com.example.datamanagement;

import com.example.datamanagement.youdao.TrsClassify;
import com.example.datamanagement.youdao.YoudaoWord;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class NewWordUtil {

    @Test
    public void formatFileName(){
        File newWordDir = new File("new_word");
        for (File wordDir : newWordDir.listFiles()) {
            File[] fileList = Arrays
                    .stream(Objects.requireNonNull(wordDir.listFiles(file -> file.getName().endsWith("png")
                            || file.getName().endsWith("jpg")
                            || file.getName().endsWith("jpeg")
                            || file.getName().endsWith("mp4")
                            || file.getName().endsWith("gif")
                    )))
                    .sorted(Comparator.comparing(File::getName))
                    .toArray(File[]::new);
            for (int i = 0; i < fileList.length; i++) {
                File file = fileList[i];
                System.out.println(file.getAbsolutePath());
                    file.renameTo(new File(file.getParentFile(), Strings.padStart(String.valueOf(10000 + i), 3, '0') + "_" + System.currentTimeMillis() +"."+ Files.getFileExtension(file.getName())));


            }
        }
    }

    @Test
    public void newWordData() throws IOException {
        File newWordDir = new File("new_word");
        System.out.println(newWordDir.getAbsolutePath());

        for (File wordDir : newWordDir.listFiles()) {
            getMp3FromYoudao(wordDir);
            WordResouce wordResouce = getWordResourceFromYoudao(wordDir);
        }
    }

    public static void getMp3FromYoudao(File wordDir) {
        OkHttpClient client = new OkHttpClient();
        String url = "http://dict.youdao.com/dictvoice?type=0&audio=" + wordDir.getName();
        File mp3 = new File(wordDir.getAbsolutePath()  + File.separator + "usSpeech.mp3");
        mp3.getParentFile().mkdirs();
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = new FileOutputStream(mp3);
                ByteStreams.copy(inputStream, fileOutputStream);
                fileOutputStream.close();
                inputStream.close();
                System.out.println("File downloaded and saved as " + mp3.getAbsolutePath());
            } else {
                System.out.println("Failed to download file: " + response.code() + " - " + response.message());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static WordResouce getWordResourceFromYoudao(File wordDir) throws IOException {
        File wordData = new File(wordDir.getAbsolutePath()  + File.separator + "wordData.json");
        wordData.getParentFile().mkdirs();
        OkHttpClient client = new OkHttpClient();
        String url = "http://dict.youdao.com/jsonapi?q=" + wordDir.getName();
        System.out.println(url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        YoudaoWord youdaoWord = gson.fromJson(json, YoudaoWord.class);
        WordResouce wordResouce = new WordResouce();
        wordResouce.setWord(wordDir.getName());
        try {
            wordResouce.setUsphone(youdaoWord.getEc().getWord().get(0).getUsphone());
            wordResouce.setPrototype(youdaoWord.getEc().getWord().get(0).getPrototype());
            List<String> meanList = youdaoWord.getEc().getWord().get(0).getTrs().stream()
                    .map(trs -> trs.getTr().get(0).getL().getI().get(0)).toList();
            List<TrsClassify> shortMeanList = youdaoWord.getBlngSentsPart().getTrsClassifyList().stream()
                    .sorted(Comparator.comparingDouble(classify ->
                            Double.parseDouble(classify
                                    .getProportion().replaceAll("(.+)%", "-$1"))))
                    .toList();
            StringJoiner shortMeanJoiner = new StringJoiner(",");
            for (TrsClassify trsClassify : shortMeanList.subList(1,shortMeanList.size())) {
                if (shortMeanJoiner.toString().length() > 10) {
                    break;
                }
                System.out.println(trsClassify);
                shortMeanJoiner.add(trsClassify.getTr());
            }
            wordResouce.setShortMean(shortMeanJoiner.toString());
            wordResouce.setMeanList(meanList);
        }catch (Exception e){
            wordResouce.setShortMean("");
            e.printStackTrace();
        }

        if(!wordData.exists()){
            Files.write(gson.toJson(wordResouce).getBytes(StandardCharsets.UTF_8), wordData);

        }
        return wordResouce;
    }
}
