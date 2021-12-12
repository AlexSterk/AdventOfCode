package days;

import setup.Day;

import java.util.Arrays;
import java.util.List;

public class Day11 extends Day {
    private List<Direction> steps;
    private int max = Integer.MIN_VALUE;

    @Override
    public void processInput() {
        steps = Arrays.stream(input.trim().split(",")).map(s -> switch (s) {
            case "nw" -> Direction.NORTHWEST;
            case "ne" -> Direction.NORTHEAST;
            case "sw" -> Direction.SOUTHWEST;
            case "se" -> Direction.SOUTHEAST;
            case "n" -> Direction.NORTH;
            case "s" -> Direction.SOUTH;
            default -> throw new IllegalStateException(s);
        }).toList();
    }

    @Override
    public Object part1() {
        int q , r, s;
        q = r = s = 0;
        for (Direction step : steps) {
            q += step.vq;
            r += step.vr;
            s += step.vs;
            max = Math.max(max, distance(q, r, s));
        }
        return distance(q, r, s);
    }

    private int distance(int q, int r, int s) {
        return (Math.abs(q) + Math.abs(r) + Math.abs(s)) / 2;
    }

    @Override
    public Object part2() {
        return max;
    }

    @Override
    public int getDay() {
        return 11;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "670";
    }

    @Override
    public String partTwoSolution() {
        return "1426";
    }

    private enum Direction {
        NORTHEAST(1, -1, 0),
        NORTHWEST(-1, 0, 1),
        SOUTHEAST(1, 0, -1),
        SOUTHWEST(-1, 1, 0),
        NORTH(0, -1, 1),
        SOUTH(0, 1, -1)
        ;


        public final int vq, vr, vs;

        Direction(int vq, int vr, int vs) {
            this.vq = vq;
            this.vr = vr;
            this.vs = vs;
        }
    }
}
