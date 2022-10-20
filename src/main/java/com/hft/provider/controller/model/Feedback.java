package com.hft.provider.controller.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@NoArgsConstructor
@Data
public class Feedback {
    // identifier from file
    @ApiModelProperty(value = "File identifier: File: j{size}1_1", example = "120")
    private int size;
    @ApiModelProperty(value = "File identifier: j120{par}_1", example = "13")
    private int par;
    @ApiModelProperty(value = "File identifier: j1201_{inst}", example = "10")
    private int inst;
    // given makespan
    @ApiModelProperty(value = "Given record time span", example = "24")
    private int recordTimeSpan;
    // feedback
    @ApiModelProperty(value = "Calculation if the solution was feasible", example = "true")
    private boolean isFeasible;
    @ApiModelProperty(value = "Comparison to record", example = "false")
    private boolean isNewRecord;
    @ApiModelProperty(value = "Provided solution time span", example = "24")
    private int solutionTimeSpan;
}
