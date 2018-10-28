package de.drippinger.fakegen.uninstanciable;

import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.ToStringMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static de.drippinger.fakegen.util.ReflectionUtils.getAllMethods;
import static de.drippinger.fakegen.util.ReflectionUtils.isAbstractClass;
import static de.drippinger.fakegen.util.StringUtils.firstLowerCase;
import static de.drippinger.fakegen.util.StringUtils.titleCase;

/**
 * @author Dennis Rippinger
 */
@SuppressWarnings("unchecked")
public class DynamicClassGenerator {

    private static Map<Class, Class> byteBuddyStore = new ConcurrentHashMap<>();

    @SneakyThrows
    public <T> T createSimpleInstanceOfInterfaceOrAbstract(Class<T> clazz) {
        byteBuddyStore.computeIfAbsent(clazz, this::defineClass);

        Class desiredClass = byteBuddyStore.get(clazz);

        return (T) desiredClass.newInstance();
    }

    private Class<?> defineClass(Class clazz) {
        Builder builder;
        if (clazz.isInterface()) {
            builder =
                    new ByteBuddy()
                            .subclass(Object.class)
                            .implement(clazz);
        } else {
            builder = new ByteBuddy()
                    .subclass(clazz);
        }

        return fields(builder, clazz)
                .name(clazz.getName() + "$SyntheticImpl")
                .make()
                .load(DynamicClassGenerator.class.getClassLoader())
                .getLoaded();

    }

    private <T> Builder<T> fields(Builder<T> builder, Class<?> type) {

        for (Method method : getAllMethods(type)) {

            builder = addGetMethodImpl(builder, method);

            builder = addBooleanMethodImpl(builder, method, type);
        }

        builder = addToStringMethodImpl(builder);

        return builder;
    }

    private <T> Builder<T> addToStringMethodImpl(Builder<T> builder) {
        builder = builder.defineMethod("toString", String.class, Visibility.PUBLIC)
                .intercept(ToStringMethod.prefixedByCanonicalClassName());
        return builder;
    }

    private <T> Builder<T> addBooleanMethodImpl(Builder<T> builder, Method method, Class<?> type) {
        if (method.getName().startsWith("is") && method.getName().length() > 2) {
            String name = firstLowerCase(method.getName().substring(2, method.getName().length()));

            boolean containsField = false;
            if (isAbstractClass(type)) {
                containsField = Arrays
                        .stream(type.getDeclaredFields())
                        .map(Field::getName)
                        .anyMatch(s -> s.equals(name));
            }

            if (!containsField) {
                builder = builder.defineField(name, method.getReturnType(), Visibility.PRIVATE)
                        .defineMethod("is" + titleCase(name), method.getReturnType(), Visibility.PUBLIC)
                        .intercept(FieldAccessor.ofField(name));
            }

        }
        return builder;
    }

    private <T> Builder<T> addGetMethodImpl(Builder<T> builder, Method method) {
        if (method.getName().startsWith("get") && method.getName().length() > 3) {
            String name = firstLowerCase(method.getName().substring(3, method.getName().length()));

            builder = builder.defineField(name, method.getReturnType(), Visibility.PRIVATE)
                    .defineMethod("get" + titleCase(name), method.getReturnType(), Visibility.PUBLIC)
                    .intercept(FieldAccessor.ofField(name));
        }
        return builder;
    }


}
