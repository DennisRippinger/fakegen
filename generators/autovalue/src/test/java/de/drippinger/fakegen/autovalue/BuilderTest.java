package de.drippinger.fakegen.autovalue;

import de.drippinger.fakegen.TestDataFiller;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dennis Rippinger
 */
class BuilderTest {

    private TestDataFiller tdf = new TestDataFiller();

    @Test
    void should_use_autovalue_builder() {
        Animal animal = tdf.createFromBuilder(AutoValue_Animal.Builder.class)
                .setNumberOfLegs(10)
                .build();

        assertThat(animal).isNotNull();
        assertThat(animal.name()).hasSize(10);
        assertThat(animal.numberOfLegs()).isEqualTo(10);
    }

}
