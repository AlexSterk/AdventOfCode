package days;

import setup.Day;
import util.Graph;
import util.Grid;
import util.Grid.Tile;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;
import java.util.Set;

public class Day10 extends Day {
    @Override
    public void processInput() {

    }

    @Override
    public Object part1() {
        var grid = Grid.parseGrid(input).map(s -> Pipe.fromChar(s.charAt(0)));

        var start = grid.getAll().stream().filter(p -> p.data() == Pipe.START).findAny().get();

        System.out.println(start);

        var graph = new Graph<Tile<Pipe>>();

        Queue<Tile<Pipe>> Q = new ArrayDeque<>();
        Q.offer(start);

        while (!Q.isEmpty()) {
            Tile<Pipe> u = Q.poll();
            graph.addNode(u);

            for (Direction dir : Direction.values()) {
                var v = getNeighbour(u, dir);
                if (graph.containsNode(v)) continue;
                if (v == null) continue;
                if (v.data() == null) continue;
                System.out.printf("%s %s %s ", u, dir, v);
                if (u.data().canGo(dir, v.data())) {
                    System.out.print("added");
                    graph.addEdge(u, v, 1, true);
                    Q.offer(v);
                }
                System.out.println();
            }
        }

        return Collections.max(graph.getDistance(start).values());
    }

    private static <T> Tile<T> getNeighbour(Tile<T> t, Direction d) {
        return switch (d) {
            case UP -> t.up();
            case DOWN -> t.down();
            case LEFT -> t.left();
            case RIGHT -> t.right();
        };
    }

    @Override
    public Object part2() {
        return null;
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

    private enum Direction {
        UP,DOWN,LEFT,RIGHT;

        private Direction opposite() {
            return switch (this) {
                case UP -> DOWN;
                case DOWN -> UP;
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
            };
        }
    }

    private enum Pipe {
        HORIZONTAL('-', Set.of(Direction.LEFT, Direction.RIGHT)),
        VERTICAL('|', Set.of(Direction.UP, Direction.DOWN)),
        CORNER_NORTH_EAST('L', Set.of(Direction.UP, Direction.RIGHT)),
        CORNER_NORTH_WEST('J', Set.of(Direction.UP, Direction.LEFT)),
        CORNER_SOUTH_EAST('7', Set.of(Direction.DOWN, Direction.LEFT)),
        CORNER_SOUTH_WEST('F', Set.of(Direction.DOWN, Direction.RIGHT)),
        START('S', Set.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT));

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
