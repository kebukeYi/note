package com.java.note.mybatis.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticalBrandVo {

    private String cityName;
    private String district;
    private String comarea;
    private Long brandId;
    private String brandName;
}
