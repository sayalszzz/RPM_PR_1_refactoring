package com.example.rpm_pr_1.controller;

import com.example.rpm_pr_1.dao.GradeDAO;
import com.example.rpm_pr_1.model.GradeValue;
import com.example.rpm_pr_1.model.Student;
import com.example.rpm_pr_1.model.StudentIdentity;
import com.example.rpm_pr_1.model.Subject;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class GradeController {
    private static final int PAGE_SIZE = 25;

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
    @FXML private Button btnPreviousPage;
    @FXML private Button btnNextPage;
    @FXML private Label lblPage;
    @FXML private Label lblStatus;
    @FXML private ProgressIndicator progressIndicator;

    private final GradeDAO gradeDAO = new GradeDAO();
    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor(task -> {
        Thread thread = new Thread(task, "student-database-worker");
        thread.setDaemon(true);
        return thread;
    });
    private int currentPage;
    private int totalPages = 1;
    private List<Subject> cachedSubjects = List.of();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colGroup.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        colAvgGrade.setCellValueFactory(new PropertyValueFactory<>("averageGrade"));

        loadData(true);
    }

    private void loadData(boolean reloadSubjects) {
        Task<ScreenData> task = new Task<>() {
            @Override
            protected ScreenData call() {
                int count = gradeDAO.countStudents();
                int pages = Math.max(1, (count + PAGE_SIZE - 1) / PAGE_SIZE);
                int requestedPage = Math.min(currentPage, pages - 1);
                List<Student> students = gradeDAO.getStudentsPage(PAGE_SIZE, requestedPage * PAGE_SIZE);
                List<Subject> subjects = reloadSubjects ? gradeDAO.getAllSubjects() : cachedSubjects;
                return new ScreenData(students, subjects, pages, requestedPage);
            }
        };
        runDatabaseTask(task, data -> {
            currentPage = data.page();
            totalPages = data.totalPages();
            cachedSubjects = List.copyOf(data.subjects());
            studentTable.setItems(FXCollections.observableArrayList(data.students()));
            comboStudents.setItems(FXCollections.observableArrayList(data.students()));
            comboSubjects.setItems(FXCollections.observableArrayList(cachedSubjects));
            lblPage.setText("Страница " + (currentPage + 1) + " из " + totalPages);
            btnPreviousPage.setDisable(currentPage == 0);
            btnNextPage.setDisable(currentPage + 1 >= totalPages);
        });
    }

    @FXML
    private void handleAddStudent() {
        try {
            StudentIdentity identity = new StudentIdentity(txtStudentName.getText(), txtGroupName.getText());
            runWriteTask("Добавление студента", () -> gradeDAO.addStudent(identity), () -> {
                txtStudentName.clear();
                txtGroupName.clear();
                currentPage = 0;
                loadData(false);
            });
        } catch (IllegalArgumentException exception) {
            showAlert(exception.getMessage());
        }
    }

    @FXML
    private void handleAddSubject() {
        try {
            String title = txtSubjectTitle.getText();
            runWriteTask("Добавление предмета", () -> gradeDAO.addSubject(title), () -> {
                txtSubjectTitle.clear();
                loadData(true);
            });
        } catch (IllegalArgumentException exception) {
            showAlert(exception.getMessage());
        }
    }

    @FXML
    private void handleAddGrade() {
        Student student = comboStudents.getValue();
        Subject subject = comboSubjects.getValue();
        String gradeStr = txtGradeValue.getText().trim();

        if (student == null || subject == null || gradeStr.isEmpty()) {
            showAlert("Заполните все поля для выставления оценки!");
            return;
        }
        try {
            GradeValue grade = new GradeValue(Double.parseDouble(gradeStr));
            runWriteTask(
                    "Сохранение оценки",
                    () -> gradeDAO.addGrade(student.getId(), subject.getId(), grade),
                    () -> {
                        txtGradeValue.clear();
                        loadData(false);
                    }
            );
        } catch (NumberFormatException exception) {
            showAlert("Оценка должна быть числом от 1 до 5");
        } catch (IllegalArgumentException exception) {
            showAlert(exception.getMessage());
        }
    }

    @FXML
    private void handlePreviousPage() {
        if (currentPage > 0) {
            currentPage--;
            loadData(false);
        }
    }

    @FXML
    private void handleNextPage() {
        if (currentPage + 1 < totalPages) {
            currentPage++;
            loadData(false);
        }
    }

    public void shutdown() {
        databaseExecutor.shutdownNow();
    }

    private void runWriteTask(String operation, Runnable action, Runnable onSuccess) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                action.run();
                return null;
            }
        };
        runDatabaseTask(task, ignored -> {
            lblStatus.setText(operation + " выполнено");
            onSuccess.run();
        });
    }

    private <T> void runDatabaseTask(Task<T> task, java.util.function.Consumer<T> onSuccess) {
        setBusy(true);
        task.setOnSucceeded(event -> {
            setBusy(false);
            onSuccess.accept(task.getValue());
        });
        task.setOnFailed(event -> {
            setBusy(false);
            Throwable error = task.getException();
            showError(error == null ? "Неизвестная ошибка базы данных" : error.getMessage());
        });
        databaseExecutor.execute(task);
    }

    private void setBusy(boolean busy) {
        progressIndicator.setVisible(busy);
        lblStatus.setText(busy ? "Работа с базой данных..." : "Готово");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Проверьте введённые данные");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Операция не выполнена");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private record ScreenData(List<Student> students, List<Subject> subjects, int totalPages, int page) {
    }
}
