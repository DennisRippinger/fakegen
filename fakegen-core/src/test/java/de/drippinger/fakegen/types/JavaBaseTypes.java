package de.drippinger.fakegen.types;

import lombok.Data;

import java.math.BigInteger;
import java.time.*;
import java.util.*;

/**
 * @author Dennis Rippinger
 */
@Data
public class JavaBaseTypes {

    private Date someDate;

    private LocalDate someLocalDate;

    private LocalDateTime someLocalDateTime;

    private LocalTime someLocalTime;

    private List someList;

    private Set someSet;

    private Map someMap;

    private BigInteger someBigInteger;

    private Instant someInstant;

    private Period somePeriod;

    private ZonedDateTime someZonedDateTime;

    private Calendar someCalendar;

}
