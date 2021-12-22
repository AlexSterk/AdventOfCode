package days;

import setup.Day;
import util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day24 extends Day {

    private List<Pair<Integer, Integer>> parts;

    private static int strongestBridge(List<Pair<Integer, Integer>> remainingParts, int connecting, int max) {
        int localMax = max;
        for (Pair<Integer, Integer> part : remainingParts) {
            int m = Integer.MIN_VALUE;
            if (part.a() == connecting) {
                ArrayList<Pair<Integer, Integer>> parts = new ArrayList<>(remainingParts);
                parts.remove(part);
                m = strongestBridge(parts, part.b(), max + part.a() + part.b());
            }
            else if (part.b() == connecting) {
                ArrayList<Pair<Integer, Integer>> parts = new ArrayList<>(remainingParts);
                parts.remove(part);
                m = strongestBridge(parts, part.a(), max + part.a() + part.b());
            }
            if (m > localMax) localMax = m;
        }
        return localMax;
    }

    @Override
    public void processInput() {
        parts = Arrays.stream(input.split("\n")).map(s -> {
            String[] split = s.split("/");
            return new Pair<>(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        }).toList();
    }

    @Override
    public Object part1() {
        return strongestBridge(List.copyOf(parts), 0, 0);
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 24;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}
