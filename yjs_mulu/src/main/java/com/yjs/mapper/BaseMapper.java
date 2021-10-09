package com.yjs.mapper;

import com.yjs.bean.Base;

import java.util.List;

/**
 * @author : kebukeyi
 * @date :  2021-10-05 13:14
 * @description :
 * @question :
 * @usinglink :
 **/
public interface BaseMapper {

    int insertBatchSSSBase(List<Base> bases);

    int insertBatchMISBase(List<Base> bases);

    int insertBatchZYSBase(List<Base> bases);

    int insertBatchZYSSBase(List<Base> bases);


}
