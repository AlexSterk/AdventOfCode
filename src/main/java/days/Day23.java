package days;

import setup.Day;
import util.Graph;
import util.Grid;

import java.util.*;
import java.util.function.Function;

import static util.Annotations.Solution;
import static util.Annotations.TestInput;

//@TestInput
public class Day23 extends Day {

    private Grid<String> grid;
    private Graph<Grid.Tile<String>> graph;

    @Override
    public void processInput() {
        grid = Grid.parseGrid(input);
        graph = Grid.gridToGraph(grid, (_, _) -> 1);
    }

    @Solution("2278")
    @Override
    public Object part1() {
        var start = grid.getTile(1, 0);
        var end = grid.getTile(grid.width - 2, grid.height - 1);

        graph.removeNodeIf(t -> t.data().equals("#"));

        // contract edges in nodes with only 2 neighbors
        contractEdges(!isPart2());

        var path = longestPath(start, end, (tile) -> this.neighbors(tile, !isPart2()));
        return path.length() - 1;
    }

    @Solution("6734")
    @Override
    public Object part2() {
        return part1();
    }

    private void contractEdges(boolean keepSlopes) {
        System.out.printf("Nodes before contraction: %d\n", graph.nodes().size());

        while (true) {
            boolean changed = false;
            for (var node : graph.nodes()) {
                if (graph.getNeighbours(node).size() == 2) {

                    var neighbors = new ArrayList<>(graph.getNeighbours(node));
                    var n1 = neighbors.get(0);
                    var n2 = neighbors.get(1);

                    // Keep nodes surrounding slopes intact
                    if (keepSlopes && !node.data().equals(".") || !n1.data().equals(".") || !n2.data().equals(".")) {
                        continue;
                    }

                    graph.addEdge(n1, n2, graph.getWeight(n1, node) + graph.getWeight(node, n2));
                    graph.removeNode(node);
                    changed = true;
                    break;
                }
            }

            if (!changed) break;
        }
        System.out.printf("Nodes after contraction: %d\n", graph.nodes().size());

    }

    private Path longestPath(Grid.Tile<String> start, Grid.Tile<String> end, Function<Grid.Tile<String>, Set<Grid.Tile<String>>> neighbors) {
        Path longestPath = new Path(List.of(start));

        var Q = new LinkedList<Path>();
        Q.add(new Path(List.of(start)));

        while (!Q.isEmpty()) {
            var path = Q.poll();
            var current = path.end();

            if (current == end) {
                if (path.length() > longestPath.length()) {
                    longestPath = path;
                }
                continue;
            }

            Set<Grid.Tile<String>> _neighbors = neighbors.apply(current);
            for (var neighbor : _neighbors) {
                if (path.path.contains(neighbor)) continue;
                var newPath = new Path(new ArrayList<>(path.path), path.length + graph.getWeight(current, neighbor));
                newPath.path.add(neighbor);
                Q.add(newPath);
            }
        }

        return longestPath;
    }

    private Set<Grid.Tile<String>> neighbors(Grid.Tile<String> tile, boolean followSlope) {
        Set<Grid.Tile<String>> ret = new HashSet<>();

        if (followSlope) {
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
                    ret.addAll(graph.getNeighbours(tile));
                    break;
            }
        } else {
            ret.addAll(graph.getNeighbours(tile));
        }

        ret.removeIf(t -> t == null || t.data().equals("#"));
        return ret;
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
