package de.drippinger.fakegen.freebuilder;


import org.inferred.freebuilder.FreeBuilder;

@FreeBuilder
public interface BuilderType {

    String someString();

    SimpleEnum type();

    class Builder extends BuilderType_Builder {}


}
