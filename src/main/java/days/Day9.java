package days;

import setup.Day;
import util.Grid;

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
        return null;
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
}
