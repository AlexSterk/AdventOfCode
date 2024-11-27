package util;

import java.util.Collection;

public class CollectionUtil {
    public static int sum(Collection<Integer> collection) {
        return collection.stream().mapToInt(Number::intValue).sum();
    }
}
