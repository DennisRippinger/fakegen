package de.drippinger.fakegen.basic;

import de.drippinger.fakegen.TestDataFiller;
import de.drippinger.fakegen.types.NoDefaultConstructorType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dennis Rippinger
 */
public class NoDefaultConstructorTest {

    private TestDataFiller filler = new TestDataFiller();

    @Test
    void should_not_create_class_with_no_empty_constructor() {
        NoDefaultConstructorType randomFilledInstance = filler
                .createRandomFilledInstance(NoDefaultConstructorType.class);

        assertThat(randomFilledInstance).isNull();

    }
}
