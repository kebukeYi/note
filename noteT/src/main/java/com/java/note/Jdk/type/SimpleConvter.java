package com.java.note.Jdk.type;

/**
 * @author : kebukeYi
 * @date :  2021-09-04 18:34
 * @description:
 * @question:
 * @link:
 **/
public class SimpleConvter implements Convter<String> {

    @Override
    public <T> T createT(Class<T> type) throws IllegalAccessException, InstantiationException {
        return (T) type.getClass().newInstance();
    }

}
 
