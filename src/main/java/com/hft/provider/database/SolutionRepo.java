package com.hft.provider.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface SolutionRepo extends JpaRepository<SolutionEntity, Long> {
    @Query("select distinct s.creator from Solution s")
    List<String> getCreatorOptions();
}
