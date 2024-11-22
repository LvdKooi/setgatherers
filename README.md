# Set Gatherers

Welcome to **Set Gatherers**, a small codebase designed for exploring operations on sets using Java. This project demonstrates how to work with concepts like union, intersection, and set differences while providing a functional and customizable approach.

## Features

This codebase provides several utility methods for working with sets:

- **Union**: Combine two sets, with or without resolving duplicates.
- **Intersection**: Find common elements between two sets, with optional duplicate resolution.
- **Left Difference**: Retrieve elements that are only present in the first set.
- **Right Difference**: Retrieve elements that are only present in the second set.
- **Customizable Behavior**: Use `Function` and `BinaryOperator` for custom identification and merging logic.

### Methods Overview

The key methods implemented in the `SetGatherer` class are:

- `union(Set<T> set1, Set<T> set2, Function<T, ?> identifier, BinaryOperator<T> combiner)`
- `union(Set<T> set1, Set<T> set2)`
- `intersect(Set<T> set1, Set<T> set2, Function<T, ?> identifier, BinaryOperator<T> combiner)`
- `intersect(Set<T> set1, Set<T> set2)`
- `left(Set<T> set1, Set<T> set2, Function<T, ?> identifier)`
- `right(Set<T> set1, Set<T> set2, Function<T, ?> identifier)`

Each method is designed to demonstrate a specific set operation, with options for custom behavior through lambdas or default settings for straightforward use.

## How to Use

### Prerequisites

- **Java 17 or higher** for records and modern Stream API usage.
- A build tool like **Maven** or **Gradle** (optional).

### Running the Code

1. Clone the repository:
2. Open the project in your IDE (e.g., IntelliJ IDEA, Eclipse).
3. Run the `SetGathererTest` class to see the set operations in action.

### Customization

You can extend or modify the code to experiment with different ways of combining and filtering sets. For example:

- Use a custom `BinaryOperator` to define how duplicate elements should be resolved.
- Define complex `Predicate` conditions to filter the sets during merging.

## Example Usage

Here's a quick example from the test cases:

```java
var set1 = Set.of(
    new Item(1, "pencil", 10.00),
    new Item(2, "pen", 17.00)
);

var set2 = Set.of(
    new Item(2, "pen", 13.00),
    new Item(3, "calculator", 18.00)
);

var union = SetGatherer.union(set1, set2, Item::id, (item1, item2) -> item1.price() < item2.price() ? item1 : item2);

System.out.println(union);
// Output: [Item{id=1, name='pencil', price=10.0}, Item{id=2, name='pen', price=13.0}, Item{id=3, name='calculator', price=18.0}]
```