package com.hft.provider.database;

import com.hft.provider.controller.model.Job;
import com.hft.provider.controller.model.Project;
import com.hft.provider.controller.model.StoredSolution;
import com.hft.provider.database.jdbc.ProjectSelector;
import com.hft.provider.database.jdbc.SolutionSelector;
import com.hft.provider.file.ProjectReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

@Component
public class DatabaseService {
    private final Logger LOGGER = Logger.getLogger(DatabaseService.class.getName());
    private final String initMode;
    private final ProjectRepo projectRepo;
    private final JobRepo jobRepo;
    private final SolutionRepo solutionRepo;
    private final ProjectSelector projectSelector;
    private final SolutionSelector solutionSelector;

    @Autowired
    public DatabaseService(ProjectRepo projectRepo,
                           SolutionRepo solutionRepo,
                           JobRepo jobRepo,
                           ProjectSelector projectSelector,
                           SolutionSelector solutionSelector,
                           @Value("${spring.datasource.url}") String datasource,
                           @Value("${spring.sql.init.mode}") String initMode) {
        this.projectSelector = projectSelector;
        this.solutionSelector = solutionSelector;
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

    /**
     * @return all options for project identifier
     */
    public Map<Integer, Map<Integer, List<Integer>>> selectProjectOptions() {
        Map<Integer, Map<Integer, List<Integer>>> options = new HashMap<>();
        for (int size : projectRepo.getSizeOptions()) {
            options.put(size, new HashMap<>());
            for (int par : projectRepo.getParOptions(size)) {
                options.get(size).put(par, projectRepo.getInstOptions(size, par));
            }
        }
        return options;
    }

    /**
     * @return all options for solution creators
     */
    public List<String> selectCreatorOptions() {
        return solutionRepo.getCreatorOptions();
    }

    /**
     * @param size required identifier
     * @param par  required identifier
     * @param inst required identifier
     * @return matching entity
     * @throws NoSuchElementException if no element found
     */
    public ProjectEntity selectProject(int size, int par, int inst) throws NoSuchElementException {
        String id = "" + size + par + "_" + inst;
        Optional<ProjectEntity> projectEntity = projectRepo.findById(id);
        if (projectEntity.isPresent()) {
            return projectEntity.get();
        } else {
            throw new NoSuchElementException("No project found with id=" + id);
        }
    }

    /**
     * @return all projects
     * @throws SQLException if a database access error occurs or this method is called on a closed result set
     * @throws IOException  if file not found
     */
    public List<Project> selectAllProjects() throws SQLException, IOException {
        return projectSelector.selectProjects();
    }

    /**
     * @param creator null or creator condition
     * @param size    null or size condition
     * @param par     null or par condition
     * @param inst    null or inst condition
     * @return all matching solutions
     * @throws SQLException if a database access error occurs or this method is called on a closed result set
     * @throws IOException  if file not found
     */
    public List<StoredSolution> selectSolutions(String creator, Integer size, Integer par, Integer inst) throws SQLException, IOException {
        return solutionSelector.selectSolutions(creator, size, par, inst);
    }

    /**
     * Builds solution entity and links it to related entries (project & jobs).
     *
     * @param solution with valid data
     * @param creator  null or creator field
     * @return saved solution with generated ID
     */
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
        solutionEntity.setMakespan(solution.getJobs()
                .stream().filter(job -> job.getNr() == solution.getJobs().size())
                .findFirst().orElseThrow()
                .getStartDay());
        return solutionRepo.save(solutionEntity);
    }

    // === PACKAGE PRIVATE =============================================================================================


    /**
     * Persist entities without existence check. Improves performance, but is not as secure.
     * Only internal usage.
     *
     * @param entities to persist.
     */
    @Transactional
    public void insertProjects(List<ProjectEntity> entities) {
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
