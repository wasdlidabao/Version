package com.tz.system.domain.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Data
@Table(name = "t_court_org")
@org.hibernate.annotations.Table(appliesTo = "t_court_org", comment = "法院信息")
@ApiModel("法院信息")
public class CourtOrg implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @ApiModelProperty("机构标识")
    @Column(name = "id")
    private Long id;

    /**
     * 法院编号
     */
    @ApiModelProperty("法院编号")
    @Column(name = "court_no")
    private String courtNo;

    /**
     * 机构编号
     */
    @ApiModelProperty("机构编号")
    @Column(name = "org_code")
    private String orgCode;

    /**
     * 机构名称
     */
    @ApiModelProperty("机构名称")
    @Column(name = "org_name")
    private String orgName;

    /**
     * 机构全称
     */
    @ApiModelProperty("机构全称")
    @Column(name = "org_full_name")
    private String orgFullName;

    /**
     * 父级机构
     */
    @ApiModelProperty("父级机构")
    @Column(name = "parent_id")
    private String parentId;

    /**
     * 机构类型
     */
    @ApiModelProperty("机构类型")
    @Column(name = "org_type")
    private String orgType;

    /**
     * 机构层级
     */
    @ApiModelProperty("机构层级")
    @Column(name = "org_level")
    private String orgLevel;

    /**
     * 是否删除（0:未删1:删除）
     */
    @ApiModelProperty("是否删除（0:未删1:删除）")
    @Column(name = "is_deleted")
    private int isDeleted;

    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    @Column(name = "update_user_id")
    private String updateUserId;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    @Column(name = "create_user_id")
    private String createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 备注说明
     */
    @ApiModelProperty("备注说明")
    @Column(name = "remark")
    private String remark;

    public CourtOrg() {
    }


    static Pattern pattern = Pattern.compile("(?<code>F.*)(?<!0+)");

    /**
     * 东软法院编码
     */
    public String get0FCode() {


        Matcher matcher = pattern.matcher(this.orgCode);
        if (!matcher.find()) {
            return null;
        }

        return "0" + matcher.group("code");
    }

}