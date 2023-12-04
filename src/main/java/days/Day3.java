package days;

import setup.Day;
import util.Grid;

import java.util.*;
import java.util.stream.Collectors;

public class Day3 extends Day {
    private Grid<String> grid;
    private Set<String> symbols;
    private HashMap<Grid.Tile<String>, Set<Integer>> potentialGears = new HashMap<>();

    @Override
    public void processInput() {
        grid = Grid.parseGrid(this.input);
        symbols = this.grid.getAll().stream().map(Grid.Tile::data).filter(s -> s.matches("\\D")).filter(s -> !s.matches("\\.")).collect(Collectors.toSet());
    }

    @Override
    public Object part1() {
        List<Integer> connectedParts = new ArrayList<>();

        this.grid.getRows().forEach(r -> {
            List<Grid.Tile<String>> current = new ArrayList<>();

            for (Grid.Tile<String> t : r) {
                if (t.data().matches("\\d")) {
                    current.add(t);
                } else {
                    this.checkCurrent(current, connectedParts);
                    current.clear();
                }
            }

            this.checkCurrent(current, connectedParts);
        });

        return connectedParts.stream().mapToInt(Integer::intValue).sum();
    }

    private void checkCurrent(List<Grid.Tile<String>> current, List<Integer> connectedParts) {
        if (current.isEmpty()) return;
        if (this.isConnected(current)) {
            Integer n = this.getNumber(current);
            connectedParts.add(n);
        }
    }

    private Integer getNumber(List<Grid.Tile<String>> number) {
        return Integer.parseInt(number.stream().map(Grid.Tile::data).collect(Collectors.joining()));
    }

    private boolean isConnected(List<Grid.Tile<String>> number) {
        return number.stream()
                .flatMap(tile -> this.grid.getAdjacent(tile, true).stream())
                .filter(tile -> symbols.contains(tile.data()))
                .peek(tile -> {
                    if (tile.data().equals("*")) {
                        var s = potentialGears.getOrDefault(tile, new HashSet<>());
                        s.add(this.getNumber(number));
                        potentialGears.put(tile, s);
                    }
                })
                .count() > 0;
    }

    @Override
    public Object part2() {
        var gears = potentialGears.values().stream().filter(integers -> integers.size() == 2).map(s -> s.stream().toList()).toList();
        long r = 0;

        for (var gear : gears) {
            r += (long) gear.get(0) * gear.get(1);
        }

        return r;
    }

    @Override
    public int getDay() {
        return 3;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "530495";
    }

    @Override
    public String partTwoSolution() {
        return "80253814";
    }
}
