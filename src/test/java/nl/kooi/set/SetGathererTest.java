package nl.kooi.set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static nl.kooi.set.SetGatherer.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class SetGathererTest {

    private Set<Item> set1;
    private Set<Item> set2;

    @BeforeEach
    void setup() {
        set1 = Set.of(
                new Item(1, "pencil", 10.00),
                new Item(2, "pen", 17.00),
                new Item(2, "pen", 19.00),
                new Item(3, "calculator", 12.00),
                new Item(4, "pouch", 17.00)
        );

        set2 = Set.of(
                new Item(5, "whatever", 10.00),
                new Item(2, "pen", 13.00),
                new Item(2, "pen", 19.00),
                new Item(3, "calculator", 18.00)
        );
    }

    @Test
    @DisplayName("Union of two sets with a combiner: resolve duplicates using lower price")
    void unionSets_withCombiner() {
        var outcome = union(set1, set2, Item::id, (item, item2) -> item.price() > item2.price() ? item2 : item);

        assertThat(outcome)
                .isNotNull()
                .hasSize(5)
                .contains(new Item(1, "pencil", 10.00))
                .contains(new Item(2, "pen", 13.00))
                .contains(new Item(3, "calculator", 12.00))
                .contains(new Item(4, "pouch", 17.00))
                .contains(new Item(5, "whatever", 10.00));
    }

    @Test
    @DisplayName("Union of two sets without a combiner: keep all elements with duplicates included")
    void unionSets_withoutCombiner() {
        var outcome = union(set1, set2);

        assertThat(outcome)
                .isNotNull()
                .hasSize(8)
                .contains(new Item(1, "pencil", 10.00))
                .contains(new Item(2, "pen", 13.00))
                .contains(new Item(2, "pen", 17.00))
                .contains(new Item(2, "pen", 19.00))
                .contains(new Item(3, "calculator", 12.00))
                .contains(new Item(3, "calculator", 18.00))
                .contains(new Item(4, "pouch", 17.00))
                .contains(new Item(5, "whatever", 10.00));
    }

    @Test
    @DisplayName("Intersection of two sets with a combiner: resolve duplicates using lower price")
    void intersectSets_withCombiner() {
        var outcome = intersect(set1, set2, Item::id, (item, item2) -> item.price() > item2.price() ? item2 : item);

        assertThat(outcome).isNotNull()
                .hasSize(2)
                .contains(new Item(2, "pen", 13.00))
                .contains(new Item(3, "calculator", 12.00));
    }

    @Test
    @DisplayName("Intersection of two sets without a combiner: keep only exact duplicates")
    void intersectSets_withoutCombiner() {
        var outcome = intersect(set1, set2);

        assertThat(outcome).isNotNull()
                .hasSize(1)
                .contains(new Item(2, "pen", 19.00));
    }

    @Test
    @DisplayName("Left set difference: elements only in the first set")
    void leftSets() {
        var outcome = left(set1, set2, Item::id);

        assertThat(outcome).isNotNull()
                .hasSize(2)
                .contains(new Item(1, "pencil", 10.00))
                .contains(new Item(4, "pouch", 17.00));
    }

    @Test
    @DisplayName("Right set difference: elements only in the second set")
    void rightTests() {
        var outcome = right(set1, set2, Item::id);

        assertThat(outcome).isNotNull()
                .hasSize(1)
                .contains(new Item(5, "whatever", 10.00));
    }
}

record Item(int id, String name, Double price) {
}
