package days;

import setup.Day;
import util.Grid;

public class Day8 extends Day {

    private Grid<Integer> grid;

    @Override
    public void processInput() {
        grid = Grid.mapGrid(Grid.parseGrid(input), Integer::parseInt);
    }

    @Override
    public Object part1() {
        return grid.getAll().stream().mapToInt(tree -> isVisible(tree) ? 1 : 0).sum();
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 8;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private boolean isVisible(Grid.Tile<Integer> tree) {
        var up = grid.getColumn(tree.x()).subList(0, tree.y());
        var right = grid.getRow(tree.y()).subList(tree.x() + 1, grid.width);
        var down = grid.getColumn(tree.x()).subList(tree.y() + 1, grid.height);
        var left = grid.getRow(tree.y()).subList(0, tree.x());

        if (up.isEmpty() || right.isEmpty() || down.isEmpty() || left.isEmpty()) {
            return true;
        }

        return up.stream().allMatch(t -> t.data() < tree.data()) ||
                right.stream().allMatch(t -> t.data() < tree.data()) ||
                down.stream().allMatch(t -> t.data() < tree.data()) ||
                left.stream().allMatch(t -> t.data() < tree.data());
    }

    @Override
    public String partOneSolution() {
        return "1859";
    }
}
