package de.drippinger.fakegen.util;

import de.drippinger.fakegen.domain.ObjectFiller;
import lombok.experimental.UtilityClass;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

/**
 * @author Dennis Rippinger
 */
@SuppressWarnings("unchecked")
@UtilityClass
public final class ReflectionUtils {

    private static final Set<Method> OBJECT_METHOD = new HashSet(Arrays.asList(Object.class.getMethods()));

    public static Stream<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        return getAllFields(fields, type)
                .stream()
                .filter(field -> !field.getName().equals("class"));
    }

    private static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }
        return fields;
    }

    public static Iterable<Method> getAllMethods(Class<?> type) {
        Map<String, Method> methods = new HashMap<>();
        return getAllMethods(methods, type).values();
    }

    private static Map<String, Method> getAllMethods(Map<String, Method> methods, Class<?> type) {
        for (Method method : type.getDeclaredMethods()) {
            if (!method.isDefault() && isNotStaticMethod(method) && isNotBaseObjectMethod(method)) {
                methods.put(method.getName(), method);
            }
        }

        if (type.getSuperclass() != null) {
            getAllMethods(methods, type.getSuperclass());
        }
        for (Class<?> interfaceClazz : type.getInterfaces()) {
            getAllMethods(methods, interfaceClazz);
        }

        return methods;
    }

    public static Optional<Class> mapObjectToPrimitive(Class<?> returnType) {
        if (returnType.equals(Integer.class)) {
            return Optional.of(int.class);
        } else if (returnType.equals(Double.class)) {
            return Optional.of(double.class);
        } else if (returnType.equals(Float.class)) {
            return Optional.of(float.class);
        } else if (returnType.equals(Boolean.class)) {
            return Optional.of(boolean.class);
        } else if (returnType.equals(Short.class)) {
            return Optional.of(short.class);
        } else if (returnType.equals(Long.class)) {
            return Optional.of(long.class);
        }
        return Optional.empty();
    }

    private static boolean isNotBaseObjectMethod(Method method) {
        return !OBJECT_METHOD.contains(method);
    }

    public static Map<Type, Method> getPotentialRandomFactoryMethods(ObjectFiller objectFiller) {
        Map<Type, Method> result = new HashMap<>();
        for (Method method : objectFiller.getClass().getMethods()) {
            Parameter[] parameters = method.getParameters();
            if (methodIsSuitableRandomFactory(method, parameters)) {
                result.put(method.getReturnType(), method);
                addPrimitiveType(method.getReturnType(), method, result);
            }
        }

        return result;

    }

    private static void addPrimitiveType(Class<?> returnType, Method method, Map<Type, Method> result) {
        if (returnType.equals(Integer.class)) {
            result.put(int.class, method);
        } else if (returnType.equals(Double.class)) {
            result.put(double.class, method);
        } else if (returnType.equals(Float.class)) {
            result.put(float.class, method);
        } else if (returnType.equals(Boolean.class)) {
            result.put(boolean.class, method);
        } else if (returnType.equals(Short.class)) {
            result.put(short.class, method);
        } else if (returnType.equals(Long.class)) {
            result.put(long.class, method);
        } else if (returnType.equals(Byte.class)) {
            result.put(byte.class, method);
        } else if (returnType.equals(Character.class)) {
            result.put(char.class, method);
        }
    }

    public static Optional<Method> findPossibleSetterForField(Class clazz, Field field) {
        return Arrays
                .stream(clazz.getDeclaredMethods())
                .filter(method -> method.getParameterCount() == 1)
                .filter(method -> containsIgnoreCase(method.getName(), field.getName()) ||
                        equalsIgnoreCase(method.getName(), field.getName()))
                .filter(method -> method.getParameterTypes()[0].equals(field.getType()))
                .findFirst();
    }

    private static boolean methodIsSuitableRandomFactory(Method method, Parameter[] parameters) {
        return parameters.length == 2 &&
                parameters[0].getType().equals(String.class) &&
                parameters[1].getType().equals(Field.class) &&
                method.getReturnType() != null;
    }

    public static boolean isNotFinal(Field field) {
        return !Modifier.isFinal(field.getModifiers());
    }

    public static boolean isNotFinal(Class clazz) {
        return !Modifier.isFinal(clazz.getModifiers());
    }

    public static boolean isPublic(Member member) {
        return Modifier.isPublic(member.getModifiers());
    }

    public static boolean isNotRelevantForCoverage(Field field) {
        return !field.getName().contains("jacoco");
    }

    public static boolean isAbstractClass(Class<?> clazz) {
        return Modifier.isAbstract(clazz.getModifiers());
    }

    public static boolean isStaticMethod(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }

    public static boolean isNotStaticMethod(Method method) {
        return !Modifier.isStatic(method.getModifiers());
    }

}
