package com.tz.system.domain.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "t_tribunal")
@org.hibernate.annotations.Table(appliesTo = "t_tribunal", comment = "法庭信息")
@ApiModel("法庭信息")
public class Tribunal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(unique = true, nullable = false, updatable = false, length = 36, columnDefinition = "VARCHAR(36)")
    private String id;

    @Column(name = "name")
    @ApiModelProperty("法庭名称")
    private String name;

    @ApiModelProperty("法院ID")
    @Column(name = "court_id")
    private Long courtId;

    @ApiModelProperty("法院编号")
    @Column(name = "court_no")
    private String courtNo;

    @ApiModelProperty("法院名称")
    @Column(name = "court_name")
    private String courtName;

    @Column(name = "valid")
    @ApiModelProperty("是否启用0否1是")
    private boolean valid;

    @Column(name = "deleted")
    @ApiModelProperty("是否删除0否1是")
    private boolean deleted;

    @ApiModelProperty("创建时间")
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建人ID")
    @Column(name = "create_user_id")
    private String createUserId;

    @ApiModelProperty("修改人ID")
    @Column(name = "update_user_id")
    private String updateUserId;

}
