package com.hft.provider.database.jdbc;

import com.hft.provider.controller.model.StoredSolution;
import com.hft.provider.file.StatementReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.logging.Logger;

@Component
public class JdbcExecutor {
    // private static final String MYSQL_DRIVER_NAME = "com.mysql.jdbc.Driver";
    private static final String MYSQL_DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private final Logger LOGGER = Logger.getLogger(JdbcExecutor.class.getName());
    private final String dbUrl;
    private final String username;
    private final String password;
    private final boolean showSQL;

    public JdbcExecutor(@Value("${spring.datasource.url}") String dbUrl,
                        @Value("${spring.datasource.username}") String username,
                        @Value("${spring.datasource.password}") String password,
                        @Value("${spring.jpa.show-sql}") String showSQL) {
        LOGGER.info("Datasource=(" + dbUrl + ")");
        this.dbUrl = dbUrl;
        this.username = username;
        this.password = password;
        this.showSQL = showSQL.equalsIgnoreCase("true");
    }

    /**
     * <p>Loads JDBC Driver for connections. Is loaded on construct to make sure it is there.</p>
     * <p>Additional {@link DriverManager} configuration.</p>
     *
     * @throws ClassNotFoundException if the driver class cannot be located
     */
    @PostConstruct
    void postConstruct() throws ClassNotFoundException {
        Class.forName(MYSQL_DRIVER_NAME);
        DriverManager.setLoginTimeout(5);
    }

    /**
     * <ol>
     *     <li>Reads statement from file resource.</li>
     *     <li>Connect to default database.</li>
     *     <li>Execute static sql statement.</li>
     * </ol>
     *
     * @param fileName in the directory: resources/sql-statements/fileName
     * @return extracted response
     * @throws SQLException if a database access error occurs or this method is called on a closed result set
     * @throws IOException  if file not found
     */
    public List<StoredSolution> executeStaticFile(String fileName) throws SQLException, IOException {
        String fileSqlStatement = new StatementReader().readStaticSqlStatement(fileName);
        if (showSQL) System.out.println("Jdbc: " + fileSqlStatement);
        try (Connection connection = DriverManager.getConnection(dbUrl, username, password);
             Statement statement = connection.createStatement()) {
            return executeFileStatement(statement, fileSqlStatement);
        }
    }

    /**
     * <ul>
     *     <li>If statements start with SELECT: Executes single SELECT statement.</li>
     *     <li>Else: Executes all found statements and collects instance count in the response.</li>
     * </ul>
     *
     * @param connectionStatement statement from jdbc connection
     * @param fileSqlStatement    statement from file resource
     * @return extracted response
     * @throws SQLException if a database access error occurs or this method is called on a closed result set
     */
    private List<StoredSolution> executeFileStatement(Statement connectionStatement, String fileSqlStatement) throws SQLException, IOException {
        if (fileSqlStatement.toLowerCase().startsWith("select")) {
            return executeSelectStatement(connectionStatement, fileSqlStatement);
        } else {
            throw new IOException("SQL Statement does not start with SELECT.");
        }
    }

    /**
     * Executes single SELECT statement.
     *
     * @param connectionStatement statement from jdbc connection
     * @param sqlQuery            SELECT sql statement
     * @return extracted response
     * @throws SQLException if a database access error occurs or this method is called on a closed result set
     */
    private List<StoredSolution> executeSelectStatement(Statement connectionStatement, String sqlQuery) throws SQLException {
        LOGGER.info("Execute SQL Query Statement:\n" + sqlQuery);
        try (ResultSet resultSet = connectionStatement.executeQuery(sqlQuery)) {
            return ResultMapper.resultToResponse(resultSet);
        }
    }
}
