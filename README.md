# Fakegen

Fakegen is a library to create testdata on demand. Unlike other libraries it can fill an entire object graph of your model class with random data. So you can focus on the relevant test data.

## Motivation

In projects with a complex business logic tests often require that our parameter objects, like entities or DTOs, are filled with non-null data. But for the test itself only a fraction of the parameters are relevant. They may change from test to test, but an often seen pattern is that test initialisation is copied over and over, altering only the relevant data, or hidden into class wise util methods. After a while it is difficult to identify the relevant from the non relevant test data. Fakegen is a test library that allows to automatically fill our objects with random test data, such that we can focus on the relevant data.

## Simple Usage

```java
TestDataFiller filler = new TestDataFiller();
SomeEntity entity = filler
    .createRandomFilledInstanceAndApply(SomeEntity.class, instance -> instance.setGreeting("Hello World"));

System.out.println(entity);
// SomeEntity(greetings=Hello world, birthday=2018-10-14)
```
