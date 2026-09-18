package com.example.rpm_pr_1.model;

public final class Student {
    private final int id;
    private final StudentIdentity identity;
    private final double averageGrade;

    public Student(int id, StudentIdentity identity, double averageGrade) {
        if (id < 0) {
            throw new IllegalArgumentException("Идентификатор студента не может быть отрицательным");
        }
        if (identity == null) {
            throw new IllegalArgumentException("Данные студента не заданы");
        }
        if (averageGrade < 0 || averageGrade > GradeValue.MAX_VALUE) {
            throw new IllegalArgumentException("Средний балл должен находиться в диапазоне от 0 до 5");
        }
        this.id = id;
        this.identity = identity;
        this.averageGrade = averageGrade;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return identity.name();
    }

    public String getGroupName() {
        return identity.groupName();
    }

    public double getAverageGrade() {
        return averageGrade;
    }

    @Override
    public String toString() {
        return getName() + " (" + getGroupName() + ")";
    }
}
