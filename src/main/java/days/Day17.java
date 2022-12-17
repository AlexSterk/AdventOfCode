package days;

import setup.Day;
import util.Grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day17 extends Day {

    private Grid<Character> grid;

    @Override
    public void processInput() {

    }

    @Override
    public Object part1() {
        int loop = 2022;
//        loop = 4;
        int maxHeight = 4 * loop + 1;
        grid = new Grid<>(7, maxHeight, () -> '.');
        grid.fill('.');

        int floorY = maxHeight - 1;
        grid.getRow(floorY).forEach(t -> grid.set(t, '-'));

//        System.out.println(grid);

        int minY = floorY;

        for (int i = 0; i < loop; i++) {
            boolean[][] shape = SHAPES.get(i % SHAPES.size());
            Rock currentRock = new Rock(2, minY - shape.length - 3, shape);



            while (true) {
//                if (i == 431) {
//                    currentRock.drawFalling();
//                    grid.subgrid(0, grid.width, minY - 8, minY + 4).print();
//                    System.out.println("====================================");
//                    currentRock.clearDrawing();
//                }

                jetRock(currentRock);
                if (currentRock.canMoveDown()) {
                    currentRock.moveDown();
                } else {
                    currentRock.place();
                    minY = Math.min(minY, currentRock.y);
                    break;
                }
            }

//            System.out.println((floorY - minY) );
//            grid.print();
        }

//        grid.print();
        return floorY - minY;

//        return null;
    }

    private int instructionIndex = 0;
    private final String input = super.input.trim();

    private void jetRock(Rock rock) {
//        System.out.println(input.substring(0, instructionIndex + 1));
        char c = input.charAt(instructionIndex++);
        instructionIndex %= input.length();

        if (c == '>') {
            if (rock.x + rock.width() < grid.width && rock.canMoveRight()) {
//                System.out.println("Moving right");
//                System.out.println(Arrays.deepToString(rock.shape));
//                rock.clearDrawing();
                rock.x++;
            } else {
//                System.out.println("Can't move right");
            }
        } else if (c == '<') {
            if (rock.x - 1 >= 0 && rock.canMoveLeft()) {
//                System.out.println("Moving left");
//                System.out.println(Arrays.deepToString(rock.shape));
//                rock.clearDrawing();
                rock.x--;
            } else {
//                System.out.println("Can't move left");
            }
        } else {
            throw new RuntimeException("Unknown instruction: " + c);
        }

    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 17;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private static final boolean[][] LINE_SHAPE = new boolean[][] {
            {true, true, true, true}
    };

    private static final boolean[][] PLUS_SHAPE = new boolean[][] {
        {false, true, false},
        {true, true, true},
        {false, true, false}
    };

    private static final boolean[][] L_SHAPE = new boolean[][] {
        {false, false, true},
        {false, false, true},
        {true, true, true}
    };

    private static final boolean[][] I_SHAPE = new boolean[][] {
        {true},
        {true},
        {true},
        {true}
    };

    private static final boolean[][] BLOCK_SHAPE = new boolean[][] {
        {true, true},
        {true, true}
    };

    private static final List<boolean[][]> SHAPES = List.of(LINE_SHAPE, PLUS_SHAPE, L_SHAPE, I_SHAPE, BLOCK_SHAPE);


    private class Rock {
        int x, y;
        boolean[][] shape;

        int width() {
            return shape[0].length;
        }

        int height() {
            return shape.length;
        }

        Rock(int x, int y, boolean[][] shape) {
            this.x = x;
            this.y = y;
            this.shape = shape;
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
