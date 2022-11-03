package com.hft.provider.database.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hft.provider.controller.model.Job;
import com.hft.provider.controller.model.Project;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class ProjectMapper {
    private final Project project;
    private final List<Job> jobs;
    private boolean projectIsUndefined;

    public ProjectMapper() {
        this.project = new Project();
        this.jobs = new ArrayList<>();
        this.projectIsUndefined = true;
    }

    public void appendResult(ResultSet resultSet) throws SQLException, JsonProcessingException {
        if (projectIsUndefined) {
            project.setSize(resultSet.getInt("size"));
            project.setPar(resultSet.getInt("par"));
            project.setInst(resultSet.getInt("inst"));
            project.setR1CapacityPerDay(resultSet.getInt("r1"));
            project.setR2CapacityPerDay(resultSet.getInt("r2"));
            project.setR3CapacityPerDay(resultSet.getInt("r3"));
            project.setR4CapacityPerDay(resultSet.getInt("r4"));
            project.setHorizon(resultSet.getInt("horizon"));
            project.setJobCount(resultSet.getInt("jobCount"));
            projectIsUndefined = false;
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
        jobs.add(job);
    }

    public Project retrieveProject() throws IllegalStateException {
        if (projectIsUndefined) {
            throw new IllegalStateException("Project is still undefined.");
        }
        project.setJobs(jobs);
        return project;
    }

    @SuppressWarnings("unchecked")
    private List<Integer> convertToList(String jsonString) throws JsonProcessingException {
        return new ObjectMapper().readValue(jsonString, List.class);
    }
}
