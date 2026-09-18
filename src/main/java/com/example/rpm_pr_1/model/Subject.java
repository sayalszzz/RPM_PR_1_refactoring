package com.example.rpm_pr_1.model;

public final class Subject {
    private final int id;
    private final String title;

    public Subject(int id, String title) {
        if (id < 0) {
            throw new IllegalArgumentException("Идентификатор предмета не может быть отрицательным");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Название предмета не заполнено");
        }
        this.id = id;
        this.title = title.trim();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}
