package util;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class BinarySearch<T> {
    private final BiFunction<T, T, T> mid;
    private final Function<T, T> increment;

    public BinarySearch(BiFunction<T, T, T> mid, Function<T, T> increment) {
        this.mid = mid;
        this.increment = increment;
    }

    public T search(T low, T high, Predicate<T> predicate) {
        while (!low.equals(high)) {
            T mid = this.mid.apply(low, high);
            if (predicate.test(mid)) {
                high = mid;
            } else {
                low = increment.apply(mid);
            }
        }
        return low;
    }

    public static final BinarySearch<Long> Long = new BinarySearch<>((l, h) -> (l + h) / 2, l -> l + 1);
}
