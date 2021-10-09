package com.yjs.bean;

import lombok.Data;

/**
 * @author : kebukeYi
 * @date :  2021-10-05 13:15
 * @description:
 * @question:
 * @link:
 **/
@Data
public class Base {
    String mc;
    String dm;

    public Base(String mc, String dm) {
        this.mc = mc;
        this.dm = dm;
    }
}
 
