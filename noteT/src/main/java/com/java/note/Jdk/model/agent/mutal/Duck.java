package com.java.note.Jdk.model.agent.mutal;

/**
 * @author : kebukeyi
 * @date :  2021-04-23 18:12
 * @description :
 * @usinglink :
 **/
public class Duck implements Cooking {

    @Override
    public String barbecue(String food) {
        System.out.println("大火焖烤[ " + food + " ] 肥而不腻 甜咸刚好");
        return "zhenneidao";
    }
}
 
