package days;

import setup.Day;
import util.Grid;

import java.util.ArrayList;

public class Day13 extends Day {

    private ArrayList<Grid<String>> grids;

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

            if (validRows.size() + validCols.size() != 1) {
                throw new RuntimeException("Invalid grid");
            }

            for (int validCol : validCols) {
                sum += validCol+1;
            }
            for (int validRow : validRows) {
                sum += (validRow+1)* 100L;
            }
        }

        return sum;
    }

    private boolean hasColSymmetry(Grid<String> grid, int col) {
        for (int i = col; i >= 0; i--) {
            int j = col + col - i + 1;

            if (j >= grid.width) {
                continue;
            }

            var c1 = Grid.stripTileData(grid.getColumn(i));
            var c2 = Grid.stripTileData(grid.getColumn(j));

            if (!c1.equals(c2)) {
                return false;
            }
        }

        return true;
    }

    private boolean hasRowSymmetry(Grid<String> grid, int row) {
        for (int i = row; i >= 0; i--) {
            int j = row + row - i + 1;

            if (j >= grid.height) {
                continue;
            }

            var r1 = Grid.stripTileData(grid.getRow(i));
            var r2 = Grid.stripTileData(grid.getRow(j));

            if (!r1.equals(r2)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String partOneSolution() {
        return "29213";
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 13;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}
