package com.hft.provider.controller.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel
@NoArgsConstructor
@Data
public class Job {
    // identifier
    @ApiModelProperty(value = "Defined job number", example = "3")
    private int nr;
    // precedence relations
    @ApiModelProperty(value = "Defined count of successors", example = "2")
    private int successorCount;
    @ApiModelProperty(value = "Defined successor job numbers", example = "[4,5]")
    private List<Integer> successors;
    @ApiModelProperty(value = "Calculated count of predecessors", example = "2")
    private int predecessorCount;
    @ApiModelProperty(value = "Calculated predecessor job numbers", example = "[1,2]")
    private List<Integer> predecessors;
    // requests and durations
    @ApiModelProperty(value = "Defined mode with unknown usage", example = "1")
    private int mode;
    @ApiModelProperty(value = "Defined duration in days", example = "2")
    private int durationDays;
    @ApiModelProperty(value = "Defined hours per day for resource 1", example = "2")
    private int r1HoursPerDay;
    @ApiModelProperty(value = "Defined hours per day for resource 2", example = "4")
    private int r2HoursPerDay;
    @ApiModelProperty(value = "Defined hours per day for resource 3", example = "6")
    private int r3HoursPerDay;
    @ApiModelProperty(value = "Defined hours per day for resource 4", example = "8")
    private int r4HoursPerDay;
    // solution
    @ApiModelProperty(value = "Needs to be calculated for a solution", example = "0")
    private Integer startDay; // wrapper object to send null when providing data
    @ApiModelProperty(value = "Needs to be calculated for a solution", example = "1")
    private Integer endDay; // wrapper object to send null when providing data
}
