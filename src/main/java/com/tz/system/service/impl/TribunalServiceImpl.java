package com.tz.system.service.impl;

import com.tz.core.exception.ApiException;
import com.tz.system.domain.param.TribunalParam;
import com.tz.system.domain.param.TribunalQueryParam;
import com.tz.system.domain.param.TribunalUpdateStatusParam;
import com.tz.system.domain.po.CourtOrg;
import com.tz.system.domain.po.Tribunal;
import com.tz.system.repository.CourtOrgRepository;
import com.tz.system.repository.TribunalRepository;
import com.tz.system.service.TribunalService;
import com.tz.utils.DataUtil;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TribunalServiceImpl implements TribunalService {
    @Resource
    TribunalRepository tribunalRepository;
    @Resource
    CourtOrgRepository courtOrgRepository;

    @Override
    public Tribunal findById(String tribunalId) {
        return tribunalRepository.findById(tribunalId).orElseThrow(() -> new ApiException("根据id查无相关法庭信息！"));
    }

    @Override
    public List<Tribunal> listByCourtNo(String courtNo, Boolean valid) {
        return tribunalRepository.findByCourtNoAndDeletedIsFalse(courtNo, valid);
    }

    @Override
    public Page<Tribunal> list(TribunalQueryParam param) {
        Page<Tribunal> page;
        if (param.getPage() != null) {
            Pageable pageable = PageRequest.of(param.getPage(), param.getSize());
            page = tribunalRepository.findAll(createQuerySpecification(param.getCourtNo(), param.getValid(), param.getName()), pageable);
        } else {
            List<Tribunal> all = tribunalRepository.findAll(createQuerySpecification(param.getCourtNo(), param.getValid(), param.getName()));
            page = new PageImpl<>(all);
        }
        return page;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public Tribunal addAndUpdate(TribunalParam param) {
        CourtOrg court = courtOrgRepository.findCourorgByCourtNo(param.getCourtNo());
        if (court == null) {
            throw new ApiException(400, "法庭编号不存在");
        }
        Tribunal tribunal = new Tribunal();
        tribunal.setUpdateTime(tribunal.getCreateTime());
        if (StringUtils.isNotBlank(param.getId())) {
            tribunal = this.findById(param.getId());
        } else {
            List<Tribunal> tribunalList = tribunalRepository.findByCourtNoAndNameAndDeletedFalse(param.getCourtNo(), param.getName());
            if (CollectionUtils.isNotEmpty(tribunalList)) {
                throw new ApiException(400, "法庭名称重复");
            }
            tribunal.setCreateTime(LocalDateTime.now());
            tribunal.setId(getTribunalId(tribunal.getName(), tribunal.getCourtNo()));
        }
        BeanUtils.copyProperties(param, tribunal, DataUtil.getNullPropertyNames(param));
        tribunal.setCourtId(court.getId());
        tribunal.setCourtName(court.getOrgName());
        return tribunalRepository.saveAndFlush(tribunal);
    }

    /**
     * 删除法庭信息
     *
     * @param id 主键id
     */
    @Transactional(rollbackOn = Exception.class)
    @Override
    public void deleteById(String id) {
        tribunalRepository.updateDeletedFlagById(id);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void updateStatus(TribunalUpdateStatusParam param) {
        tribunalRepository.updateStatus(param.getId(), param.getValid());
    }

    /**
     * 法庭ID
     *
     * @param name    法庭名称
     * @param courtNo 法院no
     * @return 法庭id
     */
    private String getTribunalId(String name, String courtNo) {
        // 法庭名称首字母缩写
        String tribunalNamePy = getFirstSpell(name);
        // 同法庭名称首字母缩写下法庭数量
        List<Tribunal> list = tribunalRepository.findByCourtNoAndIdLikeOrderByCreateTimeDesc(courtNo, "%_" + tribunalNamePy + "_%");
        int tribunalId = 1;
        if (CollectionUtils.isNotEmpty(list)) {
            String lastId = list.get(0).getId();
            String idStr = lastId.substring(lastId.length() - 3);
            tribunalId = Integer.parseInt(idStr) + 1;
        }
        String tribunalIdStr = String.format("%03d", tribunalId);
        // 拼接法庭编号
        return String.format("court_%s_%s_%s", courtNo, tribunalNamePy, tribunalIdStr);
    }

    /**
     * 查询规则
     *
     * @param courtNo 法院no
     * @param valid   valid
     * @param name    name
     * @return 查询
     */
    private Specification<Tribunal> createQuerySpecification(String courtNo, Boolean valid, String name) {
        return (root, query, cb) ->
                query.where(
                        createPredicates(courtNo, valid, name, root, cb)
                ).getRestriction();
    }

    /**
     * 查询条件
     *
     * @param courtNo 法院no
     * @param valid   是否启用
     * @param name    名字
     * @param root    root
     * @param cb      cb
     * @return 查询条件
     */
    private Predicate[] createPredicates(String courtNo, Boolean valid, String name, Root<Tribunal> root, CriteriaBuilder cb) {
        List<Predicate> predicateList = new ArrayList<>();
        predicateList.add(cb.isFalse(root.get("deleted").as(Boolean.class)));
        if (StringUtils.isNotBlank(name)) {
            predicateList.add(cb.like(root.get("name").as(String.class), "%" + name + "%"));
        }
        if (StringUtils.isNotBlank(courtNo)) {
            predicateList.add(cb.equal(root.get("courtNo").as(String.class), courtNo));
        }
        if (valid != null) {
            if (valid) {
                predicateList.add(cb.isTrue(root.get("valid").as(Boolean.class)));
            } else {
                predicateList.add(cb.isFalse(root.get("valid").as(Boolean.class)));
            }
        }
        return predicateList.toArray(new Predicate[0]);
    }

    private String getFirstSpell(String chinese) {
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char c : arr) {
            if (c > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat);
                    if (temp != null) {
                        pybf.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(c);
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim();
    }

}
