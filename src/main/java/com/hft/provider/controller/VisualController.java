package com.hft.provider.controller;

import com.hft.provider.controller.model.Project;
import com.hft.provider.excel.ProjectVisualizer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.logging.Logger;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(tags = "Visualizer")
@RestController
@RequestMapping("/api/visualize")
public class VisualController {
    private final Logger LOGGER = Logger.getLogger(VisualController.class.getName());

    @ApiOperation("Visualize solution and export as excel file.")
    @PostMapping(path = "/solution", produces = "application/ms-excel", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> visualizeSolution(@RequestBody Project solution) throws IOException {
        String fileName = "j" + solution.getSize() + solution.getPar() + "_" + solution.getInst() + "_solution.xlsx";
        LOGGER.info("Requesting visualization of solution (filename=" + fileName + ")");
        Resource resource = new UrlResource(ProjectVisualizer.convert(solution).toURI());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .header(HttpHeaders.CONTENT_TYPE, new MediaType("application", "ms-excel").toString())
                .body(resource);
    }
}
