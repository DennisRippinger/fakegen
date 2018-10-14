package de.drippinger.fakegen.basic;

import de.drippinger.fakegen.TestDataFiller;
import de.drippinger.fakegen.filler.BasicObjectFiller;
import de.drippinger.fakegen.types.NestedType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dennis Rippinger
 */
public class NestedObjectTest {

    private TestDataFiller filler = new TestDataFiller(new BasicObjectFiller());

    @Test
    @DisplayName("Should fill a complex nested Type")
    void createRandomFilledInstance_fill_nested() {
        NestedType randomFilledInstance = filler.createRandomFilledInstance(NestedType.class);

        assertThat(randomFilledInstance).isNotNull();
        assertThat(randomFilledInstance.getName()).hasSize(10);
        assertThat(randomFilledInstance.getSimpleType().getBirthday()).isToday();
        assertThat(randomFilledInstance.getSimpleType().getName()).hasSize(10);
    }
}
