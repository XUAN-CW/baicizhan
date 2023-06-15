package com.example.datamanagement.youdao;
import lombok.Data;

import java.util.List;

@Data
public class Word {
    private String usphone;
    private String ukphone;
    private String ukspeech;
    private List<Trs> trs;
    private List<Wfs> wfs;
    private String usspeech;
    private String prototype;

}