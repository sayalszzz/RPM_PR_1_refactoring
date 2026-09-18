package com.example.rpm_pr_1.dao;

import com.example.rpm_pr_1.model.GradeValue;
import com.example.rpm_pr_1.model.Student;
import com.example.rpm_pr_1.model.StudentIdentity;
import com.example.rpm_pr_1.model.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class GradeDAO {
    private static final String SELECT_STUDENTS_PAGE = """
            SELECT s.id, s.name, s.group_name, COALESCE(AVG(g.grade_value), 0) AS avg_grade
            FROM students s
            LEFT JOIN grades g ON s.id = g.student_id
            GROUP BY s.id, s.name, s.group_name
            ORDER BY s.name, s.id
            LIMIT ? OFFSET ?
            """;
    private static final String COUNT_STUDENTS = "SELECT COUNT(*) FROM students";
    private static final String SELECT_SUBJECTS = "SELECT id, title FROM subjects ORDER BY title, id";
    private static final String INSERT_STUDENT =
            "INSERT INTO students (name, group_name) VALUES (?, ?)";
    private static final String INSERT_SUBJECT =
            "INSERT INTO subjects (title) VALUES (?) ON DUPLICATE KEY UPDATE title = VALUES(title)";
    private static final String INSERT_GRADE =
            "INSERT INTO grades (student_id, subject_id, grade_value) VALUES (?, ?, ?)";

    public List<Student> getStudentsPage(int limit, int offset) {
        if (limit <= 0 || offset < 0) {
            throw new IllegalArgumentException("Некорректные параметры страницы");
        }
        List<Student> students = new ArrayList<>(limit);
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_STUDENTS_PAGE)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    students.add(mapStudent(resultSet));
                }
            }
            return students;
        } catch (SQLException exception) {
            throw databaseFailure("Не удалось загрузить студентов", exception);
        }
    }

    public int countStudents() {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(COUNT_STUDENTS);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (SQLException exception) {
            throw databaseFailure("Не удалось определить количество студентов", exception);
        }
    }

    public List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_SUBJECTS);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                subjects.add(mapSubject(resultSet));
            }
            return subjects;
        } catch (SQLException exception) {
            throw databaseFailure("Не удалось загрузить предметы", exception);
        }
    }

    public void addStudent(StudentIdentity identity) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_STUDENT)) {
            statement.setString(1, identity.name());
            statement.setString(2, identity.groupName());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw databaseFailure("Не удалось добавить студента", exception);
        }
    }

    public void addSubject(String title) {
        Subject subject = new Subject(0, title);
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SUBJECT)) {
            statement.setString(1, subject.getTitle());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw databaseFailure("Не удалось добавить предмет", exception);
        }
    }

    public void addGrade(int studentId, int subjectId, GradeValue grade) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_GRADE)) {
            statement.setInt(1, studentId);
            statement.setInt(2, subjectId);
            statement.setDouble(3, grade.value());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw databaseFailure("Не удалось сохранить оценку", exception);
        }
    }

    private Student mapStudent(ResultSet resultSet) throws SQLException {
        StudentIdentity identity = new StudentIdentity(
                resultSet.getString("name"),
                resultSet.getString("group_name")
        );
        return new Student(
                resultSet.getInt("id"),
                identity,
                GradeValue.roundAverage(resultSet.getDouble("avg_grade"))
        );
    }

    private Subject mapSubject(ResultSet resultSet) throws SQLException {
        return new Subject(resultSet.getInt("id"), resultSet.getString("title"));
    }

    private DataAccessException databaseFailure(String message, SQLException cause) {
        return new DataAccessException(message, cause);
    }
}
