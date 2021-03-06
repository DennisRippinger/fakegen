package de.drippinger.fakegen.domain;

import de.drippinger.fakegen.TestDataFiller;
import de.drippinger.fakegen.types.NestedType;
import de.drippinger.fakegen.types.SimpleType;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dennis Rippinger
 */
@SuppressWarnings("unchecked")
class DomainSpecificFactoryTest {

    @Test
    void should_Use_Domain_Specific_factory() {
        TestDataFiller tdf = new TestDataFiller(TestConfiguration.class);

        NestedType randomFilledInstance = tdf.fillInstance(NestedType.class);

        assertThat(randomFilledInstance.getSimpleType().getName()).isEqualTo("Hello from factory");
    }

    public static class TestConfiguration extends DomainConfiguration {

        public TestConfiguration(Random random, TestDataFiller testDataFiller) {
            super(random, testDataFiller);
        }

        @Override
        public void init(Random random, TestDataFiller testDataFiller) {
            // NOP
        }

        public SimpleType createSimpleType(String fieldName, Field field) {
            SimpleType simpleType = new SimpleType();
            simpleType.setName("Hello from factory");

            return simpleType;
        }
    }

}
