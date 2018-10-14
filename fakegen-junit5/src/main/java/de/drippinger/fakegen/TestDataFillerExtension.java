package de.drippinger.fakegen;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Function;

/**
 * @author Dennis Rippinger
 */
public class TestDataFillerExtension implements TestExecutionExceptionHandler {

    @Override
    public void handleTestExecutionException(ExtensionContext extensionContext, Throwable throwable) throws Throwable {
        extensionContext
                .getTestClass()
                .map(fieldWithTestDataFiller())
                .ifPresent(field -> extractInstanceAndLogSeed(field, extensionContext));
    }

    private void extractInstanceAndLogSeed(Field field, ExtensionContext extensionContext) {
        extensionContext.getTestInstance()
                .map(testInstance -> getInstance(field, testInstance))
                .ifPresent(testDataFiller -> logSeed(testDataFiller, extensionContext.getDisplayName()));
    }

    private void logSeed(TestDataFiller testDataFiller, String displayName) {
        String message = String
                .format("Seed used in Test '%s' was %s",
                        displayName,
                        testDataFiller.getSeed()
                );

        System.out.println(message);
    }

    private TestDataFiller getInstance(Field field, Object o) {
        try {
            field.setAccessible(true);
            return (TestDataFiller) field.get(o);
        } catch (IllegalAccessException e) {
            return null;
        }

    }

    private Function<Class<?>, Field> fieldWithTestDataFiller() {
        return aClass -> Arrays
                .stream(aClass.getDeclaredFields())
                .filter(field -> field.getType().equals(TestDataFiller.class))
                .findFirst()
                .orElse(null);
    }

}
