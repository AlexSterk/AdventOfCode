package days;

import days.Day10.KnotHash;
import setup.Day;
import util.Graph;
import util.Grid;

import java.util.Set;

public class Day14 extends Day {
    private final String key = input.trim();
    private Grid<Boolean> grid;

    @Override
    public void processInput() {
        grid = new Grid<>(128, 128);
        grid.init(() -> null, false);
    }

    @Override
    public Object part1() {
        for (int y = 0; y < grid.getRows().size(); y++) {
            String hash = KnotHash.getHash(key + "-" + y);
            String bits = KnotHash.getBits(hash);
            for (int x = 0; x < bits.toCharArray().length; x++) {
                grid.set(x, y, bits.charAt(x) == '1');
            }
        }

        return grid.getAll().stream().filter(Grid.Tile::data).count();
    }

    @Override
    public Object part2() {
        Graph<Grid.Tile<?>> graph = new Graph<>();
        grid.getAll().stream().filter(Grid.Tile::data).forEach(graph::addNode);
        grid.getAll().stream().filter(Grid.Tile::data).forEach(t -> {
            Set<Grid.Tile<Boolean>> adjacent = grid.getAdjacent(t, false);
            adjacent.stream().filter(Grid.Tile::data).forEach(n -> graph.addEdge(t, n, 1));
        });

        return graph.floodFill().values().stream().distinct().count();
    }

    @Override
    public int getDay() {
        return 14;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}
