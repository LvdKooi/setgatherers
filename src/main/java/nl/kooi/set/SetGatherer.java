package nl.kooi.set;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SetGatherer {

    private SetGatherer() {
    }

    /**
     * Creates the union of two sets. If duplicate elements are identified based on the provided identifier, they are
     * combined using the specified combiner function.
     *
     * @param set1       the first set
     * @param set2       the second set
     * @param identifier a function that provides a unique identifier for each element
     * @param combiner   a binary operator to resolve duplicates
     * @param <T>        the type of elements in the sets
     * @return a set containing the union of {@code set1} and {@code set2}
     */
    public static <T> Set<T> union(Set<T> set1,
                                   Set<T> set2,
                                   Function<T, ?> identifier,
                                   BinaryOperator<T> combiner) {

        return merge(set1, set2, x -> true, identifier, combiner);
    }

    /**
     * Creates the union of two sets. In case of duplicates, elements from the first set are preferred.
     *
     * @param set1 the first set
     * @param set2 the second set
     * @param <T>  the type of elements in the sets
     * @return a set containing the union of {@code set1} and {@code set2}
     */
    public static <T> Set<T> union(Set<T> set1,
                                   Set<T> set2) {

        return merge(set1, set2, x -> true, Function.identity(), (x1, x2) -> x1);
    }

    /**
     * Creates the intersection of two sets. If duplicate elements are identified based on the provided identifier,
     * they are combined using the specified combiner function.
     *
     * @param set1       the first set
     * @param set2       the second set
     * @param identifier a function that provides a unique identifier for each element
     * @param combiner   a binary operator to resolve duplicates
     * @param <T>        the type of elements in the sets
     * @return a set containing the intersection of {@code set1} and {@code set2}
     */
    public static <T> Set<T> intersect(Set<T> set1,
                                       Set<T> set2,
                                       Function<T, ?> identifier,
                                       BinaryOperator<T> combiner) {
        return merge(set1,
                set2,
                containsItemWithId(set1, identifier).and(containsItemWithId(set2, identifier)),
                identifier,
                combiner);
    }

    /**
     * Creates the intersection of two sets. In case of duplicates, elements from the first set are preferred.
     *
     * @param set1 the first set
     * @param set2 the second set
     * @param <T>  the type of elements in the sets
     * @return a set containing the intersection of {@code set1} and {@code set2}
     */
    public static <T> Set<T> intersect(Set<T> set1,
                                       Set<T> set2) {
        return merge(set1,
                set2,
                containsItemWithId(set1, Function.identity()).and(containsItemWithId(set2, Function.identity())),
                Function.identity(),
                (x1, x2) -> x1);
    }


    private static <T> Predicate<T> containsItemWithId(Set<T> set, Function<T, ?> identifier) {
        return item -> set
                .stream()
                .map(identifier)
                .anyMatch(identifier.apply(item)::equals);
    }

    /**
     * Extracts elements from the first set that are not present in the second set, based on a unique identifier.
     *
     * @param set1       the first set
     * @param set2       the second set
     * @param identifier a function that provides a unique identifier for each element
     * @param <T>        the type of elements in the sets
     * @return a set containing elements from {@code set1} not in {@code set2}
     */
    public static <T> Set<T> left(Set<T> set1,
                                  Set<T> set2,
                                  Function<T, ?> identifier) {

        return merge(set1,
                set2,
                containsItemWithId(set1, identifier).and(containsItemWithId(set2, identifier).negate()),
                identifier,
                (x1, x2) -> x1);
    }

    /**
     * Extracts elements from the second set that are not present in the first set, based on a unique identifier.
     *
     * @param set1       the first set
     * @param set2       the second set
     * @param identifier a function that provides a unique identifier for each element
     * @param <T>        the type of elements in the sets
     * @return a set containing elements from {@code set2} not in {@code set1}
     */
    public static <T> Set<T> right(Set<T> set1,
                                   Set<T> set2,
                                   Function<T, ?> identifier) {

        return merge(set1,
                set2,
                containsItemWithId(set1, identifier).negate().and(containsItemWithId(set2, identifier)),
                identifier,
                (x1, x2) -> x1);
    }

    private static <T> Set<T> merge(Set<T> set1,
                                    Set<T> set2,
                                    Predicate<T> condition,
                                    Function<T, ?> identifier,
                                    BinaryOperator<T> combiner) {

        var combined = Stream.of(set1, set2)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .filter(condition)
                .collect(Collectors.toSet());

        return combined
                .stream()
                .collect(Collectors.groupingBy(identifier, Collectors.reducing(combiner)))
                .values()
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }
}
