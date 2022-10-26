package com.hft.provider.database;

import com.hft.provider.controller.model.Job;
import com.hft.provider.controller.model.Project;
import com.hft.provider.file.ProjectReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Component
public class DatabaseService {
    private final Logger LOGGER = Logger.getLogger(DatabaseService.class.getName());
    private final String initMode;
    private final ProjectRepo projectRepo;
    private final JobRepo jobRepo;
    private final SolutionRepo solutionRepo;

    @Autowired
    public DatabaseService(ProjectRepo projectRepo, SolutionRepo solutionRepo, JobRepo jobRepo, @Value("${spring.datasource.url}") String datasource, @Value("${spring.sql.init.mode}") String initMode) {
        LOGGER.info("Datasource=(" + datasource + ") with Init-Mode=(" + initMode + ")");
        this.initMode = initMode;
        this.projectRepo = projectRepo;
        this.jobRepo = jobRepo;
        this.solutionRepo = solutionRepo;
    }

    @PostConstruct
    private void afterStartup() throws IOException {
        if (!this.initMode.equals("always")) {
            LOGGER.info("Skipping project initialization because of Init-Mode (is not 'always').");
            return;
        }
        if (projectRepo.getProjectCount() >= 2040) {
            LOGGER.info("Found 2040 or more projects in the database. No project initialization required.");
            return;
        }
        insertProjects(parseAllProjects());
    }

    @Transactional
    public ProjectEntity selectProject(int size, int par, int inst) {
        return projectRepo.getById("" + size + par + "_" + inst);
    }

    public List<ProjectEntity> selectAllProjects() {
        return projectRepo.findAll();
    }

    @Transactional
    public List<SolutionEntity> selectSolutions(int size, int par, int inst) {
        return solutionRepo.findSolutionsByProject("" + size + par + "_" + inst);
    }

    @Transactional
    public SolutionEntity insertSolution(Project solution, String creator) {
        ProjectEntity projectEntity = selectProject(solution.getSize(), solution.getPar(), solution.getInst());
        SolutionEntity solutionEntity = new SolutionEntity(projectEntity);
        solutionEntity.setCreator(creator);
        for (Job job : solution.getJobs()) {
            JobEntity jobEntity = jobRepo.findById(projectEntity.getId() + "_" + job.getNr()).orElseThrow();
            solutionEntity.addDetail(
                    new SolutionDetailEntity(solutionEntity, jobEntity)
                            .setStartDay(job.getStartDay()));
        }
        return solutionRepo.save(solutionEntity);
    }

    // === PACKAGE PRIVATE =============================================================================================

    @Transactional
    protected void insertProjects(List<ProjectEntity> entities) {
        LOGGER.info("Starting to save " + entities.size() + " projects to the database.");
        projectRepo.saveAllFast(entities);
        LOGGER.info("Finished saving projects to the database.");
    }

    // === PRIVATE =====================================================================================================

    private List<ProjectEntity> parseAllProjects() throws IOException {
        ProjectReader reader = new ProjectReader();
        List<String> filePaths = reader.getAllFilePaths();
        LOGGER.info("Starting to parse " + filePaths.size() + " projects from resources.");
        List<ProjectEntity> entities = filePaths.stream().map((path) -> {
            try {
                return EntityMapper.mapToEntity(reader.parseProject(path));
            } catch (IOException e) {
                LOGGER.severe(e.getClass().getSimpleName() + ": " + e.getMessage() + " (at parsing file: " + path + ")");
                throw new RuntimeException(e);
            }
        }).toList();
        LOGGER.info("Successfully parsed " + entities.size() + " of " + filePaths.size() + " projects.");
        return entities;
    }
}
