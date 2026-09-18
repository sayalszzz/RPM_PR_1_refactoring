package com.example.rpm_pr_1.controller;

import com.example.rpm_pr_1.dao.GradeDAO;
import com.example.rpm_pr_1.model.Student;
import com.example.rpm_pr_1.model.Subject;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class GradeController {

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, Integer> colId;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, String> colGroup;
    @FXML private TableColumn<Student, Double> colAvgGrade;

    @FXML private TextField txtStudentName;
    @FXML private TextField txtGroupName;
    @FXML private TextField txtSubjectTitle;
    @FXML private TextField txtGradeValue;

    @FXML private ComboBox<Student> comboStudents;
    @FXML private ComboBox<Subject> comboSubjects;

    private final GradeDAO gradeDAO = new GradeDAO();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colGroup.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        colAvgGrade.setCellValueFactory(new PropertyValueFactory<>("averageGrade"));

        loadData();
    }

    private void loadData() {
        studentTable.setItems(FXCollections.observableArrayList(gradeDAO.getAllStudentsWithAverage()));
        comboStudents.setItems(FXCollections.observableArrayList(gradeDAO.getAllStudentsWithAverage()));
        comboSubjects.setItems(FXCollections.observableArrayList(gradeDAO.getAllSubjects()));
    }

    @FXML
    private void handleAddStudent() {
        String name = txtStudentName.getText().trim();
        String group = txtGroupName.getText().trim();

        if (!name.isEmpty() && !group.isEmpty()) {
            gradeDAO.addStudent(name, group);
            txtStudentName.clear();
            txtGroupName.clear();
            loadData();
        } else {
            showAlert("Заполните поля имени и группы!");
        }
    }

    @FXML
    private void handleAddSubject() {
        String title = txtSubjectTitle.getText().trim();
        if (!title.isEmpty()) {
            gradeDAO.addSubject(title);
            txtSubjectTitle.clear();
            loadData();
        } else {
            showAlert("Введите название предмета!");
        }
    }

    @FXML
    private void handleAddGrade() {
        Student student = comboStudents.getValue();
        Subject subject = comboSubjects.getValue();
        String gradeStr = txtGradeValue.getText().trim();

        if (student != null && subject != null && !gradeStr.isEmpty()) {
            try {
                double grade = Double.parseDouble(gradeStr);
                gradeDAO.addGrade(student.getId(), subject.getId(), grade);
                txtGradeValue.clear();
                loadData();
            } catch (NumberFormatException e) {
                showAlert("Некорректный формат оценки!");
            }
        } else {
            showAlert("Заполните все поля для выставления оценки!");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
