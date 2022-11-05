package com.hft.provider.database.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hft.provider.controller.model.Job;
import com.hft.provider.controller.model.StoredSolution;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper to retrieve {@link StoredSolution} from a {@link ResultSet}.
 *
 * <ol>
 *     <li>First {@link SolutionMapper#appendResult(ResultSet)} will add the general solution data & the first job with details.</li>
 *     <li>Every other {@link SolutionMapper#appendResult(ResultSet)} adds a new job with details.</li>
 *     <li>{@link SolutionMapper#retrieveSolution()} ()} is used when the solution is completed. Use a new object afterwards.</li>
 * </ol>
 */
class SolutionMapper {

    private final StoredSolution solution;
    private final List<Job> jobs;
    private boolean solutionIsUndefined;

    public SolutionMapper() {
        this.solution = new StoredSolution();
        this.jobs = new ArrayList<>();
        this.solutionIsUndefined = true;
    }

    /**
     * <ol>
     *     <li>First call will add the general solution data & the first job with details.</li>
     *     <li>Every other call adds a new job with details.</li>
     * </ol>
     *
     * @param resultSet row of data
     * @throws SQLException            if a column label does not match the result set
     * @throws JsonProcessingException if an exception occurs by parsing the json arrays
     */
    public void appendResult(ResultSet resultSet) throws SQLException, JsonProcessingException {
        if (solutionIsUndefined) {
            solution.setId(resultSet.getLong("id"));
            solution.setCreationDate(resultSet.getString("date"));
            solution.setCreationTime(resultSet.getString("time"));
            solution.setCreator(resultSet.getString("creator"));
            solution.setMakespan(resultSet.getInt("makespan"));
            solution.setSize(resultSet.getInt("size"));
            solution.setPar(resultSet.getInt("par"));
            solution.setInst(resultSet.getInt("inst"));
            solution.setR1CapacityPerDay(resultSet.getInt("r1"));
            solution.setR2CapacityPerDay(resultSet.getInt("r2"));
            solution.setR3CapacityPerDay(resultSet.getInt("r3"));
            solution.setR4CapacityPerDay(resultSet.getInt("r4"));
            solution.setHorizon(resultSet.getInt("horizon"));
            solution.setJobCount(resultSet.getInt("jobCount"));
            solutionIsUndefined = false;
        }
        Job job = new Job();
        job.setNr(resultSet.getInt("jNr"));
        job.setSuccessorCount(resultSet.getInt("jSucCount"));
        job.setSuccessors(convertToList(resultSet.getString("jSuc")));
        job.setPredecessorCount(resultSet.getInt("jPreCount"));
        job.setPredecessors(convertToList(resultSet.getString("jPre")));
        job.setMode(resultSet.getInt("jMode"));
        job.setDurationDays(resultSet.getInt("jDuration"));
        job.setR1HoursPerDay(resultSet.getInt("jR1"));
        job.setR2HoursPerDay(resultSet.getInt("jR2"));
        job.setR3HoursPerDay(resultSet.getInt("jR3"));
        job.setR4HoursPerDay(resultSet.getInt("jR4"));
        job.setStartDay(resultSet.getInt("jStart"));
        jobs.add(job);
    }

    /**
     * Is used when the solution is completed. Use a new object afterwards.
     *
     * @return solution with appended data
     * @throws IllegalStateException if solution is still undefined
     */
    public StoredSolution retrieveSolution() throws IllegalStateException {
        if (solutionIsUndefined) {
            throw new IllegalStateException("Solution is still undefined.");
        }
        solution.setJobs(jobs);
        return solution;
    }

    @SuppressWarnings("unchecked")
    private List<Integer> convertToList(String jsonString) throws JsonProcessingException {
        return new ObjectMapper().readValue(jsonString, List.class);
    }
}
