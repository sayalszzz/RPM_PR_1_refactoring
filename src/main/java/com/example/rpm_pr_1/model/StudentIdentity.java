package com.example.rpm_pr_1.model;

public record StudentIdentity(String name, String groupName) {
    public StudentIdentity {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("ФИО студента не заполнено");
        }
        if (groupName == null || groupName.isBlank()) {
            throw new IllegalArgumentException("Группа студента не заполнена");
        }
        name = name.trim();
        groupName = groupName.trim();
    }
}
