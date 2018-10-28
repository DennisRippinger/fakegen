package de.drippinger.fakegen.util;

import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Dennis Rippinger
 */
@UtilityClass
public class Ignore {

    private static final Set<String> IGNORED_PACKAGES;

    static {
        IGNORED_PACKAGES = new HashSet<>();

        IGNORED_PACKAGES.add("java.security.cert");
        IGNORED_PACKAGES.add("javafx");
        IGNORED_PACKAGES.add("java.awt");
        IGNORED_PACKAGES.add("javax.swing");
        IGNORED_PACKAGES.add("java.util.logging");
        IGNORED_PACKAGES.add("javax.rmi");
        IGNORED_PACKAGES.add("sun");
        IGNORED_PACKAGES.add("javassist");
    }


    public static boolean isJvmRelevant(Class clazz) {
        return IGNORED_PACKAGES.stream().anyMatch(s -> {
            Package aPackage = clazz.getPackage();
            if (aPackage != null) {
                return clazz.getPackage().getName().startsWith(s);
            }
            return false;
        });
    }
}
