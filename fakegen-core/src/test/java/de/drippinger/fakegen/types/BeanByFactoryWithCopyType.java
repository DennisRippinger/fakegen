package de.drippinger.fakegen.types;

import lombok.Data;

/**
 * @author Dennis Rippinger
 */
@Data
public class BeanByFactoryWithCopyType {

    private final String someField;

    private BeanByFactoryWithCopyType(String someField) {
        this.someField = someField;
    }

    public static BeanByFactoryWithCopyType copyFactory(BeanByFactoryWithCopyType original) {
        return new BeanByFactoryWithCopyType(original.getSomeField());
    }

    public static BeanByFactoryWithCopyType createBeanWith(String someField) {
        return new BeanByFactoryWithCopyType(someField);
    }

}
