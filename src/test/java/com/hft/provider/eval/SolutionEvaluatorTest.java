package com.hft.provider.eval;

import com.hft.provider.controller.model.Feedback;
import com.hft.provider.controller.model.Project;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static utility.DataGenerator.generateProject;

class SolutionEvaluatorTest {

    @Test
    void evaluateValid() {
        Project project = generateProject(1);
        Feedback feedback = SolutionEvaluator.evaluate(project, new Feedback());
        assertNotNull(feedback);
        assertTrue(feedback.isFeasible());
        assertNull(feedback.getNotFeasibleReason());
    }

    @Test
    void evaluateStartDays() {
        Project project = generateProject(0);
        Feedback feedback = SolutionEvaluator.evaluate(project, new Feedback());
        assertNotNull(feedback);
        assertFalse(feedback.isFeasible());
        assertNotNull(feedback.getNotFeasibleReason());
        assertTrue(feedback.getNotFeasibleReason().toLowerCase().contains("start"), "Contains 'start'");
    }

    @Test
    void evaluateResources() {
        Project project = generateProject(2);
        Feedback feedback = SolutionEvaluator.evaluate(project, new Feedback());
        assertNotNull(feedback);
        assertFalse(feedback.isFeasible());
        assertNotNull(feedback.getNotFeasibleReason());
        assertTrue(feedback.getNotFeasibleReason().toLowerCase().contains("resource"), "Contains 'resource'");
    }

    @Test
    void evaluateRelationships() {
        Project project = generateProject(3);
        Feedback feedback = SolutionEvaluator.evaluate(project, new Feedback());
        assertNotNull(feedback);
        assertFalse(feedback.isFeasible());
        assertNotNull(feedback.getNotFeasibleReason());
        assertTrue(feedback.getNotFeasibleReason().toLowerCase().contains("relationship"), "Contains 'relationship'");
    }

    @Test
    void evaluateRelationshipAndResource() {
        Project project = generateProject(4);
        Feedback feedback = SolutionEvaluator.evaluate(project, new Feedback());
        assertNotNull(feedback);
        assertFalse(feedback.isFeasible());
        assertNotNull(feedback.getNotFeasibleReason());
        String msg = feedback.getNotFeasibleReason().toLowerCase();
        assertTrue(msg.contains("relationship"), "Contains 'relationship'");
        assertTrue(msg.contains("resource"), "Contains 'resource'");

    }
}
