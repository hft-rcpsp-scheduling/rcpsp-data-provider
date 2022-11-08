package utility;

import com.hft.provider.controller.model.Job;
import com.hft.provider.controller.model.Project;
import com.hft.provider.controller.model.StoredSolution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssertExtension {
    public static void assertContains(String expected, String actual) {
        assertTrue(actual.contains(expected), "'" + actual + "' should contain '" + expected + "'");
    }

    public static void assertListEquals(List<Job> expected, List<Job> actual) {
        // preprocessing
        ArrayList<Job> expectedJobs = new ArrayList<>(expected);
        expectedJobs.sort(Comparator.comparingInt(Job::getNr));
        ArrayList<Job> actualJobs = new ArrayList<>(actual);
        actualJobs.sort(Comparator.comparingInt(Job::getNr));
        // comparison
        assertEquals(expectedJobs, actualJobs, "Jobs should be equal after sorting.");
    }

    public static void assertSolutionEquals(Project expected, StoredSolution actual) {
        // identifier from file
        assertEquals(expected.getSize(), actual.getSize(), "Project ID: size");
        assertEquals(expected.getPar(), actual.getPar(), "Project ID: par");
        assertEquals(expected.getInst(), actual.getInst(), "Project ID: inst");
        // resource availabilities
        assertEquals(expected.getR1CapacityPerDay(), actual.getR1CapacityPerDay(), "Project Capacity: R1");
        assertEquals(expected.getR2CapacityPerDay(), actual.getR2CapacityPerDay(), "Project Capacity: R2");
        assertEquals(expected.getR3CapacityPerDay(), actual.getR3CapacityPerDay(), "Project Capacity: R3");
        assertEquals(expected.getR4CapacityPerDay(), actual.getR4CapacityPerDay(), "Project Capacity: R4");
        // jobs
        assertEquals(expected.getHorizon(), actual.getHorizon(), "Project Horizon");
        assertEquals(expected.getJobCount(), actual.getJobCount(), "Project Job Count");
        assertListEquals(expected.getJobs(), actual.getJobs());
    }
}
