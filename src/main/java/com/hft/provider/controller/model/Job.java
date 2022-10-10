package com.hft.provider.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class Job {
    // identifier
    private int nr;
    // precedence relations
    private int successorCount;
    private List<Integer> successors;
    private int predecessorCount;
    private List<Integer> predecessors;
    // requests and durations
    private int mode;
    private int durationDays;
    private int r1HoursPerDay;
    private int r2HoursPerDay;
    private int r3HoursPerDay;
    private int r4HoursPerDay;
}
