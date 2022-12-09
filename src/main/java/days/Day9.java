package days;

import setup.Day;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static util.Grid.InfiniteGrid;
import static util.Grid.Tile;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Day9 extends Day {
    private InfiniteGrid grid;

    @Override
    public void processInput() {
        grid = new InfiniteGrid(() -> null);
    }

    @Override
    public Object part1() {
        Tile[] knots = new Tile[2];
        Arrays.fill(knots, grid.getTile(100, 100));
        Set<Tile> visited = new HashSet<>();
        visited.add(knots[1]);

        return runSimulation(knots, visited);
    }

    @Override
    public Object part2() {
        Tile[] knots = new Tile[10];
        Arrays.fill(knots, grid.getTile(100, 100));
        Set<Tile> visited = new HashSet<>();
        visited.add(knots[9]);

        return runSimulation(knots, visited);
    }

    private Object runSimulation(Tile[] knots, Set<Tile> visited) {
        for (String line : lines()) {
            String[] s = line.split(" ");
            String dir = s[0];
            int steps = Integer.parseInt(s[1]);

            for (int i = 0; i < steps; i++) {
                moveHead(knots, dir);
                moveKnots(knots, visited);
            }
        }

        return visited.size();
    }

    private void moveKnots(Tile[] knots, Set<Tile> visited) {
        for (int i = 1; i < knots.length; i++) {
            var previous = knots[i - 1];
            var current = knots[i];

            int dx = previous.x() - current.x();
            int dy = previous.y() - current.y();

            if (current.manhattanDistance(previous) > 1 && (Math.abs(dx) != 1 || Math.abs(dy) != 1)) {
                knots[i] = grid.getTile(current.x() + Integer.signum(dx), current.y() + Integer.signum(dy));
            }

            if (i == knots.length - 1) {
                visited.add(knots[i]);
            }
        }
    }

    private void moveHead(Tile[] knots, String dir) {
        knots[0] = switch (dir) {
            case "U" -> grid.getTile(knots[0].x(), knots[0].y() - 1);
            case "D" -> grid.getTile(knots[0].x(), knots[0].y() + 1);
            case "L" -> grid.getTile(knots[0].x() - 1, knots[0].y());
            case "R" -> grid.getTile(knots[0].x() + 1, knots[0].y());
            default -> knots[0];
        };
    }

    @Override
    public int getDay() {
        return 9;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "6018";
    }

    @Override
    public String partTwoSolution() {
        return "2619";
    }
}
