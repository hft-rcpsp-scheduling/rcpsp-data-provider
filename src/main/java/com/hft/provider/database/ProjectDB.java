package com.hft.provider.database;

import com.hft.provider.file.ProjectReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Component
public class ProjectDB {
    private final Logger LOGGER = Logger.getLogger(ProjectDB.class.getName());
    private final ProjectRepository projectRepo;
    private final Environment environment;

    @Autowired
    public ProjectDB(ProjectRepository projectRepo, @Value("${spring.datasource.url}") String datasource, Environment environment) {
        this.projectRepo = projectRepo;
        LOGGER.info("Datasource=(" + datasource + ")");
        this.environment = environment;
    }

    @PostConstruct
    private void afterStartup() throws IOException {
        if (projectRepo.getProjectCount() < 2040) {
            ProjectReader reader = new ProjectReader();
            List<String> filePaths = reader.getAllFilePaths();
            LOGGER.info("Starting to parse " + filePaths.size() + " projects from resources.");
            List<ProjectEntity> entities = filePaths.stream().map((path) -> {
                try {
                    return ProjectMapper.mapToEntity(reader.parseProject(path));
                } catch (IOException e) {
                    LOGGER.severe(e.getClass().getSimpleName() + ": " + e.getMessage() + " (at parsing file: " + path + ")");
                    throw new RuntimeException(e);
                }
            }).toList();
            LOGGER.info("Parsed " + entities.size() + " of " + filePaths.size() + " projects.");
            LOGGER.info("Saving " + entities.size() + " projects to the database. This takes a some time.");
            if (Arrays.asList(this.environment.getActiveProfiles()).contains("test")) {
                projectRepo.save(entities.stream().filter(p -> p.getSize() == 30 && p.getPar() == 1 && p.getInst() == 1).findFirst().orElseThrow());
                LOGGER.info("Saved 1 project (size=30, par=1, inst=1) to the database because of test environment.");
            } else {
                projectRepo.saveAll(entities);
                LOGGER.info("Saved " + entities.size() + " projects to the database.");
            }
        } else {
            LOGGER.info("Found 2040 or more projects in the database. No parsing required.");
        }
    }

    public List<ProjectEntity> selectProjects(int size) {
        return projectRepo.findBySize(size);
    }

    public List<ProjectEntity> selectProjects(int size, int par) {
        return projectRepo.findBySizeAndPar(size, par);
    }

    public ProjectEntity selectProject(int size, int par, int inst) {
        return projectRepo.findById(new ProjectEntity.Key(size, par, inst)).orElseThrow();
    }
}
