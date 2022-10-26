package com.hft.provider.database;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Entity(name = "Project")
@Table(name = "projects")
class ProjectEntity implements Serializable {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "size")
    private int size;
    @Column(name = "par")
    private int par;
    @Column(name = "inst")
    private int inst;
    @Column(name = "r1_capacity")
    private int r1Capacity;
    @Column(name = "r2_capacity")
    private int r2Capacity;
    @Column(name = "r3_capacity")
    private int r3Capacity;
    @Column(name = "r4_capacity")
    private int r4Capacity;
    @Column(name = "horizon")
    private int horizon;
    @Column(name = "job_count")
    private int jobCount;
    @OneToMany(mappedBy = "projectEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JobEntity> jobEntities;

    public ProjectEntity(int size, int par, int inst) {
        this.id = "" + size + par + "_" + inst;
        this.size = size;
        this.par = par;
        this.inst = inst;
        this.jobEntities = new LinkedHashSet<>();
    }

    public void setR1Capacity(int r1Capacity) {
        this.r1Capacity = r1Capacity;
    }

    public void setR2Capacity(int r2Capacity) {
        this.r2Capacity = r2Capacity;
    }

    public void setR3Capacity(int r3Capacity) {
        this.r3Capacity = r3Capacity;
    }

    public void setR4Capacity(int r4Capacity) {
        this.r4Capacity = r4Capacity;
    }

    public void setHorizon(int horizon) {
        this.horizon = horizon;
    }

    public void setJobCount(int jobCount) {
        this.jobCount = jobCount;
    }

    public void addJob(JobEntity job) {
        this.jobEntities.add(job);
    }
}
