package com.tz.system.domain.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.MappedSuperclass;

/**
 * 分页信息
 */
@Data
@MappedSuperclass
public class PageParam {

    @ApiModelProperty(value = "页数，不传查询全部", example = "1")
    private Integer page;

    @ApiModelProperty(value = "每页条数", example = "10")
    private Integer size;
}
