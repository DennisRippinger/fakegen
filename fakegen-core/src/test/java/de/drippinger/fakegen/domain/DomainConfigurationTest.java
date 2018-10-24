package de.drippinger.fakegen.domain;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dennis Rippinger
 */
class DomainConfigurationTest {

    @Test
    void should_add_and_get_for_string() {
        DomainConfiguration domain = new SimpleDomainConfiguration(new Random());

        domain.fieldForStringShouldUse("someField", () -> "someName");

        String someName = domain.getSupplier("someField", String.class)
                .map(Supplier::get)
                .orElse("");

        assertThat(someName).isEqualTo("someName");
    }

    @Test
    void should_add_and_get_for_a_object_type() {
        DomainConfiguration domain = new SimpleDomainConfiguration(new Random());

        domain.fieldForClassShouldUse("someField", Integer.class, () -> 1);

        Integer someInteger = domain.getSupplier("someField", Integer.class)
                .map(Supplier::get)
                .orElse(0);

        assertThat(someInteger).isEqualTo(1);
    }

    @Test
    void should_add_and_get_for_a_primitive_type() {
        DomainConfiguration domain = new SimpleDomainConfiguration(new Random());

        domain.fieldForClassShouldUse("someField", int.class, () -> 1);

        Integer someInteger = domain.getSupplier("someField", Integer.class)
                .map(Supplier::get)
                .orElse(0);

        assertThat(someInteger).isEqualTo(1);
    }

}
