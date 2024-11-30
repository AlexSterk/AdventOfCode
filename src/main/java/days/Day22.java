package days;

import setup.Day;
import util.Grid;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static util.Annotations.Solution;
import static util.Annotations.TestInput;

//@TestInput
public class Day22 extends Day {

    private List<Brick> bricks;

    @Override
    public void processInput() {
        bricks = lines().stream().map(Brick::fromString).toList();
    }

    private void simulateFalling() {
        bricks = new ArrayList<>(bricks);
        bricks.sort(Comparator.comparingInt(b -> b.z1));

        var PQ = new PriorityQueue<Brick>(Comparator.comparingInt(b -> b.z1));
        PQ.addAll(bricks);

        while (!PQ.isEmpty()) {
            var brick = PQ.poll();
            if (canMove(brick)) {
                var newBrick = brick.moveDown();
                bricks.set(bricks.indexOf(brick), newBrick);
                PQ.add(newBrick);
            }
        }
    }

    private boolean noneBelow(Brick brick) {
        return bricks.stream().noneMatch(b -> {
            if (b == brick) return false;
            return b.supports(brick);
        });
    }

    private boolean canMove(Brick brick) {
        return brick.z1 > 1 && noneBelow(brick);
    }

    private List<Brick> unstableBricks() {
        return bricks.stream().filter(this::canMove).toList();
    }

    @Solution("424")
    @Override
    public Object part1() {
        simulateFalling();

        Map<Integer, List<Brick>> bricksByZ = new HashMap<>();

        Map<Brick, List<Brick>> supports = new HashMap<>();
        Map<Brick, List<Brick>> supportedBy = new HashMap<>();

        for (Brick brick : bricks) {
            supports.put(brick, new ArrayList<>());
            supportedBy.put(brick, new ArrayList<>());
        }

        for (Brick b1 : bricks) {
            for (Brick b2 : bricks) {
                if (b1 != b2 && b1.supports(b2)) {
                    supports.get(b1).add(b2);
                    supportedBy.get(b2).add(b1);
                }
            }
        }

        int sum = 0;
        for (Brick brick : bricks) {
            if (canBeRemoved(brick, supports, supportedBy)) {
                sum += 1;
            }
        }

        return sum;
    }

    private static boolean canBeRemoved(Brick brick, Map<Brick, List<Brick>> supports, Map<Brick, List<Brick>> supportedBy) {
        // can be removed if it does not support anything
        // or if the bricks it supports, are still supported by other bricks
        // dont actually remove
        return supports.get(brick).isEmpty() || supports.get(brick).stream().allMatch(b -> supportedBy.get(b).size() > 1);
    }

    @Solution("")
    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 22;
    }

    private record Brick(int x1, int x2, int y1, int y2, int z1, int z2) {
        public Brick(String x1, String x2, String y1, String y2, String z1, String z2) {
            this(
                    Integer.parseInt(x1),
                    Integer.parseInt(x2),
                    Integer.parseInt(y1),
                    Integer.parseInt(y2),
                    Integer.parseInt(z1),
                    Integer.parseInt(z2)
            );
        }

        public static Brick fromString(String s) {
            Matcher matcher = Pattern.compile("(\\d+),(\\d+),(\\d+)~(\\d+),(\\d+),(\\d+)").matcher(s);

            if (matcher.matches()) {
                return new Brick(
                        matcher.group(1),
                        matcher.group(4),
                        matcher.group(2),
                        matcher.group(5),
                        matcher.group(3),
                        matcher.group(6)
                );
            }

            throw new IllegalArgumentException("Invalid brick string: " + s);
        }

        public Brick moveDown() {
            return new Brick(x1, x2, y1, y2, z1 - 1, z2 - 1);
        }

        public boolean supports(Brick other) {
            var p1 = new Plane(x1, x2, y1, y2);
            var p2 = new Plane(other.x1, other.x2, other.y1, other.y2);
            return z2+1 == other.z1 && p1.overlaps(p2);
        }
    }

    private record Plane(int x1, int x2, int y1, int y2) {
        public boolean overlaps(Plane other) {
            if (this.x2 < other.x1 || other.x2 < this.x1) return false;
            if (this.y2 < other.y1 || other.y2 < this.y1) return false;
            return true;
        }
    }
}
