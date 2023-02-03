package com.tz.system.service.impl;

import com.tz.core.exception.ApiException;
import com.tz.system.domain.form.CourtOrgFrom;
import com.tz.system.domain.po.CourtOrg;
import com.tz.system.repository.CourtOrgRepository;
import com.tz.system.service.CourtOrgService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CourtOrgServiceImpl implements CourtOrgService {

    @Resource
    CourtOrgRepository courtOrgRepository;

    @Override
    public List<CourtOrg> listByStartAits() {
        try {
            Specification<CourtOrg> courtOrgSpecification = courtOrgList("", true);
            return courtOrgRepository.findAll(courtOrgSpecification);
        } catch (Exception e) {
            throw new ApiException(500, "查询异常");
        }
    }

    @Override
    public void updateStart(String courtNo, Boolean startAits) {
        try {
            courtOrgRepository.updateStart(courtNo, startAits);
        } catch (Exception e) {
            throw new ApiException(500, "更新异常");
        }
    }

    @Override
    public CourtOrg save(CourtOrg courtOrg) {
        return courtOrgRepository.save(courtOrg);
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            throw new ApiException(400, "编号不能为空");
        }
        courtOrgRepository.deleteById(id);
    }

    @Override
    public CourtOrg findById(Integer id) {
        if (id == null) {
            throw new ApiException(400, "编号不能为空");
        }
        return courtOrgRepository.getOne(id);
    }

    @Override
    public Page<CourtOrg> pageList(CourtOrgFrom courtOrgFrom, Pageable pageable) {
        Specification<CourtOrg> courtOrgSpecification = courtOrgList(courtOrgFrom.getOrgType(), null);
        return courtOrgRepository.findAll(courtOrgSpecification, pageable);
    }

    @Override
    public List<CourtOrg> list(CourtOrgFrom courtOrgFrom) {
        Specification<CourtOrg> courtOrgSpecification = courtOrgList(courtOrgFrom.getOrgType(), null);
        return courtOrgRepository.findAll(courtOrgSpecification);
    }

    private Specification<CourtOrg> courtOrgList(String orgType, Boolean startAits) {
        return (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (StringUtils.isNotBlank(orgType)) {
                list.add(cb.equal(root.get("orgType").as(String.class), orgType));
            }
            if (ObjectUtils.isNotEmpty(startAits)) {
                list.add(cb.equal(root.get("startAits").as(Boolean.class), startAits));
            }
            list.add(cb.equal(root.get("isDeleted").as(String.class), 0));
            query.orderBy(cb.asc(root.get("createTime")));
            return query.where(list.toArray(new Predicate[0])).getRestriction();
        };
    }

}
