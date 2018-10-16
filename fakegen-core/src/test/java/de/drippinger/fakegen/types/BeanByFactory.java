package de.drippinger.fakegen.types;

import lombok.Data;

/**
 * @author Dennis Rippinger
 */
@Data
public class BeanByFactory {

    private final String someField;

    private BeanByFactory(String someField) {
        this.someField = someField;
    }

    public static BeanByFactory createBeanWith(String someField) {
        return new BeanByFactory(someField);
    }

}
