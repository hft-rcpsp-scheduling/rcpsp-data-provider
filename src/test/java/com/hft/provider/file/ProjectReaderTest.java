package com.hft.provider.file;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProjectReaderTest {
    ProjectReader projectReader = new ProjectReader();

    @Test
    void getAllFilePaths() throws IOException {
        List<String> paths = projectReader.getAllFilePaths();
        assertFalse(paths.isEmpty(), "Paths not empty.");
        assertTrue(paths.get(0).endsWith(".sm"), "Ends with '.sm'");
        assertTrue(paths.get(0).startsWith("projects/"), "Starts with 'project/'");
    }

    @Test
    void parseFile() {
    }
}
