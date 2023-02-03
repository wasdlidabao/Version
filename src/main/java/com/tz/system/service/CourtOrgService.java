package com.tz.system.service;

import com.tz.system.domain.form.CourtOrgFrom;
import com.tz.system.domain.po.CourtOrg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourtOrgService {

    /**
     * 获取开启智慧庭审的法院列表
     *
     * @return 法庭集合
     */
    List<CourtOrg> listByStartAits();

    /**
     * 更新开启智慧庭审状态
     *
     * @param courtNo   法院编号
     * @param startAits 状态
     */
    void updateStart(String courtNo, Boolean startAits);

    /**
     * 保存法庭相关信息
     *
     * @param courtOrg 参数体
     * @return 法庭相关信息
     */
    CourtOrg save(CourtOrg courtOrg);

    /**
     * 根据编号删除法院信息
     *
     * @param id 编号
     */
    void delete(Integer id);

    /**
     * 根据编号获取法庭详情
     *
     * @param id 庭审ID
     * @return 法庭详情信息
     */
    CourtOrg findById(Integer id);

    /**
     * 分页查询法院信息
     *
     * @param courtOrgFrom 查询参数
     * @param pageable     分页对象
     * @return 法院信息
     */
    Page<CourtOrg> pageList(CourtOrgFrom courtOrgFrom, Pageable pageable);

    /**
     * 查询列表法院信息
     *
     * @param courtOrgFrom 法院查询参数
     * @return 法院信息
     */
    List<CourtOrg> list(CourtOrgFrom courtOrgFrom);
}
