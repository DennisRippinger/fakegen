package de.drippinger.fakegen.factory;

import de.drippinger.fakegen.TestDataFiller;
import de.drippinger.fakegen.types.BeanByFactoryType;
import de.drippinger.fakegen.types.BeanByFactoryWithCopyType;
import de.drippinger.fakegen.types.BeanByFactoryZeroArgsType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dennis Rippinger
 */
class FirstFactoryTest {

    @Test
    void should_fill_object_by_factory() {
        TestDataFiller tdf = new TestDataFiller();

        BeanByFactoryType randomFilledByFactory = tdf
                .fillByFactory(BeanByFactoryType.class);

        assertThat(randomFilledByFactory.getSomeField())
                .isNotEmpty()
                .hasSize(10);
    }

    @Test
    void should_fill_object_by_factory_zero_args() {
        TestDataFiller tdf = new TestDataFiller();

        BeanByFactoryZeroArgsType randomFilledByFactory = tdf
                .fillByFactory(BeanByFactoryZeroArgsType.class);

        assertThat(randomFilledByFactory.getSomeField())
                .isEqualTo("Hello there General Kenobi");
    }

    @Test
    void should_fill_object_by_factory_omit_copy_factory() {
        TestDataFiller tdf = new TestDataFiller();

        BeanByFactoryWithCopyType randomFilledByFactory = tdf
                .fillByFactory(BeanByFactoryWithCopyType.class);

        assertThat(randomFilledByFactory.getSomeField())
                .isNotEmpty()
                .hasSize(10);
    }

}
