
package com.example.datamanagement.youdao;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;
@Data
public class BlngSentsPart {

    @SerializedName("trs-classify")
    private List<TrsClassify> trsClassifyList;


}