package days;

import setup.Day;
import util.Grid;

import java.util.*;

import static util.Annotations.*;

//@TestInput
public class Day23 extends Day {

    private Grid<String> grid;

    @Override
    public void processInput() {
        grid = Grid.parseGrid(input);
    }

    @Solution("2278")
    @Override
    public Object part1() {
        var start = grid.getTile(1,0);
        var end = grid.getTile(grid.width - 2, grid.height - 1);

        var paths = getAllPaths(start, end);

        // get max length path
        var maxPath = paths.stream().max(Comparator.comparingInt(Path::length)).orElseThrow();

        return maxPath.length() - 1;
    }

    private List<Path> getAllPaths(Grid.Tile<String> start, Grid.Tile<String> end) {
        List<Path> paths = new ArrayList<>();

        var Q = new LinkedList<Path>();
        Q.add(new Path(List.of(start)));

        while (!Q.isEmpty()) {
            var path = Q.poll();
            var current = path.end();

            if (current == end) {
                paths.add(path);
                continue;
            }

            Set<Grid.Tile<String>> neighbors = neighbors(current, path);
            for (var neighbor : neighbors) {
                var newPath = new Path(new ArrayList<>(path.path));
                newPath.path.add(neighbor);
                Q.add(newPath);
            }
        }

        return paths;
    }

    private Set<Grid.Tile<String>> neighbors(Grid.Tile<String> tile, Path visited) {
        Set<Grid.Tile<String>> ret = new HashSet<>();

        switch (tile.data()) {
            case "v":
                ret.add(grid.getTile(tile.x(), tile.y() + 1));
                break;
            case "^":
                ret.add(grid.getTile(tile.x(), tile.y() - 1));
                break;
            case ">":
                ret.add(grid.getTile(tile.x() + 1, tile.y()));
                break;
            case "<":
                ret.add(grid.getTile(tile.x() - 1, tile.y()));
                break;
            case ".":
                ret.add(grid.getTile(tile.x(), tile.y() + 1));
                ret.add(grid.getTile(tile.x(), tile.y() - 1));
                ret.add(grid.getTile(tile.x() + 1, tile.y()));
                ret.add(grid.getTile(tile.x() - 1, tile.y()));
                break;
        }

        ret.removeIf(t -> t == null || t.data().equals("#") || visited.path.contains(t));
        return ret;
    }

    @Solution("")
    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 23;
    }

    private record Path(List<Grid.Tile<String>> path) {
        public int length() {
            return path.size();
        }

        public Grid.Tile<String> start() {
            return path.getFirst();
        }

        public Grid.Tile<String> end() {
            return path.getLast();
        }
    }
}
