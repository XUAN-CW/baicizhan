package com.example.datamanagement;

import lombok.Data;

import java.util.List;

@Data
public class WordResouce {

    String word;
    String shortMean;
    String usphone;

    String image;
    List<String> meanList;
    String prototype;
    String createTime;
    String updateTime;
}
