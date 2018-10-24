# Fakegen
[![Build Status](https://img.shields.io/travis/com/DennisRippinger/fakegen.svg)](https://travis-ci.com/DennisRippinger/fakegen)
[![Coveralls](https://img.shields.io/coveralls/github/DennisRippinger/fakegen.svg)](https://coveralls.io/github/DennisRippinger/fakegen?branch=master)


Fakegen is a library to create testdata on demand. Unlike other libraries it can fill an entire object graph of your model class with random data. So you can focus on the relevant test data.

## Motivation

In projects with a complex business logic tests often require that our parameter objects, like entities or DTOs, are filled with non-null data. But for the test itself only a fraction of the parameters are relevant. They may change from test to test, but an often seen pattern is that test initialisation is copied over and over, altering only the relevant data, or hidden into class-wise util methods. After a while it is difficult to identify the relevant from the non relevant test data. Fakegen is a test library that allows to automatically fill our objects with random test data, so that we can focus on the relevant data.

## Usage

The simplest use case could look similar to the following:

```java
TestDataFiller tdf = new TestDataFiller();
SomeEntity entity = tdf
    .createRandomFilledInstance(SomeEntity.class, instance -> instance.setGreeting("Hello World"));

System.out.println(entity);
// SomeEntity(greetings=Hello world, birthday=2018-10-14)
```

### Domain Specific Knowledge

In many cases pure random fields would do the job, but in an error case one would have to deal with fields that do not resemble the domain specific knowledge. 
A `surname` is not something like `3_fGu8C`.
But this knowledge is highly domain specific.
Therefore it is possible to create a `DomainConfiguration` class.
One can build them specific for a test, but it makes more sense to create a project specific class for the domain.

```java
public class TestConfiguration extends DomainConfiguration {

    @Override
    public void init(Random random) {
        Faker faker = new Faker(random);

        this.fieldForStringShouldUse("name", () -> faker.name().firstName());
    }
}
```
Here done together with the cool [Faker library](https://github.com/DiUS/java-faker).

```java
TestDataFiller tdf = new TestDataFiller(new TestConfiguration());

SimpleType entity = tdf.createRandomFilledInstance(SimpleType.class);
System.out.println(entity.getName());
// Jane
```
