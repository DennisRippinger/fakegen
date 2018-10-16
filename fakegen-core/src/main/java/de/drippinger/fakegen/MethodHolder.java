package de.drippinger.fakegen;

import lombok.Getter;

import java.lang.reflect.Method;

/**
 * @author Dennis Rippinger
 */
@Getter
public class MethodHolder<T> {

    private final String method;

    private final Class[] parameterTypes;

    private MethodHolder(String method, Class[] parameterTypes) {
        this.method = method;
        this.parameterTypes = parameterTypes;
    }

    public static MethodHolder method(String method, Class... parameterTypes) {
        return new MethodHolder(method, parameterTypes);
    }

    Method createMethod(Class<T> clazz) throws NoSuchMethodException {
        return clazz.getMethod(method, parameterTypes);
    }

}
