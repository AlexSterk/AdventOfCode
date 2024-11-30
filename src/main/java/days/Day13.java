package days;

import setup.Day;
import static util.Annotations.*;
import util.Grid;

import java.util.ArrayList;
import java.util.List;

import static util.CollectionUtil.differenceIndices;
import static util.Grid.parseGrid;
import static util.Grid.stripTileData;

public class Day13 extends Day {

    private ArrayList<Grid<String>> grids;

    @Override
    public void processInput() {
        var split = input.split("(\r?\n){2}");
        grids = new ArrayList<>();

        for (var s : split) {
            grids.add(parseGrid(s));
        }
    }

    @Solution("29213")
    @Override
    public Object part1() {
        long sum = 0;

        for (Grid<String> grid : grids) {
            var s = getSymmetryNumber(grid, 0);

            assert s.size() == 1;
            sum += s.get(0);
        }

        return sum;
    }

    private List<Integer> getSymmetryNumber(Grid<String> grid, int allowedErrors) {
        List<Integer> ret = new ArrayList<>();

        for (int i = 0; i < grid.width - 1; i++) {
            if (symmetryErrors(grid, i, false) == allowedErrors) {
                ret.add(i + 1);
            }
        }

        for (int i = 0; i < grid.height - 1; i++) {
            if (symmetryErrors(grid, i, true) == allowedErrors) {
                ret.add(i * 100 + 100);
            }
        }

        return ret;
    }

    private int symmetryErrors(Grid<String> grid, int mirrorIndex, boolean horizontalAxis) {
        int errors = 0;

        for (int i = mirrorIndex; i >= 0; i--) {
            int j = mirrorIndex + mirrorIndex - i + 1;

            if (horizontalAxis) {
                if (j >= grid.height) {
                    continue;
                }
            } else {
                if (j >= grid.width) {
                    continue;
                }
            }

            var r1 = horizontalAxis ? stripTileData(grid.getRow(i)) : stripTileData(grid.getColumn(i));
            var r2 = horizontalAxis ? stripTileData(grid.getRow(j)) : stripTileData(grid.getColumn(j));

            errors += differenceIndices(r1, r2).size();
        }

        return errors;
    }

    @Solution("37453")
    @Override
    public Object part2() {
        long sum = 0;

        for (Grid<String> grid : grids) {
            var s = getSymmetryNumber(grid, 1);

            assert s.size() == 1;
            sum += s.get(0);
        }

        return sum;
    }

    @Override
    public int getDay() {
        return 13;
    }
}
