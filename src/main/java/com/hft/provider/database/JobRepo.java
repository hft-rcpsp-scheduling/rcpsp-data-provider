package com.hft.provider.database;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepo extends JpaRepository<JobEntity, String> {
}