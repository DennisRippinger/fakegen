package de.drippinger.fakegen.domain;

import de.drippinger.fakegen.TestDataFiller;
import de.drippinger.fakegen.types.JavaBaseTypes;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dennis Rippinger
 */
@SuppressWarnings("unchecked")
class DomainBasicTest {

    private DomainConfiguration configuration = new TestConfiguration();

    @Test
    void should_Use_Domain_Specific_Values() {
        TestDataFiller tdf = new TestDataFiller(configuration);

        JavaBaseTypes randomFilledInstance = tdf.createRandomFilledInstance(JavaBaseTypes.class);

        assertThat(randomFilledInstance.getSomeBigInteger()).isEqualTo(123);
        assertThat(randomFilledInstance.getSomeDate()).isEqualToIgnoringSeconds(DateUtils.addDays(new Date(), -1));
        assertThat(randomFilledInstance.getSomeLocalDate()).isBeforeOrEqualTo(LocalDate.now().minusDays(1));
        assertThat(randomFilledInstance.getSomeLocalDateTime()).isBeforeOrEqualTo(LocalDateTime.now().minusDays(1));
        assertThat(randomFilledInstance.getSomeLocalTime()).isBeforeOrEqualTo(LocalTime.now().minusHours(1));
        assertThat(randomFilledInstance.getSomeList()).containsOnly("Test");
        assertThat(randomFilledInstance.getSomeSet()).containsOnly("Test");
        assertThat(randomFilledInstance.getSomeMap()).containsEntry("Test", "Value");
    }

    private class TestConfiguration extends DomainConfiguration {

        @Override
        public void init(Random random) {
            configuration.fieldForClassShouldUse("someDate", Date.class, () -> DateUtils.addDays(new Date(), -1));
            configuration.fieldForClassShouldUse("someLocalDate", LocalDate.class, () -> LocalDate.now().minusDays(1));
            configuration.fieldForClassShouldUse("someLocalDateTime", LocalDateTime.class, () -> LocalDateTime.now().minusDays(1));
            configuration.fieldForClassShouldUse("someLocalTime", LocalTime.class, () -> LocalTime.now().minusHours(1));
            configuration.fieldForClassShouldUse("someList", List.class, () -> Collections.singletonList("Test"));
            configuration.fieldForClassShouldUse("someSet", Set.class, () -> Collections.singleton("Test"));
            configuration.fieldForClassShouldUse("someMap", Map.class, () -> Collections.singletonMap("Test", "Value"));
            configuration.fieldForClassShouldUse("someBigInteger", BigInteger.class, () -> new BigInteger("123"));
        }
    }

}
