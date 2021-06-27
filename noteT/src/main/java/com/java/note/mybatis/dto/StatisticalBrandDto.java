package com.java.note.mybatis.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticalBrandDto {

    private String cityName;

    private List<String> districtList;
    private List<String> comareaList;


    private String brandNameChar;

    private Integer pageindex = 1;
    private Integer pagesize = 5000;
    @JsonIgnore
    private Integer offset;

}
