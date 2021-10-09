package com.yjs.bean;

import lombok.Data;

/**
 * @author : kebukeYi
 * @date :  2021-10-05 23:32
 * @description:
 * @question:
 * @link:
 **/
@Data
public class Request {

    String url;
    String path;
    String action;
    String ssdm;//城市代码
    String mldm;//门类类别 代码
    String yjxkdm;//学科类别 代码
    String zymc;//专业名称 代码
    String xxfs;

    public Request(String url, String path, String action, String ssdm, String mldm, String yjxkdm, String zymc, String xxfs) {
        this.url = url;
        this.path = path;
        this.action = action;
        this.ssdm = ssdm;
        this.mldm = mldm;
        this.yjxkdm = yjxkdm;
        this.zymc = zymc;
        this.xxfs = xxfs;
    }

    public Request() {
    }
}
 
