package de.drippinger.fakegen.basic;

import de.drippinger.fakegen.TestDataFiller;
import de.drippinger.fakegen.types.OptionalType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dennis Rippinger
 */
class OptionalTypeTest {

    TestDataFiller tdf = new TestDataFiller();

    @Test
    void should_fill_optional() {
        OptionalType optionalType = tdf.fillInstance(OptionalType.class);

        assertThat(optionalType.getOptionalString()).isPresent();
        assertThat(optionalType.getOptionalString().get()).hasSize(10);

        assertThat(optionalType.getOptionalStringList()).isNotNull();
        assertThat(optionalType.getOptionalStringList().get()).isEmpty();

        assertThat(optionalType.getOptionalMap()).isNotNull();
        assertThat(optionalType.getOptionalMap().get()).isEmpty();

    }
}
