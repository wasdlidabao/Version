package com.tz.system.service;

import com.tz.system.domain.param.TribunalParam;
import com.tz.system.domain.param.TribunalQueryParam;
import com.tz.system.domain.param.TribunalUpdateStatusParam;
import com.tz.system.domain.po.Tribunal;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TribunalService {

    /**
     * 根据法庭id查询所有法庭信息
     *
     * @param tribunalId 法庭编号
     * @return 法庭信息
     */
    Tribunal findById(String tribunalId);

    /**
     * 根据法院编号查询所有法庭信息
     *
     * @param courtNo 法院编号
     * @param valid   是否启用
     * @return 法庭集合
     */
    List<Tribunal> listByCourtNo(String courtNo, Boolean valid);

    /**
     * 分页查询法庭信息
     *
     * @param param 参数
     * @return 法庭分页
     */
    Page<Tribunal> list(TribunalQueryParam param);

    /**
     * 根据ID删除法庭信息
     *
     * @param id 编号
     */
    void deleteById(String id);

    /**
     * 修改法庭禁用启用状态
     *
     * @param param 参数
     */
    void updateStatus(TribunalUpdateStatusParam param);

    /**
     * 新增法庭
     *
     * @param param 参数
     * @return 法庭信息
     */
    Tribunal addAndUpdate(TribunalParam param);

}
