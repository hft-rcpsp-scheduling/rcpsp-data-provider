package com.hft.provider.controller;

import com.hft.provider.Application;
import com.hft.provider.controller.model.Error;
import com.hft.provider.controller.model.Feedback;
import com.hft.provider.controller.model.Project;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
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
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebAppConfiguration
@SpringBootTest(classes = {Application.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EvaluationControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Test
    void validate() throws Exception {
        Project project = DataGenerator.generateSimpleSolution(30, 1, 2);
        String requestJson = JsonFactory.convertToJson(project);
        MvcResult result = mockMvc.perform(
                        post("/api/eval")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Feedback feedback = JsonFactory.convertToObject(result, Feedback.class);
        assertNotNull(feedback);
        assertEquals(30, feedback.getSize());
        assertEquals(1, feedback.getPar());
        assertEquals(2, feedback.getInst());
        assertEquals(47, feedback.getRecordTimeSpan());
        assertEquals(project.getHorizon(), feedback.getSolutionTimeSpan());
        assertFalse(feedback.isNewRecord());
        assertTrue(feedback.isFeasible());
        assertNull(feedback.getNotFeasibleReason());
    }

    @Test
    void validateNotFeasible() throws Exception {
        Project project = DataGenerator.readProject(30, 1, 2);
        String requestJson = JsonFactory.convertToJson(project);
        MvcResult result = mockMvc.perform(
                        post("/api/eval")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Feedback feedback = JsonFactory.convertToObject(result, Feedback.class);
        assertNotNull(feedback);
        assertEquals(30, feedback.getSize());
        assertEquals(1, feedback.getPar());
        assertEquals(2, feedback.getInst());
        assertEquals(47, feedback.getRecordTimeSpan());
        assertNull(feedback.getSolutionTimeSpan());
        assertFalse(feedback.isNewRecord());
        assertFalse(feedback.isFeasible());
        assertNotNull(feedback.getNotFeasibleReason());
    }

    @Test
    void noMatchingMakespanFile() throws Exception {
        String requestJson = JsonFactory.convertToJson(new Project());
        MvcResult result = mockMvc.perform(
                        post("/api/eval")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Error error = JsonFactory.convertToObject(result, Error.class);
        assertNotNull(error);
        assertEquals(404, error.getStatus());
        assertEquals(IOException.class.getSimpleName(), error.getOrigin());
        assertEquals("/api/eval", error.getPath());
    }

    @Test
    void noMatchingMakespanElement() throws Exception {
        Project project = new Project();
        project.setSize(30);
        String requestJson = JsonFactory.convertToJson(project);
        MvcResult result = mockMvc.perform(
                        post("/api/eval")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Error error = JsonFactory.convertToObject(result, Error.class);
        assertNotNull(error);
        assertEquals(404, error.getStatus());
        assertEquals(NoSuchElementException.class.getSimpleName(), error.getOrigin());
        assertEquals("/api/eval", error.getPath());
    }
}
