package days;

import setup.Day;
import util.Graph;
import util.Grid;
import util.Grid.Tile;

public class Day15 extends Day {

    private Graph<Tile<Int>> graph;
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
        Grid<Int> largerGrid = new Grid<>(grid.width * 5, grid.height * 5);

        for (Tile<Int> tile : grid.getAll()) {
            for (int vy = 0; vy <= 4; vy++) {
                for (int vx = 0; vx <= 4; vx++) {
                    int x = tile.x() + grid.width * vx;
                    int y = tile.y() + grid.height * vy;
                    int n = (tile.data().n() + vx + vy - 1) % 9 + 1;
                    largerGrid.set(x, y, new Int(n));
                }
            }
        }

        var graph = Grid.gridToGraph(largerGrid);

        return graph.getDistance(largerGrid.getTile(0, 0), largerGrid.getTile(largerGrid.width - 1, largerGrid.height - 1));
    }

    @Override
    public int getDay() {
        return 15;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "613";
    }

    @Override
    public String partTwoSolution() {
        return "2899";
    }

    private record Int(int n) implements Grid.Weighted {

        @Override
        public String toString() {
            return Integer.toString(n);
        }

        @Override
        public Integer getWeight() {
            return n;
        }
    }
}
