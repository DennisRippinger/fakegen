package de.drippinger.fakegen;

import de.drippinger.fakegen.domain.DomainConfiguration;
import de.drippinger.fakegen.exception.FakegenException;
import de.drippinger.fakegen.filler.BasicObjectFiller;
import de.drippinger.fakegen.filler.ObjectFiller;
import de.drippinger.fakegen.uninstanciable.DynamicClassGenerator;
import de.drippinger.fakegen.util.ReflectionUtils;
import lombok.SneakyThrows;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Consumer;

import static de.drippinger.fakegen.exception.ExceptionHelper.createExceptionMessage;
import static de.drippinger.fakegen.util.ReflectionUtils.*;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;

/**
 * @author Dennis Rippinger
 */
@SuppressWarnings("unchecked")
public class TestDataFiller {

    private final ObjectFiller objectFiller;

    private final DynamicClassGenerator dynamicClassGenerator;

    private final Map<Type, Method> objectFillerFactoryMethods;

    public TestDataFiller() {
        this(new BasicObjectFiller());
    }

    public TestDataFiller(Long seed) {
        this(new BasicObjectFiller(seed));
    }

    public TestDataFiller(Long seed, DomainConfiguration domainConfiguration) {
        this(new BasicObjectFiller(seed, domainConfiguration));
    }

    public TestDataFiller(DomainConfiguration domainConfiguration) {
        this(new BasicObjectFiller(domainConfiguration));
    }

    public TestDataFiller(ObjectFiller objectFiller) {
        this.objectFiller = objectFiller;
        this.dynamicClassGenerator = new DynamicClassGenerator();
        this.objectFillerFactoryMethods = getPotentialRandomFactoryMethods(objectFiller);
    }

    public <T> T createRandomFilledInstance(Class<T> clazz, Consumer<T> consumer) {
        T randomFilledInstance = createRandomFilledInstanceInternal(clazz, 0);
        consumer.accept(randomFilledInstance);

        return randomFilledInstance;
    }

    public <T> T createRandomFilledInstance(Class<T> clazz) {
        return createRandomFilledInstanceInternal(clazz, 0);
    }

    public <T> List<T> createRandomFilledInstance(Class<T> clazz, int amount) {
        if (amount < 0) {
            throw new FakegenException("Can not handle negative amount");
        }

        List<T> result = new ArrayList(amount + 1);
        for (int i = 0; i < amount; i++) {
            result.add(createRandomFilledInstance(clazz));
        }

        return result;
    }


    public <T> T createFromBuilder(Class<T> clazz) {
        return createRandomFilledInstanceInternal(clazz, 0, true, singleton("initBits"), true);
    }

    public <T> T createFromBuilder(T instance) {
        return (T) fill(instance, instance.getClass(), 0, emptySet(), false);
    }

    public <T> T createRandomFilledByFactory(Class<T> clazz) {
        try {

            Optional<Method> possibleFactoryMethod = Arrays.stream(clazz.getMethods())
                    .filter(ReflectionUtils::isStaticMethod)
                    .filter(method -> method.getReturnType().equals(clazz))
                    .filter(method -> Arrays.stream(method.getParameterTypes()).noneMatch(clazz::equals))
                    .findFirst();

            if (possibleFactoryMethod.isPresent()) {
                Method method = possibleFactoryMethod.get();

                Object[] instances = Arrays.stream(method.getParameterTypes())
                        .map(this::createRandomFilledInstance)
                        .toArray();

                return (T) method.invoke(null, instances);
            }

        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new FakegenException("Could not invoke factory method", e);
        }

        return null;

    }

    public <T> T createRandomFilledByFactory(Class<T> clazz, MethodHolder methodHolder) {
        try {
            Method method = methodHolder.createMethod(clazz);

            Object[] instances = Arrays.stream(methodHolder.getParameterTypes())
                    .map(this::createRandomFilledInstance)
                    .toArray();

            return (T) method.invoke(null, instances);

        } catch (NoSuchMethodException e) {
            String message = createExceptionMessage(clazz, methodHolder);
            throw new FakegenException(message, e);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new FakegenException("Could not invoke factory method", e);
        }

    }


    public String getSeed() {
        return objectFiller.getSeed();
    }

    private <T> T createRandomFilledInstanceInternal(Class<T> clazz, int recursionCounter) {
        return createRandomFilledInstanceInternal(clazz, recursionCounter, false, emptySet(), false);
    }

    private <T> T createRandomFilledInstanceInternal(Class<T> clazz, int recursionCounter, boolean usePrivateConstructor, Set<String> ignoreFields, boolean useSetter) {

        if (objectFillerFactoryMethods.containsKey(clazz)) {
            Method method = objectFillerFactoryMethods.get(clazz);
            return (T) callFactoryMethod(null, method);
        } else if (clazz.isEnum()) {
            return (T) objectFiller.createEnum(null, clazz);
        } else if (clazz.isInterface() || isAbstractClass(clazz)) {
            T simpleImpl = dynamicClassGenerator.createSimpleInstanceOfInterfaceOrAbstract(clazz);
            fill(simpleImpl, simpleImpl.getClass(), recursionCounter, ignoreFields, useSetter);
            return simpleImpl;
        } else {
            return (T) extractPlainConstructor(clazz, usePrivateConstructor)
                    .map(this::newInstance)
                    .map(instance -> fill(instance, clazz, recursionCounter, ignoreFields, useSetter))
                    .orElse(null);
        }
    }

    private <T> Object fill(Object instance, Class<T> clazz, int recursionCounter, Set<String> ignoreFields, boolean useSetter) {

        getAllFields(clazz)
                .filter(field -> !ignoreFields.contains(field.getName()))
                .filter(ReflectionUtils::isNotFinal)
                .filter(ReflectionUtils::isNotRelevantForCoverage)
                .forEach(field -> fillField(instance, field, recursionCounter, clazz, useSetter));

        return instance;
    }

    @SneakyThrows
    private void fillField(Object instance, Field field, int recursionCounter, Class clazz, boolean useSetter) {
        Object value = createRandomValueForField(field);

        if (value == null && recursionCounter < 1) {
            recursionCounter++;
            value = createRandomFilledInstanceInternal(field.getType(), recursionCounter);
        }

        if (value != null) {
            if (isNotFinal(field) && !useSetter) {
                field.setAccessible(true);
                field.set(instance, value);
            } else {
                Optional<Method> possibleSetterForField = findPossibleSetterForField(clazz, field);
                if (possibleSetterForField.isPresent()) {
                    Method setter = possibleSetterForField.get();
                    setter.setAccessible(true);
                    setter.invoke(instance, value);
                }
            }
        }
    }

    private Object createRandomValueForField(Field field) {

        Class<?> type = field.getType();
        String fieldName = field.getName();

        if (type.isEnum()) {
            return objectFiller.createEnum(fieldName, type);
        }

        Method method = objectFillerFactoryMethods.get(type);
        if (method != null) {
            return callFactoryMethod(fieldName, method);
        }

        return null;
    }

    @SneakyThrows
    private Object callFactoryMethod(String fieldName, Method method) {
        return method.invoke(objectFiller, fieldName);
    }

    @SneakyThrows
    private Object newInstance(Constructor constructor) {
        return constructor.newInstance();
    }

    private Optional<Constructor> extractPlainConstructor(Class clazz, boolean usePrivateConstructor) {
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.getParameterCount() == 0 && (isPublic(constructor) || usePrivateConstructor)) {
                constructor.setAccessible(true);
                return Optional.of(constructor);
            }
        }
        return Optional.empty();
    }

}
