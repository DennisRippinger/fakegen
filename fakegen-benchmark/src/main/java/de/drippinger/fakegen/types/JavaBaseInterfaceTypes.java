package de.drippinger.fakegen.types;

import de.drippinger.fakegen.exception.FakegenException;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.isNull;

/**
 * @author Dennis Rippinger
 */
public interface JavaBaseInterfaceTypes {

    Date getSomeDate();

    LocalDate getSomeLocalDate();

    LocalDateTime getSomeLocalDateTime();

    LocalTime getSomeLocalTime();

    List getSomeList();

    Set getSomeSet();

    Map getSomeMap();

    BigInteger getSomeBigInteger();

    default void hasNoNullValues() {
        if (isNull(getSomeDate()) ||
                isNull(getSomeLocalDate()) ||
                isNull(getSomeLocalDateTime()) ||
                isNull(getSomeLocalTime()) ||
                isNull(getSomeSet()) ||
                isNull(getSomeMap()) ||
                isNull(getSomeBigInteger())) {
            throw new FakegenException("Should not be null");
        }
    }

}
