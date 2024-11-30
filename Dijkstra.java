package util;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Dijkstra {
    public static <T> SearchResult<T> shortestPath(
            T start,
            Function<T, Boolean> endCondition,
            Function<T, Collection<T>> neighbors,
            BiFunction<T, T, Integer> cost
    ) {
        var distances = new HashMap<>(Map.of(start, 0));
        var queue = new PriorityQueue<T>(Comparator.comparingInt(distances::get));
        T end = null;

        queue.add(start);

        while (end == null) {
            if (queue.isEmpty()) {
                return new SearchResult<>(start, null, distances);
            }

            var current = queue.poll();
            var currentDistance = distances.get(current);

            end = endCondition.apply(current) ? current : null;

            Collection<T> neighbourTs = neighbors.apply(current);
            neighbourTs.stream()
                    .filter(n -> !distances.containsKey(n)).toList()
                    .forEach(n -> {
                        var newDistance = currentDistance + cost.apply(current, n);
                        distances.put(n, newDistance);
                        queue.add(n);
                    });
        }

        return new SearchResult<>(start, end, distances);
    }

    public static <T> SearchResult<T> shortestPath(
            T start,
            Function<T, Collection<T>> neighbors,
            BiFunction<T, T, Integer> cost
    ) {
        return shortestPath(start, _ -> false, neighbors, cost);
    }

    public record SearchResult<T>(T start, T end, Map<T, Integer> distances) {
        public int get() {
            return distances.get(end);
        }
    }
}
