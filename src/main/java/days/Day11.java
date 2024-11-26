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
        List<Integer> emptyRowsIndexes = new ArrayList<>();
        List<Integer> emptyColumnsIndexes = new ArrayList<>();

        for (int i = 0; i < image.getRows().size(); i++) {
            if (image.getRow(i).stream().allMatch(c -> c.data().equals("."))) {
                emptyRowsIndexes.add(i);
            }
        }

        for (int i = 0; i < image.getColumns().size(); i++) {
            if (image.getColumn(i).stream().allMatch(c -> c.data().equals("."))) {
                emptyColumnsIndexes.add(i);
            }
        }

        System.out.println(emptyRowsIndexes);
        System.out.println(emptyColumnsIndexes);

        StringBuilder sb = new StringBuilder();

        int nWidth = image.width + emptyColumnsIndexes.size();

        System.out.println("Creating new image");

        for (int y = 0; y < image.getRows().size(); y++) {
            if (emptyRowsIndexes.contains(y)) {
                sb.append(".".repeat(nWidth));
                sb.append("\n");
            }

            for (int x = 0; x < image.getColumns().size(); x++) {
                if (emptyColumnsIndexes.contains(x)) {
                    sb.append(".");
                }

                sb.append(image.getTile(x, y).data());
            }

            sb.append("\n");
        }

        System.out.println("Parsing new image");

        image = Grid.parseGrid(sb.toString());
        Grid<S> grid = image.map(S::new);
        var graph = Grid.gridToGraph(grid);
        var galaxies = grid.getAll().stream().filter(c -> c.data().c.equals("#")).toList();

        System.out.println("Calculating distances for " + galaxies.size() + " galaxies");

        Map<String, Integer> distances = new HashMap<>();

        // cartesian product of galaxies
        for (int i = 0; i < galaxies.size(); i++) {
            for (int j = i + 1; j < galaxies.size(); j++) {
                String s = i + "," + j;
                System.out.println("Calculating distance for " + s);
                if (distances.containsKey(s)) continue;
                var distance = graph.getDistance(galaxies.get(i), galaxies.get(j));
                distances.put(s, distance);
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

    private record S(String c) implements Grid.Weighted {
        @Override
        public Integer getWeight() {
            return 1;
        }

        @Override
        public String toString() {
            return c;
        }
    }
}
