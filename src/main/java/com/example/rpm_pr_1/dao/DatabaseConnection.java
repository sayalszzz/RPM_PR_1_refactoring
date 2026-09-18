package com.example.rpm_pr_1.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {
    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/student_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DEFAULT_USER = "root";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        String url = readSetting("student.db.url", "STUDENT_DB_URL", DEFAULT_URL);
        String user = readSetting("student.db.user", "STUDENT_DB_USER", DEFAULT_USER);
        String password = readSetting("student.db.password", "STUDENT_DB_PASSWORD", "");
        return DriverManager.getConnection(url, user, password);
    }

    private static String readSetting(String propertyName, String environmentName, String defaultValue) {
        String propertyValue = System.getProperty(propertyName);
        if (propertyValue != null && !propertyValue.isBlank()) {
            return propertyValue;
        }
        String environmentValue = System.getenv(environmentName);
        return environmentValue == null || environmentValue.isBlank() ? defaultValue : environmentValue;
    }
}
