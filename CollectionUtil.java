package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtil {
    public static int sum(Collection<Integer> collection) {
        return collection.stream().mapToInt(Number::intValue).sum();
    }

    public static <T> List<Integer> differenceIndices(List<T> list1, List<T> list2) {
        List<Integer> result = new ArrayList<>();

        if (list1.size() != list2.size()) {
            throw new IllegalArgumentException("Lists must be of the same size");
        }

        for (int i = 0; i < list1.size(); i++) {
            if (!list2.get(i).equals(list1.get(i))) {
                result.add(i);
            }
        }

        return List.copyOf(result);
    }
}
