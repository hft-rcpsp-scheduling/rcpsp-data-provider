package com.hft.provider.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity(name = "Job")
@Table(name = "jobs")
class JobEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private String id;
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntity projectEntity;
    @Column(name = "nr", nullable = false)
    private int nr;
    @Column(name = "suc_count")
    private int successorCount;
    @Column(name = "successors")
    private String successors;
    @Column(name = "pre_count")
    private int predecessorCount;
    @Column(name = "predecessors")
    private String predecessors;
    @Column(name = "mode")
    private int mode;
    @Column(name = "duration")
    private int duration;
    @Column(name = "r1")
    private int r1;
    @Column(name = "r2")
    private int r2;
    @Column(name = "r3")
    private int r3;
    @Column(name = "r4")
    private int r4;

    public JobEntity(int nr, ProjectEntity projectEntity) {
        this.id = projectEntity.getId() + "_" + nr;
        this.nr = nr;
        this.projectEntity = projectEntity;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setR1(int r1) {
        this.r1 = r1;
    }

    public void setR2(int r2) {
        this.r2 = r2;
    }

    public void setR3(int r3) {
        this.r3 = r3;
    }

    public void setR4(int r4) {
        this.r4 = r4;
    }

    public void setSuccessorCount(int successorCount) {
        this.successorCount = successorCount;
    }

    public void setPredecessorCount(int predecessorCount) {
        this.predecessorCount = predecessorCount;
    }

    public List<Integer> getSuccessors() throws JsonProcessingException {
        return convertToList(successors);
    }

    public void setSuccessors(List<Integer> successors) throws JsonProcessingException {
        this.successors = convertToJson(successors);
    }

    public List<Integer> getPredecessors() throws JsonProcessingException {
        return convertToList(predecessors);
    }

    public void setPredecessors(List<Integer> predecessors) throws JsonProcessingException {
        this.predecessors = convertToJson(predecessors);
    }

    // === PRIVATE =====================================================================================================

    private String convertToJson(List<Integer> obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

    @SuppressWarnings("unchecked")
    private List<Integer> convertToList(String jsonString) throws JsonProcessingException {
        return new ObjectMapper().readValue(jsonString, List.class);
    }
}
