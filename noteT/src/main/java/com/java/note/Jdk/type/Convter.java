package com.java.note.Jdk.type;

/**
 * @author : kebukeyi
 * @date :  2021-09-04 18:34
 * @description :
 * @question :
 * @usinglink :
 **/
public interface Convter<T> {

    <T> T createT(Class<T> type) throws IllegalAccessException, InstantiationException;
}
