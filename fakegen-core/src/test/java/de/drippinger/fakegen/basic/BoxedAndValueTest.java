package de.drippinger.fakegen.basic;

import de.drippinger.fakegen.TestDataFiller;
import de.drippinger.fakegen.types.BoxedType;
import de.drippinger.fakegen.types.PrimitiveType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dennis Rippinger
 */
class BoxedAndValueTest {

    private TestDataFiller filler = new TestDataFiller();

    @Test
    @DisplayName("Should fill an object with boxed types")
    void createRandomFilledInstance_fill_Boxed_type() {
        BoxedType randomFilledInstance = filler.fillInstance(BoxedType.class);

        assertThat(randomFilledInstance).isNotNull();
        assertThat(randomFilledInstance.getSomeBoolean()).isNotNull();
        assertThat(randomFilledInstance.getSomeDouble())
                .isNotNull()
                .isNotEqualTo(0D);
        assertThat(randomFilledInstance.getSomeFloat())
                .isNotNull()
                .isNotEqualTo(0);
        assertThat(randomFilledInstance.getSomeInteger())
                .isNotNull()
                .isNotEqualTo(0);
        assertThat(randomFilledInstance.getSomeLong())
                .isNotNull()
                .isNotEqualTo(0L);
        assertThat(randomFilledInstance.getSomeShort())
                .isNotNull()
                .isNotEqualTo(0);
        assertThat(randomFilledInstance.getSomeByte())
                .isNotNull()
                .isNotEqualTo(Byte.valueOf("0"));
        assertThat(randomFilledInstance.getSomeChar())
                .isNotNull()
                .isNotEqualTo("");

    }

    @Test
    @DisplayName("Should fill an object with primitive types")
    void createRandomFilledInstance_fill_primitive_type() {
        PrimitiveType randomFilledInstance = filler.fillInstance(PrimitiveType.class);

        assertThat(randomFilledInstance).isNotNull();
        assertThat(randomFilledInstance.getSomeDouble())
                .isNotNull()
                .isNotEqualTo(0D);
        assertThat(randomFilledInstance.getSomeFloat())
                .isNotNull()
                .isNotEqualTo(0);
        assertThat(randomFilledInstance.getSomeInteger())
                .isNotNull()
                .isNotEqualTo(0);
        assertThat(randomFilledInstance.getSomeLong())
                .isNotNull()
                .isNotEqualTo(0L);
        assertThat(randomFilledInstance.getSomeShort())
                .isNotNull()
                .isNotEqualTo(0);
        assertThat(randomFilledInstance.getSomeByte())
                .isNotNull()
                .isNotEqualTo(Byte.valueOf("0"));
        assertThat(randomFilledInstance.getSomeChar())
                .isNotNull()
                .isNotEqualTo("");

    }

}
