package com.hft.provider.controller.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.List;

@ApiModel
@NoArgsConstructor
@Data
public class StoredSolution {
    @ApiModelProperty(value = "Database ID", example = "123")
    private long id;
    @ApiModelProperty(value = "Creation date", example = "2010-01-10")
    private LocalDate creationDate;
    @ApiModelProperty(value = "Creation time", example = "14:59:48.907000+00:00")
    private OffsetTime creationTime;
    @ApiModelProperty(value = "Creator of the solution", example = "AI")
    private String creator;
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
    private int jobCount = 0;
    @ApiModelProperty(value = "Defined details for the solution")
    private List<Job> jobs = new ArrayList<>();
}
