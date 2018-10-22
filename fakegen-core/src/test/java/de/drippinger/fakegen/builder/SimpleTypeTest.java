package de.drippinger.fakegen.builder;

import de.drippinger.fakegen.TestDataFiller;
import de.drippinger.fakegen.types.SimpleBuilderType;
import de.drippinger.fakegen.types.SimpleEnum;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dennis Rippinger
 */
class SimpleTypeTest {

    private TestDataFiller filler = new TestDataFiller();

    @Test
    void should_use_lombok_builder() {

        SimpleBuilderType fromBuilder = filler.createFromBuilder(SimpleBuilderType.builder())
                .simpleEnum(SimpleEnum.VALUE_2)
                .build();

        assertThat(fromBuilder.getSimpleEnum()).isEqualTo(SimpleEnum.VALUE_2);
        assertThat(fromBuilder.getBirthday()).isToday();
        assertThat(fromBuilder.getName())
                .isNotNull()
                .hasSize(10);
    }
}
