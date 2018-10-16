package de.drippinger.fakegen.exception;

import de.drippinger.fakegen.MethodHolder;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

import static de.drippinger.fakegen.util.LevenstheinDistance.editDistance;

/**
 * @author Dennis Rippinger
 */
public final class ExceptionHelper {

    public static String createExceptionMessage(Class clazz, MethodHolder methodHolder) {
        String similarMethod = null;
        boolean nameIsCorrect = false;
        Method originalMethod = null;
        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(methodHolder.getMethod())) {
                nameIsCorrect = true;
                originalMethod = method;
                break;
            }

            int editDistance = editDistance(method.getName(), methodHolder.getMethod());

            if (editDistance < 5) {
                similarMethod = method.getName();
                break;
            }

        }

        if (nameIsCorrect) {
            return String.format(
                    "Could not find method `%s` with given parameter `%s`, but valid could be the Parameter set: `%s`",
                    methodHolder.getMethod(),
                    classParameterString(methodHolder.getParameterTypes()),
                    classParameterString(originalMethod));
        }

        if (similarMethod != null) {
            return String.format(
                    "Could not find method `%s`, but a similar named one called `%s` was found.",
                    methodHolder.getMethod(),
                    similarMethod);
        }

        return String.format(
                "Could not find method `%s`, maybe a refactoring missed the string.",
                methodHolder.getMethod());
    }

    private static String classParameterString(Class[] classes) {
        return Arrays
                .stream(classes)
                .map(aClass -> aClass.getSimpleName() + ".class")
                .collect(Collectors.joining(" ,"));
    }

    private static String classParameterString(Method originalMethod) {
        return classParameterString(originalMethod.getParameterTypes());
    }
}
