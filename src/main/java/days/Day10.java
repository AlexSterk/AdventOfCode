package days;

import setup.Day;
import util.Direction;
import util.Graph;
import util.Grid;
import util.Grid.Tile;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;
import java.util.Set;

public class Day10 extends Day {
    private Grid<String> grid;
    private Graph<Tile<Pipe>> graph;

    @Override
    public void processInput() {

    }

    @Override
    public Object part1() {
        this.grid = Grid.parseGrid(input);
        var grid = this.grid.map(s -> Pipe.fromChar(s.charAt(0)));

        var start = grid.getAll().stream().filter(p -> p.data() == Pipe.START).findAny().get();

        System.out.println(start);

        graph = new Graph<>();

        Queue<Tile<Pipe>> Q = new ArrayDeque<>();
        Q.offer(start);

        while (!Q.isEmpty()) {
            Tile<Pipe> u = Q.poll();
            if (graph.containsNode(u)) continue;
            graph.addNode(u);

            for (Direction dir : Direction.CARDINAL) {
                var v = grid.getTile(u.x() + dir.dx, u.y() + dir.dy);
                if (v == null) continue;
                if (v.data() == null) continue;
                if (u.data().canGo(dir, v.data())) {
                    graph.addEdge(u, v, 1, true);
                    Q.offer(v);
                }
            }
        }

        return Collections.max(graph.getDistance(start).values());
    }

    @Override
    public Object part2() {
        graph.nodes().forEach(n -> {
            var up = n.up();
            if (graph.getNeighbours(n).contains(up)) grid.set(n.x(), n.y(), "!");
            else grid.set(n.x(), n.y(), "_");
        });

        var count = 0;

        String g = grid.toString();
        var lines = g.split("\n");
        for (String line : lines) {
            line = line.replace("_", "").replace("!!", "");
            var in = false;
            for (char c : line.toCharArray()) {
                if (c == '!') in = !in;
                else if (in) count++;
            }
        }

        return count;
    }

    @Override
    public int getDay() {
        return 10;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "6867";
    }

    @Override
    public String partTwoSolution() {
        return "595";
    }

    private enum Pipe {
        HORIZONTAL('-', Set.of(Direction.W, Direction.E)),
        VERTICAL('|', Set.of(Direction.N, Direction.S)),
        CORNER_NORTH_EAST('L', Set.of(Direction.N, Direction.E)),
        CORNER_NORTH_WEST('J', Set.of(Direction.N, Direction.W)),
        CORNER_SOUTH_EAST('7', Set.of(Direction.S, Direction.W)),
        CORNER_SOUTH_WEST('F', Set.of(Direction.S, Direction.E)),
        START('S', Set.of(Direction.N, Direction.S, Direction.W, Direction.E));

        private final char c;
        private final Set<Direction> allowedDirections;

        Pipe(char c, Set<Direction> allowedDirections) {
            this.c = c;
            this.allowedDirections = allowedDirections;
        }

        private static Pipe fromChar(char c) {
            for (Pipe p : Pipe.values()) {
                if (p.c == c) return p;
            }
            return null;
        }

        private boolean canGo(Direction d, Pipe o) {
            return allowedDirections.contains(d) && o.allowedDirections.contains(d.opposite());
        }


        @Override
        public String toString() {
            return String.valueOf(c);
        }
    }
}
