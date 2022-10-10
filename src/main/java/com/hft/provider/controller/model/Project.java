package com.hft.provider.controller.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel
@NoArgsConstructor
@Data
public class Project {
    // identifier from file
    @ApiModelProperty(value = "File identifier: File: j{size}1_1", example = "120")
    private int size;
    @ApiModelProperty(value = "File identifier: j120{par}_1", example = "13")
    private int par;
    @ApiModelProperty(value = "File identifier: j1201_{inst}", example = "10")
    private int inst;
    // resource availabilities
    @ApiModelProperty(value = "Defined hours capacity per day for resource 1", example = "2")
    private int r1CapacityPerDay;
    @ApiModelProperty(value = "Defined hours capacity per day for resource 2", example = "4")
    private int r2CapacityPerDay;
    @ApiModelProperty(value = "Defined hours capacity per day for resource 3", example = "6")
    private int r3CapacityPerDay;
    @ApiModelProperty(value = "Defined hours capacity per day for resource 4", example = "8")
    private int r4CapacityPerDay;
    // jobs
    @ApiModelProperty(value = "Defined day horizon for the project", example = "24")
    private int horizon;
    @ApiModelProperty(value = "Defined job count (includes dummy start and end)", example = "122")
    private int jobCount;
    @ApiModelProperty(value = "Defined jobs for the project")
    private List<Job> jobs;
}
