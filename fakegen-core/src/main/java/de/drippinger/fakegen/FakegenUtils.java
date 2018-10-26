package de.drippinger.fakegen;

import de.drippinger.fakegen.exception.FakegenException;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.util.function.Consumer;

import static de.drippinger.fakegen.exception.ExceptionHelper.createExceptionMessage;

/**
 * This class provides util methods to work with the generated test data.
 *
 * @author Dennis Rippinger
 */
@UtilityClass
@SuppressWarnings("WeakerAccess")
public class FakegenUtils {

    /**
     * Helps to inject an object into a field. This is relevant with interfaces
     * and abstract classes that do not provide a setter. The
     * {@link de.drippinger.fakegen.uninstanciable.DynamicClassGenerator} provides more inside.
     *
     * @param target    the instance of a class, not null.
     * @param fieldName the field name as String, not null.
     * @param value     the value that should be set.
     * @throws FakegenException Can throw a Runtime Exception if the field is not present.
     *                          Fakegen can identify simple typos and will offer in the
     *                          exception message the possible correct name.
     */
    public static void inject(@NonNull Object target, @NonNull String fieldName, Object value) {
        Field field;
        try {
            field = target.getClass().getDeclaredField(fieldName);

            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException e) {
            throw new FakegenException(createExceptionMessage(target.getClass(), fieldName), e);
        } catch (IllegalAccessException e) {
            throw new FakegenException("Could not set field " + fieldName, e);
        }

    }

    /**
     * Modifies an Iterable with a variable number of {@link Consumer}s. With Fakegen you can generate
     * an arbitrary number of Fake objects, but they might require each slightly different speciality.
     * The usage might be:
     * <pre>{@code
     * List<SomeDto> dtos = testDataFiller
     *                      .createRandomFilledInstance(SomeDto.class, 3);
     * FakegenUtils.modifyCollection(
     *              dtos,
     *              someDto -> someDto.setName("Set By Consumer 0"),
     *              someDto -> someDto.setName("Set By Consumer 1"),
     *              someDto -> someDto.setName("Set By Consumer 2"));
     *
     * System.out.println(dtos.get(2).getName())
     * // Set By Consumer 2
     * }</pre>
     * The Consumers are applied in modulo of the consumers size.
     *
     * @param targets   the collection of target elements, not null.
     * @param suppliers the varargs of suppliers, not null.
     * @param <T>       the generic type of the collection.
     */
    @SafeVarargs
    public static <T> void modifyCollection(@NonNull Iterable<T> targets, @NonNull Consumer<T>... suppliers) {
        int counter = 0;

        for (T target : targets) {
            suppliers[counter % suppliers.length].accept(target);
            counter++;
        }
    }

}
