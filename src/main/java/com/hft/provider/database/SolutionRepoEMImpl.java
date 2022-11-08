package com.hft.provider.database;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Set;
import java.util.logging.Logger;


public class SolutionRepoEMImpl implements SolutionRepoEM {

    private final Logger LOGGER = Logger.getLogger(SolutionRepoEMImpl.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Persist entities without existence check. Improves performance, but is not as secure.
     *
     * @param entities to persist.
     */
    @Override
    @Transactional
    public void saveDetailsFast(Set<SolutionDetailEntity> entities) {
        LOGGER.fine("Start persisting " + entities.size() + " entities.");
        for (SolutionDetailEntity entity : entities) {
            entityManager.persist(entity);
        }
        entityManager.flush(); //flush a batch of inserts and release memory
        entityManager.clear();
        LOGGER.fine("Successfully persisted " + entities.size() + " entities.");
    }
}
