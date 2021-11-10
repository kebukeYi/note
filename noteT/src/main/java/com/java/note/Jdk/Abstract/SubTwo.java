package com.java.note.Jdk.Abstract;

import lombok.Data;

/**
 * @author : kebukeyi
 * @date :  2021-07-09 22:07
 * @description :
 * @question :
 * @usinglink :
 **/
@Data
public class SubTwo extends AbstractFat {

    public SubTwo(int abstractNum) {
        super(abstractNum);

    }

    public SubTwo() {
        super();
        System.out.println(map);
        map.put("SubTwo", "SubTwo");
    }

}
 
