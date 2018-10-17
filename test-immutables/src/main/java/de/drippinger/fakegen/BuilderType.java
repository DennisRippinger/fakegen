package de.drippinger.fakegen;


import org.immutables.value.Value;

/**
 * @author Dennis Rippinger
 */
@Value.Immutable
public interface BuilderType {

    String someString();

    SimpleEnum type();

}
