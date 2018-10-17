package de.drippinger.fakegen.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * Immutable implementation of {@link BuilderType}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableBuilderType.builder()}.
 */
@SuppressWarnings({"all"})
@ParametersAreNonnullByDefault
@Generated("org.immutables.processor.ProxyProcessor")
@Immutable
public final class ImmutableBuilderType implements BuilderType {
  private final String someString;
  private final SimpleEnum type;

  private ImmutableBuilderType(String someString, SimpleEnum type) {
    this.someString = someString;
    this.type = type;
  }

  /**
   * @return The value of the {@code someString} attribute
   */
  @Override
  public String someString() {
    return someString;
  }

  /**
   * @return The value of the {@code type} attribute
   */
  @Override
  public SimpleEnum type() {
    return type;
  }

  /**
   * Copy the current immutable object by setting a value for the {@link BuilderType#someString() someString} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for someString
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableBuilderType withSomeString(String value) {
    if (this.someString.equals(value)) return this;
    String newValue = Objects.requireNonNull(value, "someString");
    return new ImmutableBuilderType(newValue, this.type);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link BuilderType#type() type} attribute.
   * A value equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for type
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableBuilderType withType(SimpleEnum value) {
    if (this.type == value) return this;
    SimpleEnum newValue = Objects.requireNonNull(value, "type");
    return new ImmutableBuilderType(this.someString, newValue);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableBuilderType} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another) return true;
    return another instanceof ImmutableBuilderType
        && equalTo((ImmutableBuilderType) another);
  }

  private boolean equalTo(ImmutableBuilderType another) {
    return someString.equals(another.someString)
        && type.equals(another.type);
  }

  /**
   * Computes a hash code from attributes: {@code someString}, {@code type}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 5381;
    h += (h << 5) + someString.hashCode();
    h += (h << 5) + type.hashCode();
    return h;
  }

  /**
   * Prints the immutable value {@code BuilderType} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return "BuilderType{"
        + "someString=" + someString
        + ", type=" + type
        + "}";
  }

  /**
   * Creates an immutable copy of a {@link BuilderType} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable BuilderType instance
   */
  public static ImmutableBuilderType copyOf(BuilderType instance) {
    if (instance instanceof ImmutableBuilderType) {
      return (ImmutableBuilderType) instance;
    }
    return ImmutableBuilderType.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableBuilderType ImmutableBuilderType}.
   * @return A new ImmutableBuilderType builder
   */
  public static ImmutableBuilderType.Builder builder() {
    return new ImmutableBuilderType.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableBuilderType ImmutableBuilderType}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  @NotThreadSafe
  public static final class Builder {
    private static final long INIT_BIT_SOME_STRING = 0x1L;
    private static final long INIT_BIT_TYPE = 0x2L;
    private long initBits = 0x3L;

    private @Nullable String someString;
    private @Nullable SimpleEnum type;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code BuilderType} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(BuilderType instance) {
      Objects.requireNonNull(instance, "instance");
      someString(instance.someString());
      type(instance.type());
      return this;
    }

    /**
     * Initializes the value for the {@link BuilderType#someString() someString} attribute.
     * @param someString The value for someString 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder someString(String someString) {
      this.someString = Objects.requireNonNull(someString, "someString");
      initBits &= ~INIT_BIT_SOME_STRING;
      return this;
    }

    /**
     * Initializes the value for the {@link BuilderType#type() type} attribute.
     * @param type The value for type 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder type(SimpleEnum type) {
      this.type = Objects.requireNonNull(type, "type");
      initBits &= ~INIT_BIT_TYPE;
      return this;
    }

    /**
     * Builds a new {@link ImmutableBuilderType ImmutableBuilderType}.
     * @return An immutable instance of BuilderType
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableBuilderType build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new ImmutableBuilderType(someString, type);
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<>();
      if ((initBits & INIT_BIT_SOME_STRING) != 0) attributes.add("someString");
      if ((initBits & INIT_BIT_TYPE) != 0) attributes.add("type");
      return "Cannot build BuilderType, some of required attributes are not set " + attributes;
    }
  }
}
