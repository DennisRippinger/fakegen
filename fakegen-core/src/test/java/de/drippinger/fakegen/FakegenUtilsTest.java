package de.drippinger.fakegen;

import de.drippinger.fakegen.exception.FakegenException;
import de.drippinger.fakegen.types.SimpleType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * @author Dennis Rippinger 
 */
class FakegenUtilsTest {

    @Test
    void inject() {
        SimpleType simpleType = new SimpleType();

        FakegenUtils.inject(simpleType, "name", "Hello World");

        assertThat(simpleType.getName()).isEqualTo("Hello World");
    }

    @Test
    void inject_failed() {
        SimpleType simpleType = new SimpleType();

        Throwable fakegenException = catchThrowable(() -> FakegenUtils.inject(simpleType, "namo", "Hello World"));

        assertThat(fakegenException)
                .isInstanceOf(FakegenException.class)
                .hasMessage("Could not find field `namo`, but a similar named one called `name` was found.");
    }

    @Test
    void modifyCollection_one() {
        TestDataFiller tdf = new TestDataFiller();

        List<SimpleType> simpleTypes = tdf.fillInstances(SimpleType.class, 1);

        FakegenUtils.modifyCollection(simpleTypes, simpleType -> simpleType.setName("Set By Consumer"));

        assertThat(simpleTypes)
                .allSatisfy(simpleType -> assertThat(simpleType.getName()).isEqualTo("Set By Consumer"));
    }

    @Test
    void modifyCollection_many() {
        TestDataFiller tdf = new TestDataFiller();

        List<SimpleType> simpleTypes = tdf.fillInstances(SimpleType.class, 3);

        FakegenUtils.modifyCollection(simpleTypes, simpleType -> simpleType.setName("Set By Consumer"));

        assertThat(simpleTypes)
                .allSatisfy(simpleType -> assertThat(simpleType.getName()).isEqualTo("Set By Consumer"));
    }

    @Test
    void modifyCollection_different() {
        TestDataFiller tdf = new TestDataFiller();

        List<SimpleType> simpleTypes = tdf.fillInstances(SimpleType.class, 3);

        FakegenUtils.modifyCollection(simpleTypes,
                simpleType -> simpleType.setName("Set By Consumer 1"),
                simpleType -> simpleType.setName("Set By Consumer 2"),
                simpleType -> simpleType.setName("Set By Consumer 3"));


        assertThat(simpleTypes.get(0).getName()).isEqualTo("Set By Consumer 1");
        assertThat(simpleTypes.get(1).getName()).isEqualTo("Set By Consumer 2");
        assertThat(simpleTypes.get(2).getName()).isEqualTo("Set By Consumer 3");
    }

    @Test
    void modifyCollection_circle() {
        TestDataFiller tdf = new TestDataFiller();

        List<SimpleType> simpleTypes = tdf.fillInstances(SimpleType.class, 3);

        FakegenUtils.modifyCollection(simpleTypes,
                simpleType -> simpleType.setName("Set By Consumer 1"),
                simpleType -> simpleType.setName("Set By Consumer 2"));

        assertThat(simpleTypes.get(0).getName()).isEqualTo("Set By Consumer 1");
        assertThat(simpleTypes.get(1).getName()).isEqualTo("Set By Consumer 2");
        assertThat(simpleTypes.get(2).getName()).isEqualTo("Set By Consumer 1");
    }

}
