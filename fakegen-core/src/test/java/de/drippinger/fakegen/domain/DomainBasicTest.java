package de.drippinger.fakegen.domain;

import de.drippinger.fakegen.TestDataFiller;
import de.drippinger.fakegen.types.JavaBaseTypes;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dennis Rippinger
 */
@SuppressWarnings("unchecked")
class DomainBasicTest {

    @Test
    void should_Use_Domain_Specific_Values() {
        TestDataFiller tdf = new TestDataFiller(TestConfiguration.class);

        JavaBaseTypes randomFilledInstance = tdf.fillInstance(JavaBaseTypes.class);

        assertThat(randomFilledInstance.getSomeBigInteger()).isEqualTo(123);
        assertThat(randomFilledInstance.getSomeDate()).isEqualToIgnoringSeconds(DateUtils.addDays(new Date(), -1));
        assertThat(randomFilledInstance.getSomeLocalDate()).isBeforeOrEqualTo(LocalDate.now().minusDays(1));
        assertThat(randomFilledInstance.getSomeLocalDateTime()).isBeforeOrEqualTo(LocalDateTime.now().minusDays(1));
        assertThat(randomFilledInstance.getSomeLocalTime()).isBeforeOrEqualTo(LocalTime.now().minusHours(1));
        assertThat(randomFilledInstance.getSomeList()).containsOnly("Test");
        assertThat(randomFilledInstance.getSomeSet()).containsOnly("Test");
        assertThat(randomFilledInstance.getSomeMap()).containsEntry("Test", "Value");
    }

    public static class TestConfiguration extends DomainConfiguration {

        public TestConfiguration(Random random, TestDataFiller testDataFiller) {
            super(random, testDataFiller);
        }

        @Override
        public void init(Random random, TestDataFiller testDataFiller) {
            fieldForClassShouldUse("someDate", Date.class, () -> DateUtils.addDays(new Date(), -1));
            fieldForClassShouldUse("someLocalDate", LocalDate.class, () -> LocalDate.now().minusDays(1));
            fieldForClassShouldUse("someLocalDateTime", LocalDateTime.class, () -> LocalDateTime.now().minusDays(1));
            fieldForClassShouldUse("someLocalTime", LocalTime.class, () -> LocalTime.now().minusHours(1));
            fieldForClassShouldUse("someList", List.class, () -> Collections.singletonList("Test"));
            fieldForClassShouldUse("someSet", Set.class, () -> Collections.singleton("Test"));
            fieldForClassShouldUse("someMap", Map.class, () -> Collections.singletonMap("Test", "Value"));
            fieldForClassShouldUse("someBigInteger", BigInteger.class, () -> new BigInteger("123"));
        }
    }

}
