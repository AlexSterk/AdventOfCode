package days;

import setup.Day;
import util.Graph;
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

        return maxPath.length();
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

    @Solution("6734")
    @Override
    public Object part2() {
        Graph<Grid.Tile<String>> graph = Grid.gridToGraph(grid, (_, _) -> 1);
        graph.removeNodeIf(t -> t.data().equals("#"));

        System.out.printf("Nodes before contraction: %d\n", graph.nodes().size());

        // contract edges in nodes with only 2 neighbors
        while (true) {
            boolean changed = false;
            for (var node : graph.nodes()) {
                if (graph.getNeighbours(node).size() == 2) {
                    var neighbors = new ArrayList<>(graph.getNeighbours(node));
                    var n1 = neighbors.get(0);
                    var n2 = neighbors.get(1);

                    graph.addEdge(n1, n2, graph.getWeight(n1, node) + graph.getWeight(node, n2));
                    graph.removeNode(node);
                    changed = true;
                    break;
                }
            }

            if (!changed) break;
        }

        System.out.printf("Nodes after contraction: %d\n", graph.nodes().size());

        var start = grid.getTile(1,0);
        var end = grid.getTile(grid.width - 2, grid.height - 1);

        var paths = getAllPaths(graph, start, end);

        return paths.stream().mapToInt(Path::length).max().orElseThrow() - 1;
    }

    private static List<Path> getAllPaths(Graph<Grid.Tile<String>> graph, Grid.Tile<String> start, Grid.Tile<String> end) {
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

            Set<Grid.Tile<String>> neighbors = graph.getNeighbours(current);
            for (var neighbor : neighbors) {
                if (path.path.contains(neighbor)) continue;
                var newPath = new Path(new ArrayList<>(path.path), path.length + graph.getWeight(current, neighbor));
                newPath.path.add(neighbor);
                Q.add(newPath);
            }
        }

        return paths;
    }

    @Override
    public int getDay() {
        return 23;
    }

    private record Path(List<Grid.Tile<String>> path, int length) {
        public Path(List<Grid.Tile<String>> path) {
            this(path, path.size());
        }

        public Grid.Tile<String> start() {
            return path.getFirst();
        }

        public Grid.Tile<String> end() {
            return path.getLast();
        }
    }
}
