package com.hft.provider.database;

import com.hft.provider.Application;
import com.hft.provider.controller.model.Project;
import com.hft.provider.controller.model.StoredSolution;
import com.hft.provider.file.ProjectReader;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import utility.DataGenerator;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebAppConfiguration
@SpringBootTest(classes = {Application.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DatabaseServiceTest {

    Project project;
    Project solution;
    @Autowired
    private DatabaseService projectDB;

    @BeforeAll
    void setUp() throws IOException {
        this.project = new ProjectReader().parseProject("projects/j30/j301_1.sm");
        this.solution = DataGenerator.generateProject(1);
        projectDB.insertProjects(
                List.of(EntityMapper.mapToEntity(this.project), EntityMapper.mapToEntity(solution)));
    }

    @Test
    void selectProject() {
        ProjectEntity entity = projectDB.selectProject(project.getSize(), project.getPar(), project.getInst());
        assertEquals(project, EntityMapper.mapToModel(entity));
    }

    @Test
    void selectAllProjects() {
        assertEquals(2, projectDB.selectAllProjects().size());
    }

    @Test
    @Order(1)
    void insertSolution() {
        SolutionEntity entity = projectDB.insertSolution(solution, "tester");
        StoredSolution storedSolution = EntityMapper.mapToModel(entity);

        assertNotNull(storedSolution.getCreationDate());
        assertNotNull(storedSolution.getCreationTime());

        assertEquals(solution.getSize(), storedSolution.getSize());
        assertEquals(solution.getPar(), storedSolution.getPar());
        assertEquals(solution.getInst(), storedSolution.getInst());
        assertEquals("tester", storedSolution.getCreator());

        assertEquals(solution.getR1CapacityPerDay(), storedSolution.getR1CapacityPerDay());
        assertEquals(solution.getR2CapacityPerDay(), storedSolution.getR2CapacityPerDay());
        assertEquals(solution.getR3CapacityPerDay(), storedSolution.getR3CapacityPerDay());
        assertEquals(solution.getR4CapacityPerDay(), storedSolution.getR4CapacityPerDay());

        assertEquals(solution.getHorizon(), storedSolution.getHorizon());

        assertEquals(solution.getJobs(), storedSolution.getJobs());
    }

    @Test
    @Order(2)
    void selectSolutions() {
        SolutionEntity entity = projectDB.selectSolutions(solution.getSize(), solution.getPar(), solution.getInst()).get(0);
        StoredSolution storedSolution = EntityMapper.mapToModel(entity);

        assertNotNull(storedSolution.getCreationDate());
        assertNotNull(storedSolution.getCreationTime());

        assertEquals(solution.getSize(), storedSolution.getSize());
        assertEquals(solution.getPar(), storedSolution.getPar());
        assertEquals(solution.getInst(), storedSolution.getInst());
        assertEquals("tester", storedSolution.getCreator());

        assertEquals(solution.getR1CapacityPerDay(), storedSolution.getR1CapacityPerDay());
        assertEquals(solution.getR2CapacityPerDay(), storedSolution.getR2CapacityPerDay());
        assertEquals(solution.getR3CapacityPerDay(), storedSolution.getR3CapacityPerDay());
        assertEquals(solution.getR4CapacityPerDay(), storedSolution.getR4CapacityPerDay());

        assertEquals(solution.getHorizon(), storedSolution.getHorizon());

        assertEquals(solution.getJobs(), storedSolution.getJobs());
    }
}
