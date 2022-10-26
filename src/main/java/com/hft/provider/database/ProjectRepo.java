package com.hft.provider.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
interface ProjectRepo extends JpaRepository<ProjectEntity, String>, ProjectRepoEM {
    @Query("select count(p) FROM Project p")
    long getProjectCount();
}
