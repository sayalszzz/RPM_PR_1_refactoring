package com.example.rpm_pr_1.dao;

import com.example.rpm_pr_1.model.Student;
import com.example.rpm_pr_1.model.Subject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeDAO {

    public List<Student> getAllStudentsWithAverage() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT s.id, s.name, s.group_name, AVG(g.grade_value) AS avg_grade " +
                "FROM students s " +
                "LEFT JOIN grades g ON s.id = g.student_id " +
                "GROUP BY s.id, s.name, s.group_name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String groupName = rs.getString("group_name");
                double avgGrade = rs.getDouble("avg_grade");
                avgGrade = Math.round(avgGrade * 100.0) / 100.0; // Округление

                students.add(new Student(id, name, groupName, avgGrade));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        String query = "SELECT * FROM subjects";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                subjects.add(new Subject(rs.getInt("id"), rs.getString("title")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return subjects;
    }

    public void addStudent(String name, String groupName) {
        String query = "INSERT INTO students (name, group_name) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, groupName);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void addSubject(String title) {
        String query = "INSERT INTO subjects (title) VALUES (?) ON DUPLICATE KEY UPDATE title=title";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, title);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void addGrade(int studentId, int subjectId, double gradeValue) {
        String query = "INSERT INTO grades (student_id, subject_id, grade_value) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, subjectId);
            stmt.setDouble(3, gradeValue);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
