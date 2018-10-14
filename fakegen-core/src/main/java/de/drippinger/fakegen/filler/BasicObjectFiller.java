package de.drippinger.fakegen.filler;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static de.drippinger.fakegen.util.ReflectionUtils.getFieldValue;

/**
 * @author Dennis Rippinger
 */
public class BasicObjectFiller implements ObjectFiller {

    protected AtomicLong seed;

    protected Random random;

    public BasicObjectFiller() {
        this.random = new Random();
        this.seed = getFieldValue(random, "seed");
    }

    public BasicObjectFiller(Long seed) {
        this.seed = new AtomicLong(seed);
        this.random = new Random(seed);
    }

    @Override
    public String getSeed() {
        return seed.toString();
    }

    @Override
    public String createString(String fieldName) {
        return createString(fieldName, 10);
    }

    @Override
    public String createString(String fieldName, int length) {
        int leftLimit = 48; // Letter '0'
        int rightLimit = 122; // Letter 'z'

        StringBuilder buffer = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomLimitedInt = leftLimit + (int) (random.nextDouble() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    @Override
    public Integer createInteger(String fieldName) {
        return random.nextInt();
    }

    @Override
    public Long createLong(String fieldName) {
        return random.nextLong();
    }

    @Override
    public Short createShort(String fieldName) {
        return (short) random.nextInt(Short.MAX_VALUE + 1);
    }

    @Override
    public Double createDouble(String fieldName) {
        return random.nextDouble();
    }

    @Override
    public Float createFloat(String fieldName) {
        return random.nextFloat();
    }

    @Override
    public BigInteger createBitInteger(String fieldName) {
        return BigInteger.valueOf(random.nextLong());
    }

    @Override
    public Boolean createBoolean(String fieldName) {
        return random.nextBoolean();
    }

    @Override
    public Date createDate(String fieldName) {
        return new Date();
    }

    @Override
    public LocalDate createLocalDate(String fieldName) {
        return LocalDate.now();
    }

    @Override
    public LocalDateTime createLocalDateTime(String fieldName) {
        return LocalDateTime.now();
    }

    @Override
    public LocalTime createLocalTime(String fieldName) {
        return LocalTime.now();
    }

    @Override
    public List createList(String fieldName) {
        return new ArrayList();
    }

    @Override
    public Set createSet(String fieldName) {
        return new HashSet();
    }

    @Override
    public Map createMap(String fieldName) {
        return new HashMap();
    }

    @Override
    public Object createEnum(String fieldName, Class enumClass) {
        Object[] enumValues = enumClass.getEnumConstants();
        if (enumValues.length > 0) {
            return enumValues[random.nextInt(enumValues.length)];
        }
        return null;
    }


}
