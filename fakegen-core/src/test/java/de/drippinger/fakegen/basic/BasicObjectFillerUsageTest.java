package de.drippinger.fakegen.basic;

import de.drippinger.fakegen.TestDataFiller;
import de.drippinger.fakegen.types.EmptyEnum;
import de.drippinger.fakegen.types.JavaBaseTypes;
import de.drippinger.fakegen.types.SimpleEnum;
import de.drippinger.fakegen.types.SimpleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumSet;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dennis Rippinger
 */
@SuppressWarnings("unchecked")
class BasicObjectFillerUsageTest {

    private TestDataFiller filler = new TestDataFiller();

    @Test
    @DisplayName("Should fill a simple Object")
    void createRandomFilledInstance_simple_object() {
        SimpleType randomFilledInstance = filler.fillInstance(SimpleType.class);

        assertThat(randomFilledInstance).isNotNull();
        assertThat(randomFilledInstance.getBirthday()).isToday();
        assertThat(randomFilledInstance.getName()).hasSize(10);
        assertThat(randomFilledInstance.getSimpleEnum()).isNotNull();
    }

    @Test
    @DisplayName("Should fill a regular String")
    void createRandomFilledInstance_regular_string() {
        String randomFilledInstance = filler.fillInstance(String.class);

        assertThat(randomFilledInstance).isNotNull();
        assertThat(randomFilledInstance).hasSize(10);
    }

    @Test
    @DisplayName("Should use a consumer")
    void createRandomFilledInstance_apply_consumer() {
        SimpleType randomFilledInstance = filler.fillInstance(
                SimpleType.class,
                simpleType -> simpleType.setName("Dennis")
        );

        assertThat(randomFilledInstance).isNotNull();
        assertThat(randomFilledInstance.getName()).isEqualTo("Dennis");
    }


    @Test
    @DisplayName("Should fill a class with regular java types")
    void createRandomFilledInstance_should_fill_reglar_java_types() {
        JavaBaseTypes randomFilledInstance = filler.fillInstance(JavaBaseTypes.class);

        assertThat(randomFilledInstance).isNotNull();
        assertThat(randomFilledInstance.getSomeBigInteger())
                .isNotNull()
                .isNotEqualTo(0L);
        assertThat(randomFilledInstance.getSomeDate())
                .isNotNull()
                .isToday();
        assertThat(randomFilledInstance.getSomeLocalDate())
                .isNotNull()
                .isToday();
        assertThat(randomFilledInstance.getSomeLocalDateTime())
                .isNotNull()
                .isEqualToIgnoringSeconds(LocalDateTime.now());
        assertThat(randomFilledInstance.getSomeLocalTime())
                .isNotNull()
                .isEqualToIgnoringSeconds(LocalTime.now());
        assertThat(randomFilledInstance.getSomeList())
                .isNotNull()
                .isEmpty();
        assertThat(randomFilledInstance.getSomeSet())
                .isNotNull()
                .isEmpty();
        assertThat(randomFilledInstance.getSomeMap())
                .isNotNull()
                .isEmpty();
    }

    @Nested
    class Enums {

        @Test
        @DisplayName("Should fill an Enum Type")
        void createRandomFilledInstance_enum_type() {
            SimpleEnum randomFilledInstance = filler.fillInstance(SimpleEnum.class);

            assertThat(randomFilledInstance).isNotNull();
            assertThat(randomFilledInstance).isInstanceOf(SimpleEnum.class);
            assertThat(randomFilledInstance).isIn(EnumSet.allOf(SimpleEnum.class));
        }

        @Test
        @DisplayName("Should return null on empty enum")
        void createRandomFilledInstance_empty_enum_type() {
            EmptyEnum randomFilledInstance = filler.fillInstance(EmptyEnum.class);

            assertThat(randomFilledInstance).isNull();
        }
    }


}
