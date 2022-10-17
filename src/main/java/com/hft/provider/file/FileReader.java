package com.hft.provider.file;

import com.hft.provider.controller.model.Job;
import com.hft.provider.controller.model.Project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FileReader {

    public Project parseFile(File file) throws FileNotFoundException {
        String fileName = file.getName();
        Scanner fileScanner = new Scanner(file);
        Project project = new Project();
        Map<Integer, Job> jobs = new HashMap<>();

        project.setSize(extractSize(fileName)); // j{size}1_1
        project.setPar(extractPar(fileName)); // j120{par}_1
        project.setInst(extractInst(fileName)); // j1201_{inst}

        ScanningState state = ScanningState.STARTED;
        while (fileScanner.hasNext()) {
            String line = fileScanner.nextLine();
            if (line.equals("")) continue;
            state = identifyScanningState(state, line);
            Scanner lineScanner = new Scanner(line);
            Job job;
            switch (state) {
                case INFORMATION:
                    if (line.startsWith("jobs")) {
                        String[] split = line.split(":");
                        project.setJobCount(Integer.parseInt(split[1])); // jobs
                    }
                    if (line.startsWith("horizon")) {
                        String[] split = line.split(":");
                        project.setHorizon(Integer.parseInt(split[1])); // horizon
                    }
                    break;
                case RELATIONS:
                    if (!line.startsWith(" ")) continue;
                    job = new Job();
                    job.setNr(lineScanner.nextInt()); // jobnr.
                    lineScanner.next(); // #modes
                    job.setSuccessorCount(lineScanner.nextInt()); // #successors
                    List<Integer> successors = new ArrayList<>();
                    while (lineScanner.hasNext()) {
                        successors.add(lineScanner.nextInt()); // successors
                    }

                    job.setSuccessors(successors);
                    jobs.put(job.getNr(), job);
                    break;
                case DURATIONS:
                    job = jobs.get(lineScanner.nextInt()); // jobnr.
                    job.setMode(lineScanner.nextInt()); // mode
                    job.setDurationDays(lineScanner.nextInt()); // duration
                    job.setR1HoursPerDay(lineScanner.nextInt()); // R1
                    job.setR2HoursPerDay(lineScanner.nextInt()); // R2
                    job.setR3HoursPerDay(lineScanner.nextInt()); // R3
                    job.setR4HoursPerDay(lineScanner.nextInt()); // R4
                    break;
                case RESOURCE:
                    project.setR1CapacityPerDay(lineScanner.nextInt()); // R1
                    project.setR2CapacityPerDay(lineScanner.nextInt()); // R2
                    project.setR3CapacityPerDay(lineScanner.nextInt()); // R3
                    project.setR4CapacityPerDay(lineScanner.nextInt()); // R4
                    break;
                case FINISHED:
                    project.setJobs(processPredecessors(jobs));
                    break;
                default:
                    continue;
            }
            if (state == ScanningState.FINISHED) {
                break;
            }
        }
        return project;
    }


    private ScanningState identifyScanningState(ScanningState currentState, String line) {
        return switch (currentState) {
            //TODO fill conditions
            case STARTED -> line.startsWith("jobs") ? ScanningState.INFORMATION : ScanningState.STARTED;
            case INFORMATION -> line.startsWith("jobnr.") ? ScanningState.RELATIONS : ScanningState.INFORMATION;
            case RELATIONS -> line.startsWith("REQUESTS/DURATIONS") ? ScanningState.DURATIONS : ScanningState.RELATIONS;
            case DURATIONS ->
                    line.startsWith("RESOURCEAVAILABILITIES") ? ScanningState.RESOURCE : ScanningState.DURATIONS;
            case RESOURCE -> line.startsWith("*****") ? ScanningState.FINISHED : ScanningState.RESOURCE;
            default -> ScanningState.FINISHED;
        };
    }

    private List<Job> processPredecessors(Map<Integer, Job> jobs) {
        // TODO precess predecessor fields
        return new ArrayList<>(jobs.values());
    }

    private int extractSize(String fileName) {
        // TODO extract size with regex
        return 404;
    }

    private int extractPar(String fileName) {
        // TODO extract par with regex
        return 404;
    }

    private int extractInst(String fileName) {
        // TODO extract inst with regex
        return 404;
    }

    private enum ScanningState {
        STARTED, INFORMATION, RELATIONS, DURATIONS, RESOURCE, FINISHED
    }
}
