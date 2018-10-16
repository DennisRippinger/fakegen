package de.drippinger.fakegen.factory;

import de.drippinger.fakegen.TestDataFiller;
import de.drippinger.fakegen.exception.FakegenException;
import de.drippinger.fakegen.types.BeanByFactoryType;
import de.drippinger.fakegen.types.SimpleType;
import org.junit.jupiter.api.Test;

import static de.drippinger.fakegen.MethodHolder.method;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * @author Dennis Rippinger
 */
class FactoryTest {

    @Test
    void should_fill_object_by_factory() {
        TestDataFiller tdf = new TestDataFiller();

        BeanByFactoryType randomFilledByFactory = tdf
                .createRandomFilledByFactory(BeanByFactoryType.class, method("createBeanWith", String.class));

        assertThat(randomFilledByFactory.getSomeField())
                .isNotEmpty()
                .hasSize(10);
    }

    @Test
    void should_throw_an_exception_because_wrong_name() {
        TestDataFiller tdf = new TestDataFiller();


        Throwable fakegenException = catchThrowable(() -> tdf
                .createRandomFilledByFactory(BeanByFactoryType.class, method("createBeanWitf", String.class)));

        assertThat(fakegenException)
                .isInstanceOf(FakegenException.class)
                .hasMessage("Could not find method `createBeanWitf`," +
                        " but a similar named one called `createBeanWith` was found.");
    }

    @Test
    void should_throw_an_exception_because_wrong_parameter() {
        TestDataFiller tdf = new TestDataFiller();

        Throwable fakegenException = catchThrowable(() -> tdf
                .createRandomFilledByFactory(BeanByFactoryType.class, method("createBeanWith", Double.class)));

        assertThat(fakegenException)
                .isInstanceOf(FakegenException.class)
                .hasMessage("Could not find method `createBeanWith` with given parameter `Double.class`, " +
                        "but valid could be the Parameter set: `String.class`");
    }

    @Test
    void should_throw_an_exception_because_all_wrong() {
        TestDataFiller tdf = new TestDataFiller();

        Throwable fakegenException = catchThrowable(() -> tdf
                .createRandomFilledByFactory(BeanByFactoryType.class, method("somethingElse", SimpleType.class)));

        assertThat(fakegenException)
                .isInstanceOf(FakegenException.class)
                .hasMessage("Could not find method `somethingElse`, maybe a refactoring missed the string.");
    }

    @Test
    void should_throw_an_exception_null() {
        TestDataFiller tdf = new TestDataFiller();

        Throwable fakegenException = catchThrowable(() -> tdf
                .createRandomFilledByFactory(BeanByFactoryType.class, method("createBeanWith")));

        assertThat(fakegenException)
                .isInstanceOf(FakegenException.class)
                .hasMessage("Could not find method `createBeanWith` with given parameter ``, " +
                        "but valid could be the Parameter set: `String.class`");
    }

}
