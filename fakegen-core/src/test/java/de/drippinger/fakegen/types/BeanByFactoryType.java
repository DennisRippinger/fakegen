package de.drippinger.fakegen.types;

import lombok.Data;

/**
 * @author Dennis Rippinger
 */
@Data
public class BeanByFactoryType {

    private final String someField;

    private BeanByFactoryType(String someField) {
        this.someField = someField;
    }

    public static BeanByFactoryType createBeanWith(String someField) {
        return new BeanByFactoryType(someField);
    }

}
