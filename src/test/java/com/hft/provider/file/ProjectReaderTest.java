package com.hft.provider.file;

import com.hft.provider.controller.model.Job;
import com.hft.provider.controller.model.Project;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectReaderTest {
    ProjectReader projectReader = new ProjectReader();

    @Test
    void getAllFilePaths() throws IOException {
        List<String> paths = projectReader.getAllFilePaths();
        assertNotNull(paths);
        assertFalse(paths.isEmpty(), "Paths not empty.");
        assertTrue(paths.get(0).endsWith(".sm"), "Ends with '.sm'");
        assertTrue(paths.get(0).startsWith("projects/"), "Starts with 'project/'");
    }

    @Test
    void parseFile301_1() throws IOException {
        Project project = projectReader.parseProject("projects/j30/j301_1.sm");
        assertNotNull(project);
        assertEquals(30, project.getSize());
        assertEquals(1, project.getPar());
        assertEquals(1, project.getInst());

        assertEquals(12, project.getR1CapacityPerDay());
        assertEquals(13, project.getR2CapacityPerDay());
        assertEquals(4, project.getR3CapacityPerDay());
        assertEquals(12, project.getR4CapacityPerDay());

        assertEquals(32, project.getJobCount());
        assertEquals(158, project.getHorizon());

        Job job2 = project.getJobs().stream().filter(job -> job.getNr() == 2).findFirst().orElse(null);
        assertNotNull(job2);
        assertEquals(2, job2.getNr());

        assertEquals(3, job2.getSuccessorCount());
        assertTrue(job2.getSuccessors().contains(6), "Successors contain '6'");
        assertTrue(job2.getSuccessors().contains(11), "Successors contain '11'");
        assertTrue(job2.getSuccessors().contains(15), "Successors contain '15'");

        assertEquals(1, job2.getPredecessorCount());
        assertTrue(job2.getPredecessors().contains(1), "Predecessors contain '1'");

        assertEquals(1, job2.getMode());
        assertEquals(8, job2.getDurationDays());
        assertEquals(4, job2.getR1HoursPerDay());
        assertEquals(0, job2.getR2HoursPerDay());
        assertEquals(0, job2.getR3HoursPerDay());
        assertEquals(0, job2.getR4HoursPerDay());

        Job job32 = project.getJobs().stream().filter(job -> job.getNr() == 32).findFirst().orElse(null);
        assertNotNull(job32);
        assertEquals(job32.getNr(), 32);

        assertEquals(0, job32.getSuccessorCount());
        assertTrue(job32.getSuccessors().isEmpty(), "Successors is empty");

        assertEquals(3, job32.getPredecessorCount());
        assertTrue(job32.getPredecessors().contains(29), "Predecessors contain '29'");
        assertTrue(job32.getPredecessors().contains(30), "Predecessors contain '30'");
        assertTrue(job32.getPredecessors().contains(31), "Predecessors contain '31'");

        assertEquals(1, job32.getMode());
        assertEquals(0, job32.getDurationDays());
        assertEquals(0, job32.getR1HoursPerDay());
        assertEquals(0, job32.getR2HoursPerDay());
        assertEquals(0, job32.getR3HoursPerDay());
        assertEquals(0, job32.getR4HoursPerDay());
    }

    @Test
    void parseAllFiles() throws IOException {
        List<String> paths = projectReader.getAllFilePaths();
        List<Project> projects = new ArrayList<>();
        for (String path : paths) {
            projects.add(projectReader.parseProject(path));
        }
        assertEquals(2040, projects.size());
    }
}
