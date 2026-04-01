package com.org.dto;

public record Employee(
        String name,
        String department,
        int age,
        double salary,
        long mobile
) {
    // ✅ Compact constructor (for validation only, no field assignments)
    public Employee {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (age < 18 || age > 100) {
            throw new IllegalArgumentException("Age must be between 18 and 100");
        }
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }
    }

    // ✅ Additional constructor (must delegate to canonical)
    public Employee(String name, String department, int age, double salary) {
        this(name, department, age, salary, 0L);  // Default mobile
    }

    // ✅ No-arg constructor (must delegate to canonical)
    public Employee() {
        this("Unknown", "Unassigned", 0, 0.0, 0L);
    }
}