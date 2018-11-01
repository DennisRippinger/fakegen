package de.drippinger.fakegen;

import de.drippinger.fakegen.domain.DomainConfiguration;
import de.drippinger.fakegen.domain.SimpleDomainConfiguration;
import de.drippinger.fakegen.exception.FakegenException;
import de.drippinger.fakegen.uninstanciable.DynamicClassGenerator;
import de.drippinger.fakegen.util.ReflectionUtils;
import lombok.SneakyThrows;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Consumer;

import static de.drippinger.fakegen.exception.ExceptionHelper.createExceptionMessage;
import static de.drippinger.fakegen.util.Ignore.isJvmRelevant;
import static de.drippinger.fakegen.util.ReflectionUtils.*;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;

/**
 * This class offers methods to create and fill an instance of a class. The intended use is for
 * test cases where complex business logic requires non-null values for test execution, but rather
 * as invariant than as relevant for the test execution. This forces developers to fill entities,
 * DTOs, Pojos and alike with some data by hand. Over the time it is difficult to distinguish
 * the variable parts from the logic triggers.
 * <p>
 * Use the methods of this class to create an instance of a pojo filled with (semi) random data.
 * Once created it is only necessary to alter the logic triggers to the tests needs. Fakegen offers
 * different methods to create instances of regular pojos and with factories and builder style
 * pattern. An example:
 * <pre>{@code
 * TestDataFiller tdf = new TestDataFiller();
 * SomeEntity entity = tdf
 *     .createRandomFilledInstance(SomeEntity.class, instance -> instance.setGreeting("Hello World"));
 *
 * System.out.println(entity);
 * // SomeEntity(greetings=Hello world, birthday=2018-10-14)
 * }</pre>
 *
 * Since the pojos usually resemble some form of domain knowledge it is also possible to define
 * a configuration that helps Fakegen to fill the object as desired. For more on this see
 * {@link DomainConfiguration}.
 * <p>
 * Also it is possible to provide a static seed which is used by the underlying random class,
 * which provides all values. In a case were the created data identify a constellation which
 * failed the test, it is possible to replay them with the seed. See the JUnit 4 and 5 Rule
 * and Extension for more details.
 *
 * @author Dennis Rippinger
 */
@SuppressWarnings("unchecked")
public class TestDataFiller {

    private final DynamicClassGenerator dynamicClassGenerator = new DynamicClassGenerator();

    private final DomainConfiguration domainConfiguration;

    private final Map<Type, Method> objectFillerFactoryMethods;

    private final Random random;

    private final Long seed;

    /**
     * Creates a new instance with a {@link SimpleDomainConfiguration} and a new Random seed.
     */
    public TestDataFiller() {
        this.random = new Random();
        this.domainConfiguration = new SimpleDomainConfiguration(this.random, this);
        this.objectFillerFactoryMethods = getPotentialRandomFactoryMethods(domainConfiguration);
        this.seed = random.nextLong();
        random.setSeed(seed);

        domainConfiguration.init(this.random, this);
    }

    /**
     * Creates a new instance with a {@link SimpleDomainConfiguration} with a fixed seed.
     *
     * @param seed the fixed seed, non null.
     */
    public TestDataFiller(Long seed) {
        this.random = new Random(seed);
        this.seed = seed;
        this.domainConfiguration = new SimpleDomainConfiguration(random, this);
        this.objectFillerFactoryMethods = getPotentialRandomFactoryMethods(domainConfiguration);

        domainConfiguration.init(random, this);
    }

    /**
     * Creates a new instance based on a custom domain class. It will create an instance and
     * provide the correct initialization of its random part.
     *
     * @param clazz the domain class definition, non null.
     */
    public TestDataFiller(Class<? extends DomainConfiguration> clazz) {
        this.random = new Random();
        this.domainConfiguration = (DomainConfiguration) newInstance(clazz, random);
        this.objectFillerFactoryMethods = getPotentialRandomFactoryMethods(domainConfiguration);
        this.seed = random.nextLong();
        random.setSeed(seed);

        domainConfiguration.init(random, this);
    }

    /**
     * Creates a new instance based on a custom domain class. It will create an instance and
     * provide the correct initialization of its random part by the given seed.
     *
     * @param clazz the domain class definition, non null.
     * @param seed  the fixed seed, non null.
     */
    public TestDataFiller(Class<? extends DomainConfiguration> clazz, Long seed) {
        this.random = new Random(seed);
        this.seed = seed;
        this.domainConfiguration = (DomainConfiguration) newInstance(clazz, random);
        this.objectFillerFactoryMethods = getPotentialRandomFactoryMethods(domainConfiguration);

        domainConfiguration.init(random, this);
    }

    /**
     * Get the used seed within this instance.
     *
     * @return the seed.
     */
    public Long getSeed() {
        return seed;
    }

    /**
     * Creates an object of the required type. It will be recursively filled till it is composed
     * out of basic java types or it visits the same type more than once.
     *
     * @param <T>   The type of the class.
     * @param clazz the desired class, non null.
     * @return a new filled object with random data.
     */
    public <T> T fillInstance(Class<T> clazz) {
        return createRandomFilledInstanceInternal(clazz, 0);
    }

    /**
     * Creates an object of the required type. It will be recursively filled till it is composed
     * out of basic java types or it visits the same type more than once.
     *
     * @param <T>      The type of the class.
     * @param clazz    the desired class, non null.
     * @param consumer lambda consumer which will be applied on the created instance.
     * @return a new filled object with random data.
     */
    public <T> T fillInstance(Class<T> clazz, Consumer<T> consumer) {
        T randomFilledInstance = createRandomFilledInstanceInternal(clazz, 0);
        consumer.accept(randomFilledInstance);

        return randomFilledInstance;
    }

    /**
     * Creates a list of the required type. It will be recursively filled till it is composed
     * out of basic java types or it visits the same type more than once.
     *
     * @param <T>    The type of the class.
     * @param clazz  the desired class, non null.
     * @param amount the required number of random instances, greater 0.
     * @return a new filled object with random data.
     */
    public <T> List<T> fillInstances(Class<T> clazz, int amount) {
        if (amount < 0) {
            throw new FakegenException("Can not handle negative amount");
        }

        List<T> result = new ArrayList(amount + 1);
        for (int i = 0; i < amount; i++) {
            result.add(fillInstance(clazz));
        }

        return result;
    }

    /**
     * Creates an object of the required builder type. It will be recursively filled till it is composed
     * out of basic java types or it visits the same type more than once. The usage is optimized
     * to work with the builder pattern of common code generators like Immutables, AutoValue and
     * Freebuilder. An example:
     *
     * <pre>{@code
     * BuilderType fromBuilder = filler
     *      .createFromBuilder(ImmutableBuilderType.Builder.class)
     *      .type(SimpleEnum.SOME)
     *      .build();
     * }</pre>
     * <p>
     * For Lombok better use {@link #fillBuilder(Object)}.
     *
     * @param <T>   The type of the class.
     * @param clazz the desired builder class, non null.
     * @return a new filled builder with random data.
     */
    public <T> T fillBuilder(Class<T> clazz) {
        return createRandomFilledInstanceInternal(clazz, 0, true, singleton("initBits"), true);
    }

    /**
     * Creates an object of the required builder type. It will be recursively filled till it is composed
     * out of basic java types or it visits the same type more than once. Unlike
     * {@link #fillBuilder(Class)} this is intended for the usage with lombok.
     *
     * <pre>{@code
     * SimpleBuilderType fromBuilder = filler.createFromBuilder(SimpleBuilderType.builder())
     *      .simpleEnum(SimpleEnum.VALUE_2)
     *      .build();
     * }</pre>
     * <p>
     * For Lombok better use {@link #fillBuilder(Object)}.
     *
     * @param <T>      The type of the class.
     * @param instance the builder instance, non null.
     * @return a new filled builder with random data.
     */
    public <T> T fillBuilder(T instance) {
        return (T) fill(instance, instance.getClass(), 0, emptySet(), false);
    }


    /**
     * Creates an object of the required type by the usage of a Factory method. It will be recursively
     * filled till it is composed out of basic java types or it visits the same type more than once.
     * The method will try to identify a factory method and use it to create the instance. A factory
     * method is static and returns the the desired type within the class with zero or more parameters.
     * The parameters will be created by the random filler and applied to the method call.
     * <p>
     * This will use the first factory method it can find, which may depend on the used JVM. To define the
     * exact method, use {@link #fillByFactory(Class, MethodHolder)}.
     *
     * @param <T>   the desired type.
     * @param clazz the desired class containing a factory method.
     * @return a new instance created by the first found factory method.
     */
    public <T> T fillByFactory(Class<T> clazz) {
        try {

            Optional<Method> possibleFactoryMethod = Arrays.stream(clazz.getMethods())
                    .filter(ReflectionUtils::isStaticMethod)
                    .filter(method -> method.getReturnType().equals(clazz))
                    .filter(method -> Arrays.stream(method.getParameterTypes()).noneMatch(clazz::equals))
                    .findFirst();

            if (possibleFactoryMethod.isPresent()) {
                Method method = possibleFactoryMethod.get();

                Object[] instances = Arrays.stream(method.getParameterTypes())
                        .map(this::fillInstance)
                        .toArray();

                return (T) method.invoke(null, instances);
            }

        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new FakegenException("Could not invoke factory method", e);
        }

        return null;

    }

    /**
     * Creates an object of the required type by the usage of a Factory method. It will be recursively
     * filled till it is composed out of basic java types or it visits the same type more than once.
     * The method will the factory method defined and use it to create the instance. A factory
     * method is static and returns the the desired type within the class with zero or more parameters.
     * The parameters will be created by the random filler and applied to the method call. An Example:
     * <pre>{@code
     * BeanByFactoryType randomFilledByFactory = tdf
     *                 .createRandomFilledByFactory(BeanByFactoryType.class, method("createBeanWith", String.class));
     * }</pre>
     * <p>
     * Use {@link MethodHolder#method(String, Class[])} for convenience. In case of missing method
     * an exception will try to identify a similar named method to identify missing refactoring or typos easier.
     *
     * @param <T>          the desired type.
     * @param clazz        the desired class containing a factory method, non null.
     * @param methodHolder a wrapper to identify a method, non null.
     * @return a new instance created by the desired factory method.
     */
    public <T> T fillByFactory(Class<T> clazz, MethodHolder methodHolder) {
        try {
            Method method = methodHolder.createMethod(clazz);

            Object[] instances = Arrays.stream(methodHolder.getParameterTypes())
                    .map(this::fillInstance)
                    .toArray();

            return (T) method.invoke(null, instances);

        } catch (NoSuchMethodException e) {
            String message = createExceptionMessage(clazz, methodHolder);
            throw new FakegenException(message, e);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new FakegenException("Could not invoke factory method", e);
        }

    }


    private <T> T createRandomFilledInstanceInternal(Class<T> clazz, int recursionCounter) {
        return createRandomFilledInstanceInternal(clazz, recursionCounter, false, emptySet(), false);
    }

    private <T> T createRandomFilledInstanceInternal(Class<T> clazz, int recursionCounter, boolean usePrivateConstructor,
                                                     Set<String> ignoreFields, boolean useSetter) {

        if (objectFillerFactoryMethods.containsKey(clazz)) {
            Method method = objectFillerFactoryMethods.get(clazz);
            return (T) callFactoryMethod(null, method, null);
        } else if (isJvmRelevant(clazz)) {
            return null;
        } else if (clazz.isEnum()) {
            return (T) domainConfiguration.createEnum(null, clazz);
        } else if (possibleForByteBuddy(clazz)) {
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

    private <T> boolean possibleForByteBuddy(Class<T> clazz) {
        Package aPackage = clazz.getPackage();

        if (aPackage != null) {
            return
                    !aPackage.getName().startsWith("java") &&
                            !aPackage.getName().startsWith("sun") &&
                            !aPackage.getName().startsWith("com.sun") &&
                            (
                                    clazz.isInterface() ||
                                            (isAbstractClass(clazz) &&
                                                    isNotFinal(clazz))
                            );
        }

        return false;

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
            return domainConfiguration.createEnum(fieldName, type);
        }

        Method method = objectFillerFactoryMethods.get(type);
        if (method != null) {
            return callFactoryMethod(fieldName, method, field);
        }

        return null;
    }

    @SneakyThrows
    private Object callFactoryMethod(String fieldName, Method method, Field field) {
        return method.invoke(domainConfiguration, fieldName, field);
    }

    @SneakyThrows
    private Object newInstance(Constructor constructor) {
        return constructor.newInstance();
    }

    private Object newInstance(Class clazz, Random random) {
        try {
            return clazz
                    .getDeclaredConstructor(Random.class, TestDataFiller.class)
                    .newInstance(random, this);
        } catch (ReflectiveOperationException e) {
            throw new FakegenException("Could not find Constructor with Random as parameter. " +
                    "If it is an inner class, is is static?", e);
        }

    }

    private Optional<Constructor> extractPlainConstructor(Class clazz, boolean usePrivateConstructor) {

        if (clazz.isInterface() || isAbstractClass(clazz)) {
            return Optional.empty();
        }

        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.getParameterCount() == 0 && (isPublic(constructor) || usePrivateConstructor)) {
                constructor.setAccessible(true);
                return Optional.of(constructor);
            }
        }
        return Optional.empty();
    }

}
