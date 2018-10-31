package de.drippinger.fakegen.domain;

import com.github.javafaker.Faker;
import de.drippinger.fakegen.TestDataFiller;
import de.drippinger.fakegen.types.SimpleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dennis Rippinger
 */
@SuppressWarnings("unchecked")
class DomainFixedSeedTest {

    @RepeatedTest(3)
    @DisplayName("Should use Domain Specific Configuration with static seed")
    void should_Use_Domain_Specific_Values() {
        TestDataFiller tdf = new TestDataFiller(TestConfiguration.class, 123L);

        SimpleType randomFilledInstance = tdf.fillInstance(SimpleType.class);

        assertThat(randomFilledInstance.getName()).isEqualTo("Jayne");
    }

    public static class TestConfiguration extends DomainConfiguration {

        public TestConfiguration(Random random) {
            super(random);
        }

        @Override
        public void init(Random random, TestDataFiller testDataFiller) {
            Faker faker = new Faker(random);

            this.fieldForStringShouldUse("name", () -> faker.name().firstName());
        }
    }

}
