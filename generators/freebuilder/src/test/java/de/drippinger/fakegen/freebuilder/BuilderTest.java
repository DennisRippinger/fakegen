package de.drippinger.fakegen.freebuilder;

import de.drippinger.fakegen.TestDataFiller;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dennis Rippinger
 */
class BuilderTest {

    private TestDataFiller filler = new TestDataFiller();

    @Test
    void should_use_freetype_builder() {

        BuilderType fromBuilder = filler.fillBuilder(BuilderType.Builder.class)
                .type(SimpleEnum.SOME)
                .build();


        assertThat(fromBuilder.type()).isEqualTo(SimpleEnum.SOME);
        assertThat(fromBuilder.someString())
                .isNotNull()
                .hasSize(10);
    }
}
