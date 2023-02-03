package com.tz.system.domain.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@ApiModel(value = "TribunalUpdateStatusParam", description = "修改法庭启用禁用状态参数")
public class TribunalUpdateStatusParam {
    @ApiModelProperty(value = "法庭ID", required = true, example = "court_1856_abc_001")
    private String id;

    @ApiModelProperty(value = "是否启用，启用true禁用false", required = true, example = "true")
    private Boolean valid;

}
