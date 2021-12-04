package days;

import setup.Day;

import java.util.*;
import java.util.stream.Collectors;

public class Day6 extends Day {
    private static final List<Character> ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".chars().mapToObj(c -> (char) c).toList();

    private int maxX, maxY;
    private List<Point> points;

    @Override
    public void processInput() {
        points = Arrays.stream(input.split("\n")).map(line -> {
            var s = line.split(", ");
            return new Point(s[0], s[1]);
        }).collect(Collectors.toList());

        maxX = points.stream().max(Comparator.comparing(Point::x)).get().x;
        maxY = points.stream().max(Comparator.comparing(Point::y)).get().y;
    }

    @Override
    public Object part1() {
        if (points.size() <= ALPHABET.size())
            for (int i = 0; i < points.size(); i++) System.out.printf("%s: %c\n", points.get(i), ALPHABET.get(i));

        var areas = new HashMap<Point, Integer>();
        var inf = new HashSet<Point>();

        for (int y = 0; y < maxY + 2; y++) {
            for (int x = 0; x < maxX + 2; x++) {
                var dist = new HashMap<Integer, List<Point>>();
                for (Point point : points) {
                    dist.merge(new Point(x, y).distanceTo(point), List.of(point), (a, b) -> {
                        var s = new ArrayList<>(a);
                        s.addAll(b);
                        return s;
                    });
                }
                Map.Entry<Integer, List<Point>> min = Collections.min(dist.entrySet(), Map.Entry.comparingByKey());
                if (min.getValue().size() == 1) {
                    Point e = min.getValue().get(0);
                    if (points.size() <= ALPHABET.size())
                        System.out.print(min.getKey() == 0 ? ALPHABET.get(points.indexOf(e)) : Character.toLowerCase(ALPHABET.get(points.indexOf(e))));
                    areas.merge(e, 1, Integer::sum);
                    if (x == 0 || x == maxX + 1 || y == 0 || y == maxY + 1) inf.add(e);
                } else if (points.size() <= ALPHABET.size()) System.out.print(".");
            }
            if (points.size() <= ALPHABET.size()) System.out.println();
        }

        areas.keySet().removeIf(inf::contains);
        return Collections.max(areas.entrySet(), Map.Entry.comparingByValue()).getValue();
    }

    @Override
    public Object part2() {
        List<Point> locations = new ArrayList<>();
        for (int y = 0; y < maxY + 2; y++) {
            for (int x = 0; x < maxX + 2; x++) {
                final Point p = new Point(x, y);
                int sum = points.stream().mapToInt(p::distanceTo).sum();
                if (sum < 10000) locations.add(p);
            }
        }
        return locations.size();
    }

    @Override
    public int getDay() {
        return 6;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private record Point(int x, int y) {
        public Point(String x, String y) {
            this(Integer.parseInt(x), Integer.parseInt(y));
        }

        public int distanceTo(Point other) {
            return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
        }
    }
}


