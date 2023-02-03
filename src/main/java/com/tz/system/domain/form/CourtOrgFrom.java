package com.tz.system.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CourtOrgFrom implements Serializable {

    @ApiModelProperty("法院编号")
    private String courtNo;

    @ApiModelProperty("机构编号")
    private String orgCode;

    @ApiModelProperty("机构名称")
    private String orgName;

    @ApiModelProperty("机构全称")
    private String orgFullName;

    @ApiModelProperty("父级机构")
    private String parentId;

    @ApiModelProperty("机构类型")
    private String orgType;

    @ApiModelProperty("机构层级")
    private String orgLevel;

    @ApiModelProperty("是否删除（0:未删1:删除）")
    private int isDeleted;

    @ApiModelProperty("更新人")
    private String updateUserId;

    @ApiModelProperty("创建人")
    private String createUserId;


}
