package com.hft.provider.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SolutionRepo extends JpaRepository<SolutionEntity, Long> {
}
