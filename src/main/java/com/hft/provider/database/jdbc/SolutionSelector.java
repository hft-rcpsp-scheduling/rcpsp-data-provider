package com.hft.provider.database.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hft.provider.controller.model.StoredSolution;
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
public class SolutionSelector extends JdbcExecutor {
    private final Logger LOGGER = Logger.getLogger(SolutionSelector.class.getName());


    /**
     * <ol>
     *     <li>Reads statement from file resource.</li>
     *     <li>Connect to default database.</li>
     *     <li>Execute static sql statement.</li>
     * </ol>
     *
     * @param creator null or creator condition
     * @param size    null or size condition
     * @param par     null or par condition
     * @param inst    null or inst condition
     * @return parsed solution set
     * @throws IOException  if file not found
     * @throws SQLException if a database access error occurs or this method is called on a closed result set
     */
    public List<StoredSolution> selectSolutions(String creator, Integer size, Integer par, Integer inst) throws IOException, SQLException {
        String selectString = new StatementReader().readSelectSolution();
        if (creator != null || size != null || par != null || inst != null) {
            selectString += " WHERE ";
        }
        if (creator != null) {
            selectString += "creator = '" + creator + "'";
        }
        if (size != null) {
            if (!selectString.endsWith(" WHERE ")) {
                selectString += " AND ";
            }
            selectString += "size = '" + size + "'";
        }
        if (par != null) {
            if (!selectString.endsWith(" WHERE ")) {
                selectString += " AND ";
            }
            selectString += "par = '" + par + "'";
        }
        if (inst != null) {
            if (!selectString.endsWith(" WHERE ")) {
                selectString += " AND ";
            }
            selectString += "inst = '" + inst + "'";
        }

        if (showSQL) System.out.println("Jdbc: " + selectString);

        try (Connection connection = createConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectString)) {

            return retrieveSolution(resultSet);
        }
    }

    private List<StoredSolution> retrieveSolution(ResultSet resultSet) throws SQLException, JsonProcessingException {
        List<StoredSolution> solutions = new ArrayList<>();
        LOGGER.info("Start mapping solutions of query.");
        long solutionId = -1;
        SolutionMapper mapper = new SolutionMapper();
        while (resultSet.next()) {
            long resultId = resultSet.getLong("id");
            // sets first id
            if (solutionId == -1) {
                solutionId = resultId;
            }
            // if solution id changes, it resets the mapper
            if (solutionId != resultId) {
                solutionId = resultId;
                solutions.add(mapper.retrieveSolution());
                mapper = new SolutionMapper();
            }
            mapper.appendResult(resultSet);
        }
        try {
            solutions.add(mapper.retrieveSolution());
        } catch (IllegalStateException ignored) {
        }
        LOGGER.info("Retrieved " + solutions.size() + " solutions.");
        return solutions;
    }
}
