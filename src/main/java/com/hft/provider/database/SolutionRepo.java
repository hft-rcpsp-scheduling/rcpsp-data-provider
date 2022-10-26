package com.hft.provider.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface SolutionRepo extends JpaRepository<SolutionEntity, Long> {
    @Query("select e from Solution e where e.projectEntity.id = :pid")
    List<SolutionEntity> findSolutionsByProject(@Param("pid") String projectId);
}
