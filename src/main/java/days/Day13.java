package days;

import setup.Day;
import util.CollectionUtil;
import util.Grid;
import util.Line;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day13 extends Day {

    private ArrayList<Grid<String>> grids;
    private Map<Grid<String>, Integer> cache = new HashMap<>();

    @Override
    public void processInput() {
        var split = input.split("(\r?\n){2}");
        grids = new ArrayList<>();

        for (var s : split) {
            grids.add(Grid.parseGrid(s));
        }
    }

    @Override
    public Object part1() {
        long sum = 0;

        for (Grid<String> grid : grids) {
            var s = getSymmetryNumber(grid);

            assert s.size() == 1;
            sum += s.get(0);

            cache.put(grid, s.get(0));
        }

        return sum;
    }

    private List<Integer> getSymmetryNumber(Grid<String> grid) {
        List<Integer> ret = new ArrayList<>();

        var sameColsIndices = new ArrayList<Integer>();
        var sameRowsIndices = new ArrayList<Integer>();

        for (int i = 0; i < grid.width - 1; i++) {
            int j = i + 1;

            var r1 = Grid.stripTileData(grid.getColumn(i));
            var r2 = Grid.stripTileData(grid.getColumn(j));

            if (r1.equals(r2)) {
                sameColsIndices.add(i);
            }
        }

        for (int i = 0; i < grid.height - 1; i++) {
            int j = i + 1;

            var r1 = Grid.stripTileData(grid.getRow(i));
            var r2 = Grid.stripTileData(grid.getRow(j));

            if (r1.equals(r2)) {
                sameRowsIndices.add(i);
            }
        }

        var validCols = new ArrayList<Integer>();
        var validRows = new ArrayList<Integer>();

        for (int sameColsIndex : sameColsIndices) {
            if (hasColSymmetry(grid, sameColsIndex)) {
                validCols.add(sameColsIndex);
            }
        }
        for (int sameRowsIndex : sameRowsIndices) {
            if (hasRowSymmetry(grid, sameRowsIndex)) {
                validRows.add(sameRowsIndex);
            }
        }

        for (Integer validCol : validCols) {
            ret.add(validCol + 1);
        }
        for (Integer validRow : validRows) {
            ret.add((validRow + 1) * 100);
        }

        return ret;
    }

    private List<Line.Point> symmetryErrors(Grid<String> grid, int i, boolean horizontalAxis) {
        List<Line.Point> errors = new ArrayList<>();

        for (int j = i; j >= 0; j--) {
            int k = i + i - j + 1;

            if (horizontalAxis) {
                if (k >= grid.height) {
                    continue;
                }
            } else {
                if (k >= grid.width) {
                    continue;
                }
            }

            var r1 = horizontalAxis ? Grid.stripTileData(grid.getRow(j)) : Grid.stripTileData(grid.getColumn(j));
            var r2 = horizontalAxis ? Grid.stripTileData(grid.getRow(k)) : Grid.stripTileData(grid.getColumn(k));

            var diffIndices = CollectionUtil.differenceIndices(r1, r2);

            for (Integer diffIndex : diffIndices) {
                var p1 = horizontalAxis ? new Line.Point(diffIndex, j) : new Line.Point(j, diffIndex);
                var p2 = horizontalAxis ? new Line.Point(diffIndex, k) : new Line.Point(k, diffIndex);

                errors.add(p1);
                errors.add(p2);
            }
        }

        return errors;
    }

    private boolean hasColSymmetry(Grid<String> grid, int col) {
        return symmetryErrors(grid, col, false).isEmpty();
    }

    private boolean hasRowSymmetry(Grid<String> grid, int row) {
        return symmetryErrors(grid, row, true).isEmpty();
    }

    @Override
    public String partOneSolution() {
        return "29213";
    }

    @Override
    public Object part2() {
        long sum = 0;

        for (Grid<String> grid : grids) {
            List<Line.Point> errors = new ArrayList<>();

            for (int i = 0; i < grid.width; i++) {
                var _errors = symmetryErrors(grid, i, false);
                if (_errors.size() == 2) {
                    errors.addAll(_errors);
                }
            }
            for (int i = 0; i < grid.height; i++) {
                var _errors = symmetryErrors(grid, i, true);
                if (_errors.size() == 2) {
                    errors.addAll(_errors);
                }
            }

            var error = errors.get(0);
            var newGrid = correctError(grid, error.x(), error.y());

            var s = getSymmetryNumber(newGrid);
            s.remove(cache.get(grid));

            assert s.size() == 1;

            sum += s.get(0);
        }

        return sum;
    }

    private Grid<String> correctError(Grid<String> grid, int x, int y) {
        var newGrid = grid.copy();
        var cur = newGrid.getTile(x, y).data();

        newGrid.set(x, y, cur.equals("#") ? "." : "#");
        return newGrid;
    }

    @Override
    public int getDay() {
        return 13;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partTwoSolution() {
        return "37453";
    }
}
