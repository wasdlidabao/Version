package com.tz.system.repository;

import com.tz.system.domain.po.CourtOrg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CourtOrgRepository extends JpaRepository<CourtOrg, Integer>, JpaSpecificationExecutor<CourtOrg> {

    CourtOrg findCourorgByCourtNo(String courtNo);

    @Query(value = "SELECT * from t_court_org where start_aits=1", nativeQuery = true)
    List<CourtOrg> listByStartAits();

    @Modifying
    @Transactional
    @Query(value = "UPDATE t_court_org SET start_aits=:startAits WHERE court_no=:courtNo ", nativeQuery = true)
    void updateStart(@Param("courtNo") String courtNo, @Param("startAits") Boolean startAits);
}