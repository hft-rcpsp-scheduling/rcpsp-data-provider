package com.hft.provider.database;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "Project")
@Table(name = "projects")
@IdClass(ProjectEntity.Key.class)
class ProjectEntity implements Serializable {

    @Id
    @Column(name = "size")
    private int size;
    @Id
    @Column(name = "par")
    private int par;
    @Id
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
    private Set<JobEntity> jobEntities = new LinkedHashSet<>();

    public void addJob(JobEntity job) {
        jobEntities.add(job);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Key implements Serializable {
        private int size;
        private int par;
        private int inst;
    }
}
