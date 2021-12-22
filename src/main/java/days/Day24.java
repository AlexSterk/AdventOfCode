package days;

import setup.Day;
import util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day24 extends Day {

    private List<Pair<Integer, Integer>> parts;
    private List<List<Pair<Integer, Integer>>> allBridges;

    private int strongestBridge(List<Pair<Integer, Integer>> current, List<Pair<Integer, Integer>> remainingParts, int connecting, int max) {
        allBridges.add(current);
        int localMax = max;
        for (Pair<Integer, Integer> part : remainingParts) {
            int m = Integer.MIN_VALUE;
            if (part.a() == connecting) {
                ArrayList<Pair<Integer, Integer>> parts = new ArrayList<>(remainingParts);
                parts.remove(part);
                ArrayList<Pair<Integer, Integer>> curUpdate = new ArrayList<>(current);
                curUpdate.add(part);
                m = strongestBridge(curUpdate, parts, part.b(), max + part.a() + part.b());
            }
            else if (part.b() == connecting) {
                ArrayList<Pair<Integer, Integer>> parts = new ArrayList<>(remainingParts);
                parts.remove(part);
                ArrayList<Pair<Integer, Integer>> curUpdate = new ArrayList<>(current);
                curUpdate.add(part);
                m = strongestBridge(curUpdate, parts, part.a(), max + part.a() + part.b());
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
        allBridges = new ArrayList<>();
        return strongestBridge(new ArrayList<>(), List.copyOf(parts), 0, 0);
    }

    @Override
    public Object part2() {
        var maxLength = allBridges.stream().mapToInt(List::size).max().getAsInt();
        return allBridges.stream().filter(l -> l.size() == maxLength).mapToInt(l -> l.stream().mapToInt(p -> p.a() + p.b()).sum()).max().getAsInt();
    }

    @Override
    public String partOneSolution() {
        return "1695";
    }

    @Override
    public String partTwoSolution() {
        return "1673";
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
