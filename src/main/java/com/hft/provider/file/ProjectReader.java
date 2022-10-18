package com.hft.provider.file;

import com.hft.provider.controller.model.Job;
import com.hft.provider.controller.model.Project;

import java.io.IOException;
import java.util.*;

public class ProjectReader extends FileReader {
    private final static String PROJECT_PATH = "projects/";

    /**
     * @return list with full paths of files in the project directory
     * @throws IOException if input stream is null
     */
    public List<String> getAllFilePaths() throws IOException {
        List<String> paths = new ArrayList<>();
        List<String> sizeDirs = getDirectoryPaths(PROJECT_PATH);
        for (String directory : sizeDirs) {
            paths.addAll(getDirectoryPaths(directory));
        }
        return paths;
    }

    /**
     * @param resourceFilePath path within the resource directory (example: directory/file.txt)
     * @return project with all available properties from file
     * @throws IOException if input stream is null
     */
    public Project parseProject(String resourceFilePath) throws IOException {
        String fileName = extractFileName(resourceFilePath);
        Scanner fileScanner = new Scanner(getResourceReader(resourceFilePath));

        Project project = new Project();
        Map<Integer, Job> jobs = new HashMap<>();

        project.setSize(extractSize(fileName)); // j{size}1_1
        project.setPar(extractPar(fileName, project.getSize())); // j120{par}_1
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
                        project.setJobCount(Integer.parseInt(split[1].replaceAll(" ", ""))); // jobs
                    }
                    if (line.startsWith("horizon")) {
                        String[] split = line.split(":");
                        project.setHorizon(Integer.parseInt(split[1].replaceAll(" ", ""))); // horizon
                    }
                    break;
                case RELATIONS:
                    if (lineStartsNotWithNumber(line))
                        continue;
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
                    if (lineStartsNotWithNumber(line))
                        continue;
                    job = jobs.get(lineScanner.nextInt()); // jobnr.
                    job.setMode(lineScanner.nextInt()); // mode
                    job.setDurationDays(lineScanner.nextInt()); // duration
                    job.setR1HoursPerDay(lineScanner.nextInt()); // R1
                    job.setR2HoursPerDay(lineScanner.nextInt()); // R2
                    job.setR3HoursPerDay(lineScanner.nextInt()); // R3
                    job.setR4HoursPerDay(lineScanner.nextInt()); // R4
                    break;
                case RESOURCE:
                    if (lineStartsNotWithNumber(line))
                        continue;
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

    private boolean lineStartsNotWithNumber(String line) {
        return !Character.isDigit(line.replaceAll(" ", "").charAt(0));
    }

    private ScanningState identifyScanningState(ScanningState currentState, String line) {
        return switch (currentState) {
            case STARTED -> line.startsWith("jobs") ? ScanningState.INFORMATION : ScanningState.STARTED;
            case INFORMATION -> line.startsWith("jobnr.") ? ScanningState.RELATIONS : ScanningState.INFORMATION;
            case RELATIONS -> line.startsWith("REQUESTS/DURATIONS") ? ScanningState.DURATIONS : ScanningState.RELATIONS;
            case DURATIONS ->
                    line.startsWith("RESOURCEAVAILABILITIES") ? ScanningState.RESOURCE : ScanningState.DURATIONS;
            case RESOURCE -> line.startsWith("*****") ? ScanningState.FINISHED : ScanningState.RESOURCE;
            default -> ScanningState.FINISHED;
        };
    }

    /**
     * @param jobs map with empty predecessor fields (successors required)
     * @return map values with filled predecessor fields
     */
    private List<Job> processPredecessors(Map<Integer, Job> jobs) {
        for (Job job : jobs.values()) {
            for (int successorNr : job.getSuccessors()) {
                Job successor = jobs.get(successorNr);
                successor.getPredecessors().add(job.getNr());
                successor.setPredecessorCount(successor.getPredecessorCount() + 1);
                jobs.put(successorNr, successor);
            }
        }
        return new ArrayList<>(jobs.values());
    }

    /**
     * @param path full path with '/' divided
     * @return last part of the path as file name
     */
    private String extractFileName(String path) {
        String[] pathSplit = path.split("/");
        return pathSplit[pathSplit.length - 1];
    }

    /**
     * @param fileName like j1201_10.sm
     * @return j{size}1_10.sm
     */
    private int extractSize(String fileName) {
        String number = fileName.split("_")[0].replaceAll("j", "");
        if (number.startsWith("30"))
            return 30;
        if (number.startsWith("60"))
            return 60;
        if (number.startsWith("90"))
            return 90;
        return 120;
    }

    /**
     * @param fileName like j1201_10.sm
     * @param size     j{size}1_10.sm
     * @return j120{par}_10.sm
     */
    private int extractPar(String fileName, Integer size) {
        String number = fileName.split("_")[0]
                .replaceAll("j", "");
        return Integer.parseInt(number.replaceFirst(size.toString(), ""));
    }

    /**
     * @param fileName like j3048_10.sm
     * @return j3048_{inst}.sm
     */
    private int extractInst(String fileName) {
        return Integer.parseInt(
                fileName.split("_")[1]
                        .split("\\.")[0]);
    }

    private enum ScanningState {
        STARTED, INFORMATION, RELATIONS, DURATIONS, RESOURCE, FINISHED
    }
}
