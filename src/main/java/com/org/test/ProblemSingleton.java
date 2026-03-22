package com.org.test;

import java.io.Serializable;

public class ProblemSingleton implements Serializable, Cloneable {

    // MUST be volatile to ensure proper visibility in DCL
    private static volatile ProblemSingleton instance;

    private ProblemSingleton() {
        // Prevent reflection attacks: if an instance already exists, reject new construction
        if (instance != null) {
            throw new IllegalStateException(
                    "Singleton instance already created. Reflection attack prevented."
            );
        }
    }

    public static ProblemSingleton getInstance() {
        if (instance == null) {
            synchronized (ProblemSingleton.class) {
                if (instance == null) {
                    instance = new ProblemSingleton();
                }
            }
        }
        return instance;
    }

    // Serialization hook: ensure deserialization returns the existing instance
    private Object readResolve() {
        return instance;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning of this singleton is not allowed.");
    }
}
