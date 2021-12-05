package util;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.Spliterators;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public record Line(Point a, Point b) {
    final static Pattern LINE_PATTERN = Pattern.compile("(\\d+),(\\d+) -> (\\d+),(\\d+)");

    public Line(int x1, int y1, int x2, int y2) {
        this(new Point(x1, y1), new Point(x2, y2));
    }

    public Line(String x1, String y1, String x2, String y2) {
        this(
                Integer.parseInt(x1),
                Integer.parseInt(y1),
                Integer.parseInt(x2),
                Integer.parseInt(y2)
        );
    }

    public static Line toLine(String s) {
        Matcher m = LINE_PATTERN.matcher(s);
        return m.matches() ? new Line(
                m.group(1),
                m.group(2),
                m.group(3),
                m.group(4)
        ) : null;
    }

    public boolean horizontal() {
        return a.b().equals(b.b());
    }

    public boolean vertical() {
        return a.a().equals(b.a());
    }

    public List<Point> expand() {
        List<Point> points = new ArrayList<>();

        PrimitiveIterator.OfInt xs = Spliterators.iterator((vertical() ? IntStream.generate(a::x) : IntStream.rangeClosed(Math.min(a.x(), b.x()), Math.max(a.x(), b.x()))).spliterator());
        PrimitiveIterator.OfInt ys = Spliterators.iterator((horizontal() ? IntStream.generate(a::y) : IntStream.rangeClosed(Math.min(a.y(), b.y()), Math.max(a.y(), b.y()))).spliterator());

        while (xs.hasNext() && ys.hasNext()) {
            points.add(new Point(xs.nextInt(), ys.nextInt()));
        }

        return points;
    }

    public List<Point> overlap(Line other) {
        List<Point> expand = expand();
        expand.retainAll(other.expand());
        return expand;
    }

    public static final class Point extends Pair<Integer, Integer> {
        public Point(Integer x, Integer y) {
            super(x, y);
        }

        public int x() {
            return a();
        }

        public int y() {
            return b();
        }

        @Override
        public String toString() {
            return "(%d,%d)".formatted(x(), y());
        }
    }
}
