package com.hft.provider.database;

import java.util.Set;

/**
 * Implementation {@link SolutionRepoEMImpl} gets used via dependency injection on the {@link SolutionRepo}.
 */
interface SolutionRepoEM {
    /**
     * Persist entities without existence check. Improves performance, but is not as secure.
     *
     * @param entities to persist.
     */
    void saveDetailsFast(Set<SolutionDetailEntity> entities);
}
