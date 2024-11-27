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
        return cache.computeIfAbsent(arg, function);
    }
}
