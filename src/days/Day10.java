package days;

import setup.Day;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day10 extends Day {

    private List<Point> points;
    private int seconds = 0;

    @Override
    public void processInput() {
        Pattern p = Pattern.compile("\\D*?(-?\\d+)\\D*?(-?\\d+)\\D*?(-?\\d+)\\D*?(-?\\d+)\\D*?");
        points = Arrays.stream(input.split("\n"))
                .map(p::matcher)
                .peek(Matcher::matches)
                .map(m -> new Point(
                        Integer.parseInt(m.group(1).trim()),
                        Integer.parseInt(m.group(2).trim()),
                        Integer.parseInt(m.group(3).trim()),
                        Integer.parseInt(m.group(4).trim())
                )).collect(Collectors.toList());
    }

    void printGrid(Set<Position> positions) {
        int minX = Collections.min(positions, compareX).x;
        int minY = Collections.min(positions, compareY).y;
        int maxX = Collections.max(positions, compareX).x;
        int maxY = Collections.max(positions, compareY).y;

        for (int y = minY; y < maxY +1; y++) {
            for (int x = minX; x < maxX +1; x++) {
                System.out.print(positions.contains(new Position(x, y)) ? "#" : ".");
            }
            System.out.println();
        }
    }

    @Override
    public Object part1() {
        int oldHeight = Integer.MAX_VALUE;
        Set<Position> oldPositions = null;

        while (true) {
            Set<Position> positions = points.stream().map(p -> p.pos).collect(Collectors.toSet());
            int minY = Collections.min(positions, compareY).y;
            int maxY = Collections.max(positions, compareY).y;
            int height = maxY - minY;

            if (oldHeight < height) {
                printGrid(oldPositions);
                break;
            } else {
                oldHeight = height;
                oldPositions = positions;
                points.forEach(Point::move);
                seconds++;
            }
        }
        return null;
    }

    @Override
    public Object part2() {
        return seconds - 1;
    }

    @Override
    public int getDay() {
        return 10;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private static class Point {
        Position pos;
        final int vx, vy;

        Point(int x, int y, int vx, int vy) {
            this.vx = vx;
            this.vy = vy;
            this.pos = new Position(x, y);
        }

        void move() {
            pos = new Position(pos.x + vx, pos.y + vy);
        }

        @Override
        public String toString() {
            return "" + pos;
        }
    }

    private record Position(int x, int y)  {}

    private static final Comparator<Position> compareX = Comparator.comparingInt(o -> o.x);
    private static final Comparator<Position> compareY = Comparator.comparingInt(o -> o.y);
}
