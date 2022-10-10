package com.hft.provider.controller;

import com.hft.provider.controller.model.Feedback;
import com.hft.provider.controller.model.Project;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(tags = "Data Provider")
@RestController
@RequestMapping("/provider")
public class Controller {

    @ApiOperation("Get all sets of data.")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(null);
    }

    @ApiOperation("Get all sets of data.")
    @GetMapping(path = "/{size}/{par}/{inst}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> getProject(@ApiParam(value = "File: j{size}1_1", example = "120")
                                              @PathVariable Integer size,
                                              @ApiParam(value = "File: j120{par}_1", example = "1")
                                              @PathVariable Integer par,
                                              @ApiParam(value = "File: j1201_{inst}", example = "1")
                                              @PathVariable Integer inst) {
        return ResponseEntity.ok(null);
    }

    @ApiOperation("Evaluate feasibility of solution.")
    @PostMapping(path = "/validate", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Feedback>> validate(@RequestBody List<Project> solutions) {
        return ResponseEntity.ok(null);
    }
}
