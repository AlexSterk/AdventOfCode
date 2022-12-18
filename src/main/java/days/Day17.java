package days;

import setup.Day;
import util.Grid;
import util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day17 extends Day {

    private static final boolean[][] LINE_SHAPE = new boolean[][]{
            {true, true, true, true}
    };
    private static final boolean[][] PLUS_SHAPE = new boolean[][]{
            {false, true, false},
            {true, true, true},
            {false, true, false}
    };
    private static final boolean[][] L_SHAPE = new boolean[][]{
            {false, false, true},
            {false, false, true},
            {true, true, true}
    };
    private static final boolean[][] I_SHAPE = new boolean[][]{
            {true},
            {true},
            {true},
            {true}
    };
    private static final boolean[][] BLOCK_SHAPE = new boolean[][]{
            {true, true},
            {true, true}
    };
    private static final List<boolean[][]> SHAPES = List.of(LINE_SHAPE, PLUS_SHAPE, L_SHAPE, I_SHAPE, BLOCK_SHAPE);
    private final String input = super.input.trim();
    private final Map<State, Pair<Integer, Integer>> seen = new HashMap<>();
    private Grid<Character> grid;
    private int instructionIndex = 0;
    private int minY;
    private int floorY;

    @Override
    public void processInput() {

    }

    @Override
    public Object part1() {
        int loop = 2022;
        int maxHeight = 4 * loop + 1;

        grid = new Grid<>(7, maxHeight, () -> '.');
        grid.fill('.');
        floorY = maxHeight - 1;
        grid.getRow(floorY).forEach(t -> grid.set(t, '-'));
        minY = floorY;

        for (int i = 0; i < loop; i++) {
            minY = simulateRock(minY, i);
        }

        return floorY - minY;
    }

    private int simulateRock(int minY, long i) {
        boolean[][] shape = SHAPES.get(Math.toIntExact(i % SHAPES.size()));
        Rock currentRock = new Rock(2, minY - shape.length - 3, shape);

        while (true) {
            jetRock(currentRock);
            if (currentRock.canMoveDown()) {
                currentRock.moveDown();
            } else {
                currentRock.place();
                minY = Math.min(minY, currentRock.y);
                break;
            }
        }

        return minY;
    }

    private void jetRock(Rock rock) {
        char c = input.charAt(instructionIndex++);
        instructionIndex %= input.length();

        if (c == '>') {
            if (rock.x + rock.width() < grid.width && rock.canMoveRight()) {
                rock.x++;
            }
        } else if (c == '<') {
            if (rock.x - 1 >= 0 && rock.canMoveLeft()) {
                rock.x--;
            }
        } else {
            throw new RuntimeException("Unknown instruction: " + c);
        }

    }

    @Override
    public Object part2() {
        long limit = 1_000_000_000_000L;
        int maxHeight = 4 * 10000 + 1;
        long offset = 0;

        instructionIndex = 0;
        grid = new Grid<>(7, maxHeight, () -> '.');
        grid.fill('.');
        floorY = maxHeight - 1;
        grid.getRow(floorY).forEach(t -> grid.set(t, '-'));
        minY = floorY;

        for (long i = 0; i < limit; i++) {
            minY = simulateRock(minY, i);

            if (i >= 2022 && offset == 0) {
                String gridState = grid.subgrid(0, grid.width, minY, minY + 30).toString();
                State state = new State(instructionIndex, (int) (i % SHAPES.size()), gridState);
                if (seen.containsKey(state)) {
                    var old = seen.get(state);
                    long di = i - old.a();
                    int dy = minY - old.b();
                    long remaining = limit - i;
                    long loops = remaining / di;
                    i += loops * di;
                    offset += loops * dy;
                }
                seen.put(state, new Pair<>((int) i, minY));
            }
        }

        return floorY - minY - offset;
    }

    @Override
    public int getDay() {
        return 17;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "3197";
    }

    @Override
    public String partTwoSolution() {
        return "1568513119571";
    }

    private record State(int inputIndex, int rockIndex, String gridState) {
    }

    private class Rock {
        int x, y;
        boolean[][] shape;

        Rock(int x, int y, boolean[][] shape) {
            this.x = x;
            this.y = y;
            this.shape = shape;
        }

        int width() {
            return shape[0].length;
        }

        int height() {
            return shape.length;
        }

        public List<Grid.Tile<Character>> getTiles() {
            List<Grid.Tile<Character>> t = new ArrayList<>();

            for (int i = 0; i < height(); i++) {
                for (int j = 0; j < width(); j++) {
                    if (shape[i][j]) {
                        t.add(grid.getTile(x + j, y + i));
                    }
                }
            }

            return List.copyOf(t);
        }

        public void drawFalling() {
            for (int _y = 0; _y < height(); _y++) {
                for (int _x = 0; _x < width(); _x++) {
                    if (shape[_y][_x]) grid.set(x + _x, y + _y, '@');
                }
            }
        }

        public void clearDrawing() {
            for (int _y = 0; _y < height(); _y++) {
                for (int _x = 0; _x < width(); _x++) {
                    if (shape[_y][_x]) grid.set(x + _x, y + _y, '.');
                }
            }
        }

        public boolean canMoveDown() {
            return getTiles().stream().allMatch(t -> t.down().data() != '#' && t.down().data() != '-');
        }

        public boolean canMoveRight() {
            return getTiles().stream().allMatch(t -> t.right().data() != '#' && t.right().data() != '-');
        }

        public boolean canMoveLeft() {
            return getTiles().stream().allMatch(t -> t.left().data() != '#' && t.left().data() != '-');
        }

        public void moveDown() {

            y++;

        }

        public void place() {
            for (int _y = 0; _y < height(); _y++) {
                for (int _x = 0; _x < width(); _x++) {
                    if (shape[_y][_x]) grid.set(x + _x, y + _y, '#');
                }
            }
        }
    }
}
