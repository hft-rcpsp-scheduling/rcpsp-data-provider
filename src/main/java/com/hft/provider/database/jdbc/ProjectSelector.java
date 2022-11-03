package com.hft.provider.database.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hft.provider.controller.model.Project;
import com.hft.provider.file.StatementReader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class ProjectSelector extends JdbcExecutor {
    private final Logger LOGGER = Logger.getLogger(ProjectSelector.class.getName());

    /**
     * <ol>
     *     <li>Reads statement from file resource.</li>
     *     <li>Connect to the database.</li>
     *     <li>Execute sql statement.</li>
     *     <li>Retrieve data.</li>
     * </ol>
     *
     * @return parsed project set
     * @throws IOException  if file not found
     * @throws SQLException if a database access error occurs or this method is called on a closed result set
     */
    public List<Project> selectProjects() throws IOException, SQLException {
        String selectString = new StatementReader().readSelectProjects();

        if (showSQL) System.out.println("Jdbc: " + selectString);

        try (Connection connection = createConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectString)) {

            return retrieveProjects(resultSet);
        }
    }

    private List<Project> retrieveProjects(ResultSet resultSet) throws SQLException, JsonProcessingException {
        List<Project> projects = new ArrayList<>();
        LOGGER.info("Start mapping projects of query.");
        String projectId = "";
        ProjectMapper mapper = new ProjectMapper();
        while (resultSet.next()) {
            String resultId = resultSet.getString("id");
            // sets first id
            if (projectId.equals("")) {
                projectId = resultId;
            }
            // if solution id changes, it resets the mapper
            if (!projectId.equals(resultId)) {
                projectId = resultId;
                projects.add(mapper.retrieveProject());
                mapper = new ProjectMapper();
            }
            mapper.appendResult(resultSet);
        }
        try {
            projects.add(mapper.retrieveProject());
        } catch (IllegalStateException ignored) {
        }
        LOGGER.info("Retrieved " + projects.size() + " projects.");
        return projects;
    }
}
