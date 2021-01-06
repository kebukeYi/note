package com.java.note.Jdk.reflaft.one;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author : fang.com
 * @CreatTime : 2021-01-06 12:52
 * @Description :
 * @Version :  0.0.1
 */
@Data
public class DictionaryConstant {

    static Map<String, DictDTO> dictionaryMap = new HashMap();
}
