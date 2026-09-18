package com.example.rpm_pr_1.model;

public class Subject {
    private int id;
    private String title;

    // Пустой конструктор
    public Subject() {}

    // Конструктор со всеми полями
    public Subject(int id, String title) {
        this.id = id;
        this.title = title;
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    // Переопределение метода для красивого отображения в ComboBox
    @Override
    public String toString() {
        return title;
    }
}
