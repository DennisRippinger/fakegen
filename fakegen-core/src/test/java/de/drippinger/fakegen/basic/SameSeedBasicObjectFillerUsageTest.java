package de.drippinger.fakegen.basic;

import de.drippinger.fakegen.TestDataFiller;
import de.drippinger.fakegen.filler.BasicObjectFiller;
import de.drippinger.fakegen.types.SimpleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dennis Rippinger
 */
class SameSeedBasicObjectFillerUsageTest {

    private TestDataFiller filler = new TestDataFiller(new BasicObjectFiller(123456789L));

    @Test
    @DisplayName("Should fill a simple Object with the same value")
    void createRandomFilledInstance_simple_object() {
        SimpleType randomFilledInstance = filler.createRandomFilledInstance(SimpleType.class);

        assertThat(randomFilledInstance).isNotNull();
        assertThat(randomFilledInstance.getName())
                .hasSize(10)
                .isEqualTo("aRMshJST8B");
    }

}
