package com.java.note.Jdk.model.strategy.pay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : kebukeyi
 * @date :  2021-05-07 12:20
 * @description :
 * @question :
 * @usinglink :
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResult {

    private int result;
    private int code;
    private String message;

}
 
