package de.drippinger.fakegen.filler;

import de.drippinger.fakegen.domain.DomainConfiguration;
import de.drippinger.fakegen.domain.SimpleDomainConfiguration;

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
@SuppressWarnings("unchecked")
public class BasicObjectFiller implements ObjectFiller {

    protected AtomicLong seed;

    protected Random random;

    protected DomainConfiguration domainConfiguration = new SimpleDomainConfiguration();

    public BasicObjectFiller() {
        this.random = new Random();
        this.seed = getFieldValue(random, "seed");

        domainConfiguration.init(random);
    }

    public BasicObjectFiller(Long seed) {
        this.seed = new AtomicLong(seed);
        this.random = new Random(seed);

        domainConfiguration.init(random);
    }

    public BasicObjectFiller(Long seed, DomainConfiguration domainConfiguration) {
        this.seed = new AtomicLong(seed);
        this.random = new Random(seed);
        this.domainConfiguration = domainConfiguration;

        domainConfiguration.init(random);
    }

    public BasicObjectFiller(DomainConfiguration domainConfiguration) {
        this.random = new Random();
        this.seed = getFieldValue(random, "seed");

        this.domainConfiguration = domainConfiguration;
        domainConfiguration.init(random);
    }

    @Override
    public void setDomainConfiguration(DomainConfiguration domainConfiguration) {
        this.domainConfiguration = domainConfiguration;
        domainConfiguration.init(random);
    }

    @Override
    public String getSeed() {
        return seed.toString();
    }

    @Override
    public String createString(String fieldName) {
        return domainConfiguration
                .getSupplierValue(fieldName, String.class)
                .orElseGet(() -> createString(fieldName, 10));
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
        return domainConfiguration
                .getSupplierValue(fieldName, Integer.class)
                .orElseGet(() -> random.nextInt());
    }

    @Override
    public Long createLong(String fieldName) {
        return domainConfiguration
                .getSupplierValue(fieldName, Long.class)
                .orElseGet(() -> random.nextLong());
    }

    @Override
    public Short createShort(String fieldName) {
        return domainConfiguration
                .getSupplierValue(fieldName, Short.class)
                .orElseGet(() -> (short) random.nextInt(Short.MAX_VALUE + 1));
    }

    @Override
    public Double createDouble(String fieldName) {
        return domainConfiguration
                .getSupplierValue(fieldName, Double.class)
                .orElseGet(() -> random.nextDouble());
    }

    @Override
    public Float createFloat(String fieldName) {
        return domainConfiguration
                .getSupplierValue(fieldName, Float.class)
                .orElseGet(() -> random.nextFloat());
    }

    @Override
    public BigInteger createBitInteger(String fieldName) {
        return domainConfiguration
                .getSupplierValue(fieldName, BigInteger.class)
                .orElseGet(() -> BigInteger.valueOf(random.nextLong()));
    }

    @Override
    public Boolean createBoolean(String fieldName) {
        return domainConfiguration
                .getSupplierValue(fieldName, Boolean.class)
                .orElseGet(() -> random.nextBoolean());
    }

    @Override
    public Date createDate(String fieldName) {
        return domainConfiguration
                .getSupplierValue(fieldName, Date.class)
                .orElseGet(Date::new);
    }

    @Override
    public LocalDate createLocalDate(String fieldName) {
        return domainConfiguration
                .getSupplierValue(fieldName, LocalDate.class)
                .orElseGet(LocalDate::now);
    }

    @Override
    public LocalDateTime createLocalDateTime(String fieldName) {
        return domainConfiguration
                .getSupplierValue(fieldName, LocalDateTime.class)
                .orElseGet(LocalDateTime::now);
    }

    @Override
    public LocalTime createLocalTime(String fieldName) {
        return domainConfiguration
                .getSupplierValue(fieldName, LocalTime.class)
                .orElseGet(LocalTime::now);
    }

    @Override
    public List createList(String fieldName) {
        return domainConfiguration
                .getSupplierValue(fieldName, List.class)
                .orElseGet(ArrayList::new);
    }

    @Override
    public Set createSet(String fieldName) {
        return domainConfiguration
                .getSupplierValue(fieldName, Set.class)
                .orElseGet(HashSet::new);
    }

    @Override
    public Map createMap(String fieldName) {
        return domainConfiguration
                .getSupplierValue(fieldName, Map.class)
                .orElseGet(HashMap::new);
    }

    @Override
    public Object createEnum(String fieldName, Class enumClass) {
        return domainConfiguration
                .getSupplierValue(fieldName, enumClass)
                .orElseGet(() -> {
                    Object[] enumValues = enumClass.getEnumConstants();
                    if (enumValues.length > 0) {
                        return enumValues[random.nextInt(enumValues.length)];
                    }
                    return null;
                });
    }

}
