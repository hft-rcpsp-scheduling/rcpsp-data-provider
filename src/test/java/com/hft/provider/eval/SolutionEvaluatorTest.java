package com.hft.provider.eval;

import com.hft.provider.controller.model.Feedback;
import com.hft.provider.controller.model.Job;
import com.hft.provider.controller.model.Project;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    /**
     * @param option 0 = null, 1 = valid, 2 = resources, 3 = relationships, 4 = resources & relationships
     * @return project for testing
     */
    private Project generateProject(int option) {
        Project project = new Project();
        project.setR1CapacityPerDay(1);
        project.setR2CapacityPerDay(1);
        project.setR3CapacityPerDay(1);
        project.setR4CapacityPerDay(1);
        project.setHorizon(4);
        Job job1 = new Job();
        job1.setNr(1);
        job1.setSuccessors(Arrays.asList(2, 3));
        job1.setDurationDays(0);
        job1.setR1HoursPerDay(0);
        job1.setR2HoursPerDay(0);
        job1.setR3HoursPerDay(0);
        job1.setR4HoursPerDay(0);
        Job job2 = new Job();
        job2.setNr(2);
        job2.setPredecessors(List.of(1));
        job2.setDurationDays(2);
        job2.setR1HoursPerDay(1);
        job2.setR2HoursPerDay(1);
        job2.setR3HoursPerDay(1);
        job2.setR4HoursPerDay(1);
        Job job3 = new Job();
        job3.setNr(3);
        job3.setPredecessors(List.of(1));
        job3.setDurationDays(2);
        job3.setR1HoursPerDay(1);
        job3.setR2HoursPerDay(1);
        job3.setR3HoursPerDay(1);
        job3.setR4HoursPerDay(1);
        switch (option) {
            case 1 -> {
                // valid
                job1.setStartDay(0);
                job2.setStartDay(0);
                job3.setStartDay(2);
            }
            case 2 -> {
                // violates resources
                job1.setStartDay(0);
                job2.setStartDay(0);
                job3.setStartDay(0);
            }
            case 3 -> {
                // violates relationships
                job1.setStartDay(1);
                job2.setStartDay(0);
                job3.setStartDay(2);
            }
            case 4 -> {
                // violates relationships and resources
                job1.setStartDay(1);
                job2.setStartDay(0);
                job3.setStartDay(1);
            }
            default -> {
                // null
            }
        }
        project.setJobCount(3);
        project.setJobs(Arrays.asList(job1, job2, job3));
        return project;
    }
}
