package de.drippinger.fakegen.lombok;

import de.drippinger.fakegen.TestDataFiller;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dennis Rippinger 
 */
class SimpleTypeTest {

    private TestDataFiller filler = new TestDataFiller();

    @Test
    void should_use_lombok_builder() {

        SimpleType fromBuilder = filler.createFromBuilder(SimpleType.builder())
                .simpleEnum(SimpleEnum.VALUE_2)
                .build();
        
        assertThat(fromBuilder.getSimpleEnum()).isEqualTo(SimpleEnum.VALUE_2);
        assertThat(fromBuilder.getBirthday()).isToday();
        assertThat(fromBuilder.getName())
                .isNotNull()
                .hasSize(10);

    }
}
