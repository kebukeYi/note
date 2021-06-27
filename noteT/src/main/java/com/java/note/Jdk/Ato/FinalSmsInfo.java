package com.java.note.Jdk.Ato;

import lombok.Data;

/**
 * @author : kebukeyi
 * @date :  2021-06-05 14:22
 * @description :
 * @question :
 * @usinglink :
 **/
@Data
public final class FinalSmsInfo {


    private final Integer id;
    private final String url;
    private final Long maxSize;

    public FinalSmsInfo(Integer id, String url, Long maxSize) {
        this.id = id;
        this.url = url;
        this.maxSize = maxSize;
    }

    public FinalSmsInfo(FinalSmsInfo finalSmsInfo) {
        this.id = finalSmsInfo.getId();
        this.maxSize = finalSmsInfo.getMaxSize();
        this.url = finalSmsInfo.getUrl();
    }
}
 
