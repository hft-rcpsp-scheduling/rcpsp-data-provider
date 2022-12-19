package com.hft.provider.controller.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@NoArgsConstructor
@Data
public class SolutionStatistic {

    @ApiModelProperty(value = "Database ID", example = "123")
    private long id;
    @ApiModelProperty(value = "Creator of the solution", example = "AI")
    private String creator;
    @ApiModelProperty(value = "Time span of the solution.", example = "168")
    private int makespan;

    @ApiModelProperty(value = "File identifier: File: j{size}1_1", example = "120")
    private int size;
    @ApiModelProperty(value = "File identifier: j120{par}_1", example = "13")
    private int par;
    @ApiModelProperty(value = "File identifier: j1201_{inst}", example = "10")
    private int inst;
}
