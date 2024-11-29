package util;

import java.util.List;

public class Maths {
    public static long gcd(long a, long b) {
        if (a == 0) return b;
        return gcd(b % a, a);
    }

    public static long lcm(long a, long b) {
        return (a * b) / gcd(a, b);
    }

    public static long shoelaceArea(List<Line.Point> vertices) {
        if (!vertices.getFirst().equals(vertices.getLast())) {
            throw new IllegalArgumentException("Vertices must form a closed polygon");
        }

        long total = 0;
        for (int i = 0; i < vertices.size(); i++) {
            var p1 = vertices.get(i);
            var p2 = vertices.get((i + 1) % vertices.size());
            total += (long) p1.x() * p2.y() - (long) p1.y() * p2.x();
        }
        return Math.abs(total) / 2L;
    }

    public static class PicksTheorem {
        public static long area(long interior, long boundary) {
            return interior + boundary / 2 - 1;
        }

        public static long interiorArea(long area, long boundary) {
            return area - boundary / 2 + 1;
        }
    }
}
