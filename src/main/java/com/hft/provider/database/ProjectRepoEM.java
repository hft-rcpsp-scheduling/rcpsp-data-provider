package com.hft.provider.database;

import java.util.List;

/**
 * Implementation {@link ProjectRepoEMImpl} gets used via dependency injection on the {@link ProjectRepo}.
 */
interface ProjectRepoEM {
    /**
     * Persist entities without existence check. Improves performance, but is not as secure.
     *
     * @param entities to persist.
     */
    void saveAllFast(List<ProjectEntity> entities);
}
