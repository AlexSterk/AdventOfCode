package days;

import setup.Day;
import util.Grid;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Day21 extends Day {
    private Map<Grid<Character>, Grid<Character>> rules;

    private static final String start =
            """
            .#.
            ..#
            ###
            """;

    private Grid<Character> grid;

    @Override
    public void processInput() {
        String[] sd = start.split("\n");
        grid = new Grid<>(sd[0].length(), sd.length);
        for (int y = 0; y < sd.length; y++) {
            char[] chars = sd[y].toCharArray();
            for (int x = 0; x < chars.length; x++) {
                grid.set(x, y, chars[x]);
            }
        }

        rules = new HashMap<>();
        for (String s : input.split("\n")) {
            String[] a = s.split(" => ");
            String _from = a[0];
            String _to = a[1];

            String[] from = _from.split("/");
            Grid<Character> fromGrid = new Grid<>(from.length, from.length);
            for (int y = 0; y < from.length; y++) {
                for (int x = 0; x < from[y].length(); x++) {
                    fromGrid.set(x, y, from[y].charAt(x));
                }
            }

            String[] to = _to.split("/");
            Grid<Character> toGrid = new Grid<>(to.length, to.length);
            for (int y = 0; y < to.length; y++) {
                for (int x = 0; x < to[y].length(); x++) {
                    toGrid.set(x, y, to[y].charAt(x));
                }
            }

            rules.put(fromGrid, toGrid);
        }
    }

    @Override
    public Object part1() {
        for (int i = 0; i < 5; i++) {
            grid = iteration(grid);
        }
        System.out.println(grid);

        return grid.getAll().stream().filter(t -> t.data() == '#').count();
    }

    private Grid<Character> iteration(Grid<Character> grid) {
        if (grid.width % 2 == 0) {
            Grid<Character> newGrid = new Grid<>(grid.width / 2 * 3, grid.width / 2 * 3);
            for (int y = 0; y < grid.width / 2; y++) {
                for (int x = 0; x < grid.width / 2; x++) {
                    Grid<Character> subgrid = grid.subgrid(x * 2, x * 2 + 1, y * 2, y * 2 + 1);
                    Grid<Character> match = subgrid.allVariations().stream().map(rules::get).filter(Objects::nonNull).findFirst().get();
                    int finalX = x;
                    int finalY = y;
                    match.getAll().forEach(t -> newGrid.set(t.x() + finalX *3, t.y() + finalY *3, t.data()));
                }
            }
            return newGrid;
        }
        else if (grid.width % 3 == 0) {
            Grid<Character> newGrid = new Grid<>(grid.width / 3 * 4, grid.width / 3 * 4);
            for (int y = 0; y < grid.width / 3; y++) {
                for (int x = 0; x < grid.width / 3; x++) {
                    Grid<Character> subgrid = grid.subgrid(x * 3, x * 3 + 2, y * 3, y * 3 + 2);
                    Grid<Character> match = subgrid.allVariations().stream().map(rules::get).filter(Objects::nonNull).findFirst().get();
                    int finalX = x;
                    int finalY = y;
                    for (Grid.Tile<Character> t : match.getAll()) {
                        newGrid.set(t.x() + finalX * 4, t.y() + finalY * 4, t.data());
                    }
                }
            }
            return newGrid;
        }
        return null;
    }

    @Override
    public boolean resetForPartTwo() {
        return true;
    }

    @Override
    public Object part2() {
        for (int i = 0; i < 18; i++) {
            grid = iteration(grid);
        }

        return grid.getAll().stream().filter(t -> t.data() == '#').count();
    }

    @Override
    public int getDay() {
        return 21;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "160";
    }

    @Override
    public String partTwoSolution() {
        return "2271537";
    }
}
