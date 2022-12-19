package com.hft.provider.database.jdbc;

import com.hft.provider.controller.model.Job;
import com.hft.provider.controller.model.SolutionStatistic;
import com.hft.provider.controller.model.StoredSolution;
import com.hft.provider.database.utility.JsonParser;
import com.hft.provider.file.StatementReader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class SolutionSelector extends JdbcExecutor {
    private final Logger LOGGER = Logger.getLogger(SolutionSelector.class.getName());

    public List<SolutionStatistic> getSolutionStatistics() throws SQLException {
        List<SolutionStatistic> stats = new ArrayList<>();
        String sqlQuery = """
                SELECT s.id       as id,
                       s.creator  as creator,
                       s.makespan as makespan,
                       p.size     as size,
                       p.par      as par,
                       p.inst     as inst
                FROM solutions s
                         INNER JOIN
                     (SELECT MIN(s1.makespan) as makespan, s1.project_id as project_id
                      FROM solutions s1
                      GROUP BY s1.project_id) as sGroup
                     ON s.project_id = sGroup.project_id
                         AND s.makespan = sGroup.makespan
                         JOIN projects p on p.id = s.project_id
                """;
        try (Connection connection = createConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    SolutionStatistic stat = new SolutionStatistic();
                    stat.setId(resultSet.getLong("id"));
                    stat.setCreator(resultSet.getString("creator"));
                    stat.setMakespan(resultSet.getInt("makespan"));
                    stat.setSize(resultSet.getInt("size"));
                    stat.setPar(resultSet.getInt("par"));
                    stat.setInst(resultSet.getInt("inst"));
                    stats.add(stat);
                }
            }
        }
        return stats;
    }

    /**
     * <ol>
     *     <li>Reads statement from file resource.</li>
     *     <li>Append conditions.</li>
     *     <li>Connect to the database.</li>
     *     <li>Execute sql statement.</li>
     *     <li>Retrieve data.</li>
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
        String selectString = StatementReader.readSelectSolution();
        if (creator != null || size != null || par != null || inst != null) {
            selectString += " WHERE ";
        }
        if (creator != null) {
            selectString += "creator = ?";
        }
        if (size != null) {
            if (!selectString.endsWith(" WHERE ")) {
                selectString += " AND ";
            }
            selectString += "size = ?";
        }
        if (par != null) {
            if (!selectString.endsWith(" WHERE ")) {
                selectString += " AND ";
            }
            selectString += "par = ?";
        }
        if (inst != null) {
            if (!selectString.endsWith(" WHERE ")) {
                selectString += " AND ";
            }
            selectString += "inst = ?";
        }

        if (showSQL)
            System.out.println("Jdbc: " + selectString + " (creator=" + creator + ", size=" + size + ", par=" + par + ", inst=" + inst + ")");

        try (Connection connection = createConnection();
             PreparedStatement statement = connection.prepareStatement(selectString)) {

            int param = 1;
            if (creator != null)
                statement.setString(param++, creator);
            if (size != null)
                statement.setInt(param++, size);
            if (par != null)
                statement.setInt(param++, par);
            if (inst != null)
                statement.setInt(param, inst);

            try (ResultSet resultSet = statement.executeQuery()) {
                return retrieveSolution(resultSet);
            }
        }
    }

    private List<StoredSolution> retrieveSolution(ResultSet resultSet) throws SQLException {
        List<StoredSolution> solutions = new ArrayList<>();
        LOGGER.fine("Start mapping solutions of query.");
        long solutionId = -1;
        while (resultSet.next()) {
            long resultId = resultSet.getLong("id");
            // if solution id changes, it resets the mapper
            if (solutionId != resultId) {
                solutionId = resultId;
                solutions.add(readSolution(resultSet));
            }
            solutions.get(solutions.size() - 1).addJob(readJob(resultSet));
        }
        LOGGER.fine("Successfully retrieved " + solutions.size() + " solutions from query.");
        return solutions;
    }

    private StoredSolution readSolution(ResultSet resultSet) throws SQLException {
        StoredSolution storedSolution = new StoredSolution();
        storedSolution.setId(resultSet.getLong("id"));
        storedSolution.setCreationDate(resultSet.getString("date"));
        storedSolution.setCreationTime(resultSet.getString("time"));
        storedSolution.setCreator(resultSet.getString("creator"));
        storedSolution.setMakespan(resultSet.getInt("makespan"));
        storedSolution.setSize(resultSet.getInt("size"));
        storedSolution.setPar(resultSet.getInt("par"));
        storedSolution.setInst(resultSet.getInt("inst"));
        storedSolution.setR1CapacityPerDay(resultSet.getInt("r1"));
        storedSolution.setR2CapacityPerDay(resultSet.getInt("r2"));
        storedSolution.setR3CapacityPerDay(resultSet.getInt("r3"));
        storedSolution.setR4CapacityPerDay(resultSet.getInt("r4"));
        storedSolution.setHorizon(resultSet.getInt("horizon"));
        storedSolution.setJobCount(resultSet.getInt("jobCount"));
        return storedSolution;
    }

    private Job readJob(ResultSet resultSet) throws SQLException {
        Job job = new Job();
        job.setNr(resultSet.getInt("jNr"));
        job.setSuccessorCount(resultSet.getInt("jSucCount"));
        job.setSuccessors(JsonParser.convertToList(resultSet.getString("jSuc")));
        job.setPredecessorCount(resultSet.getInt("jPreCount"));
        job.setPredecessors(JsonParser.convertToList(resultSet.getString("jPre")));
        job.setMode(resultSet.getInt("jMode"));
        job.setDurationDays(resultSet.getInt("jDuration"));
        job.setR1HoursPerDay(resultSet.getInt("jR1"));
        job.setR2HoursPerDay(resultSet.getInt("jR2"));
        job.setR3HoursPerDay(resultSet.getInt("jR3"));
        job.setR4HoursPerDay(resultSet.getInt("jR4"));
        job.setStartDay(resultSet.getInt("jStart"));
        return job;
    }
}
