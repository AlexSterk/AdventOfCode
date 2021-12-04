package days;

import setup.Day;
import util.Grid;

import java.util.*;
import java.util.stream.Collectors;

public class Day18 extends Day {
    private Grid<Acre> grid;

    private static int getResourceValue(Grid<Acre> grid) {
        int woods, lumberyards;
        woods = (int) grid.getAll().stream().filter(t -> t.data() == Acre.TREE).count();
        lumberyards = (int) grid.getAll().stream().filter(t -> t.data() == Acre.LUMBERYARD).count();
        return woods * lumberyards;
    }

    private static Grid<Acre> runRound(Grid<Acre> grid) {
        Grid<Acre> newState = grid.copy();
        grid.getAll().forEach(t -> {
            Set<Grid.Tile<Acre>> adjacent = grid.getAdjacent(t, true);
            Map<Acre, Integer> surrounding = Arrays.stream(Acre.values()).collect(Collectors.toMap(a -> a, a -> (int) adjacent.stream().filter(x -> x.data() == a).count()));
            newState.set(t, switch (t.data()) {
                case GROUND -> surrounding.get(Acre.TREE) >= 3 ? Acre.TREE : Acre.GROUND;
                case TREE -> surrounding.get(Acre.LUMBERYARD) >= 3 ? Acre.LUMBERYARD : Acre.TREE;
                case LUMBERYARD -> surrounding.get(Acre.LUMBERYARD) >= 1 && surrounding.get(Acre.TREE) >= 1 ? Acre.LUMBERYARD : Acre.GROUND;
            });
        });
        return newState;
    }

    @Override
    public void processInput() {
        String[] lines = input.split("\n");
        grid = new Grid<>(lines[0].length(), lines.length);
        for (int y = 0; y < lines.length; y++) {
            char[] line = lines[y].toCharArray();
            for (int x = 0; x < line.length; x++) {
                grid.set(x, y, switch (line[x]) {
                    case '.' -> Acre.GROUND;
                    case '|' -> Acre.TREE;
                    case '#' -> Acre.LUMBERYARD;
                    default -> throw new IllegalStateException("Unexpected value: " + line[x]);
                });
            }
        }
    }

    @Override
    public Object part1() {
        for (int round = 0; round < 10; round++) {
            grid = runRound(grid);
        }
        return getResourceValue(grid);
    }

    @Override
    public Object part2() {
        processInput();
        List<Grid<Acre>> previous = new ArrayList<>();
        while (!previous.contains(grid)) {
            previous.add(grid);
            grid = runRound(grid);
        }
        int firstOccurence = previous.indexOf(grid);
        int cycle = previous.size() - firstOccurence;
        int a = (1000000000 - firstOccurence) % cycle;
        int b = firstOccurence + a;
        return getResourceValue(previous.get(b));
    }

    @Override
    public int getDay() {
        return 18;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private enum Acre {
        GROUND("."),
        TREE("|"),
        LUMBERYARD("#");

        private final String __str;

        Acre(String str) {
            __str = str;
        }

        @Override
        public String toString() {
            return __str;
        }
    }
}
