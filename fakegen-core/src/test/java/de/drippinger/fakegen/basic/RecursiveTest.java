package de.drippinger.fakegen.basic;

import de.drippinger.fakegen.TestDataFiller;
import de.drippinger.fakegen.types.RecursiveType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dennis Rippinger
 */
public class RecursiveTest {

    private TestDataFiller filler = new TestDataFiller();

    @Test
    @DisplayName("Should fill a recursive Type once")
    void createRandomFilledInstance_fill_nested() {
        RecursiveType randomFilledInstance = filler.createRandomFilledInstance(RecursiveType.class);

        assertThat(randomFilledInstance).isNotNull();
        assertThat(randomFilledInstance.getName()).hasSize(10);
        assertThat(randomFilledInstance.getRecursiveType().getName()).hasSize(10);
        assertThat(randomFilledInstance.getRecursiveType().getRecursiveType()).isNull();
    }
}
