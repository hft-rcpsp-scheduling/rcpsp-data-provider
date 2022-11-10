package com.hft.provider.database;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

public class ProjectRepoEMImpl implements ProjectRepoEM {
    private final Logger LOGGER = Logger.getLogger(ProjectRepoEMImpl.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Persist entities without existence check. Improves performance, but is not as secure.
     * Transaction gets flushed every 100 entries.
     *
     * @param entities to persist.
     */
    @Override
    @Transactional
    public void saveAllFast(List<ProjectEntity> entities) {
        LOGGER.fine("Start persisting " + entities.size() + " entities.");
        for (int i = 0; i < entities.size(); i++) {
            entityManager.persist(entities.get(i));
            if (i % 50 == 0) {
                try {
                    entityManager.flush(); //flush a batch of inserts and release memory
                    entityManager.clear();
                    LOGGER.fine("Progress of persisting entities: " + (i * 100 / entities.size()) + "%");
                } catch (Exception e) {
                    LOGGER.severe(e.getClass().getSimpleName() + ": " + e.getMessage());
                }
            }
        }
        entityManager.flush(); //flush a batch of inserts and release memory
        entityManager.clear();
        LOGGER.fine("Successfully persisted " + entities.size() + " entities.");
    }
}
