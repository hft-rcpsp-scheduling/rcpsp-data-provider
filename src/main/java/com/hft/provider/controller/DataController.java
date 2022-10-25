package com.hft.provider.controller;

import com.hft.provider.controller.model.Feedback;
import com.hft.provider.controller.model.Project;
import com.hft.provider.eval.SolutionEvaluator;
import com.hft.provider.file.ProjectReader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(tags = "Data Provider")
@RestController
@RequestMapping("/api")
public class DataController {
    private final Logger LOGGER = Logger.getLogger(DataController.class.getName());

    @ApiOperation("Get all sets of data from the files.")
    @GetMapping(path = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @ApiOperation("Get a set of data from a file.")
    @GetMapping(path = "/data/{size}/{par}/{inst}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> getProject(
            @ApiParam(value = "File: j{size}1_1 (30/60/90/120)", example = "30") @PathVariable Integer size,
            @ApiParam(value = "File: j120{par}_1 (1 to 48/60)", example = "1") @PathVariable Integer par,
            @ApiParam(value = "File: j1201_{inst} (1 to 10)", example = "1") @PathVariable Integer inst) throws IOException {
        LOGGER.info("Fetch project from file (size=" + size + ", par=" + par + ", inst=" + inst + ")");
        ProjectReader reader = new ProjectReader();
        return ResponseEntity.ok(reader.parseProject("projects/j" + size + "/j" + size + par + "_" + inst + ".sm"));
    }

    @Deprecated // this endpoint should replace the old endpoint with the file
    @ApiOperation("Get a set of data from the database (equivalent to file).")
    @GetMapping(path = "/database/{size}/{par}/{inst}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> getProjectFromDatabase(
            @ApiParam(value = "File: j{size}1_1 (30/60/90/120)", example = "30") @PathVariable Integer size,
            @ApiParam(value = "File: j120{par}_1 (1 to 48/60)", example = "1") @PathVariable Integer par,
            @ApiParam(value = "File: j1201_{inst} (1 to 10)", example = "1") @PathVariable Integer inst) {
        LOGGER.info("Fetch project from database (size=" + size + ", par=" + par + ", inst=" + inst + ")");
        // TODO
        return ResponseEntity.ok(null);
    }

    @ApiOperation("Get a list of solutions for a project from the database.")
    @GetMapping(path = "/solution/{size}/{par}/{inst}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Project>> getSolutionFromDatabase(
            @ApiParam(value = "File: j{size}1_1 (30/60/90/120)", example = "30") @PathVariable Integer size,
            @ApiParam(value = "File: j120{par}_1 (1 to 48/60)", example = "1") @PathVariable Integer par,
            @ApiParam(value = "File: j1201_{inst} (1 to 10)", example = "1") @PathVariable Integer inst) {
        LOGGER.info("Fetch solutions from database (size=" + size + ", par=" + par + ", inst=" + inst + ")");
        // TODO
        return ResponseEntity.ok(null);
    }

    @ApiOperation("Save solution to the database.")
    @PostMapping(path = "/solution", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Feedback> saveSolutionToDatabase(@RequestBody Project solution) throws InvalidObjectException {
        if (!SolutionEvaluator.evaluate(solution, new Feedback()).isFeasible()) {
            throw new InvalidObjectException("Solution is not feasible.");
        }
        LOGGER.info("Saving solution. (size=" + solution.getSize() + ", par=" + solution.getPar() + ", inst=" + solution.getInst() + ")");
        // TODO
        return ResponseEntity.ok(null);
    }
}
