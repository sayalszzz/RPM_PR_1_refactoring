package com.example.rpm_pr_1.model;

public class Student {
    private int id;
    private String name;
    private String groupName;
    private double averageGrade;

    public Student() {}

    public Student(int id, String name, String groupName, double averageGrade) {
        this.id = id;
        this.name = name;
        this.groupName = groupName;
        this.averageGrade = averageGrade;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public double getAverageGrade() { return averageGrade; }
    public void setAverageGrade(double averageGrade) { this.averageGrade = averageGrade; }

    @Override
    public String toString() {
        return name + " (" + groupName + ")";
    }
}
