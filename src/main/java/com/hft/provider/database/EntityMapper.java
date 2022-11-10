package com.hft.provider.database;

import com.hft.provider.controller.model.Job;
import com.hft.provider.controller.model.Project;
import com.hft.provider.controller.model.StoredSolution;

import java.util.ArrayList;
import java.util.Comparator;

public class EntityMapper {

    public static ProjectEntity mapToEntity(Project project) {
        ProjectEntity projectEntity = new ProjectEntity(project.getSize(), project.getPar(), project.getInst());
        projectEntity.setR1Capacity(project.getR1CapacityPerDay());
        projectEntity.setR2Capacity(project.getR2CapacityPerDay());
        projectEntity.setR3Capacity(project.getR3CapacityPerDay());
        projectEntity.setR4Capacity(project.getR4CapacityPerDay());
        projectEntity.setHorizon(project.getHorizon());
        projectEntity.setJobCount(project.getJobCount());
        project.getJobs().forEach(job -> {
            JobEntity jobEntity = new JobEntity(projectEntity, job.getNr());
            jobEntity.setSuccessorCount(job.getSuccessorCount());
            jobEntity.setPredecessorCount(job.getPredecessorCount());
            jobEntity.setSuccessors(job.getSuccessors());
            jobEntity.setPredecessors(job.getPredecessors());
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

    public static StoredSolution mapToModel(SolutionEntity entity) {
        StoredSolution model = new StoredSolution();
        model.setId(entity.getId());
        model.setCreationDate(entity.getCreationDate());
        model.setCreationTime(entity.getCreationTime());
        model.setCreator(entity.getCreator());
        model.setMakespan(entity.getMakespan());
        model.setSize(entity.getProjectEntity().getSize());
        model.setPar(entity.getProjectEntity().getPar());
        model.setInst(entity.getProjectEntity().getInst());
        model.setR1CapacityPerDay(entity.getProjectEntity().getR1Capacity());
        model.setR2CapacityPerDay(entity.getProjectEntity().getR2Capacity());
        model.setR3CapacityPerDay(entity.getProjectEntity().getR3Capacity());
        model.setR4CapacityPerDay(entity.getProjectEntity().getR4Capacity());
        model.setHorizon(entity.getProjectEntity().getHorizon());
        model.setJobCount(entity.getProjectEntity().getJobCount());
        model.setJobs(new ArrayList<>(entity.getDetailEntities().stream().map(EntityMapper::mapToModel).toList()));
        model.getJobs().sort(Comparator.comparingInt(Job::getNr));
        return model;
    }

    public static Project mapToModel(ProjectEntity entity) {
        Project model = new Project();
        model.setSize(entity.getSize());
        model.setPar(entity.getPar());
        model.setInst(entity.getInst());
        model.setR1CapacityPerDay(entity.getR1Capacity());
        model.setR2CapacityPerDay(entity.getR2Capacity());
        model.setR3CapacityPerDay(entity.getR3Capacity());
        model.setR4CapacityPerDay(entity.getR4Capacity());
        model.setHorizon(entity.getHorizon());
        model.setJobCount(entity.getJobCount());
        model.setJobs(new ArrayList<>(entity.getJobEntities().stream().map(EntityMapper::mapToModel).toList()));
        model.getJobs().sort(Comparator.comparingInt(Job::getNr));
        return model;
    }

    // === PRIVATE =====================================================================================================

    private static Job mapToModel(JobEntity job) {
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

    private static Job mapToModel(SolutionDetailEntity detail) {
        Job model = new Job();
        model.setNr(detail.getJobNr());
        model.setSuccessorCount(detail.getJobEntity().getSuccessorCount());
        model.setSuccessors(detail.getJobEntity().getSuccessors());
        model.setPredecessorCount(detail.getJobEntity().getPredecessorCount());
        model.setPredecessors(detail.getJobEntity().getPredecessors());
        model.setMode(detail.getJobEntity().getMode());
        model.setDurationDays(detail.getJobEntity().getDuration());
        model.setR1HoursPerDay(detail.getJobEntity().getR1());
        model.setR2HoursPerDay(detail.getJobEntity().getR2());
        model.setR3HoursPerDay(detail.getJobEntity().getR3());
        model.setR4HoursPerDay(detail.getJobEntity().getR4());
        model.setStartDay(detail.getStartDay());
        return model;
    }
}
