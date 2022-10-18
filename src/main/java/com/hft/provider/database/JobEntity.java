package com.hft.provider.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "Job")
@Table(name = "jobs")
class JobEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Setter(AccessLevel.NONE)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumns({
            @JoinColumn(name = "size", nullable = false),
            @JoinColumn(name = "par", nullable = false),
            @JoinColumn(name = "inst", nullable = false)})
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

    private String convertToJson(List<Integer> obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

    @SuppressWarnings("unchecked")
    private List<Integer> convertToList(String jsonString) throws JsonProcessingException {
        return new ObjectMapper().readValue(jsonString, List.class);
    }
}
