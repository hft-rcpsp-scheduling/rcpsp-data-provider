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

    @Override
    @Transactional
    public void saveAllFast(List<ProjectEntity> entities) {
        for (int i = 0; i < entities.size(); i++) {
            entityManager.persist(entities.get(i));
            if (i % 50 == 0) {
                try {
                    entityManager.flush(); //flush a batch of inserts and release memory
                    entityManager.clear();
                    LOGGER.fine("Progress of saving entities: " + (i * 100 / entities.size()) + "%");
                } catch (Exception e) {
                    LOGGER.severe(e.getClass().getSimpleName() + ": " + e.getMessage());
                }
            }
        }
        entityManager.flush(); //flush a batch of inserts and release memory
        entityManager.clear();
        LOGGER.info("Progress of saving entities: Finished");
    }
}
