package de.drippinger.fakegen.types;

import de.drippinger.fakegen.exception.FakegenException;
import lombok.Data;

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

    public void hasNoNullValues() {
        if (isNull(someDate) ||
                isNull(someLocalDate) ||
                isNull(someLocalDateTime) ||
                isNull(someLocalTime) ||
                isNull(someSet) ||
                isNull(someMap) ||
                isNull(someBigInteger)) {
            throw new FakegenException("Should not be null");
        }
    }

}
