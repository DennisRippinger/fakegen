package de.drippinger.fakegen.jvm;

import de.drippinger.fakegen.TestDataFiller;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Modifier;
import java.net.MulticastSocket;
import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dennis Rippinger
 */
class JvmTypesTest {


    @ParameterizedTest
    @MethodSource("jvmClasses")
    void forClass(Class clazz) {
        TestDataFiller tdf = new TestDataFiller();

        Throwable throwable = Assertions.catchThrowable(() -> tdf.fillInstance(clazz));

        assertThat(throwable).isNull();
    }

    private static Stream<Class<?>> jvmClasses() {
        Reflections reflections = new Reflections("java", new SubTypesScanner(false));

        return reflections.getSubTypesOf(Object.class)
                .stream()
                .filter(aClass -> Modifier.isPublic(aClass.getModifiers()))
                .filter(aClass -> !Objects.equals(aClass, MulticastSocket.class));
    }

}
