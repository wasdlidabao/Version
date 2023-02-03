package com.tz.version.repository;

import com.tz.version.domain.po.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VersionRepository extends JpaRepository<Version, String>, JpaSpecificationExecutor<Version> {

    @Query(value = "SELECT * FROM t_version " +
            "WHERE deleted = '0' " +
            "AND valid = '1' " +
            "AND IF(:applicationName IS NOT NULL, application_name = :applicationName , 1=1) " +
            "AND IF(:applicationType IS NOT NULL, application_type = :applicationType , 1=1) " +
            "ORDER BY application_version_1 DESC,application_version_2 DESC,application_version_3 DESC", nativeQuery = true)
    List<Version> findAllSortByApplicationVersion(@Param("applicationName") String applicationName, @Param("applicationType") Integer applicationType);

}