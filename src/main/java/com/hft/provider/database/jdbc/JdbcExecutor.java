package com.hft.provider.database.jdbc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
class JdbcExecutor {
    // private static final String MYSQL_DRIVER_NAME = "com.mysql.jdbc.Driver";
    private static final String MYSQL_DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    protected boolean showSQL = false;
    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.jpa.show-sql}")
    private String showSQLProp;

    /**
     * <p>Loads JDBC Driver for connections. Is loaded on construct to make sure it is there.</p>
     * <p>Additional {@link DriverManager} configuration.</p>
     *
     * @throws ClassNotFoundException if the driver class cannot be located
     */
    @PostConstruct
    void postConstruct() throws ClassNotFoundException {
        showSQL = showSQLProp.equalsIgnoreCase("true");
        Class.forName(MYSQL_DRIVER_NAME);
        DriverManager.setLoginTimeout(5);
    }

    /**
     * Creates connection to the default database from the application.properties.
     * <p>Note: Dont forget to close the connection!</p>
     *
     * @return JDBC connection
     * @throws SQLException if a database access error occurs or the url is null
     */
    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, username, password);
    }
}
