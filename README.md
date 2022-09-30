# Fakegen
[![Build Status](https://github.com/DennisRippinger/fakegen/actions/workflows/maven.yml/badge.svg)](https://github.com/DennisRippinger/fakegen/actions)
[![Coveralls](https://img.shields.io/coveralls/github/DennisRippinger/fakegen/master.svg)](https://coveralls.io/github/DennisRippinger/fakegen?branch=master)
[![Maven metadata URL](https://img.shields.io/maven-metadata/v/http/central.maven.org/maven2/de/drippinger/fakegen/fakegen-core/maven-metadata.xml.svg)](https://search.maven.org/search?q=g:de.drippinger.fakegen%20AND%20a:fakegen-core)


Fakegen is a library to create test data on demand.
Unlike other libraries, it can fill an entire object graph of your model class with random data.
So you can focus on the relevant test data.

## Motivation

In projects with complex business logic tests often require that our parameter objects, like entities or DTOs, are filled with non-null data.
However, for the test itself, only a fraction of the parameters are relevant.
They may change from test to test, but an often seen pattern is that test initialization is copied, altering only some data.
Or it is hidden in test-wise until methods.
Both result in noise within the test.
After a while, it is difficult to identify the relevant from the non-relevant test data.
Fakegen is a test library that allows to automatically fill the test objects with predefined or random data so that we can focus on the relevant data.

### Relation to property based testing
Filling the test objects with random data is a topic related to property-based testing, see for instance [QuickTheories](https://github.com/ncredinburgh/QuickTheories) or [jqwik](https://jqwik.net/).
Property-based testing algorithms try to identify a set of properties, which fail the test, e.g. trying the usual problematic cases like for Integer: min/max/zero.
Fakegen does not try to identify such value sets actively.
Its main purpose is to simplify the given part of the test generation.
The generated values should satisfy a minimal requirement, such that the developer can focus on the variable parts for the test.
Due to the random nature of Fakegen, it could identify a problematic set of data, just like property-based testing.
In such cases, it offers possibilities to make the test reproducible.
See [Testing Support](#testing-support) for more input.

## Dependency

````xml
<dependency>
    <groupId>de.drippinger.fakegen</groupId>
    <artifactId>fakegen-core</artifactId>
    <version>0.2</version>
    <scope>test</scope>
</dependency>
````

See also [Testing Support](#testing-support) for specific JUnit 4 and 5 support.

## Usage

A simple use case could look similar to the following:

```java
TestDataFiller tdf = new TestDataFiller();
SomeEntity entity = tdf
    .fillInstance(SomeEntity.class, instance -> instance.setGreeting("Hello World"));

System.out.println(entity);
// SomeEntity(greetings=Hello world, birthday=2018-10-14)
```

### Domain Specific Knowledge

Debugging a test with random strings and numbers can be difficult because they contain no human-comprehensible semantic.
A `surname` string with a value `3_fGu8C` can be confusing.
And Fakegen cannot infer the semantic of the domain model.

Therefore Fakegen understands a `DomainConfiguration` class.
They are intended to be built once for your project and referenced by the TestDataFiller instance.

```java
public class TestConfiguration extends DomainConfiguration {

    public TestConfiguration(Random random) {
        super(random);
    }

    @Override
    public void init(Random random, TestDataFiller testDataFiller) {
        Faker faker = new Faker(random);

        fieldForStringShouldUse("name", () -> faker.name().firstName());
        fieldForClassShouldUse("birthday", LocalDate.class, () -> LocalDate.of(2000, 6, 1);

    }
}
```
`DomainConfiguration` provides methods to configure a Supplier for a specific field name and type combination.
The example works together with the [Faker library](https://github.com/DiUS/java-faker).
Faker allows choosing from predefined values for different domains, such that the following is possible.

```java
TestDataFiller tdf = new TestDataFiller(TestConfiguration.class);

SimpleType entity = tdf.createRandomFilledInstance(SimpleType.class);
System.out.println(entity.getName());
// Jane
```
Usually, Fakegen would recursively break down objects until they consist of the basic java types.
Within a `DomainConfiguration` it is possible to define a default method for Type creation.
This allows creating an instance in a more controlled way.
Currently, it is required that the signature consists of a string.

An Example:

````java
    public static class TestConfiguration extends DomainConfiguration {

        public TestConfiguration(Random random) {
            super(random);
        }

        @Override
        public void init(Random random, TestDataFiller testDataFiller) {
            // NOP
        }

        public SimpleType createSimpleType(String fieldName) {
            SimpleType simpleType = new SimpleType();
            simpleType.setName("Hello there");

            return simpleType;
        }
    }
````
The method `createSimpleType` will now be used for all fields of the type, regardless of their field name.

## Features

Besides the regular support for POJO classes, Fakegen supports some special cases:

### Factory Methods

Factory methods usually provide the only way to create an instance of the object.
If there is only one factory method in the class it is possible to use the following.
```java

// Factory Method in Class `BeanByFactoryType`
public static BeanByFactoryType createBeanWith(String someField) {
    return new BeanByFactoryType(someField);
}

// Fakegen
BeanByFactoryType randomFilledByFactory = tdf
        .fillByFactory(BeanByFactoryType.class);
```
Fakegen will look for a static method returning the requested type within the class.
The parameters objects will be created with the regular Fakegen logic.
It also identifies copy constructors and omits them.
In cases were multiple Factory methods are present it will use the first one found.
This not reliably the same first method on all JVMs.
When a distinct method is required it is possible to identify the method directly:

````java
BeanByFactoryType randomFilledByFactory = tdf
        .fillByFactory(BeanByFactoryType.class, method("createBeanWith", String.class));
````

The identification of a method via a string can be problematic when it comes to refactoring or typos.
In case of a missing method, Fakegen will try to identify a similarly named method and print out an Exception message pointing to the similar method and signature.

### Builder

Fakegen also supports the mayor builder pattern currently present like [Lombok](https://projectlombok.org/features/Builder), [Immutables](https://immutables.github.io/), [Freebuilder](https://github.com/inferred/FreeBuilder) or [AutoValue](https://github.com/google/auto/tree/master/value).
Fakegen can pick up the Builder class and fill the builder fields.
Via `build()` a real (immutable) instance will be created.
```java
BuilderType fromBuilder = filler.fillBuilder(ImmutableBuilderType.Builder.class)
                .type(SimpleEnum.SOME)
                .build();
```

### Interface & Abstract class support

If your data classes work with Interface or Abstract classes Fakegen will try to create a simple implementation with the help of [ByteBuddy](https://github.com/raphw/byte-buddy).
Imagine an Interface like

````java
public interface User {
    String getName();
    LocalDate getBirthday();
    boolean isRegistered();
}
````

Fakegen will create an implementation at runtime with the fields `name`, `birthday` and `registered` and the according getter.
Currently, the approach only detects `get` and `is` prefixes.
Methods other than that will be implemented by throwing an Exception.


## Testing Support
As mentioned above could Fakegen find a set of parameters which will fail the test.
These generated values are depended on the used seed, generated with each instance if the `TestDataFiller`.
The JUnit Libs help to print the currently used seed for the failing test to STOUT.
This seed can then be facilitated to the constructors of `TestDataFiller` to replay the failed scenario.

### JUnit 4

For JUnit 4 this Rule works as Delegate for the TestDataFiller:

````java
@Rule
public TestDataFillerRule testData = new TestDataFillerRule();

@Test
public void failing_test_should_print_seed() {
    throw new RuntimeException();
}
// On System out:
// Seed used in Test 'com.company.logic.MyTest' was 874208787563157915
````

````xml
<dependency>
    <groupId>de.drippinger.fakegen</groupId>
    <artifactId>fakegen-junit4</artifactId>
    <version>0.2</version>
    <scope>test</test>
</dependency>
````

### JUnit 5
For JUnit 5 Fakegen provides an Extension:

````java
@ExtendWith(TestDataFillerExtension.class)
public class MyTest {

    private TestDataFiller testDataFiller = new TestDataFiller();

    @Test
    public void failing_test_should_print_seed() {
        throw new RuntimeException();
    }
}

// On System out:
// Seed used in Test 'com.company.logic.MyTest' was 874208787563157915
````

The Extension implements an ExceptionListener and will try to find a field with the regular `TestDataFiller`.

````xml
<dependency>
    <groupId>de.drippinger.fakegen</groupId>
    <artifactId>fakegen-junit5</artifactId>
    <version>0.2</version>
    <scope>test</test>
</dependency>
````

