package com.hft.provider.database;

import java.util.List;

interface ProjectRepoEM {
    void saveAllFast(List<ProjectEntity> entities);
}
