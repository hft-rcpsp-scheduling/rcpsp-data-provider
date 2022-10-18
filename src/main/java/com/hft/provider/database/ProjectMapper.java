package com.hft.provider.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hft.provider.controller.model.Job;
import com.hft.provider.controller.model.Project;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ProjectMapper {
    private static final Logger LOGGER = Logger.getLogger(ProjectMapper.class.getName());

    public static ProjectEntity mapToEntity(Project project) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setSize(project.getSize());
        projectEntity.setPar(project.getPar());
        projectEntity.setInst(project.getInst());
        projectEntity.setR1Capacity(project.getR1CapacityPerDay());
        projectEntity.setR2Capacity(project.getR2CapacityPerDay());
        projectEntity.setR3Capacity(project.getR3CapacityPerDay());
        projectEntity.setR4Capacity(project.getR4CapacityPerDay());
        projectEntity.setHorizon(project.getHorizon());
        projectEntity.setJobCount(project.getJobCount());
        project.getJobs().forEach(job -> {
            JobEntity jobEntity = new JobEntity();
            jobEntity.setProjectEntity(projectEntity);
            jobEntity.setNr(job.getNr());
            jobEntity.setSuccessorCount(job.getSuccessorCount());
            jobEntity.setPredecessorCount(job.getPredecessorCount());
            try {
                jobEntity.setSuccessors(job.getSuccessors());
                jobEntity.setPredecessors(job.getPredecessors());
            } catch (JsonProcessingException e) {
                LOGGER.severe(e.getClass().getSimpleName() + ": " + e.getMessage());
            }
            jobEntity.setMode(job.getMode());
            jobEntity.setDuration(job.getDurationDays());
            jobEntity.setR1(job.getR1HoursPerDay());
            jobEntity.setR2(job.getR2HoursPerDay());
            jobEntity.setR3(job.getR3HoursPerDay());
            jobEntity.setR4(job.getR4HoursPerDay());
            projectEntity.addJob(jobEntity);
        });
        return projectEntity;
    }

    public static List<Project> mapToModel(List<ProjectEntity> entities) {
        return entities.stream().map(ProjectMapper::mapToModel).collect(Collectors.toList());
    }

    public static Project mapToModel(ProjectEntity entity) {
        Project project = new Project();
        project.setSize(entity.getSize());
        project.setPar(entity.getPar());
        project.setInst(entity.getInst());
        project.setR1CapacityPerDay(entity.getR1Capacity());
        project.setR2CapacityPerDay(entity.getR2Capacity());
        project.setR3CapacityPerDay(entity.getR3Capacity());
        project.setR4CapacityPerDay(entity.getR4Capacity());
        project.setHorizon(entity.getHorizon());
        project.setJobCount(entity.getJobCount());
        project.setJobs(entity.getJobEntities().stream().map(job -> {
            try {
                return mapJobToModel(job);
            } catch (JsonProcessingException e) {
                LOGGER.severe(e.getClass().getSimpleName() + ": " + e.getMessage());
                throw new RuntimeException(e);
            }
        }).toList());
        return project;
    }

    private static Job mapJobToModel(JobEntity job) throws JsonProcessingException {
        Job model = new Job();
        model.setNr(job.getNr());
        model.setSuccessorCount(job.getSuccessorCount());
        model.setSuccessors(job.getSuccessors());
        model.setPredecessorCount(job.getPredecessorCount());
        model.setPredecessors(job.getPredecessors());
        model.setMode(job.getMode());
        model.setDurationDays(job.getDuration());
        model.setR1HoursPerDay(job.getR1());
        model.setR2HoursPerDay(job.getR2());
        model.setR3HoursPerDay(job.getR3());
        model.setR4HoursPerDay(job.getR4());
        return model;
    }
}
