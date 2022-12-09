package days;

import setup.Day;
import util.Grid;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Day9 extends Day {


    @Override
    public void processInput() {

    }

    @Override
    public Object part1() {
        Grid.InfiniteGrid<String> grid = new Grid.InfiniteGrid<>(() -> ".");
        Grid.Tile<String> head, tail;
        head = tail = grid.getTile(100, 100);
        Set<Grid.Tile<String>> visited = new HashSet<>();
        visited.add(tail);

        for (String line : lines()) {
            String[] s = line.split(" ");
            String dir = s[0];
            int steps = Integer.parseInt(s[1]);

            for (int i = 0; i < steps; i++) {
                head = switch (dir) {
                    case "U" -> grid.getTile(head.x(), head.y() - 1);
                    case "D" -> grid.getTile(head.x(), head.y() + 1);
                    case "L" -> grid.getTile(head.x() - 1, head.y());
                    case "R" -> grid.getTile(head.x() + 1, head.y());
                    default -> head;
                };

                int dx = head.x() - tail.x();
                int dy = head.y() - tail.y();

                if (tail.manhattanDistance(head) <= 1) {
                    // nothing
                }

                 else if (Math.abs(dx) == 1 && Math.abs(dy) == 1) {
                    // nothing
                } else {
                    tail = grid.getTile(tail.x() + Integer.signum(dx), tail.y() + Integer.signum(dy));
                }

                visited.add(tail);
            }
        }

        return visited.size();
    }

    @Override
    public Object part2() {
        Grid.InfiniteGrid<String> grid = new Grid.InfiniteGrid<>(() -> ".");
        Grid.Tile[] knots = new Grid.Tile[10];
        Arrays.fill(knots, grid.getTile(100, 100));
        Set<Grid.Tile<String>> visited = new HashSet<>();
        visited.add(knots[9]);

        for (String line : lines()) {
            String[] s = line.split(" ");
            String dir = s[0];
            int steps = Integer.parseInt(s[1]);

            for (int i = 0; i < steps; i++) {
                knots[0] = switch (dir) {
                    case "U" -> grid.getTile(knots[0].x(), knots[0].y() - 1);
                    case "D" -> grid.getTile(knots[0].x(), knots[0].y() + 1);
                    case "L" -> grid.getTile(knots[0].x() - 1, knots[0].y());
                    case "R" -> grid.getTile(knots[0].x() + 1, knots[0].y());
                    default -> knots[0];
                };

                for (int j = 1; j < knots.length; j++) {
                    var previous = knots[j - 1];
                    var current = knots[j];

                    int dx = previous.x() - current.x();
                    int dy = previous.y() - current.y();

                    if (current.manhattanDistance(previous) <= 1) {
                        // nothing
                    } else if (Math.abs(dx) == 1 && Math.abs(dy) == 1) {
                        // nothing
                    } else {
                        knots[j] = grid.getTile(current.x() + Integer.signum(dx), current.y() + Integer.signum(dy));
                    }

                    if (j == knots.length - 1) {
                        visited.add(knots[j]);
                    }
                }
            }
        }

        return visited.size();
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
