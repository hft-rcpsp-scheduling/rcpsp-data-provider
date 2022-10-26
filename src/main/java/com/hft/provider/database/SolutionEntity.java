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
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntity projectEntity;
    @Column(name = "creation_date", columnDefinition = "DATE")
    private LocalDate creationDate;
    @Column(name = "creation_time", columnDefinition = "TIME")
    private OffsetTime creationTime;
    @Column(name = "creator")
    private String creator;
    @OneToMany(mappedBy = "solutionEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SolutionDetailEntity> detailEntities;

    public SolutionEntity(ProjectEntity projectEntity) {
        this.id = null; // generated
        this.projectEntity = projectEntity;
        this.creationDate = LocalDate.now();
        this.creationTime = OffsetTime.now();
        this.detailEntities = new LinkedHashSet<>();
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void addDetail(SolutionDetailEntity detail) {
        this.detailEntities.add(detail);
    }
}
