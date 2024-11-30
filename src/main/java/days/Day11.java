package days;

import setup.Day;
import static util.Annotations.*;
import util.Grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11 extends Day {

    private ArrayList<Integer> emptyRows;
    private ArrayList<Integer> emptyCols;
    private List<Grid.Tile<String>> galaxies;

    private static Map<String, Long> getDistances(List<Grid.Tile<String>> galaxies, ArrayList<Integer> emptyRows, ArrayList<Integer> emptyCols, long d) {
        Map<String, Long> distances = new HashMap<>();

        for (int i = 0; i < galaxies.size(); i++) {
            for (int j = i + 1; j < galaxies.size(); j++) {
                var a = galaxies.get(i);
                var b = galaxies.get(j);

                if (a.equals(b)) {
                    continue;
                }

                var key = "%d,%d".formatted(i, j);
                long distance = Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());

                // add 1 for every empty row or column between the two galaxies
                for (int row : emptyRows) {
                    if (a.y() < row && b.y() > row || a.y() > row && b.y() < row) {
                        distance += d;
                    }
                }
                for (int col : emptyCols) {
                    if (a.x() < col && b.x() > col || a.x() > col && b.x() < col) {
                        distance += d;
                    }
                }

                distances.put(key, distance);
            }
        }
        return distances;
    }

    @Override
    public void processInput() {
        var image = Grid.parseGrid(input);
        emptyRows = new ArrayList<>();
        emptyCols = new ArrayList<>();

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

        galaxies = image.getAll().stream().filter(c -> c.data().equals("#")).toList();
    }

    @Solution("9214785")
    @Override
    public Object part1() {
        long d = 2 - 1;
        Map<String, Long> distances = getDistances(galaxies, emptyRows, emptyCols, d);

        long sum = 0;
        for (long value : distances.values()) {
            sum += value;
        }

        return sum;
    }

    @Solution("613686987427")
    @Override
    public Object part2() {
        long d = 1_000_000 - 1;
        Map<String, Long> distances = getDistances(galaxies, emptyRows, emptyCols, d);

        long sum = 0;
        for (long value : distances.values()) {
            sum += value;
        }

        return sum;
    }

    @Override
    public int getDay() {
        return 11;
    }
}
