package days;

import setup.Day;
import util.Direction;
import util.Line.Point;

import java.util.*;
import java.util.stream.Collectors;

public class Day24 extends Day {
    private static int maxX;
    private static int maxY;
    private List<Blizzard> blizzards;

    @Override
    public void processInput() {
        maxX = lines().get(0).length() - 1;
        maxY = lines().size() - 1;
        blizzards = new ArrayList<>();
        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {
                switch (lines().get(y).charAt(x)) {
                    case '^' -> blizzards.add(new Blizzard(new Point(x, y), Direction.N));
                    case 'v' -> blizzards.add(new Blizzard(new Point(x, y), Direction.S));
                    case '>' -> blizzards.add(new Blizzard(new Point(x, y), Direction.E));
                    case '<' -> blizzards.add(new Blizzard(new Point(x, y), Direction.W));
                }
            }
        }
    }

    @Override
    public Object part1() {
        var start = new Point(1, 0);
        var end = new Point(maxX - 1, maxY);

        return run(start, end, 1);
    }

    @Override
    public Object part2() {
        var start = new Point(1, 0);
        var end = new Point(maxX - 1, maxY);
        int t = 1;

        var run1 = run(start, end, t);
        var run2 = run(end, start, run1 + 1);
        var run3 = run(start, end, run2 + 1);

        return run3;
    }

    private int run(Point start, Point end, int t) {
        Queue<Point> positions = new LinkedList<>();
        positions.add(start);
        while (true) {
            Set<Point> nextPoints = new HashSet<>();
            int finalT = t;
            Set<Point> blocked = blizzards.stream().map(b -> b.positionAtTime(finalT)).collect(Collectors.toSet());
            while (!positions.isEmpty()) {
                var point = positions.poll();
                for (Direction value : Direction.CARDINAL) {
                    var next = point.add(new Point(value.dx, value.dy));
                    if (next.equals(end)) {
                        return t;
                    }
                    if (next.x() > 0 && next.x() < maxX && next.y() > 0 && next.y() < maxY) {
                        if (!blocked.contains(next)) {
                            nextPoints.add(next);
                        }
                    }
                }
                if (!blocked.contains(point)) {
                    nextPoints.add(point);
                }
            }
            positions.addAll(nextPoints);
            t++;
        }
    }

    @Override
    public int getDay() {
        return 24;
    }

    @Override
    public String partOneSolution() {
        return "251";
    }

    @Override
    public String partTwoSolution() {
        return "758";
    }

    @Override
    public boolean isTest() {
        return false;
    }



    private record Blizzard(Point position, Direction facing) {
        private static int modulo(int a, int b) {
            return (a % b + b) % b;
        }

        private Point positionAtTime(int t) {
            return new Point(1 + modulo(position.x() - 1 + facing.dx * t, maxX - 1), 1 + modulo(position.y() - 1 + facing.dy * t, maxY - 1));
        }
    }
}
