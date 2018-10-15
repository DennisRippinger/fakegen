package de.drippinger.fakegen.domain;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static de.drippinger.fakegen.util.ReflectionUtils.mapObjectToPrimitive;

/**
 * @author Dennis Rippinger
 */
public abstract class DomainConfiguration {

    private final Map<DomainTuple, Supplier> domain = new ConcurrentHashMap<>();

    public abstract void init(Random random);

    @SuppressWarnings("unchecked")
    public <T> void fieldForClassShouldUse(String fieldName, Class<T> clazz, Supplier<T> supplier) {
        Class primitiveTypeOrOriginal = mapObjectToPrimitive(clazz)
                .orElse(clazz);

        domain.put(new DomainTuple<>(fieldName, primitiveTypeOrOriginal), supplier);
    }

    public void fieldForStringShouldUse(String fieldName, Supplier<String> supplier) {
        domain.put(new DomainTuple<>(fieldName, String.class), supplier);
    }

    public <T> Optional<T> getSupplierValue(String fieldName, Class<T> clazz) {
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

}
