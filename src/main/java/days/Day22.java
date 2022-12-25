package days;

import setup.Day;
import util.Grid;
import util.Grid.Tile;
import util.Line.Point;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Day22 extends Day {

    private Grid<String> grid;
    private List<String> instructions;

    @Override
    public void processInput() {
        String[] split = input.split("(\r?\n){2}");
        grid = Grid.parseGrid(split[0]);
        Pattern p = Pattern.compile("(\\d+)|[RL]");
        instructions = p.matcher(split[1]).results()
                .map(MatchResult::group)
                .toList();
    }

    @Override
    public Object part1() {
        var current = grid.getRow(0).stream().filter(s -> s.data().equals(".")).findFirst().get();
        Direction dir = Direction.EAST;

        System.out.println(grid);

        for (String instruction : instructions) {
            if (instruction.equals("L")) {
                dir = dir.turnLeft();
            } else if (instruction.equals("R")) {
                dir = dir.turnRight();
            } else {
                int steps = Integer.parseInt(instruction);
                for (int i = 0; i < steps; i++) {
                    var next = switch (dir) {
                        case NORTH -> current.up();
                        case SOUTH -> current.down();
                        case WEST -> current.left();
                        case EAST -> current.right();
                    };
                    if (next == null || next.data().equals(" ")) {
                        // wrap around and search for non-space
                        next = switch (dir) {
                            case NORTH ->
                                    grid.getColumn(current.x()).stream().filter(Objects::nonNull).filter(s -> !s.data().equals(" ")).reduce((a, b) -> b).get();
                            case SOUTH ->
                                    grid.getColumn(current.x()).stream().filter(Objects::nonNull).filter(s -> !s.data().equals(" ")).reduce((a, b) -> a).get();
                            case WEST ->
                                    grid.getRow(current.y()).stream().filter(Objects::nonNull).filter(s -> !s.data().equals(" ")).reduce((a, b) -> b).get();
                            case EAST ->
                                    grid.getRow(current.y()).stream().filter(Objects::nonNull).filter(s -> !s.data().equals(" ")).reduce((a, b) -> a).get();
                        };
                    }
                    if (next.data().equals("#")) {
                        break;
                    }
                    current = next;
                }
            }
        }

        return 1000 * (current.y() + 1) + 4 * (current.x() + 1) + switch (dir) {
            case EAST -> 0;
            case SOUTH -> 1;
            case WEST -> 2;
            case NORTH -> 3;
        };
    }

    @Override
    public Object part2() {

        return null;
    }

    private TreeMap<Point, CubeFace> findFaces() {
        TreeMap<Point, CubeFace> sides = new TreeMap<>(Comparator.comparingInt(Point::y).thenComparingInt(Point::x));

        int size = getSize();
        int cubes = 0;
        for (int y = 0; y <= grid.height - size; y += size) {
            for (int x = 0; x <= grid.width - size; x += size) {
                var possibleCube = grid.subgrid(x, x + size - 1, y, y + size - 1);
                List<Tile<String>> all = possibleCube.getAll();
                if (all.size() == size * size && all.stream().noneMatch(s -> s == null || s.data().equals(" "))) {
                    Point key = new Point(x / size, y / size);
                    sides.put(key, new CubeFace(Character.toString('A' + (cubes++)), possibleCube, key));
                }
            }
        }

        return sides;
    }

    private int getSize() {
        return isTest() ? 4 : 50;
    }

    @Override
    public int getDay() {
        return 22;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "55244";
    }

    private enum Direction {
        EAST(1, 0),
        SOUTH(0, 1),
        WEST(-1, 0),
        NORTH(0, -1);
        private static final List<Direction> values = List.of(Direction.values());
        public final int dx, dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public Direction turnLeft() {
            return values.get((values.indexOf(this) + 3) % 4);
        }

        public Direction turnRight() {
            return values.get((values.indexOf(this) + 1) % 4);
        }

        public Direction opposite() {
            return values.get((values.indexOf(this) + 2) % 4);
        }

    }

    private record CubeFace(String name, Grid<String> grid, Point position) {
        @Override
        public String toString() {
            return "CubeSide{" +
                    "name='" + name + '\'' +
                    ", position=" + position +
                    ", grid=\n" + grid +
                    "\n}";
        }
    }


}
