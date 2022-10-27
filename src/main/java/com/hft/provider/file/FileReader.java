package com.hft.provider.file;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
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

        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    /**
     * @param baseDir path within the resource directory (example: directory/)
     * @return list with full paths of files in the directory
     * @throws IOException if input stream is null
     */

    protected List<String> getDirectoryPaths(String baseDir) throws IOException {
        String rootUri = getResource(baseDir).getURI().toString();
        String utf8RootUri = URLDecoder.decode(rootUri.endsWith("/") ? rootUri : rootUri + "/", StandardCharsets.UTF_8);
        // Get direct children names
        List<String> fileNames = new ArrayList<>();
        for (Resource resource : getResourcesIn(baseDir)) {
            String utf8ChildUri = URLDecoder.decode(resource.getURI().toString(), StandardCharsets.UTF_8);
            fileNames.add(utf8ChildUri.substring(utf8RootUri.length()));
        }
        return fileNames.stream().map(file -> baseDir + file).toList();
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

    private Resource[] getResourcesIn(String rootPath) throws IOException {
        String rootUri = getResource(rootPath).getURI().toString();
        String childPaths = (rootPath.endsWith("/")) ? rootPath + "**" : rootPath + "/**";
        // Filter only direct children
        return Arrays.stream(getResources(childPaths)).filter(resource -> {
            try {
                String childUri = resource.getURI().toString();
                boolean isChild = childUri.length() > rootUri.length() && !childUri.equals(rootUri + "/");
                boolean isDirInside = childUri.indexOf("/", rootUri.length() + 1) == childUri.length() - 1;
                boolean isFileInside = childUri.indexOf("/", rootUri.length() + 1) == -1;
                return isChild && (isDirInside || isFileInside);
            } catch (IOException e) {
                return false;
            }
        }).toArray(Resource[]::new);
    }

    private Resource[] getResources(String path) throws IOException {
        return new PathMatchingResourcePatternResolver().getResources(path.replace("\\", "/"));
    }

    private Resource getResource(String path) {
        return new PathMatchingResourcePatternResolver().getResource(path.replace("\\", "/"));
    }
}
