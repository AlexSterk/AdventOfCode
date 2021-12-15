package days;

import setup.Day;
import util.Graph;
import util.Grid;

public class Day15 extends Day {

    private Graph<Grid.Tile<Int>> graph;
    private Grid<Int> grid;

    @Override
    public void processInput() {
        String[] lines = input.split("\n");
        int w = lines[0].length();
        int h = lines.length;

        grid = new Grid<>(w, h);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                grid.set(x, y, new Int(Character.getNumericValue(lines[y].charAt(x))));
            }
        }

        graph = Grid.gridToGraph(grid);
    }

    @Override
    public Object part1() {
        return graph.getDistance(grid.getTile(0, 0), grid.getTile(grid.width - 1, grid.height - 1));
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 15;
    }

    @Override
    public boolean isTest() {
        return true;
    }

    private record Int(int n) implements Grid.Weighted {

        @Override
        public Integer getWeight() {
            return n;
        }
    }
}
