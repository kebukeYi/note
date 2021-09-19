package com.mmy.mvc.bean;

import com.mmy.mvc.enums.CheckType;
import com.mmy.mvc.validated.check;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @author : kebukeYi
 * @date :  2021-09-18 21:14
 * @description:
 * @question:
 * @link:
 **/
@SuppressWarnings("serial")
@Data
@ToString
public class UserDto {

    /**
     * 32位流水号
     */
    @check(type = CheckType.REQUEST_NO, message = "请求流水号格式错误")
    private String requestNo;

    @check(type = CheckType.TOOLOOG_DATE, message = "时间戳格式不正确，格式：yyyyMMddHHmmssSSS")
    @NotBlank(message = "时间戳不能为空")
    private String timestamp;


}
 
