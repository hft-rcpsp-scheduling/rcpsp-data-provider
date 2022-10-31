package utility;

import com.hft.provider.controller.model.Job;
import com.hft.provider.controller.model.Project;
import com.hft.provider.file.ProjectReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataGenerator {

    /**
     * @param option 0 = null, 1 = valid, 2 = resources, 3 = relationships, 4 = resources & relationships
     * @return project for testing
     */
    public static Project generateMockSolution(int option) {
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

    public static Project generateSimpleSolution(int size, int par, int inst) throws IOException {
        Project project = new ProjectReader().parseProject("projects/j" + size + "/j" + size + par + "_" + inst + ".sm");
        Map<Integer, Job> map = project.getJobs().stream().collect(Collectors.toMap(Job::getNr, Function.identity()));

        int currentTime = 0;

        Job startJob = map.get(1);
        startJob.setStartDay(currentTime);
        currentTime += startJob.getDurationDays();
        map.put(startJob.getNr(), startJob);

        while (map.get(project.getJobCount()).getStartDay() == null) {
            for (Job job : map.values()) {
                if (job.getStartDay() != null) continue;
                Optional<Integer> unscheduledPredecessorNr = job.getPredecessors().stream()
                        .filter(jobNr -> map.get(jobNr).getStartDay() == null).findFirst();
                if (unscheduledPredecessorNr.isPresent()) continue;

                job.setStartDay(currentTime);
                currentTime += job.getDurationDays();
                map.put(job.getNr(), job);
            }
        }
        project.setJobs(map.values().stream().toList());
        return project;
    }
}
