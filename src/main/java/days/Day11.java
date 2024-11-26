package days;

import setup.Day;
import util.Grid;
import util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11 extends Day {

    private Grid<String> image;

    @Override
    public void processInput() {
        image = Grid.parseGrid(input);
    }

    @Override
    public Object part1() {
        var emptyRows = new ArrayList<Integer>();
        var emptyCols = new ArrayList<Integer>();

        for (int i = 0; i < image.height; i++) {
            if (image.getRow(i).stream().allMatch(c -> c.data().equals("."))) {
                emptyRows.add(i);
            }
        }

        for (int i = 0; i < image.width; i++) {
            if (image.getColumn(i).stream().allMatch(c -> c.data().equals("."))) {
                emptyCols.add(i);
            }
        }

        var galaxies = image.getAll().stream().filter(c -> c.data().equals("#")).toList();

        Map<String, Integer> distances = new HashMap<>();

        for (int i = 0; i < galaxies.size(); i++) {
            for (int j = i+1; j < galaxies.size(); j++) {
                var a = galaxies.get(i);
                var b = galaxies.get(j);

                if (a.equals(b) ) {
                    continue;
                }

                var key  = "%d,%d".formatted(i,j);
                var distance = Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());

                // add 1 for every empty row or column between the two galaxies
                for (int row : emptyRows) {
                    if (a.y() < row && b.y() > row || a.y() > row && b.y() < row) {
                        distance += 1;
                    }
                }
                for (int col : emptyCols) {
                    if (a.x() < col && b.x() > col || a.x() > col && b.x() < col) {
                        distance += 1;
                    }
                }

                distances.put(key, distance);
            }
        }

        long sum = 0;
        for (Integer value : distances.values()) {
            sum += value;
        }

        return sum;
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 11;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "9214785";
    }
}
