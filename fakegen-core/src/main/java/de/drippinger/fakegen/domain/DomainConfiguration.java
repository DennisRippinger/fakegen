package de.drippinger.fakegen.domain;

import de.drippinger.fakegen.TestDataFiller;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigInteger;
import java.time.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static de.drippinger.fakegen.util.ReflectionUtils.mapObjectToPrimitive;

/**
 * @author Dennis Rippinger
 */
public abstract class DomainConfiguration implements ObjectFiller {

    private final Map<DomainTuple, Supplier> domain = new ConcurrentHashMap<>();

    protected final Random random;

    public DomainConfiguration(Random random) {
        this.random = random;
    }

    public abstract void init(Random random, TestDataFiller testDataFiller);


    @SuppressWarnings("unchecked")
    protected <T> void fieldForClassShouldUse(String fieldName, Class<T> clazz, Supplier<T> supplier) {
        Class primitiveTypeOrOriginal = mapObjectToPrimitive(clazz)
                .orElse(clazz);

        domain.put(new DomainTuple<>(fieldName, primitiveTypeOrOriginal), supplier);
    }

    protected void fieldForStringShouldUse(String fieldName, Supplier<String> supplier) {
        domain.put(new DomainTuple<>(fieldName, String.class), supplier);
    }

    protected <T> Optional<T> getSupplierValue(String fieldName, Class<T> clazz) {
        return getSupplier(fieldName, clazz)
                .map(Supplier::get);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<Supplier<T>> getSupplier(String fieldName, Class<T> clazz) {
        Class primitiveTypeOrOriginal = mapObjectToPrimitive(clazz)
                .orElse(clazz);

        return Optional.ofNullable(domain
                .get(new DomainTuple(fieldName, primitiveTypeOrOriginal)));
    }

    @Override
    public String createString(String fieldName) {
        return getSupplierValue(fieldName, String.class)
                .orElseGet(() -> createString(fieldName, 10));
    }

    @Override
    public String createString(String fieldName, int length) {
        // Letter '0' to Letter 'z'
        return RandomStringUtils.random(length, 48, 122, true, true, null, random);
    }

    @Override
    public Integer createInteger(String fieldName) {
        return getSupplierValue(fieldName, Integer.class)
                .orElseGet(random::nextInt);
    }

    @Override
    public Long createLong(String fieldName) {
        return getSupplierValue(fieldName, Long.class)
                .orElseGet(random::nextLong);
    }

    @Override
    public Short createShort(String fieldName) {
        return getSupplierValue(fieldName, Short.class)
                .orElseGet(() -> (short) random.nextInt(Short.MAX_VALUE + 1));
    }

    @Override
    public Double createDouble(String fieldName) {
        return getSupplierValue(fieldName, Double.class)
                .orElseGet(random::nextDouble);
    }

    @Override
    public Float createFloat(String fieldName) {
        return getSupplierValue(fieldName, Float.class)
                .orElseGet(random::nextFloat);
    }

    @Override
    public BigInteger createBitInteger(String fieldName) {
        return getSupplierValue(fieldName, BigInteger.class)
                .orElseGet(() -> BigInteger.valueOf(random.nextLong()));
    }

    @Override
    public Boolean createBoolean(String fieldName) {
        return getSupplierValue(fieldName, Boolean.class)
                .orElseGet(random::nextBoolean);
    }

    @Override
    public Date createDate(String fieldName) {
        return getSupplierValue(fieldName, Date.class)
                .orElseGet(Date::new);
    }

    @Override
    public LocalDate createLocalDate(String fieldName) {
        return getSupplierValue(fieldName, LocalDate.class)
                .orElseGet(LocalDate::now);
    }

    @Override
    public LocalDateTime createLocalDateTime(String fieldName) {
        return getSupplierValue(fieldName, LocalDateTime.class)
                .orElseGet(LocalDateTime::now);
    }

    @Override
    public LocalTime createLocalTime(String fieldName) {
        return getSupplierValue(fieldName, LocalTime.class)
                .orElseGet(LocalTime::now);
    }

    @Override
    public List createList(String fieldName) {
        return getSupplierValue(fieldName, List.class)
                .orElseGet(ArrayList::new);
    }

    @Override
    public Set createSet(String fieldName) {
        return getSupplierValue(fieldName, Set.class)
                .orElseGet(HashSet::new);
    }

    @Override
    public Map createMap(String fieldName) {
        return getSupplierValue(fieldName, Map.class)
                .orElseGet(HashMap::new);
    }

    @Override
    public Object createEnum(String fieldName, Class enumClass) {
        return getSupplierValue(fieldName, enumClass)
                .orElseGet(() -> {
                    Object[] enumValues = enumClass.getEnumConstants();
                    if (enumValues.length > 0) {
                        return enumValues[random.nextInt(enumValues.length)];
                    }
                    return null;
                });
    }

    public Byte createByte(String fieldName) {
        return getSupplierValue(fieldName, Byte.class)
                .orElseGet(() -> {
                    byte[] bytes = new byte[1];
                    random.nextBytes(bytes);

                    return bytes[0];
                });
    }

    public Character createChar(String fieldName) {
        return getSupplierValue(fieldName, Character.class)
                .orElseGet(() -> (char) random.nextInt(126));
    }

    public Instant createInstant(String fieldName) {
        return getSupplierValue(fieldName, Instant.class)
                .orElseGet(Instant::now);
    }

    public Period createPeriod(String fieldName) {
        return getSupplierValue(fieldName, Period.class)
                .orElseGet(() -> Period.ofDays(random.nextInt(7)));
    }

    public ZonedDateTime createZonedDateTime(String fieldName) {
        return getSupplierValue(fieldName, ZonedDateTime.class)
                .orElseGet(ZonedDateTime::now);
    }

    public Calendar createCalendar(String fieldName) {
        return getSupplierValue(fieldName, Calendar.class)
                .orElseGet(Calendar::getInstance);
    }

}

