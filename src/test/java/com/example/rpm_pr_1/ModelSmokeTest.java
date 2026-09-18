package com.example.rpm_pr_1;

import com.example.rpm_pr_1.model.GradeValue;
import com.example.rpm_pr_1.model.Student;
import com.example.rpm_pr_1.model.StudentIdentity;

public final class ModelSmokeTest {
    private ModelSmokeTest() {
    }

    public static void main(String[] args) {
        Student student = new Student(1, new StudentIdentity("Анна", "ИС-21"), 4.5);
        require("Анна".equals(student.getName()), "Имя студента изменилось");
        require(Math.abs(student.getAverageGrade() - 4.5) < 0.0001, "Средний балл изменился");
        expectValidationFailure(() -> new GradeValue(6));
        expectValidationFailure(() -> new StudentIdentity("", "ИС-21"));
        System.out.println("MODEL_SMOKE_TEST_OK");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void expectValidationFailure(Runnable action) {
        try {
            action.run();
            throw new AssertionError("Ожидалась ошибка валидации");
        } catch (IllegalArgumentException expected) {
            // Ожидаемый результат проверки неверных данных.
        }
    }
}
