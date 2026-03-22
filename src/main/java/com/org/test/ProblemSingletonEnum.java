package com.org.test;

public enum ProblemSingletonEnum {

    INSTANCE;   // the single instance of the enum

    private int someValue;

    // business logic method
    public void doBusinessLogicWork() {
        // your logic here
    }

    public int getSomeValue() {
        return someValue;
    }

    public void setSomeValue(int someValue) {
        this.someValue = someValue;
    }
}

/*
public class Main {
    public static void main(String[] args) {
        ProblemSingletonEnum singleton = ProblemSingletonEnum.INSTANCE;

        singleton.setSomeValue(42);
        singleton.doBusinessLogicWork();

        int v = ProblemSingletonEnum.INSTANCE.getSomeValue();
        System.out.println(v);
    }
}
*/

