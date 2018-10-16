package de.drippinger.fakegen.types;

import lombok.Data;

/**
 * @author Dennis Rippinger
 */
@Data
public class BeanByFactoryZeroArgsType {

    private final String someField;

    private BeanByFactoryZeroArgsType(String someField) {
        this.someField = someField;
    }

    public static BeanByFactoryZeroArgsType createBeanWith() {
        return new BeanByFactoryZeroArgsType("Hello there General Kenobi");
    }

}
