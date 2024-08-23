package com.demo.userservice.trinoservice.service;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TrinoService {

    private static final String TRINO_JDBC_URL = "jdbc:trino://192.168.1.206:9091";
    private static final String TRINO_USER = "test";
    private static final String TRINO_PASSWORD = "";

    public List<String> getTablesInSchema(String schemaName) {
        List<String> tables = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(TRINO_JDBC_URL, TRINO_USER, TRINO_PASSWORD);
             Statement statement = connection.createStatement()) {

            String query = String.format("SHOW TABLES FROM %s", "iceberg."+schemaName);

            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    tables.add(resultSet.getString(1));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }

        return tables;
    }

    public List<Map<String, Object>> getTableData(String schemaName, String tableName) {
        List<Map<String, Object>> tableData = new ArrayList<>();

        String query = String.format("SELECT * FROM %s.%s", "iceberg."+schemaName, tableName);

        try (Connection connection = DriverManager.getConnection(TRINO_JDBC_URL, TRINO_USER, TRINO_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            int columnCount = resultSet.getMetaData().getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i);
                    Object value = resultSet.getObject(i);
                    row.put(columnName, value);
                }
                tableData.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }

        return tableData;
    }

}
