package de.drippinger.fakegen.basic;

import de.drippinger.fakegen.TestDataFiller;
import de.drippinger.fakegen.types.AbstractType;
import de.drippinger.fakegen.types.InterfaceType;
import de.drippinger.fakegen.types.NestedInterfaceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sun.awt.RepaintArea;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dennis Rippinger
 */
public class UninstanciableTest {

    private TestDataFiller filler = new TestDataFiller();

    @Test
    @DisplayName("Should fill an interface Type by creating a simple Impl of it")
    void createRandomFilledInstance_fill_interface() {
        InterfaceType randomFilledInstance = filler.fillInstance(InterfaceType.class);

        assertThat(randomFilledInstance).isNotNull();
        assertThat(randomFilledInstance.getClass().getSimpleName()).isEqualTo("InterfaceType$SyntheticImpl");
        assertThat(randomFilledInstance.getClass().getInterfaces()[0]).isEqualTo(InterfaceType.class);

        assertThat(randomFilledInstance.getName()).hasSize(10);
        assertThat(randomFilledInstance.isValid()).isNotNull();
        assertThat(randomFilledInstance.getTime()).isEqualToIgnoringSeconds(now());
        assertThat(randomFilledInstance.isSomehowValid()).isEqualTo(randomFilledInstance.isValid());
        assertThat(InterfaceType.isValid("test")).isTrue();
    }

    @Test
    @DisplayName("Should fill an abstract Type by creating a simple Impl of it")
    void createRandomFilledInstance_fill_abstract() {
        AbstractType randomFilledInstance = filler.fillInstance(AbstractType.class);

        assertThat(randomFilledInstance).isNotNull();
        assertThat(randomFilledInstance.getClass().getSimpleName()).isEqualTo("AbstractType$SyntheticImpl");
        assertThat(randomFilledInstance.getClass().getSuperclass()).isEqualTo(AbstractType.class);

        assertThat(randomFilledInstance.getName()).hasSize(10);
        assertThat(randomFilledInstance.getTime()).isEqualToIgnoringSeconds(now());
    }

    @Test
    @DisplayName("Should fill an interface nested Type by creating a simple Impl of it.")
    void createRandomFilledInstance_fill_interface_nested() {
        NestedInterfaceType randomFilledInstance = filler.fillInstance(NestedInterfaceType.class);

        assertThat(randomFilledInstance).isNotNull();
        assertThat(randomFilledInstance.getClass().getSimpleName()).isEqualTo("NestedInterfaceType$SyntheticImpl");
        assertThat(randomFilledInstance.getClass().getInterfaces()[0]).isEqualTo(NestedInterfaceType.class);

        assertThat(randomFilledInstance.getName()).hasSize(10);
        assertThat(randomFilledInstance.getTime()).isEqualToIgnoringSeconds(now());

        assertThat(randomFilledInstance.getNestedInterface().getName()).hasSize(10);
        assertThat(randomFilledInstance.getNestedInterface().getTime()).isEqualToIgnoringSeconds(now());
    }

    @Test
    @DisplayName("Should provide a regular toString implementation")
    void createRandomFilledInstance_fill_interface_toString() {

        InterfaceType randomFilledInstance = filler.fillInstance(InterfaceType.class);

        assertThat(randomFilledInstance).isNotNull();
        assertThat(randomFilledInstance.toString()).matches("de\\.drippinger\\.fakegen\\.types\\.InterfaceType" +
                "\\$SyntheticImpl\\{.*}");

    }

    @Test
    @DisplayName("Should return null on jvm internal")
    void should_return_null_on_internal() {
        RepaintArea randomFilledInstance = filler.fillInstance(RepaintArea.class);

        assertThat(randomFilledInstance).isNull();
    }

}
