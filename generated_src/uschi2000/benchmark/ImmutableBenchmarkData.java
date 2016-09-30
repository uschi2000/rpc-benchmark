package uschi2000.benchmark;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;
import javax.annotation.Generated;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * Immutable implementation of {@link BenchmarkData}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableBenchmarkData.builder()}.
 */
@SuppressWarnings({"all"})
@ParametersAreNonnullByDefault
@Generated({"Immutables.generator", "BenchmarkData"})
@Immutable
public final class ImmutableBenchmarkData implements BenchmarkData {
  private final ImmutableList<String> strings;
  private final ImmutableList<Integer> ints;

  private ImmutableBenchmarkData(
      ImmutableList<String> strings,
      ImmutableList<Integer> ints) {
    this.strings = strings;
    this.ints = ints;
  }

  /**
   * @return The value of the {@code strings} attribute
   */
  @Override
  public ImmutableList<String> strings() {
    return strings;
  }

  /**
   * @return The value of the {@code ints} attribute
   */
  @Override
  public ImmutableList<Integer> ints() {
    return ints;
  }

  /**
   * Copy the current immutable object with elements that replace the content of {@link BenchmarkData#strings() strings}.
   * @param elements The elements to set
   * @return A modified copy of {@code this} object
   */
  public final ImmutableBenchmarkData withStrings(String... elements) {
    ImmutableList<String> newValue = ImmutableList.copyOf(elements);
    return new ImmutableBenchmarkData(newValue, this.ints);
  }

  /**
   * Copy the current immutable object with elements that replace the content of {@link BenchmarkData#strings() strings}.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param elements An iterable of strings elements to set
   * @return A modified copy of {@code this} object
   */
  public final ImmutableBenchmarkData withStrings(Iterable<String> elements) {
    if (this.strings == elements) return this;
    ImmutableList<String> newValue = ImmutableList.copyOf(elements);
    return new ImmutableBenchmarkData(newValue, this.ints);
  }

  /**
   * Copy the current immutable object with elements that replace the content of {@link BenchmarkData#ints() ints}.
   * @param elements The elements to set
   * @return A modified copy of {@code this} object
   */
  public final ImmutableBenchmarkData withInts(int... elements) {
    ImmutableList<Integer> newValue = ImmutableList.copyOf(Ints.asList(elements));
    return new ImmutableBenchmarkData(this.strings, newValue);
  }

  /**
   * Copy the current immutable object with elements that replace the content of {@link BenchmarkData#ints() ints}.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param elements An iterable of ints elements to set
   * @return A modified copy of {@code this} object
   */
  public final ImmutableBenchmarkData withInts(Iterable<Integer> elements) {
    if (this.ints == elements) return this;
    ImmutableList<Integer> newValue = ImmutableList.copyOf(elements);
    return new ImmutableBenchmarkData(this.strings, newValue);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableBenchmarkData} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another) return true;
    return another instanceof ImmutableBenchmarkData
        && equalTo((ImmutableBenchmarkData) another);
  }

  private boolean equalTo(ImmutableBenchmarkData another) {
    return strings.equals(another.strings)
        && ints.equals(another.ints);
  }

  /**
   * Computes a hash code from attributes: {@code strings}, {@code ints}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 31;
    h = h * 17 + strings.hashCode();
    h = h * 17 + ints.hashCode();
    return h;
  }

  /**
   * Prints the immutable value {@code BenchmarkData} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper("BenchmarkData")
        .omitNullValues()
        .add("strings", strings)
        .add("ints", ints)
        .toString();
  }

  /**
   * Creates an immutable copy of a {@link BenchmarkData} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable BenchmarkData instance
   */
  public static ImmutableBenchmarkData copyOf(BenchmarkData instance) {
    if (instance instanceof ImmutableBenchmarkData) {
      return (ImmutableBenchmarkData) instance;
    }
    return ImmutableBenchmarkData.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableBenchmarkData ImmutableBenchmarkData}.
   * @return A new ImmutableBenchmarkData builder
   */
  public static ImmutableBenchmarkData.Builder builder() {
    return new ImmutableBenchmarkData.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableBenchmarkData ImmutableBenchmarkData}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  @NotThreadSafe
  public static final class Builder {
    private ImmutableList.Builder<String> strings = ImmutableList.builder();
    private ImmutableList.Builder<Integer> ints = ImmutableList.builder();

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code BenchmarkData} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * Collection elements and entries will be added, not replaced.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(BenchmarkData instance) {
      Preconditions.checkNotNull(instance, "instance");
      addAllStrings(instance.strings());
      addAllInts(instance.ints());
      return this;
    }

    /**
     * Adds one element to {@link BenchmarkData#strings() strings} list.
     * @param element A strings element
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder addStrings(String element) {
      this.strings.add(element);
      return this;
    }

    /**
     * Adds elements to {@link BenchmarkData#strings() strings} list.
     * @param elements An array of strings elements
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder addStrings(String... elements) {
      this.strings.add(elements);
      return this;
    }

    /**
     * Sets or replaces all elements for {@link BenchmarkData#strings() strings} list.
     * @param elements An iterable of strings elements
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder strings(Iterable<String> elements) {
      this.strings = ImmutableList.builder();
      return addAllStrings(elements);
    }

    /**
     * Adds elements to {@link BenchmarkData#strings() strings} list.
     * @param elements An iterable of strings elements
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder addAllStrings(Iterable<String> elements) {
      this.strings.addAll(elements);
      return this;
    }

    /**
     * Adds one element to {@link BenchmarkData#ints() ints} list.
     * @param element A ints element
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder addInts(int element) {
      this.ints.add(element);
      return this;
    }

    /**
     * Adds elements to {@link BenchmarkData#ints() ints} list.
     * @param elements An array of ints elements
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder addInts(int... elements) {
      this.ints.addAll(Ints.asList(elements));
      return this;
    }

    /**
     * Sets or replaces all elements for {@link BenchmarkData#ints() ints} list.
     * @param elements An iterable of ints elements
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder ints(Iterable<Integer> elements) {
      this.ints = ImmutableList.builder();
      return addAllInts(elements);
    }

    /**
     * Adds elements to {@link BenchmarkData#ints() ints} list.
     * @param elements An iterable of ints elements
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder addAllInts(Iterable<Integer> elements) {
      this.ints.addAll(elements);
      return this;
    }

    /**
     * Builds a new {@link ImmutableBenchmarkData ImmutableBenchmarkData}.
     * @return An immutable instance of BenchmarkData
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableBenchmarkData build() {
      return new ImmutableBenchmarkData(strings.build(), ints.build());
    }
  }
}
