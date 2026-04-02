package com.org.learning;

import lombok.Getter;

@Getter
public enum ProblemSingletonEnum {

    INSTANCE;   // the single instance of the enum

    private int someValue;

    // business logic method
    public void doBusinessLogicWork() {
        // your logic here
    }

    public void setSomeValue(int someValue) {
        this.someValue = someValue;
    }
}

/*
public class Main {
    void main() {
        ProblemSingletonEnum singleton = ProblemSingletonEnum.INSTANCE;

        singleton.setSomeValue(42);
        singleton.doBusinessLogicWork();

        int v = ProblemSingletonEnum.INSTANCE.getSomeValue();
        System.out.println(v);
    }
}
*/

