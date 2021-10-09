package com.yjs.mapper;

import com.yjs.bean.TestRange;

import java.util.List;

/**
 * @author : kebukeyi
 * @date :  2021-10-05 15:37
 * @description :
 * @question :
 * @usinglink :
 **/
public interface TestRangeMapper {
    int insertBatchTestRangeBase(List<TestRange> testRanges);
}
