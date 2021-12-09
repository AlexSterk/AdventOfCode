package days;

import setup.Day;
import util.Grid;
import util.Grid.Tile;

import java.util.*;

public class Day9 extends Day {
    private Grid<Integer> cave;
    private List<Tile<Integer>> lowPoints;

    @Override
    public void processInput() {
        String[] lines = input.split("\n");
        cave = new Grid<>(lines[0].length(), lines.length);
        for (int y = 0; y < lines.length; y++) {
            String line = lines[y];
            for (int x = 0; x < line.length(); x++) {
                cave.set(x, y, Integer.parseInt(line.substring(x, x+1)));
            }
        }
    }

    @Override
    public Object part1() {
        lowPoints = cave.getAll().stream().filter(t -> {
            Set<Tile<Integer>> adjacent = cave.getAdjacent(t, false);
            for (Tile<Integer> adj : adjacent) {
                if (t.data() >= adj.data()) {
                    return false;
                }
            }
            return true;
        }).toList();

        return lowPoints.stream().mapToInt(t -> t.data() + 1).sum();
    }

    @Override
    public Object part2() {
        return lowPoints.stream()
                .map(this::getBasin)
                .map(Set::size)
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .reduce((a, b) -> a * b)
                .get();
    }

    private Set<Tile<Integer>> getBasin(Tile<Integer> lowPoint) {
        Set<Tile<Integer>> s = new HashSet<>();

        Queue<Tile<Integer>> Q = new ArrayDeque<>();
        Q.add(lowPoint);
        while (!Q.isEmpty()) {
            Tile<Integer> poll = Q.poll();
            s.add(poll);
            for (Tile<Integer> adj : cave.getAdjacent(poll, false)) {
                if (adj.data().equals(9) || s.contains(adj)) continue;
                Q.offer(adj);
            }
        }

        return s;
    }

    @Override
    public int getDay() {
        return 9;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "506";
    }

    @Override
    public String partTwoSolution() {
        return "931200";
    }
}
