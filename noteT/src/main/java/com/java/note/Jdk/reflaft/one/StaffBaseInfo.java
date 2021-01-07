package com.java.note.Jdk.reflaft.one;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @Author : fang.com
 * @CreatTime : 2021-01-06 12:40
 * @Description : 教职工可对自己的各方面信息（比如基本情况、论文著作等）进行纠错，然后每次修改发生变化的内容需要对应的审核部门进行审核通过以后才能真正修改库数据，
 * 不能写一堆的if-else对每个字段进行判断,最后想到用Java的反射机制并结合自定义属性注解，拿到新旧对象所有的属性集合，包括属性名称(name)、
 * 属性中文注释(姓名)以及属性值(张三)，然后拿着两个属性集合再进行比对，拿到本次修改发生变化的属性信息。
 * @Version :  0.0.1
 */
@Data
@TableName("staff_base_info")
public class StaffBaseInfo implements Serializable {

    @TableId
    private String id;
    /**
     * 单位号
     */
    @PropertyName(name = "单位号")
    private String deptNumber;
    /**
     * 工号
     */
    @PropertyName(name = "工号")
    private String workNumber;
    /**
     * 姓名
     */
    @PropertyName(name = "姓名")
    @Size
    private String name;
    /**
     * 性别
     */
    @PropertyName(name = "性别")
    private String gender;
    /**
     * 出生日期
     */
    @PropertyName(name = "出生日期")
    private String birthday;
    /**
     * 身份证件类型
     */
    @PropertyName(name = "身份证件类型")
    private String idType;
    /**
     * 身份证件号码
     */
    @PropertyName(name = "身份证件号码")
    private String idNumber;
    /**
     * 民族
     */
    @PropertyName(name = "民族")
    private String nation;
    /**
     * 政治面貌
     */
    @PropertyName(name = "政治面貌")
    private String politicsStatus;

    /**
     * 出生地
     */
    @PropertyName(name = "出生地")
    @NonNull
    private String BirthPlace;

    /**
     * 参加工作时间
     */
    @PropertyName(name = "参加工作时间")
    private String workTime;
    /**
     * 到校（院）工作时间
     */
    @PropertyName(name = "到校（院）工作时间")
    private String schoolWorkTime;
    /**
     * 最高学历
     */
    @PropertyName(name = "最高学历")
    private String highestEducation;
    /**
     * 最高学位
     */
    @PropertyName(name = "最高学位")
    private String highestDegree;
}
