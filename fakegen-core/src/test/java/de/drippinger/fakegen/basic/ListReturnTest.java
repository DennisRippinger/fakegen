package de.drippinger.fakegen.basic;

import de.drippinger.fakegen.TestDataFiller;
import de.drippinger.fakegen.exception.FakegenException;
import de.drippinger.fakegen.types.SimpleType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * @author Dennis Rippinger
 */
class ListReturnTest {

    @Test
    void should_return_multiple_random_instances() {
        TestDataFiller tdf = new TestDataFiller();

        List<SimpleType> result = tdf.fillInstances(SimpleType.class, 3);

        assertThat(result)
                .hasSize(3)
                .allSatisfy(simpleType -> {
                    assertThat(simpleType.getName()).hasSize(10);
                    assertThat(simpleType.getSimpleEnum()).isNotNull();
                    assertThat(simpleType.getBirthday()).isToday();
                });
    }

    @Test
    void should_throw_exeeption_for_negative_amount() {
        TestDataFiller tdf = new TestDataFiller();

        Throwable throwable = catchThrowable(() -> tdf.fillInstances(SimpleType.class, -1));

        assertThat(throwable)
                .isInstanceOf(FakegenException.class)
                .hasMessage("Can not handle negative amount");
    }

}
