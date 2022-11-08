package com.hft.provider.database;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.OffsetTime;
import java.util.LinkedHashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Entity(name = "Solution")
@Table(name = "solutions")
class SolutionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne(cascade = CascadeType.REMOVE, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntity projectEntity;
    @Column(name = "creation_date")
    private String creationDate;
    @Column(name = "creation_time")
    private String creationTime;
    @Column(name = "creator")
    private String creator;
    @Column(name = "makespan")
    private int makespan;
    @OneToMany(mappedBy = "solutionEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SolutionDetailEntity> detailEntities;

    /**
     * Generates id and creation properties
     *
     * @param projectEntity linked project
     */
    public SolutionEntity(ProjectEntity projectEntity) {
        this.id = null; // generated
        this.projectEntity = projectEntity;
        this.creationDate = LocalDate.now().toString();
        this.creationTime = OffsetTime.now().toString();
        this.detailEntities = new LinkedHashSet<>();
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setMakespan(int makespan) {
        this.makespan = makespan;
    }

    public void addDetail(SolutionDetailEntity detail) {
        this.detailEntities.add(detail);
    }
}
