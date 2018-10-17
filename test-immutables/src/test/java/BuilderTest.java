import de.drippinger.fakegen.BuilderType;
import de.drippinger.fakegen.ImmutableBuilderType;
import de.drippinger.fakegen.SimpleEnum;
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

        BuilderType fromBuilder = filler.createFromBuilder(ImmutableBuilderType.Builder.class)
                .type(SimpleEnum.SOME)
                .build();


        assertThat(fromBuilder.type()).isEqualTo(SimpleEnum.SOME);
        assertThat(fromBuilder.someString())
                .isNotNull()
                .hasSize(10);

    }
}
