package com.hft.provider.controller;

import com.hft.provider.controller.model.Project;
import com.hft.provider.database.ProjectDB;
import com.hft.provider.database.ProjectMapper;
import com.hft.provider.file.ProjectReader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(tags = "Data Provider")
@RestController
@RequestMapping("/api/data")
public class DataController {

    private final Logger LOGGER = Logger.getLogger(DataController.class.getName());
    private final ProjectDB projectDB;

    @Autowired
    public DataController(ProjectDB projectDB) {
        this.projectDB = projectDB;
    }

    @ApiOperation("Get all sets of project data by size. Fetched from database.")
    @GetMapping(path = "/{size}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Project>> getProjects(
            @ApiParam(value = "File: j{size}1_1 (30/60/90/120)", example = "120") @PathVariable Integer size) {
        LOGGER.fine("Fetch projects from db (size=" + size + ")");
        return ResponseEntity.ok(ProjectMapper.mapToModel(projectDB.selectProjects(size)));
    }

    @ApiOperation("Get all sets of project data by size and par. Fetched from database.")
    @GetMapping(path = "/{size}/{par}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Project>> getProjects(
            @ApiParam(value = "File: j{size}1_1 (30/60/90/120)", example = "120") @PathVariable Integer size,
            @ApiParam(value = "File: j120{par}_1 (1 to 48/60)", example = "1") @PathVariable Integer par) {
        LOGGER.fine("Fetch projects from db (size=" + size + ", par=" + par + ")");
        return ResponseEntity.ok(ProjectMapper.mapToModel(projectDB.selectProjects(size, par)));
    }

    @ApiOperation("Get project data by size, par and inst. Fetched from database.")
    @GetMapping(path = "/{size}/{par}/{inst}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> getProject(
            @ApiParam(value = "File: j{size}1_1 (30/60/90/120)", example = "120") @PathVariable Integer size,
            @ApiParam(value = "File: j120{par}_1 (1 to 48/60)", example = "1") @PathVariable Integer par,
            @ApiParam(value = "File: j1201_{inst} (1 to 10)", example = "1") @PathVariable Integer inst) {
        LOGGER.fine("Fetch project from db (size=" + size + ", par=" + par + ", inst=" + inst + ")");
        return ResponseEntity.ok(ProjectMapper.mapToModel(projectDB.selectProject(size, par, inst)));
    }

    @ApiOperation("Get project data from file.")
    @GetMapping(path = "/file/{size}/{par}/{inst}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> getProjectFromFile(
            @ApiParam(value = "File: j{size}1_1 (30/60/90/120)", example = "120") @PathVariable Integer size,
            @ApiParam(value = "File: j120{par}_1 (1 to 48/60)", example = "1") @PathVariable Integer par,
            @ApiParam(value = "File: j1201_{inst} (1 to 10)", example = "1") @PathVariable Integer inst) throws IOException {
        LOGGER.fine("Fetch project from file (size=" + size + ", par=" + par + ", inst=" + inst + ")");
        ProjectReader reader = new ProjectReader();
        return ResponseEntity.ok(reader.parseProject("projects/j" + size + "/j" + size + par + "_" + inst + ".sm"));
    }
}
