package com.hft.provider.controller;

import com.hft.provider.controller.model.Feedback;
import com.hft.provider.controller.model.Project;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(tags = "Solution Evaluation")
@RestController
@RequestMapping("/api/eval")
public class EvaluationController {

    @ApiOperation("Evaluate feasibility of solution.")
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Feedback>> validate(@RequestBody List<Project> solutions) {
        return ResponseEntity.ok(new ArrayList<>());
    }
}
