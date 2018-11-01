package de.drippinger.fakegen.domain;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * @author Dennis Rippinger
 */
public interface ObjectFiller {

    String createString(String fieldName, Field field);

    String createString(String fieldName, int length);

    Integer createInteger(String fieldName, Field field);

    Long createLong(String fieldName, Field field);

    Short createShort(String fieldName, Field field);

    Double createDouble(String fieldName, Field field);

    Float createFloat(String fieldName, Field field);

    BigInteger createBitInteger(String fieldName, Field field);

    Boolean createBoolean(String fieldName, Field field);

    Date createDate(String fieldName, Field field);

    LocalDate createLocalDate(String fieldName, Field field);

    LocalDateTime createLocalDateTime(String fieldName, Field field);

    LocalTime createLocalTime(String fieldName, Field field);

    List createList(String fieldName, Field field);

    Set createSet(String fieldName, Field field);

    Map createMap(String fieldName, Field field);

    Optional createOptional(String fieldName, Field field);

    Object createEnum(String fieldName, Class enumClass);

}
