package com.hft.provider.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

public class StatementReader extends FileReader {
    private static final String STATEMENT_DIR = "sql-statements/";

    /**
     * @param fileName in the directory: resources/sql-statements/fileName
     * @return content of file as string
     * @throws IOException if content is empty
     */
    public String readStaticSqlStatement(String fileName) throws IOException {
        String staticSqlStatement;
        try (BufferedReader reader = this.getResourceReader(STATEMENT_DIR + fileName)) {
            staticSqlStatement = reader.lines().collect(Collectors.joining("\n"));
        }
        if (!staticSqlStatement.isEmpty()) {
            return staticSqlStatement;
        } else {
            throw new IOException("Content of file " + fileName + " not found in " + STATEMENT_DIR + "!");
        }
    }
}
