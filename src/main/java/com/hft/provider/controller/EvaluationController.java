package com.hft.provider.controller;

import com.hft.provider.controller.model.Feedback;
import com.hft.provider.controller.model.Project;
import com.hft.provider.eval.SolutionEvaluator;
import com.hft.provider.file.MakespanReader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(tags = "Solution Evaluation")
@RestController
@RequestMapping("/api/eval")
public class EvaluationController {
    private final Logger LOGGER = Logger.getLogger(EvaluationController.class.getName());

    @ApiOperation("Evaluate feasibility of solution.")
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Feedback> validate(@RequestBody Project solution) throws IOException {
        LOGGER.info("Requesting evaluation of solution (size=" + solution.getSize() + ", par=" + solution.getPar() + ", inst=" + solution.getInst() + ")");
        List<Feedback> records = MakespanReader.parseMakespans("makespans/j" + solution.getSize() + "hrs.sm");
        Feedback feedback = records.stream()
                .filter(f -> f.getSize() == solution.getSize() && f.getPar() == solution.getPar() && f.getInst() == solution.getInst())
                .findFirst()
                .orElseThrow();
        SolutionEvaluator.evaluate(solution, feedback);
        LOGGER.info("Evaluated solution (feedback=" + feedback + ")");
        return ResponseEntity.ok(feedback);
    }
}
