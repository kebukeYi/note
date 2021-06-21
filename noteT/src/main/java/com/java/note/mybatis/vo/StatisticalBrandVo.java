package com.java.note.mybatis.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticalBrandVo {

    private Strings cityName;
    private Strings district;
    private Strings comarea;
    private Long brandId;
    private Strings brandName;
}
