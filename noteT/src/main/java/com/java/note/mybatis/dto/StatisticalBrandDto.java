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

    private Strings cityName;

    private List<Strings> districtList;
    private List<Strings> comareaList;


    private Strings brandNameChar;

    private Integer pageindex = 1;
    private Integer pagesize = 5000;
    @JsonIgnore
    private Integer offset;

}
