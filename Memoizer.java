package util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Memoizer <A,V> {
    private final Function<A,V> function;
    private final Map<A,V> cache = new HashMap<>();

    public Memoizer(Function<A,V> function) {
        this.function = function;
    }

    public V apply(A arg) {
        if (!cache.containsKey(arg)) {
            cache.put(arg, function.apply(arg));
        }

        return cache.get(arg);
    }

    public static <A,V> Memoizer<A,V> memoize(Function<A,V> function) {
        return new Memoizer<>(function);
    }
}
