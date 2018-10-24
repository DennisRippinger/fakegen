package de.drippinger.fakegen.domain;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Dennis Rippinger
 */
public interface ObjectFiller {

    String createString(String fieldName);

    String createString(String fieldName, int length);

    Integer createInteger(String fieldName);

    Long createLong(String fieldName);

    Short createShort(String fieldName);

    Double createDouble(String fieldName);

    Float createFloat(String fieldName);

    BigInteger createBitInteger(String fieldName);

    Boolean createBoolean(String fieldName);

    Date createDate(String fieldName);

    LocalDate createLocalDate(String fieldName);

    LocalDateTime createLocalDateTime(String fieldName);

    LocalTime createLocalTime(String fieldName);

    List createList(String fieldName);

    Set createSet(String fieldName);

    Map createMap(String fieldName);

    Object createEnum(String fieldName, Class enumClass);

}
