package days;

import setup.Day;
import util.Direction;
import util.Grid;

import java.util.*;

public class Day18 extends Day {

    private List<Instruction> instructions;

    @Override
    public void processInput() {
        instructions = lines().stream().map(Instruction::fromString).toList();
    }

    @Override
    public Object part1() {
        var grid = new Grid.InfiniteGrid<>(() -> -1);

        var cur = grid.getTile(0, 0);
        for (Instruction instruction : instructions) {
            for (int i = 0; i < instruction.amount; i++) {
                grid.set(cur, instruction.hex);
                cur = cur.add(instruction.dir.asPoint());
            }
        }

        Grid<Integer> finite = grid.toFinite().init(() -> -1, false);
        write("grid.txt", finite.toString(t -> t.data() == -1 ? "." : "#"));

        // start somewhere on the inside
        var start = isTest() ? finite.getTile(1, 1) : finite.getTile(155, 1);

        var queue = new ArrayDeque<Grid.Tile<Integer>>();
        queue.add(start);

        var visited = new HashSet<Grid.Tile<Integer>>();
        visited.add(start);

        while (!queue.isEmpty()) {
            var curr = queue.poll();
            var ns = finite.getAdjacent(curr, true);

            for (var n : ns) {
                if (visited.contains(n)) continue;
                visited.add(n);
                if (n.data() == -1) {
                    queue.add(n);
                }
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
        return 18;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private record Instruction(Direction dir, int amount, int hex) {
        public static Instruction fromString(String s) {
            var split = s.split(" ");
            var dir = split[0];
            var amount = Integer.parseInt(split[1]);
            var hex = Integer.parseInt(split[2].replaceAll("[()#]", ""), 16);

            var mapDir = switch (dir) {
                case "U" -> Direction.N;
                case "D" -> Direction.S;
                case "L" -> Direction.W;
                case "R" -> Direction.E;
                default -> throw new IllegalArgumentException("Invalid direction: " + dir);
            };

            return new Instruction(mapDir, amount, hex);
        }
    }

    private record Trench(Integer color) implements Grid.Weighted {
        @Override
        public String toString() {
            return color == null ? "." : "#";
        }

        @Override
        public Integer getWeight() {
            return 1;
        }
    }
}
