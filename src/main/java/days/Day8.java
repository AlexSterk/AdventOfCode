package days;

import setup.Day;
import util.Grid;

import java.util.Collections;

public class Day8 extends Day {

    private Grid<Integer> grid;

    @Override
    public void processInput() {
        grid = Grid.parseGrid(input).map(Integer::parseInt);
    }

    @Override
    public Object part1() {
        return grid.getAll().stream().mapToInt(tree -> isVisible(tree) ? 1 : 0).sum();
    }

    @Override
    public Object part2() {
        return grid.getAll().stream().mapToInt(this::scenicScore).max().orElseThrow();
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

    private int scenicScore(Grid.Tile<Integer> tree) {
        var up = grid.getColumn(tree.x()).subList(0, tree.y());
        var right = grid.getRow(tree.y()).subList(tree.x() + 1, grid.width);
        var down = grid.getColumn(tree.x()).subList(tree.y() + 1, grid.height);
        var left = grid.getRow(tree.y()).subList(0, tree.x());

        Collections.reverse(up);
        Collections.reverse(left);

        var upT = up.stream().filter(t -> t.data() >= tree.data()).findFirst();
        var rightT = right.stream().filter(t -> t.data() >= tree.data()).findFirst();
        var downT = down.stream().filter(t -> t.data() >= tree.data()).findFirst();
        var leftT = left.stream().filter(t -> t.data() >= tree.data()).findFirst();

        int upScore = upT.map(integerTile -> tree.y() - integerTile.y()).orElseGet(tree::y);
        int rightScore = rightT.map(integerTile -> integerTile.x() - tree.x()).orElseGet(() -> grid.width - tree.x() - 1);
        int downScore = downT.map(integerTile -> integerTile.y() - tree.y()).orElseGet(() -> grid.height - tree.y() - 1);
        int leftScore = leftT.map(integerTile -> tree.x() - integerTile.x()).orElseGet(tree::x);

        return upScore * rightScore * downScore * leftScore;
    }

    @Override
    public String partOneSolution() {
        return "1859";
    }

    @Override
    public String partTwoSolution() {
        return "332640";
    }
}
