package com.hft.provider.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface ProjectRepo extends JpaRepository<ProjectEntity, String>, ProjectRepoEM {
    @Query("select count(p) FROM Project p")
    long getProjectCount();

    @Query("select distinct p.size from Project p")
    List<Integer> getSizeOptions();

    @Query("select distinct p.par from Project p where p.size = :size")
    List<Integer> getParOptions(@Param("size") int size);

    @Query("select distinct p.inst from Project p where p.size = :size and p.par = :par")
    List<Integer> getInstOptions(@Param("size") int size, @Param("par") int par);
}
