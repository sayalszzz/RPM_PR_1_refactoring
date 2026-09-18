module com.example.rpm_pr_1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.rpm_pr_1.controller to javafx.fxml;
    opens com.example.rpm_pr_1.model to javafx.base;
    opens com.example.rpm_pr_1 to javafx.fxml;

    exports com.example.rpm_pr_1;
}
