package com.hft.provider.database.jdbc;

import com.hft.provider.controller.model.StoredSolution;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultMapper {

    public static List<StoredSolution> resultToResponse(ResultSet resultSet) throws SQLException {
        List<StoredSolution> solutions = new ArrayList<>();
        while (resultSet.next()) {
            solutions.add(extractRow(resultSet));
        }
        return solutions;
    }

    private static StoredSolution extractRow(ResultSet resultSet) throws SQLException {
        StoredSolution solution = new StoredSolution();

        solution.setId(resultSet.getLong("id"));
        return solution;
    }
}
