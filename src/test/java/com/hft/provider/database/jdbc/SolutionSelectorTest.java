package com.hft.provider.database.jdbc;

import com.hft.provider.Application;
import com.hft.provider.controller.model.Job;
import com.hft.provider.controller.model.Project;
import com.hft.provider.controller.model.StoredSolution;
import com.hft.provider.database.DatabaseService;
import com.hft.provider.database.EntityMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import utility.DataGenerator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebAppConfiguration
@SpringBootTest(classes = {Application.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SolutionSelectorTest {

    Project solution90_10_5;
    Project solution90_10_10;
    @Autowired
    private DatabaseService projectDB;

    @Autowired
    private SolutionSelector selectorService;

    @BeforeAll
    void setUp() throws IOException {
        projectDB.insertProjects(List.of(
                EntityMapper.mapToEntity(DataGenerator.readProject(90, 10, 5)),
                EntityMapper.mapToEntity(DataGenerator.readProject(90, 10, 10))));
        solution90_10_5 = DataGenerator.generateSimpleSolution(90, 10, 5);
        solution90_10_10 = DataGenerator.generateSimpleSolution(90, 10, 10);
        projectDB.insertSolution(solution90_10_5, "jdbcTester5");
        projectDB.insertSolution(solution90_10_10, "jdbcTester10");
    }

    @Test
    void selectAllSolutions() throws SQLException, IOException {
        List<StoredSolution> solutions = selectorService.selectSolutions(null, null, null, null);
        assertNotNull(solutions);
        assertFalse(solutions.isEmpty(), "Solutions empty.");
        StoredSolution s = solutions.stream()
                .filter(e -> e.getSize() == 90 && e.getPar() == 10 && e.getInst() == 5)
                .findFirst()
                .orElseThrow();
        assertNotNull(s.getCreationDate());
        assertNotNull(s.getCreationTime());
        assertEquals("jdbcTester5", s.getCreator());
        assertNotEquals(0, s.getMakespan());
        assertEquals(solution90_10_5.getSize(), s.getSize());
        assertEquals(solution90_10_5.getPar(), s.getPar());
        assertEquals(solution90_10_5.getInst(), s.getInst());
        assertEquals(solution90_10_5.getR1CapacityPerDay(), s.getR1CapacityPerDay());
        assertEquals(solution90_10_5.getR2CapacityPerDay(), s.getR2CapacityPerDay());
        assertEquals(solution90_10_5.getR3CapacityPerDay(), s.getR3CapacityPerDay());
        assertEquals(solution90_10_5.getR4CapacityPerDay(), s.getR4CapacityPerDay());
        assertEquals(solution90_10_5.getHorizon(), s.getHorizon());
        assertEquals(solution90_10_5.getJobCount(), s.getJobCount());

        ArrayList<Job> expectedJobs = new ArrayList<>(solution90_10_5.getJobs());
        expectedJobs.sort(Comparator.comparingInt(Job::getNr));
        ArrayList<Job> actualJobs = new ArrayList<>(s.getJobs());
        actualJobs.sort(Comparator.comparingInt(Job::getNr));
        assertEquals(expectedJobs, actualJobs);
    }

    @Test
    void selectSolutionByCreator() throws SQLException, IOException {
        List<StoredSolution> solutions = selectorService.selectSolutions("jdbcTester5", null, null, null);
        assertEquals(1, solutions.size());

        StoredSolution s = solutions.get(0);
        assertEquals(solution90_10_5.getSize(), s.getSize());
        assertEquals(solution90_10_5.getPar(), s.getPar());
        assertEquals(solution90_10_5.getInst(), s.getInst());

        ArrayList<Job> expectedJobs = new ArrayList<>(solution90_10_5.getJobs());
        expectedJobs.sort(Comparator.comparingInt(Job::getNr));
        ArrayList<Job> actualJobs = new ArrayList<>(s.getJobs());
        actualJobs.sort(Comparator.comparingInt(Job::getNr));
        assertEquals(expectedJobs, actualJobs);
    }

    @Test
    void selectSolutionByProjectID() throws SQLException, IOException {
        List<StoredSolution> solutions = selectorService.selectSolutions(null, 90, 10, 10);
        assertEquals(1, solutions.size());

        StoredSolution s = solutions.get(0);
        assertEquals(solution90_10_10.getSize(), s.getSize());
        assertEquals(solution90_10_10.getPar(), s.getPar());
        assertEquals(solution90_10_10.getInst(), s.getInst());

        ArrayList<Job> expectedJobs = new ArrayList<>(solution90_10_10.getJobs());
        expectedJobs.sort(Comparator.comparingInt(Job::getNr));
        ArrayList<Job> actualJobs = new ArrayList<>(s.getJobs());
        actualJobs.sort(Comparator.comparingInt(Job::getNr));
        assertEquals(expectedJobs, actualJobs);
    }

    @Test
    void selectSolutionByCreatorAndProjectID() throws SQLException, IOException {
        List<StoredSolution> solutions = selectorService.selectSolutions("jdbcTester10", 90, 10, 10);
        assertEquals(1, solutions.size());

        StoredSolution s = solutions.get(0);
        assertEquals(solution90_10_10.getSize(), s.getSize());
        assertEquals(solution90_10_10.getPar(), s.getPar());
        assertEquals(solution90_10_10.getInst(), s.getInst());

        ArrayList<Job> expectedJobs = new ArrayList<>(solution90_10_10.getJobs());
        expectedJobs.sort(Comparator.comparingInt(Job::getNr));
        ArrayList<Job> actualJobs = new ArrayList<>(s.getJobs());
        actualJobs.sort(Comparator.comparingInt(Job::getNr));
        assertEquals(expectedJobs, actualJobs);
    }

    @Test
    void selectSolutionByPartialProjectID() throws SQLException, IOException {
        List<StoredSolution> solutions = selectorService.selectSolutions(null, 90, 10, null);
        assertEquals(2, solutions.size());
    }

    @Test
    void selectEmptyResult() throws SQLException, IOException {
        List<StoredSolution> solutions = selectorService.selectSolutions("noTester", 50, 10, null);
        assertEquals(0, solutions.size());
    }
}
