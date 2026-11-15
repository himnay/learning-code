package com.org.learning.core.stream.thread.singleton;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProblemSingletonTest {

    @Test
    @DisplayName("getInstance always returns the same instance")
    void sameInstance() {
        assertThat(ProblemSingleton.getInstance()).isSameAs(ProblemSingleton.getInstance());
    }

    @Test
    @DisplayName("a second instance cannot be built through reflection")
    void reflectionIsRejected() throws Exception {
        ProblemSingleton.getInstance();
        var constructor = ProblemSingleton.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        assertThatThrownBy(constructor::newInstance)
                .isInstanceOf(InvocationTargetException.class)
                .hasCauseInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("deserialization resolves to the existing instance")
    void serializationKeepsTheSingleton() throws Exception {
        var bytes = new ByteArrayOutputStream();
        try (var out = new ObjectOutputStream(bytes)) {
            out.writeObject(ProblemSingleton.getInstance());
        }
        try (var in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            assertThat(in.readObject()).isSameAs(ProblemSingleton.getInstance());
        }
    }

    @Test
    @DisplayName("cloning is not allowed")
    void cloneIsRejected() {
        assertThatThrownBy(() -> ProblemSingleton.getInstance().clone())
                .isInstanceOf(CloneNotSupportedException.class);
    }

    @Test
    @DisplayName("the enum singleton keeps one shared state")
    void enumSingleton() {
        ProblemSingletonEnum.INSTANCE.setSomeValue(42);
        assertThat(ProblemSingletonEnum.valueOf("INSTANCE").getSomeValue()).isEqualTo(42);
    }
}
