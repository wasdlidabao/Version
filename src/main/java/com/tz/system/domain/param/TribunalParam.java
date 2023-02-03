package com.tz.system.domain.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@ApiModel(value = "TribunalParam", description = "添加/修改法庭参数")
public class TribunalParam {

    @ApiModelProperty(value = "法庭编号")
    private String id;

    @ApiModelProperty(value = "法庭名称", example = "第一审判庭")
    private String name;

    @ApiModelProperty(value = "法院编号", example = "1856")
    private String courtNo;

    @ApiModelProperty(value = "是否启用", example = "true")
    private Boolean valid;

}
