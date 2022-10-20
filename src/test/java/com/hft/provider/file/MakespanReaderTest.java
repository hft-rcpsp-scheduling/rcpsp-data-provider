package com.hft.provider.file;

import com.hft.provider.controller.model.Feedback;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MakespanReaderTest {
    MakespanReader makespanReader = new MakespanReader();

    @Test
    void getAllFilePaths() throws IOException {
        List<String> paths = makespanReader.getAllFilePaths();
        assertNotNull(paths);
        assertFalse(paths.isEmpty(), "Paths not empty.");
        assertTrue(paths.get(0).endsWith(".sm"), "Ends with '.sm'");
        assertTrue(paths.get(0).startsWith("makespans/"), "Starts with 'makespans/'");
    }

    @Test
    void parseFile30() throws IOException {
        List<Feedback> makespans = makespanReader.parseMakespans("makespans/j30hrs.sm");
        assertNotNull(makespans);
        assertFalse(makespans.isEmpty(), "Makespans not empty.");
        Feedback feedback301_1 = makespans.get(0);
        assertEquals(30, feedback301_1.getSize());
        assertEquals(1, feedback301_1.getPar());
        assertEquals(1, feedback301_1.getInst());
        assertEquals(43, feedback301_1.getRecordTimeSpan());

        Feedback feedback3048_10 = makespans.get(makespans.size() - 1);
        assertEquals(30, feedback3048_10.getSize());
        assertEquals(48, feedback3048_10.getPar());
        assertEquals(10, feedback3048_10.getInst());
        assertEquals(54, feedback3048_10.getRecordTimeSpan());
    }

    @Test
    void parseAllFiles() throws IOException {
        List<String> paths = makespanReader.getAllFilePaths();
        List<Feedback> makespans = new ArrayList<>();
        for (String path : paths) {
            makespans.addAll(makespanReader.parseMakespans(path));
        }
        assertEquals(2040, makespans.size());
    }
}
