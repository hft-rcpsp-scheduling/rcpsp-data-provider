package com.hft.provider.controller;

import com.hft.provider.controller.model.Feedback;
import com.hft.provider.controller.model.Project;
import com.hft.provider.controller.model.StoredSolution;
import com.hft.provider.database.DatabaseService;
import com.hft.provider.database.EntityMapper;
import com.hft.provider.eval.SolutionEvaluator;
import com.hft.provider.file.ProjectReader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(tags = "Data Provider")
@RestController
@RequestMapping("/api/data")
public class DataController {
    private final Logger LOGGER = Logger.getLogger(DataController.class.getName());

    private final DatabaseService dbService;

    @Autowired
    public DataController(DatabaseService dbService) {
        this.dbService = dbService;
    }

    @ApiOperation("Options: Get available data identifiers. Response layers: size > par > inst.")
    @GetMapping(path = "/identifiers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<Integer, Map<Integer, List<Integer>>>> getProjectIdentifiers() {
        LOGGER.info("Request project identifiers.");
        return ResponseEntity.ok(
                dbService.selectProjectOptions());
    }

    @ApiOperation("Get a set of data from the database (equivalent to file).")
    @GetMapping(path = "/{size}/{par}/{inst}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> getProject(
            @ApiParam(value = "File: j{size}1_1 (30/60/90/120)", example = "30") @PathVariable Integer size,
            @ApiParam(value = "File: j120{par}_1 (1 to 48/60)", example = "1") @PathVariable Integer par,
            @ApiParam(value = "File: j1201_{inst} (1 to 10)", example = "1") @PathVariable Integer inst) {
        LOGGER.info("Request project from database (size=" + size + ", par=" + par + ", inst=" + inst + ")");
        return ResponseEntity.ok(
                EntityMapper.mapToModel(
                        dbService.selectProject(size, par, inst)));
    }

    @ApiOperation("Options: Get available creators that saved solutions.")
    @GetMapping(path = "/solution/creators", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getSolutionCreators() {
        LOGGER.info("Request solution creators.");
        return ResponseEntity.ok(
                dbService.selectCreatorOptions());
    }

    @ApiOperation("Get a list of solutions from the database. All parameters are optional.")
    @GetMapping(path = "/solution", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StoredSolution>> getSolution(
            @ApiParam(value = "Creator of a solution (optional)", example = "AI") @RequestParam(required = false) String creator,
            @ApiParam(value = "File: j{size}1_1 (30/60/90/120) (optional)", example = "30") @RequestParam(required = false) Integer size,
            @ApiParam(value = "File: j120{par}_1 (1 to 48/60) (optional)", example = "1") @RequestParam(required = false) Integer par,
            @ApiParam(value = "File: j1201_{inst} (1 to 10) (optional)", example = "1") @RequestParam(required = false) Integer inst)
            throws SQLException, IOException {
        LOGGER.info("Request solutions from database (creator= " + creator + ", size=" + size + ", par=" + par + ", inst=" + inst + ")");
        return ResponseEntity.ok(
                dbService.selectSolutions(creator, size, par, inst));
    }

    @ApiOperation("Save solution to the database.")
    @PostMapping(path = "/solution", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StoredSolution> saveSolution(
            @ApiParam(value = "Creator ID", example = "AI") @RequestParam(required = false) String creator,
            @RequestBody Project solution)
            throws InvalidObjectException {
        LOGGER.info("Request saving solution to database (size=" + solution.getSize() + ", par=" + solution.getPar() + ", inst=" + solution.getInst() + ")");
        Feedback feedback = SolutionEvaluator.evaluate(solution, new Feedback());
        if (!feedback.isFeasible()) {
            throw new InvalidObjectException("Solution is not feasible. " + feedback.getNotFeasibleReason());
        }
        LOGGER.info("Saving solution (size=" + solution.getSize() + ", par=" + solution.getPar() + ", inst=" + solution.getInst() + ") from creator:" + creator);
        return ResponseEntity.ok(
                EntityMapper.mapToModel(
                        dbService.insertSolution(solution, creator)));
    }

    @Deprecated
    @ApiOperation("Get a project from a file instead of the database.")
    @GetMapping(path = "/file/{size}/{par}/{inst}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> getProjectFromFile(
            @ApiParam(value = "File: j{size}1_1 (30/60/90/120)", example = "30") @PathVariable Integer size,
            @ApiParam(value = "File: j120{par}_1 (1 to 48/60)", example = "1") @PathVariable Integer par,
            @ApiParam(value = "File: j1201_{inst} (1 to 10)", example = "1") @PathVariable Integer inst)
            throws IOException {
        LOGGER.info("Request project from file (size=" + size + ", par=" + par + ", inst=" + inst + ")");
        ProjectReader reader = new ProjectReader();
        return ResponseEntity.ok(
                reader.parseProject("projects/j" + size + "/j" + size + par + "_" + inst + ".sm"));
    }
}
