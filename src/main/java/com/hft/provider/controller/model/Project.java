package com.hft.provider.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class Project {
    // identifier from file
    private int size; // 30, 60, 90 or 120
    private int par; // contains 10 inst
    private int inst; // 1 to 10
    // resource availabilities
    private int r1CapacityPerDay;
    private int r2CapacityPerDay;
    private int r3CapacityPerDay;
    private int r4CapacityPerDay;
    // jobs
    private int horizon;
    private int jobCount;
    private List<Job> jobs;
}
