package com.example.rocksdb.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @ClassName RocksDBVo
 * @Author kebukeyi
 * @Date 2022/8/15 10:59
 * @Description
 * @Version 1.0.0
 */
@ApiModel("RocksDBVO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RocksDBVo implements Serializable {

    @NonNull
    @ApiModelProperty(value = "列族", required = true)
    @Builder.Default
    private String cfName = "default";
    @NonNull
    @ApiModelProperty(value = "键", required = true)
    private String key;
    @ApiModelProperty(value = "值", required = true)
    private String value;
}
