package com.hft.provider.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface ProjectRepository extends JpaRepository<ProjectEntity, ProjectEntity.Key> {
    @Query(value = "select p from Project p where p.size = :size")
    List<ProjectEntity> findBySize(@Param("size") int size);

    @Query(value = "select p from Project p where p.size = :size and p.par = :par")
    List<ProjectEntity> findBySizeAndPar(@Param("size") int size, @Param("par") int par);

    @Query("select count(p) FROM Project p")
    long getProjectCount();
}
