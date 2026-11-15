package com.org.learning.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class EmployeeTest {

    @Test
    @DisplayName("the convenience constructors delegate to the canonical one with valid defaults")
    void convenienceConstructors() {
        assertThat(new Employee()).isEqualTo(new Employee("Unknown", "Unassigned", 18, 0.0, 0L));
        assertThat(new Employee("Ace", "IT", 24, 4000.0).mobile()).isZero();
    }

    @Test
    @DisplayName("the compact constructor rejects a blank name, an age outside 18-100 and a negative salary")
    void validation() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Employee(" ", "IT", 24, 4000.0));
        assertThatIllegalArgumentException().isThrownBy(() -> new Employee(null, "IT", 24, 4000.0));
        assertThatIllegalArgumentException().isThrownBy(() -> new Employee("Ace", "IT", 17, 4000.0));
        assertThatIllegalArgumentException().isThrownBy(() -> new Employee("Ace", "IT", 101, 4000.0));
        assertThatIllegalArgumentException().isThrownBy(() -> new Employee("Ace", "IT", 24, -1.0));
    }
}
