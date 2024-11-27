package days;

import setup.Day;
import util.Grid;

import java.util.List;

public class Day14 extends Day {
    private Grid<String> grid;

    @Override
    public void processInput() {
        grid = Grid.parseGrid(input);
    }

    @Override
    public Object part1() {

        rollGridNorth();

        return calculateLoad();
    }

    private int calculateLoad() {
        return grid.getAll().stream().filter(t -> t.data().equals("O")).mapToInt(t -> grid.height - t.y()).sum();
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 14;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "109939";
    }

    private void rollGridNorth() {
        while (!isStable()) {
            for (Grid.Tile<String> movable : movables()) {
                var up = movable.up();
                grid.set(up, "O");
                grid.set(movable, ".");
            }
        }
    }

    private List<Grid.Tile<String>> movables() {
        return grid.getAll().stream().filter(t -> t.data().equals("O")).filter(t -> {
            var up = t.up();
            return up != null && up.data().equals(".");
        }).toList();
    }

    private boolean isStable() {
        return movables().isEmpty();
    }
}
