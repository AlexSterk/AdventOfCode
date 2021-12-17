package days;

import setup.Day;
import util.Pair;

import java.util.*;

public class Day20 extends Day {

    private List<Point> points;

    @Override
    public void processInput() {
        points = Arrays.stream(input.split("\n")).map(Point::Point).toList();
    }

    @Override
    public Object part1() {
        Map<Point, Integer> closest = new HashMap<>();
        Vector3 zero = new Vector3(0, 0, 0);
        for (int i = 0; i < 1000; i++) {
            points.stream()
                    .peek(Point::update)
                    .map(p -> new Pair<>(p, p.position.distance(zero)))
                    .min(Comparator.comparing(Pair::b))
                    .ifPresent(p -> closest.merge(p.a(), 1, Integer::sum));
        }

        int max = Collections.max(closest.values());
        for (Map.Entry<Point, Integer> pointIntegerEntry : closest.entrySet()) {
            if (pointIntegerEntry.getValue() == max) return points.indexOf(pointIntegerEntry.getKey());
        }
        throw new RuntimeException();
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 20;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private static class Point {
        private Vector3 position, velocity, acceleration;

        private Point(Vector3 position, Vector3 velocity, Vector3 acceleration) {
            this.position = position;
            this.velocity = velocity;
            this.acceleration = acceleration;
        }

        private void update() {
            velocity = velocity.add(acceleration);
            position = position.add(velocity);
        }

        private static Point Point(String s) {
            String[] split = s.split(", ");
            return new Point(Vector3.Vector3(split[0]), Vector3.Vector3(split[1]), Vector3.Vector3(split[2]));
        }

        @Override
        public String toString() {
            return
                    "Point{" +
                    "p=" + position +
                    ", v=" + velocity +
                    ", a=" + acceleration +
                    '}';
        }
    }

    private record Vector3(int x, int y, int z) {
        private static Vector3 Vector3(String s) {
            s = s.substring(3, s.length() - 1);
            String[] nums = s.split(",");

            return new Vector3(Integer.parseInt(nums[0]), Integer.parseInt(nums[1]), Integer.parseInt(nums[2]));
        }

        private Vector3 add(Vector3 o) {
            return new Vector3(
                    x + o.x,
                    y + o.y,
                    z + o.z
            );
        }

        private int distance(Vector3 o) {
            return Math.abs(x - o.x) + Math.abs(y - o.y) + Math.abs(z - o.z);
        }

        @Override
        public String toString() {
            return "<%d,%d,%d>".formatted(x, y, z);
        }
    }
}
