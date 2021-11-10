package com.java.note.Jdk.Abstract;

import lombok.Data;

import java.util.Map;

/**
 * @author : kebukeyi
 * @date :  2021-07-09 22:06
 * @description :
 * @question :
 * @usinglink :
 **/
@Data
public class SubOne extends AbstractFat {

    public SubOne(int abstractNum) {
        super(abstractNum);
        System.out.println(map);
        map.put("SubOne", "SubOne");
    }

    public void excute(Map<String, String> map) {
        map.put("excetc", "excetc");
    }
}
 
