package com.hft.provider.database;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity(name = "SolutionDetail")
@Table(name = "solution_details")
class SolutionDetailEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    private JobEntity jobEntity;
    @ManyToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "solution_id", nullable = false)
    private SolutionEntity solutionEntity;
    @Column(name = "job_nr", nullable = false)
    private int jobNr;
    @Column(name = "start")
    private int startDay;

    /**
     * Sets id = solutionId _ jobNr
     *
     * @param solutionEntity linked solution
     * @param jobEntity      linked job
     */
    public SolutionDetailEntity(SolutionEntity solutionEntity, JobEntity jobEntity) {
        this.id = solutionEntity.getId() + "_" + jobEntity.getNr();
        this.jobEntity = jobEntity;
        this.solutionEntity = solutionEntity;
        this.jobNr = jobEntity.getNr();
    }

    public SolutionDetailEntity setStartDay(int startDay) {
        this.startDay = startDay;
        return this;
    }
}
