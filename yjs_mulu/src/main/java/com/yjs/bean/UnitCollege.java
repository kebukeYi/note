package com.yjs.bean;

import lombok.Data;

/**
 * @author : kebukeYi
 * @date :  2021-10-05 15:26
 * @description:
 * @question:
 * @link:
 **/
@Data
public class UnitCollege {
    public String college_code;
    public Long college_id;
    public String college_name;
    public String college_type;
    public String comment;
    public String learning_style;
    public String major_code;
    public String mllb;//门类类别 代码  mldm : 01  zyxw
    public String xklb;//学科类别 代码  yjxkdm
    public String major_name;
    public String proposed_enrollment;
    public String research_direction;
    public Integer state = 1;
    public String test_range_id;
    public String tutor;
    public Long unit_id;
}
 
