package com.hft.provider.controller;

import com.hft.provider.Application;
import com.hft.provider.controller.model.Error;
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
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utility.AssertExtension.assertSolutionEquals;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebAppConfiguration
@SpringBootTest(classes = {Application.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DataControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DatabaseService projectDB;

    private Project solution30_1_1;

    private Project project30_1_1;

    @BeforeAll
    void setUp() throws IOException {
        project30_1_1 = DataGenerator.readProject(30, 1, 1);
        solution30_1_1 = DataGenerator.generateSimpleSolution(30, 1, 1);
        projectDB.insertProjects(List.of(EntityMapper.mapToEntity(project30_1_1)));
    }

    @Test
    void getProjectIdentifiers() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/api/data/identifiers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertNotNull(result);
    }

    @Test
    void getProject() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/api/data/30/1/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Project project = JsonFactory.convertToObject(result, Project.class);
        assertNotNull(project);
        assertEquals(project30_1_1, project);
    }

    @Test
    void getProjectNotFound() throws Exception {
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
    void getSolutionCreators() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/api/data/solution/creators")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertNotNull(result);
    }

    @Test
    @Order(1)
    void saveSolutionToDatabase() throws Exception {
        String requestJson = JsonFactory.convertToJson(solution30_1_1);
        MvcResult result = mockMvc.perform(
                        post("/api/data/solution?creator=tester")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();


        StoredSolution storedSolution = JsonFactory.convertToObject(result, StoredSolution.class);
        // stored solution properties
        assertTrue(storedSolution.getId() > 0, "DB ID > 0");
        assertNotNull(storedSolution.getCreationDate());
        assertNotNull(storedSolution.getCreationTime());
        assertEquals("tester", storedSolution.getCreator());
        assertEquals(storedSolution.getMakespan(), solution30_1_1.getHorizon());
        // project properties
        assertSolutionEquals(solution30_1_1, storedSolution);
    }

    @Test
    @Order(2)
    void selectSolutions() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/api/data/solution?size=" + solution30_1_1.getSize() + "&par=" + solution30_1_1.getPar() + "&inst=" + solution30_1_1.getInst())
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // response extraction
        String responseArray = result.getResponse().getContentAsString(); // should only contain 1 obj
        String solutionString = responseArray.substring(1, responseArray.length() - 1);
        assertNotNull(solutionString);
        assertFalse(solutionString.isEmpty(), "Body in array empty.");
        StoredSolution storedSolution = JsonFactory.convertToObject(solutionString, StoredSolution.class);
        // stored solution properties
        assertTrue(storedSolution.getId() > 0, "DB ID > 0");
        assertNotNull(storedSolution.getCreationDate());
        assertNotNull(storedSolution.getCreationTime());
        assertEquals("tester", storedSolution.getCreator());
        assertEquals(solution30_1_1.getHorizon(), storedSolution.getMakespan());
        // project properties
        assertSolutionEquals(solution30_1_1, storedSolution);
    }

    @Test
    void invalidSolutionObject() throws Exception {
        String requestJson = JsonFactory.convertToJson(project30_1_1);
        MvcResult result = mockMvc.perform(
                        post("/api/data/solution?creator=tester")
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
        assertEquals("/api/data/solution", error.getPath());
    }

    @Test
    void invalidSolutionObjectWithoutCreator() throws Exception {
        String requestJson = JsonFactory.convertToJson(project30_1_1);
        MvcResult result = mockMvc.perform(
                        post("/api/data/solution")
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
        assertEquals("/api/data/solution", error.getPath());
    }

    @Test
    void getProjectFromFile() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/api/data/file/120/1/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Project project = JsonFactory.convertToObject(result, Project.class);
        assertNotNull(project);
        assertEquals(DataGenerator.readProject(120, 1, 1), project);
    }

    @Test
    void getProjectFromFileNotFound() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/api/data/file/115/1/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Error error = JsonFactory.convertToObject(result, Error.class);
        assertNotNull(error);
        assertEquals(404, error.getStatus());
        assertEquals(IOException.class.getSimpleName(), error.getOrigin());
        assertEquals("/api/data/file/115/1/1", error.getPath());
    }
}
