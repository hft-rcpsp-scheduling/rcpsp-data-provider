package com.hft.provider.controller;

import com.hft.provider.controller.model.Project;
import com.hft.provider.file.ProjectReader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(tags = "Data Provider")
@RestController
@RequestMapping("/api/data")
public class DataController {

    private final Logger LOGGER = Logger.getLogger(DataController.class.getName());

    @ApiOperation("Get all sets of data.")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Project>> getAllProjects() throws IOException {
        ProjectReader reader = new ProjectReader();
        List<Project> projects = new ArrayList<>();
        List<String> files = reader.getAllFilePaths();
        for (String file : files) {
            try {
                projects.add(reader.parseProject(file));
            } catch (Exception e) {
                LOGGER.severe(e.getClass().getSimpleName() + ": " + e.getMessage() + " (at parsing file: " + file + ")");
            }
        }
        LOGGER.info("Parsed " + projects.size() + " of " + files.size() + " projects.");
        return ResponseEntity.ok(projects);
    }

    @ApiOperation("Get all sets of data.")
    @GetMapping(path = "/{size}/{par}/{inst}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> getProject(
            @ApiParam(value = "File: j{size}1_1", example = "120") @PathVariable Integer size,
            @ApiParam(value = "File: j120{par}_1", example = "1") @PathVariable Integer par,
            @ApiParam(value = "File: j1201_{inst}", example = "1") @PathVariable Integer inst) throws IOException {
        ProjectReader reader = new ProjectReader();
        return ResponseEntity.ok(reader.parseProject("projects/j" + size + "/j" + size + par + "_" + inst + ".sm"));
    }
}
