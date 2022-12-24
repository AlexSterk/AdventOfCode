package days;

import setup.Day;
import util.Grid;

import java.util.List;
import java.util.Objects;
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
                            case UP -> grid.getColumn(current.x()).stream().filter(Objects::nonNull).filter(s -> !s.data().equals(" ")).reduce((a, b) -> b).get();
                            case DOWN -> grid.getColumn(current.x()).stream().filter(Objects::nonNull).filter(s -> !s.data().equals(" ")).reduce((a, b) -> a).get();
                            case LEFT -> grid.getRow(current.y()).stream().filter(Objects::nonNull).filter(s -> !s.data().equals(" ")).reduce((a, b) -> b).get();
                            case RIGHT -> grid.getRow(current.y()).stream().filter(Objects::nonNull).filter(s -> !s.data().equals(" ")).reduce((a, b) -> a).get();
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
        return null;
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

    }
}
