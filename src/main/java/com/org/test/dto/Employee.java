package com.org.test.dto;

public class Employee {

    private String name;
    private String department;
    private int age;
    private double salary;
    private long mobile;

    public Employee(String name, String department, int age, double salary, long mobile) {
        super();
        this.name = name;
        this.department = department;
        this.age = age;
        this.salary = salary;
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public long getMobile() {
        return mobile;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    @Override
    public String toString() {
        return "Employee [name=" + name + ", department=" + department + ", age=" + age + ", salary=" + salary
                + ", mobile=" + mobile + "]";
    }

}
