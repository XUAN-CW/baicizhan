package com.example.datamanagement.youdao;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class YoudaoWord {

    private Ec ec;
    @SerializedName("blng_sents_part")
    private BlngSentsPart blngSentsPart;

}