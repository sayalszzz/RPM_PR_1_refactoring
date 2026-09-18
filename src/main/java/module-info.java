module com.example.rpm_pr_1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java; // Нужен для корректной работы драйвера БД
    requires org.flywaydb.core;
    // Позволяет JavaFX FXMLLoader сканировать и инициализировать ваши контроллеры
    opens com.example.rpm_pr_1.controller to javafx.fxml;

    // Позволяет TableView читать свойства (геттеры) моделей Студента и Предмета
    opens com.example.rpm_pr_1.model to javafx.base;

    // Опционально: если fxml лежит непосредственно в главном пакете
    opens com.example.rpm_pr_1 to javafx.fxml;

    exports com.example.rpm_pr_1;
}