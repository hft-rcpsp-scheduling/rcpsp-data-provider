package com.hft.provider.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public abstract class FileReader {

    /**
     * Note: Don't forget to close the reader.
     *
     * @param resourcePath path within the resource directory (example: directory/file.txt)
     * @return reader of input stream
     * @throws IOException if input stream is null
     */
    protected BufferedReader getResourceReader(String resourcePath) throws IOException {
        InputStream inputStream = FileReader.class.getClassLoader().getResourceAsStream(resourcePath);

        if (inputStream == null)
            throw new IOException("No content at " + resourcePath);

        return new BufferedReader(new InputStreamReader(inputStream));
    }

    /**
     * @param resourceDir path within the resource directory (example: directory/)
     * @return list with full paths of files in the directory
     * @throws IOException if input stream is null
     */
    protected List<String> getDirectoryPaths(String resourceDir) throws IOException {
        List<String> paths = new ArrayList<>();
        try (BufferedReader reader = this.getResourceReader(resourceDir)) {
            String contentPath;
            while ((contentPath = reader.readLine()) != null) {
                paths.add(resourceDir + contentPath);
            }
        }
        return paths;
    }

    /**
     * @param line text line
     * @return if line starts with digit (whitespace gets ignored)
     */
    protected boolean lineStartsNotWithNumber(String line) {
        return !Character.isDigit(line.replaceAll(" ", "").charAt(0));
    }

    /**
     * @param path full path divided by '/'
     * @return last part of the path as file name
     */
    protected String extractFileName(String path) {
        String[] pathSplit = path.split("/");
        return pathSplit[pathSplit.length - 1];
    }
}
