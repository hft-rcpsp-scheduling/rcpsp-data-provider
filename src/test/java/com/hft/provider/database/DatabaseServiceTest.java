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
import java.sql.SQLException;
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

    Project project30_10_10;
    Project solution40_10_10;
    @Autowired
    private DatabaseService projectDB;

    @BeforeAll
    void setUpProject3010_10() throws IOException {
        this.project30_10_10 = ProjectReader.parseProject("projects/j30/j3010_10.sm");
        this.solution40_10_10 = DataGenerator.generateMockSolution(1);
        solution40_10_10.setSize(40);
        solution40_10_10.setPar(10);
        solution40_10_10.setInst(10);
        projectDB.insertProjects(List.of(EntityMapper.mapToEntity(this.project30_10_10), EntityMapper.mapToEntity(this.solution40_10_10)));
    }

    @Test
    void selectProject() {
        ProjectEntity entity = projectDB.selectProject(project30_10_10.getSize(), project30_10_10.getPar(), project30_10_10.getInst());
        assertEquals(project30_10_10, EntityMapper.mapToModel(entity));
    }

    @Test
    @Order(1)
    void insertSolution() {
        SolutionEntity entity = projectDB.insertSolution(solution40_10_10, "tester");
        StoredSolution storedSolution = EntityMapper.mapToModel(entity);

        assertNotNull(storedSolution.getCreationDate());
        assertNotNull(storedSolution.getCreationTime());

        assertEquals(solution40_10_10.getSize(), storedSolution.getSize());
        assertEquals(solution40_10_10.getPar(), storedSolution.getPar());
        assertEquals(solution40_10_10.getInst(), storedSolution.getInst());
        assertEquals("tester", storedSolution.getCreator());

        assertEquals(solution40_10_10.getR1CapacityPerDay(), storedSolution.getR1CapacityPerDay());
        assertEquals(solution40_10_10.getR2CapacityPerDay(), storedSolution.getR2CapacityPerDay());
        assertEquals(solution40_10_10.getR3CapacityPerDay(), storedSolution.getR3CapacityPerDay());
        assertEquals(solution40_10_10.getR4CapacityPerDay(), storedSolution.getR4CapacityPerDay());

        assertEquals(solution40_10_10.getHorizon(), storedSolution.getHorizon());
        assertEquals(solution40_10_10.getJobs(), storedSolution.getJobs());
    }

    @Test
    @Order(2)
    void selectSolutions() throws SQLException, IOException {
        StoredSolution storedSolution =
                projectDB.selectSolutions(null, null, solution40_10_10.getSize(), solution40_10_10.getPar(), solution40_10_10.getInst())
                        .get(0);

        assertNotNull(storedSolution.getCreationDate());
        assertNotNull(storedSolution.getCreationTime());

        assertEquals(solution40_10_10.getSize(), storedSolution.getSize());
        assertEquals(solution40_10_10.getPar(), storedSolution.getPar());
        assertEquals(solution40_10_10.getInst(), storedSolution.getInst());
        assertEquals("tester", storedSolution.getCreator());

        assertEquals(solution40_10_10.getR1CapacityPerDay(), storedSolution.getR1CapacityPerDay());
        assertEquals(solution40_10_10.getR2CapacityPerDay(), storedSolution.getR2CapacityPerDay());
        assertEquals(solution40_10_10.getR3CapacityPerDay(), storedSolution.getR3CapacityPerDay());
        assertEquals(solution40_10_10.getR4CapacityPerDay(), storedSolution.getR4CapacityPerDay());

        assertEquals(solution40_10_10.getHorizon(), storedSolution.getHorizon());

        assertEquals(solution40_10_10.getJobs(), storedSolution.getJobs());
    }
}
