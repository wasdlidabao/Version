package com.tz.system.domain.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(value = "TribunalQueryParam", description = "法庭管理查询参数")
@EqualsAndHashCode(callSuper = false)
public class TribunalQueryParam extends PageParam {

    @ApiModelProperty(value = "法院编号", required = true, example = "1856")
    private String courtNo;
    @ApiModelProperty(value = "法庭名称", example = "第一审判庭")
    private String name;
    @ApiModelProperty(value = "是否启用", example = "true")
    private Boolean valid;
}
