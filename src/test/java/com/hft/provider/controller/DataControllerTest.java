package com.hft.provider.controller;

import com.hft.provider.Application;
import com.hft.provider.controller.model.Error;
import com.hft.provider.controller.model.Job;
import com.hft.provider.controller.model.Project;
import com.hft.provider.controller.model.StoredSolution;
import com.hft.provider.database.DatabaseService;
import com.hft.provider.database.EntityMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import utility.DataGenerator;
import utility.JsonFactory;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebAppConfiguration
@SpringBootTest(classes = {Application.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DataControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    Project solution30_1_1;

    Project project30_1_1;
    @Autowired
    private DatabaseService projectDB;

    @BeforeAll
    void setUp() throws IOException {
        this.project30_1_1 = DataGenerator.readProject(30, 1, 1);
        this.solution30_1_1 = DataGenerator.generateSimpleSolution(30, 1, 1);
        projectDB.insertProjects(List.of(EntityMapper.mapToEntity(this.project30_1_1)));
    }

    @Test
    void getDataOptions() throws Exception {
        mockMvc.perform(
                        get("/api/data/options")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getProject() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/api/file/120/1/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Project project = JsonFactory.convertToObject(result, Project.class);
        assertNotNull(project);
        assertEquals(120, project.getSize());
        assertEquals(1, project.getPar());
        assertEquals(1, project.getInst());
    }

    @Test
    void getProjectNotFound() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/api/file/115/1/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Error error = JsonFactory.convertToObject(result, Error.class);
        assertNotNull(error);
        assertEquals(404, error.getStatus());
        assertEquals(IOException.class.getSimpleName(), error.getOrigin());
        assertEquals("/api/file/115/1/1", error.getPath());
    }

    @Test
    void getProjectFromDatabase() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/api/data/30/1/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Project project = JsonFactory.convertToObject(result, Project.class);
        assertNotNull(project);
        assertEquals(30, project.getSize());
        assertEquals(1, project.getPar());
        assertEquals(1, project.getInst());
    }

    @Test
    void getProjectFromDatabaseNotFound() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/api/data/115/1/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Error error = JsonFactory.convertToObject(result, Error.class);
        assertNotNull(error);
        assertEquals(404, error.getStatus());
        assertEquals(NoSuchElementException.class.getSimpleName(), error.getOrigin());
        assertEquals("/api/data/115/1/1", error.getPath());
    }

    @Test
    void invalidSolutionObject() throws Exception {
        String requestJson = JsonFactory.convertToJson(project30_1_1);
        MvcResult result = mockMvc.perform(
                        post("/api/solution?creator=tester")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(requestJson))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Error error = JsonFactory.convertToObject(result, Error.class);
        assertNotNull(error);
        assertEquals(406, error.getStatus());
        assertEquals(InvalidObjectException.class.getSimpleName(), error.getOrigin());
        assertEquals("/api/solution", error.getPath());
    }

    @Test
    void invalidSolutionObjectWithoutCreator() throws Exception {
        String requestJson = JsonFactory.convertToJson(project30_1_1);
        MvcResult result = mockMvc.perform(
                        post("/api/solution")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(requestJson))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Error error = JsonFactory.convertToObject(result, Error.class);
        assertNotNull(error);
        assertEquals(406, error.getStatus());
        assertEquals(InvalidObjectException.class.getSimpleName(), error.getOrigin());
        assertEquals("/api/solution", error.getPath());
    }

    @Test
    @Order(1)
    void saveSolutionToDatabase() throws Exception {
        String requestJson = JsonFactory.convertToJson(solution30_1_1);
        MvcResult result = mockMvc.perform(
                        post("/api/solution?creator=tester")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();


        StoredSolution storedSolution = JsonFactory.convertToObject(result, StoredSolution.class);
        assertNotNull(storedSolution.getCreationDate());
        assertNotNull(storedSolution.getCreationTime());

        assertEquals(solution30_1_1.getSize(), storedSolution.getSize());
        assertEquals(solution30_1_1.getPar(), storedSolution.getPar());
        assertEquals(solution30_1_1.getInst(), storedSolution.getInst());
        assertEquals("tester", storedSolution.getCreator());

        assertEquals(solution30_1_1.getR1CapacityPerDay(), storedSolution.getR1CapacityPerDay());
        assertEquals(solution30_1_1.getR2CapacityPerDay(), storedSolution.getR2CapacityPerDay());
        assertEquals(solution30_1_1.getR3CapacityPerDay(), storedSolution.getR3CapacityPerDay());
        assertEquals(solution30_1_1.getR4CapacityPerDay(), storedSolution.getR4CapacityPerDay());

        assertEquals(solution30_1_1.getHorizon(), storedSolution.getHorizon());

        ArrayList<Job> expectedJobs = new ArrayList<>(solution30_1_1.getJobs());
        expectedJobs.sort(Comparator.comparingInt(Job::getNr));
        ArrayList<Job> actualJobs = new ArrayList<>(storedSolution.getJobs());
        actualJobs.sort(Comparator.comparingInt(Job::getNr));
        assertEquals(expectedJobs, actualJobs);
    }

    @Test
    @Order(2)
    void selectSolutions() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/api/solution/" + solution30_1_1.getSize() + "/" + solution30_1_1.getPar() + "/" + solution30_1_1.getInst())
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseArray = result.getResponse().getContentAsString(); // should only contain 1 obj
        String solutionString = responseArray.substring(1, responseArray.length() - 1);
        assertNotNull(solutionString);
        assertFalse(solutionString.isEmpty(), "Body in array empty.");
        StoredSolution storedSolution = JsonFactory.convertToObject(solutionString, StoredSolution.class);
        assertNotNull(storedSolution.getCreationDate());
        assertNotNull(storedSolution.getCreationTime());

        assertEquals(solution30_1_1.getSize(), storedSolution.getSize());
        assertEquals(solution30_1_1.getPar(), storedSolution.getPar());
        assertEquals(solution30_1_1.getInst(), storedSolution.getInst());
        assertEquals("tester", storedSolution.getCreator());

        assertEquals(solution30_1_1.getR1CapacityPerDay(), storedSolution.getR1CapacityPerDay());
        assertEquals(solution30_1_1.getR2CapacityPerDay(), storedSolution.getR2CapacityPerDay());
        assertEquals(solution30_1_1.getR3CapacityPerDay(), storedSolution.getR3CapacityPerDay());
        assertEquals(solution30_1_1.getR4CapacityPerDay(), storedSolution.getR4CapacityPerDay());

        assertEquals(solution30_1_1.getHorizon(), storedSolution.getHorizon());

        ArrayList<Job> expectedJobs = new ArrayList<>(solution30_1_1.getJobs());
        expectedJobs.sort(Comparator.comparingInt(Job::getNr));
        ArrayList<Job> actualJobs = new ArrayList<>(storedSolution.getJobs());
        actualJobs.sort(Comparator.comparingInt(Job::getNr));
        assertEquals(expectedJobs, actualJobs);
    }
}
