package de.drippinger.fakegen.util;

/**
 * @author Dennis Rippinger (msg systems ag) 2018
 */
public final class StringUtils {

    public static String titleCase(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static String firstLowerCase(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

}
