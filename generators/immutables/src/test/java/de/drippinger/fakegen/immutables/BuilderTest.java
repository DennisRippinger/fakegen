package de.drippinger.fakegen.immutables;

import de.drippinger.fakegen.TestDataFiller;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dennis Rippinger
 */
class BuilderTest {

    private TestDataFiller filler = new TestDataFiller();

    @Test
    void build_should_use_build() {

        BuilderType fromBuilder = filler.fillBuilder(ImmutableBuilderType.Builder.class)
                .type(SimpleEnum.SOME)
                .build();


        assertThat(fromBuilder.type()).isEqualTo(SimpleEnum.SOME);
        assertThat(fromBuilder.someString())
                .isNotNull()
                .hasSize(10);

    }
}
