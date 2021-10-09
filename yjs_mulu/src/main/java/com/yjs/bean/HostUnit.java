package com.yjs.bean;

import lombok.Data;

/**
 * @author : kebukeYi
 * @date :  2021-10-05 14:01
 * @description:
 * @question:
 * @link:
 **/
@Data
public class HostUnit {
    public Integer doctoral_degree;
    public Integer graduate_school;
    public Integer self_marking;
    public Integer state = 1;
    public String unit_address;
    public String unit_code;
    public Long unit_id;
    public String unit_name;

    public HostUnit(String unit_address, String unit_code, Long unit_id, String unit_name) {
        this.unit_address = unit_address;
        this.unit_code = unit_code;
        this.unit_id = unit_id;
        this.unit_name = unit_name;
    }

    public HostUnit() {
    }
}
 
