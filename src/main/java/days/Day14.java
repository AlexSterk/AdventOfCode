package days;

import setup.Day;
import util.Direction;
import util.Grid;

import java.util.ArrayList;
import java.util.List;

public class Day14 extends Day {
    private Grid<String> grid;

    @Override
    public void processInput() {
        grid = Grid.parseGrid(input);
    }

    @Override
    public Object part1() {
        rollGrid(Direction.N);

        return calculateLoad();
    }

    private int calculateLoad() {
        return grid.getAll().stream().filter(t -> t.data().equals("O")).mapToInt(t -> grid.height - t.y()).sum();
    }

    @Override
    public Object part2() {
        List<Grid<String>> grids = new ArrayList<>();

        for (long i = 0; i < 1000000000L; i++) {
            grid = grid.copy();
            cycle();
            if (grids.contains(grid)) {
                var cycleLength = grids.size() - grids.indexOf(grid);
                var remaining = 1000000000L - i - 1;
                var cycleIndex = (int) (remaining % cycleLength);
                grid = grids.get(grids.indexOf(grid) + cycleIndex);
                return calculateLoad();
            }
            grids.add(grid);
        }

        return null;
    }

    private void cycle() {
        rollGrid(Direction.N);
        rollGrid(Direction.W);
        rollGrid(Direction.S);
        rollGrid(Direction.E);
    }

    @Override
    public int getDay() {
        return 14;
    }

    @Override
    public boolean resetForPartTwo() {
        return true;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "109939";
    }

    @Override
    public String partTwoSolution() {
        return "101010";
    }

    private void rollGrid(Direction dir) {
        var movables = movables(dir);
        while (!movables.isEmpty()) {
            for (Grid.Tile<String> movable : movables) {
                var next = grid.getTile(movable.x() + dir.dx, movable.y() + dir.dy);
                grid.set(next, "O");
                grid.set(movable, ".");
            }
            movables = movables(dir);
        }
    }

    private List<Grid.Tile<String>> movables(Direction dir) {
        return grid.getAll().stream().filter(t -> t.data().equals("O")).filter(t -> {
            var next = grid.getTile(t.x() + dir.dx, t.y() + dir.dy);
            return next != null && next.data().equals(".");
        }).toList();
    }
}
