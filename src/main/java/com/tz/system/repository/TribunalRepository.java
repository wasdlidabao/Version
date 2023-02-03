package com.tz.system.repository;

import com.tz.system.domain.po.Tribunal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TribunalRepository extends JpaRepository<Tribunal, String>, JpaSpecificationExecutor<Tribunal> {

    List<Tribunal> findByCourtNoAndIdLikeOrderByCreateTimeDesc(String courtNo, String py);

    /**
     * @param courtNo 法院编号
     * @param valid   是否可用
     * @return 法庭集合
     */
    @Query(value = "SELECT * FROM t_tribunal WHERE deleted = '0' AND IF(:courtNo IS NOT NULL, court_no=:courtNo , 1=1) AND IF(:valid IS NOT NULL, valid = :valid , 1=1) ", nativeQuery = true)
    List<Tribunal> findByCourtNoAndDeletedIsFalse(@Param("courtNo") String courtNo, @Param("valid") Boolean valid);

    /**
     * 查询法院下重名的
     *
     * @param courtNo 法院编号
     * @param name    名称
     * @return 法庭集合
     */
    List<Tribunal> findByCourtNoAndNameAndDeletedFalse(String courtNo, String name);

    @Modifying
    @Query(value = "UPDATE t_tribunal SET deleted = 1 WHERE id = ?1", nativeQuery = true)
    void updateDeletedFlagById(String id);

    @Modifying
    @Query(value = "UPDATE t_tribunal SET valid = ?2 WHERE id = ?1", nativeQuery = true)
    void updateStatus(String id, boolean valid);

}
