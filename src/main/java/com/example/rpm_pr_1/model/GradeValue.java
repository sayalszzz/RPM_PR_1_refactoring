package com.example.rpm_pr_1.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record GradeValue(double value) {
    public static final double MIN_VALUE = 1.0;
    public static final double MAX_VALUE = 5.0;

    public GradeValue {
        if (!Double.isFinite(value) || value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException("Оценка должна быть числом от 1 до 5");
        }
    }

    public static double roundAverage(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
