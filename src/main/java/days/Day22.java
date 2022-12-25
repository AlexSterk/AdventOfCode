package days;

import setup.Day;
import util.Grid;
import util.Grid.Tile;
import util.Line.Point;
import util.Pair;

import java.util.*;
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
        Direction dir = Direction.RIGHT;

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
                        case UP -> current.up();
                        case DOWN -> current.down();
                        case LEFT -> current.left();
                        case RIGHT -> current.right();
                    };
                    if (next == null || next.data().equals(" ")) {
                        // wrap around and search for non-space
                        next = switch (dir) {
                            case UP ->
                                    grid.getColumn(current.x()).stream().filter(Objects::nonNull).filter(s -> !s.data().equals(" ")).reduce((a, b) -> b).get();
                            case DOWN ->
                                    grid.getColumn(current.x()).stream().filter(Objects::nonNull).filter(s -> !s.data().equals(" ")).reduce((a, b) -> a).get();
                            case LEFT ->
                                    grid.getRow(current.y()).stream().filter(Objects::nonNull).filter(s -> !s.data().equals(" ")).reduce((a, b) -> b).get();
                            case RIGHT ->
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
            case RIGHT -> 0;
            case DOWN -> 1;
            case LEFT -> 2;
            case UP -> 3;
        };
    }

    @Override
    public Object part2() {
        var sides = findCubeSubgrids();
        var map = getAdjacencyMap(sides, getSize());

        var currentSide = sides.values().stream().filter(s -> s.name.equals("A")).findFirst().get();
        var currentTile = currentSide.grid.getRow(0).stream().filter(s -> s.data().equals(".")).findFirst().get();
        var currentDir = Direction.RIGHT;

        for (String instruction : instructions) {
            if (instruction.equals("L")) {
                currentDir = currentDir.turnLeft();
            } else if (instruction.equals("R")) {
                currentDir = currentDir.turnRight();
            } else {
                int steps = Integer.parseInt(instruction);
                for (int i = 0; i < steps; i++) {
                    var next = switch (currentDir) {
                        case UP -> currentTile.up();
                        case DOWN -> currentTile.down();
                        case LEFT -> currentTile.left();
                        case RIGHT -> currentTile.right();
                    };
                    var nextSide = currentSide;
                    if (next == null) {
                        // move to next side
                        nextSide = map.get(new Pair<>(currentSide, currentDir));
                        next = nextSide.grid.getTile(switch (currentDir) {
                            case UP, DOWN -> currentTile.x();
                            case LEFT -> currentSide.grid.width - 1;
                            case RIGHT -> 0;
                        }, switch (currentDir) {
                            case UP -> currentSide.grid.height - 1;
                            case DOWN -> 0;
                            case LEFT, RIGHT -> currentTile.y();
                        });
                    }
                    if (next.data().equals("#")) {
                        break;
                    }
                    currentTile = next;
                    currentSide = nextSide;
                }
            }
        }

        System.out.println(currentSide);

        return null;
    }

    private Map<Point, CubeSide> findCubeSubgrids() {
        Map<Point, CubeSide> sides = new HashMap<>();

        int size = getSize();
        int cubes = 0;
        for (int y = 0; y <= grid.height - size; y += size) {
            for (int x = 0; x <= grid.width - size; x += size) {
                var possibleCube = grid.subgrid(x, x + size - 1, y, y + size - 1);
                List<Tile<String>> all = possibleCube.getAll();
                if (all.size() == size * size && all.stream().noneMatch(s -> s == null || s.data().equals(" "))) {
                    Point key = new Point(x, y);
                    sides.put(key, new CubeSide(Character.toString('A' + (cubes++)), possibleCube));
                }
            }
        }

        return sides;
    }

    private int getSize() {
        return isTest() ? 4 : 50;
    }

    private CubeSide getAdjacent(Map<Pair<CubeSide, Direction>, CubeSide> adjacency, CubeSide side, Direction dir) {
        if (adjacency.containsKey(new Pair<>(side, dir))) {
            return adjacency.get(new Pair<>(side, dir));
        }
        var leftTurn = dir.turnLeft();
        CubeSide adjacent = getAdjacent(adjacency, side, leftTurn);
        CubeSide adjacent1 = getAdjacent(adjacency, adjacent, dir);
        adjacency.put(new Pair<>(side, dir), new CubeSide(adjacent1.name + "_rot", adjacent1.grid.rotate()));
        return adjacent1;
    }

    private Map<Pair<CubeSide, Direction>, CubeSide> getAdjacencyMap(Map<Point, CubeSide> sides, int size) {
        Map<Pair<CubeSide, Direction>, CubeSide> adjacencyMap = new HashMap<>();
        for (var entry : sides.entrySet()) {
            Point key = entry.getKey();
            CubeSide value = entry.getValue();
            for (Direction dir : Direction.values()) {
                Point next = switch (dir) {
                    case UP -> new Point(key.x(), key.y() - size);
                    case DOWN -> new Point(key.x(), key.y() + size);
                    case LEFT -> new Point(key.x() - size, key.y());
                    case RIGHT -> new Point(key.x() + size, key.y());
                };
                if (sides.containsKey(next)) {
                    adjacencyMap.put(new Pair<>(value, dir), sides.get(next));
                }
            }
        }

        while (true) {
            var temp = new HashMap<>(adjacencyMap);
            for (CubeSide side : new HashSet<>(temp.values())) {
                for (Direction direction : Direction.values()) {
                    if (adjacencyMap.containsKey(new Pair<>(side, direction))) {
                        continue;
                    }
                    // go thrice in the opposite direction
                    var opposite = direction.opposite();
                    try {
                        var next = adjacencyMap.get(new Pair<>(side, opposite));
                        next = adjacencyMap.get(new Pair<>(next, opposite));
                        next = adjacencyMap.get(new Pair<>(next, opposite));
                        adjacencyMap.put(new Pair<>(side, direction), next);
                        if (next != null) continue;
                    } catch (NullPointerException e) {
                        // ignore
                    }
                    // turn once, then go in the desired direction
                    var turn = direction.turnLeft();
                    try {
                        var next = adjacencyMap.get(new Pair<>(side, turn));
                        var nextNext = adjacencyMap.get(new Pair<>(next, direction));
                        CubeSide rotated = new CubeSide(nextNext.name + "_rot", nextNext.grid.rotate());
                        adjacencyMap.put(new Pair<>(side, direction), rotated);
                        adjacencyMap.put(new Pair<>(rotated, turn.opposite()), side);
                    } catch (NullPointerException ignored) {

                    }
                }
            }
            adjacencyMap.values().removeIf(Objects::isNull);
            if (temp.size() == adjacencyMap.size()) {
                break;
            }
        }



        return adjacencyMap;
    }

    @Override
    public int getDay() {
        return 22;
    }

    @Override
    public boolean isTest() {
        return true;
    }

    @Override
    public String partOneSolution() {
        return "55244";
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT;

        public Direction turnLeft() {
            return switch (this) {
                case UP -> LEFT;
                case DOWN -> RIGHT;
                case LEFT -> DOWN;
                case RIGHT -> UP;
            };
        }

        public Direction turnRight() {
            return switch (this) {
                case UP -> RIGHT;
                case DOWN -> LEFT;
                case LEFT -> UP;
                case RIGHT -> DOWN;
            };
        }

        public Direction opposite() {
            return switch (this) {
                case UP -> DOWN;
                case DOWN -> UP;
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
            };
        }

    }

    private record CubeSide(String name, Grid<String> grid) {
        @Override
        public String toString() {
            return "CubeSide{" +
                    "name='" + name + '\'' +
                    ", grid=\n" + grid +
                    "\n}";
        }
    }
}
