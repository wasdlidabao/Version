package com.tz.version.domain.param;

import com.tz.version.domain.ApplicationType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CheckParam {

    @ApiModelProperty(name = "applicationName", value = "项目标识")
    @NotBlank(message = "项目标识不能为空！")
    String applicationName;

    @ApiModelProperty(name = "applicationVersion", value = "项目版本")
    @NotBlank(message = "项目版本不能为空！")
    String applicationVersion;

    @ApiModelProperty(name = "applicationType", value = "项目类型")
    ApplicationType applicationType;

    @ApiModelProperty(name = "tribunalId", value = "法庭编号")
    @NotBlank(message = "法庭编号不能为空！")
    String tribunalId;

}
