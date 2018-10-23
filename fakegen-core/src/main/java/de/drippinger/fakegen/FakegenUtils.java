package de.drippinger.fakegen;

import de.drippinger.fakegen.exception.FakegenException;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.util.function.Consumer;

import static de.drippinger.fakegen.exception.ExceptionHelper.createExceptionMessage;

/**
 * @author Dennis Rippinger
 */
@UtilityClass
public class FakegenUtils {

    @SneakyThrows
    public static void inject(Object target, String fieldName, Object value) {
        Field field;
        try {
            field = target.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new FakegenException(createExceptionMessage(target.getClass(), fieldName), e);
        }

        field.setAccessible(true);
        field.set(target, value);
    }

    @SafeVarargs
    public static <T> void modifyCollection(Iterable<T> targets, Consumer<T>... suppliers) {
        int counter = 0;

        for (T target : targets) {
            suppliers[counter % suppliers.length].accept(target);
            counter++;
        }
    }

}
