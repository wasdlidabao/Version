package com.tz.version.domain.po;

import com.tz.version.domain.ApplicationType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "t_version")
@org.hibernate.annotations.Table(appliesTo = "t_version", comment = "版本信息")
@EntityListeners(AuditingEntityListener.class)
@ApiModel(value = "版本信息")
public class Version implements Serializable {

    @GeneratedValue(generator = "uid")
    @GenericGenerator(name = "uid", strategy = "uuid")
    @Id
    @Column(unique = true, nullable = false, updatable = false, length = 32, columnDefinition = "VARCHAR(32)")
    String id;

    @ApiModelProperty(name = "applicationName", value = "项目标识")
    @Column(name = "application_name", columnDefinition = "varchar(36) COMMENT '项目标识' default null ")
    String applicationName;

    @ApiModelProperty(name = "applicationType", value = "项目类型")
    @Column(name = "application_type", columnDefinition = "varchar(2) COMMENT '项目类型' default null ")
    ApplicationType applicationType;

    @ApiModelProperty(name = "applicationVersion1", value = "项目版本部分1")
    @Column(name = "application_version_1", columnDefinition = "varchar(10) COMMENT '项目版本部分1' default 0 ")
    int applicationVersion1;

    @ApiModelProperty(name = "applicationVersion2", value = "项目版本部分2")
    @Column(name = "application_version_2", columnDefinition = "varchar(10) COMMENT '项目版本部分2' default 0 ")
    int applicationVersion2;

    @ApiModelProperty(name = "applicationVersion3", value = "项目版本部分3")
    @Column(name = "application_version_3", columnDefinition = "varchar(10) COMMENT '项目版本部分3' default 0 ")
    int applicationVersion3;

    @ApiModelProperty(name = "applicationArea", value = "适用范围")
    @Column(name = "application_area", columnDefinition = "TEXT COMMENT '适用范围' default null ")
    String applicationArea;

    @ApiModelProperty(name = "applicationForce", value = "是否强制升级")
    @Column(name = "application_force", columnDefinition = "bit(1) COMMENT '是否强制升级' default false ")
    boolean applicationForce;

    @ApiModelProperty(name = "remark", value = "版本内容")
    @Column(name = "remark", columnDefinition = "TEXT COMMENT '版本内容' default null ")
    String remark;

    @ApiModelProperty(name = "fileName", value = "文件名称")
    @Column(name = "file_name", columnDefinition = "varchar(128) COMMENT '文件名称' default null ")
    String fileName;

    @ApiModelProperty(name = "filePath", value = "本地文件路径")
    @Column(name = "file_path", columnDefinition = "varchar(256) COMMENT '本地文件路径' default null ")
    String filePath;

    @ApiModelProperty("创建时间")
    @CreatedDate
    @Column(name = "create_time")
    Date createTime;

    @ApiModelProperty("修改时间")
    @LastModifiedDate
    @Column(name = "update_time")
    Date updateTime;

    @Column(columnDefinition = "VARCHAR(10) DEFAULT TRUE COMMENT '是否启用（0:否1:是）'", name = "valid")
    @ApiModelProperty("是否启用0否1是")
    boolean valid;

    @Column(columnDefinition = "VARCHAR(10) DEFAULT FALSE COMMENT '是否删除'", name = "deleted")
    @ApiModelProperty("是否删除0否1是")
    boolean deleted;

    @Transient
    String applicationVersion;

    public String getApplicationVersion() {
        return String.format("%s.%s.%s", applicationVersion1, applicationVersion2, applicationVersion3);
    }

}
