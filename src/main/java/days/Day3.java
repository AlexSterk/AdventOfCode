package days;

import setup.Day;
import util.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day3 extends Day {
    private Grid<String> grid;
    private Set<String> symbols;

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
        return number.stream().flatMap(tile -> this.grid.getAdjacent(tile, true).stream()).anyMatch(tile -> symbols.contains(tile.data()));
    }

    @Override
    public Object part2() {
        return null;
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
}
