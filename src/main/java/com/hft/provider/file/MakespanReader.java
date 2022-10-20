package com.hft.provider.file;

import com.hft.provider.controller.model.Feedback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MakespanReader extends FileReader {
    private final static String PROJECT_PATH = "makespans/";

    /**
     * @return list with full paths of files in the makespan directory
     * @throws IOException if input stream is null
     */
    public List<String> getAllFilePaths() throws IOException {
        return getDirectoryPaths(PROJECT_PATH);
    }

    /**
     * @param resourceFilePath path within the resource directory (example: directory/file.txt)
     * @return list of feedbacks with all available properties from file
     * @throws IOException if input stream is null
     */
    public List<Feedback> parseMakespans(String resourceFilePath) throws IOException {
        int size = extractSize(extractFileName(resourceFilePath)); // j{size}hrs
        List<Feedback> makespans = new ArrayList<>();

        try (Scanner fileScanner = new Scanner(getResourceReader(resourceFilePath))) {
            while (fileScanner.hasNext()) {
                String line = fileScanner.nextLine();
                if (line.equals("")) continue;
                if (lineStartsNotWithNumber(line)) continue;

                String[] split = line.split("\t");

                Feedback feedback = new Feedback();
                feedback.setSize(size);
                feedback.setPar(Integer.parseInt(split[0].replaceAll(" ", "")));
                feedback.setInst(Integer.parseInt(split[1].replaceAll(" ", "")));
                feedback.setRecordTimeSpan(Integer.parseInt(split[2].replaceAll(" ", "")));
                makespans.add(feedback);
            } // while file has content
        } // close file scanner
        return makespans;
    }

    // === PRIVATE =====================================================================================================

    /**
     * @param fileName like j1201_10.sm
     * @return j{size}1_10.sm
     */
    private int extractSize(String fileName) {
        String number = fileName.replaceAll("j", "");
        if (number.startsWith("30"))
            return 30;
        if (number.startsWith("60"))
            return 60;
        if (number.startsWith("90"))
            return 90;
        return 120;
    }
}
