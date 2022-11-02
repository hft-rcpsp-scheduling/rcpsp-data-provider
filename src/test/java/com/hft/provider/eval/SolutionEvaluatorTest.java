package com.hft.provider.eval;

import com.hft.provider.controller.model.Feedback;
import com.hft.provider.controller.model.Project;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static utility.DataGenerator.generateMockSolution;
import static utility.DataGenerator.generateSimpleSolution;

class SolutionEvaluatorTest {

    @Test
    void evaluateValidMock() {
        Project project = generateMockSolution(1);
        Feedback feedback = SolutionEvaluator.evaluate(project, new Feedback());
        assertNotNull(feedback);
        System.out.println(feedback);
        assertTrue(feedback.isFeasible());
        assertNull(feedback.getNotFeasibleReason());
    }

    @Test
    void evaluateValidSimpleSolution() throws IOException {
        Project project = generateSimpleSolution(30, 1, 1);
        Feedback feedback = SolutionEvaluator.evaluate(project, new Feedback());
        assertNotNull(feedback);
        System.out.println(feedback);
        assertTrue(feedback.isFeasible());
        assertNull(feedback.getNotFeasibleReason());
    }


    @Test
    void evaluateStartDays() {
        Project project = generateMockSolution(0);
        Feedback feedback = SolutionEvaluator.evaluate(project, new Feedback());
        assertNotNull(feedback);
        assertFalse(feedback.isFeasible());
        assertNotNull(feedback.getNotFeasibleReason());
        assertTrue(feedback.getNotFeasibleReason().toLowerCase().contains("start"), "Contains 'start'");
    }

    @Test
    void evaluateResources() {
        Project project = generateMockSolution(2);
        Feedback feedback = SolutionEvaluator.evaluate(project, new Feedback());
        assertNotNull(feedback);
        assertFalse(feedback.isFeasible());
        assertNotNull(feedback.getNotFeasibleReason());
        assertTrue(feedback.getNotFeasibleReason().toLowerCase().contains("resource"), "Contains 'resource'");
    }

    @Test
    void evaluateRelationships() {
        Project project = generateMockSolution(3);
        Feedback feedback = SolutionEvaluator.evaluate(project, new Feedback());
        assertNotNull(feedback);
        assertFalse(feedback.isFeasible());
        assertNotNull(feedback.getNotFeasibleReason());
        assertTrue(feedback.getNotFeasibleReason().toLowerCase().contains("relationship"), "Contains 'relationship'");
    }

    @Test
    void evaluateRelationshipAndResource() {
        Project project = generateMockSolution(4);
        Feedback feedback = SolutionEvaluator.evaluate(project, new Feedback());
        assertNotNull(feedback);
        assertFalse(feedback.isFeasible());
        assertNotNull(feedback.getNotFeasibleReason());
        String msg = feedback.getNotFeasibleReason().toLowerCase();
        assertTrue(msg.contains("relationship"), "Contains 'relationship'");
        assertTrue(msg.contains("resource"), "Contains 'resource'");

    }
}
