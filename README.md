# Fakegen
[![Build Status](https://img.shields.io/travis/com/DennisRippinger/fakegen.svg)](https://travis-ci.com/DennisRippinger/fakegen)
[![Coveralls](https://img.shields.io/coveralls/github/DennisRippinger/fakegen.svg)](https://coveralls.io/github/DennisRippinger/fakegen?branch=master)


Fakegen is a library to create testdata on demand. 
Unlike other libraries it can fill an entire object graph of your model class with random data. 
So you can focus on the relevant test data.

## Motivation

In projects with a complex business logic tests often require that our parameter objects, like entities or DTOs, are filled with non-null data. 
But for the test itself only a fraction of the parameters are relevant. 
They may change from test to test, but an often seen pattern is that test initialisation is copied over and over, altering only the relevant data, or hidden into class-wise util methods. 
After a while it is difficult to identify the relevant from the non relevant test data. 
Fakegen is a test library that allows to automatically fill our objects with random test data, so that we can focus on the relevant data.

### Relation to property based testing
Filling the test data with a random set of data is a topic related to property based testing, see for instance [QuickTheories](https://github.com/ncredinburgh/QuickTheories) or [jqwik](https://jqwik.net/). 
Property based testing algorithms tries to identify a set of properties which fail the test, e.g. trying the usual problematic cases like for Integer: min/max/zero. 
Fakegen does not tries to identify such value pairs actively.
Its main purpose is to simplify the given part of the test generation.
The filled values should satisfy a minimal requirement such that the developer can focus on the variable parts for the test.
Due to the random nature of Fakegen it could identify a problematic set of data, just like property based testing. 
In such cases it offers possibilities to make the test reproducible. 
See [Testing Support](#testing-support) for more input.

## Usage

A simple use case could look similar to the following:

```java
TestDataFiller tdf = new TestDataFiller();
SomeEntity entity = tdf
    .createRandomFilledInstance(SomeEntity.class, instance -> instance.setGreeting("Hello World"));

System.out.println(entity);
// SomeEntity(greetings=Hello world, birthday=2018-10-14)
```

### Domain Specific Knowledge

In many cases pure random fields would do the job, but while debugging one would have to deal with fields that do not resemble the domain specific knowledge. 
A `surname` is not something like `3_fGu8C`.
But this knowledge is highly domain specific.
Therefore it is possible to create a `DomainConfiguration` class.
One can build them specific for a test, but it makes more sense to create a project specific class for the domain.

```java
public class TestConfiguration extends DomainConfiguration {

    public TestConfiguration(Random random) {
        super(random);
    }

    @Override
    public void init(Random random, TestDataFiller testDataFiller) {
        Faker faker = new Faker(random);

        this.fieldForStringShouldUse("name", () -> faker.name().firstName());
    }
}
```
Here done together with the [Faker library](https://github.com/DiUS/java-faker).

```java
TestDataFiller tdf = new TestDataFiller(TestConfiguration.class);

SimpleType entity = tdf.createRandomFilledInstance(SimpleType.class);
System.out.println(entity.getName());
// Jane
```

In case you have a very special object which needs to be instantiated in a way that does not work with Fakegen, you can create a method which returns the specific type and has a String as parameter.
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

Beside the regular support for POJO classes, Fakegen supports some special cases:

### Factory Methods

Within domain objects a sometimes observed style is the usage of factory methods.
They are the only legit way to create an instance of the object.
If there is only one factory method in the class it is possible to:
```java

// Factory Method in Class `BeanByFactoryType`
public static BeanByFactoryType createBeanWith(String someField) {
    return new BeanByFactoryType(someField);
}

// Fakegen
BeanByFactoryType randomFilledByFactory = tdf
        .createRandomFilledByFactory(BeanByFactoryType.class);
```
Fakegen will look for a static method returning the requested type within the class. 
The parameters will be filled by the regular Fakegen logic.
It also identifies copy constructors and omits them.
In cases were multiple Factory methods are present it will use the first one found.
Keep in mind that this is not reliably the same first method on all JVMs.
When a distinct method is required it is possible to identify the method directly:

````java
BeanByFactoryType randomFilledByFactory = tdf
        .createRandomFilledByFactory(BeanByFactoryType.class, method("createBeanWith", String.class));
````

The identification of a method via a string can be problematic when it comes to refactoring or typos. 
In case of a missing method, Fakegen will try to identify a similar named method and print out an Exception message pointing to the similar method and signature.

### Builder 

Fakegen also supports the mayor builder pattern currently present. 
If you are using [Lombok](https://projectlombok.org/features/Builder), [Immutables](https://immutables.github.io/), [Freebuilder](https://github.com/inferred/FreeBuilder) or [AutoValue](https://github.com/google/auto/tree/master/value), Fakegen can pick up the Builder class, fill them, and allows to return an (immutable) instance.

```java
BuilderType fromBuilder = filler.createFromBuilder(ImmutableBuilderType.Builder.class)
                .type(SimpleEnum.SOME)
                .build();
```

### Experimental: Interface & Abstract class support

If your data classes works with Interface or Abstract classes Fakegen will try to create a simple implementation with the help of [ByteBuddy](https://github.com/raphw/byte-buddy).
Imagine an Interface like 

````java
public interface User {
    String getName();
    LocalDate getBirthday();
    boolean isRegistered();
}
````

Fakegen will create an implementation at runtime with the fields `name`, `birthday` and `registered` and the according getter.
Currently the approach only detects `get` and `is` prefixes. 
Methods other than that will be implemented by throwing an Exception.
Currently this feature is experimental and I'm happy for feedback on this approach.

## Testing Support
As mentioned above, Fakegen might find a set of parameters which will fail the test. 
To have the test repeatable one can provide the TestDataFiller a seed for the random class.
To print the used random seed in a failure one can use the JUnit libs:

### JUnit 4

For JUnit 4 Fakegen provides a Rule:

````java
@Rule
public TestDataFillerRule testData = new TestDataFillerRule();

@Test
public void failing_test_should_print_seed() {
    throw new RuntimeException();
}
// On System out:
// Seed used in Test 'com.company.logic.MyTest' was -874208787563157915
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
// Seed used in Test 'com.company.logic.MyTest' was -874208787563157915
````

The Extension implements a ExceptionListener and will try to find a field with the regular `TestDataFiller`.


## Performance

Fakegen works with heavy usage of Java reflection.
Which is usually considered an _expensive_ operation.
And this is true.
Compared to doing it manually, it is slow.

![JMH Results](/img/jmh_results.png?raw=true "JMH Tests results")
The detailed results can be inspected [here](http://jmh.morethan.io/?gist=896e1136c63a4e87ffcd1a866579fc3f).
The tests itself are within the fakegen-benchmark folder.

But what does that mean?
To put the results into perspective a test case with [Mockito](https://github.com/mockito/mockito) was added which does roughly the same as the other tests.  
Of course this is an unfair comparison, since it is not Mockitos intended usage.
But many of us use Mockito on a daily basis, and there it is not an issue.

