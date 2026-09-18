package com.example.rpm_pr_1.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // В исходной учебной версии учётные данные были зашиты в код.
    // Реальные значения удалены перед публикацией ветки на GitHub.
    private static final String URL = "jdbc:mysql://localhost:3306/student_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "CHANGE_ME";
    private static final String PASSWORD = "CHANGE_ME";

    private static Connection connection = null;

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                System.err.println("Драйвер MySQL JDBC не найден!");
                e.printStackTrace();
                throw new SQLException(e);
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Подключение к БД успешно закрыто.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
