package days;

import setup.Day;

import java.util.*;

public class Day13 extends Day {

    private static final Empty EMPTY = new Empty();
    private Track[][] grid;
    private List<Cart> carts;

    @Override
    public void processInput() {
        char[][] charGrid;

        String[] lines = input.split("\n");

        charGrid = new char[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            charGrid[i] = lines[i].toCharArray();
        }

        grid = new Track[charGrid.length][charGrid[0].length];

        for (int y = 0; y < charGrid.length; y++) {
            for (int x = 0; x < charGrid[y].length; x++) {
                Pair coords = new Pair(x, y);
                if (charGrid[y][x] == '-' | charGrid[y][x] == '<' | charGrid[y][x] == '>')
                    grid[y][x] = new Straight(coords, grid, Orientation.HORIZONTAL);
                if (charGrid[y][x] == '|' | charGrid[y][x] == '^' | charGrid[y][x] == 'v')
                    grid[y][x] = new Straight(coords, grid, Orientation.VERTICAL);
                if (charGrid[y][x] == '+') grid[y][x] = new Intersection(coords, grid);
                if (charGrid[y][x] == ' ') grid[y][x] = EMPTY;
            }
        }

        for (int y = 0; y < charGrid.length; y++) {
            for (int x = 0; x < charGrid[y].length; x++) {
                Pair coords = new Pair(x, y);
                Track down, right, up, left;
                down = y + 1 < grid.length ? grid[y + 1][x] : EMPTY;
                right = x + 1 < grid[y].length ? grid[y][x + 1] : EMPTY;
                up = y - 1 >= 0 ? grid[y - 1][x] : EMPTY;
                left = x - 1 >= 0 ? grid[y][x - 1] : EMPTY;
                if (charGrid[y][x] == '/') {
                    if ((down instanceof Straight && ((Straight) down).orientation == Orientation.VERTICAL) || down instanceof Intersection) {
                        grid[y][x] = new Curved(coords, grid, Position.TOPLEFT);
                    } else if ((right instanceof Straight && ((Straight) right).orientation == Orientation.HORIZONTAL) || right instanceof Intersection) {
                        grid[y][x] = new Curved(coords, grid, Position.TOPLEFT);
                    } else if ((up instanceof Straight && ((Straight) up).orientation == Orientation.VERTICAL) || up instanceof Intersection) {
                        grid[y][x] = new Curved(coords, grid, Position.BOTTOMRIGHT);
                    } else if ((left instanceof Straight && ((Straight) left).orientation == Orientation.HORIZONTAL) || left instanceof Intersection) {
                        grid[y][x] = new Curved(coords, grid, Position.BOTTOMRIGHT);
                    }

                } else if (charGrid[y][x] == '\\') {
                    if ((down instanceof Straight && ((Straight) down).orientation == Orientation.VERTICAL) || down instanceof Intersection) {
                        grid[y][x] = new Curved(coords, grid, Position.TOPRIGHT);
                    } else if ((left instanceof Straight && ((Straight) left).orientation == Orientation.HORIZONTAL) || left instanceof Intersection) {
                        grid[y][x] = new Curved(coords, grid, Position.TOPRIGHT);
                    } else if ((up instanceof Straight && ((Straight) up).orientation == Orientation.VERTICAL) || up instanceof Intersection) {
                        grid[y][x] = new Curved(coords, grid, Position.BOTTOMLEFT);
                    } else if ((right instanceof Straight && ((Straight) right).orientation == Orientation.HORIZONTAL) || right instanceof Intersection) {
                        grid[y][x] = new Curved(coords, grid, Position.BOTTOMLEFT);
                    }
                }
            }
        }

        carts = new ArrayList<>();

        for (int y = 0; y < charGrid.length; y++) {
            for (int x = 0; x < charGrid[y].length; x++) {
                final Track position = grid[y][x];
                switch (charGrid[y][x]) {
                    case '^' -> carts.add(new Cart(position, Direction.UP));
                    case '>' -> carts.add(new Cart(position, Direction.RIGHT));
                    case '<' -> carts.add(new Cart(position, Direction.LEFT));
                    case 'v' -> carts.add(new Cart(position, Direction.DOWN));
                }
            }
        }

        printGrid();
    }

    private void printGrid() {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                System.out.print(grid[y][x]);
            }
            System.out.println();
        }
    }

    @Override
    public void part1() {
        boolean crash = false;
        for (int n = 0; !crash; n++) {
            carts.forEach(Cart::move);
            Set<Track> tracks = new HashSet<>();
            for (Cart cart : carts) {
                if (tracks.contains(cart.position)) {
                    crash = true;
                    System.out.println(cart.position.coords());
                    break;
                }
                tracks.add(cart.position);
            }
        }
    }

    @Override
    public void part2() {
        processInput();
        while (true) {
            carts.forEach(Cart::move);
            Map<Track, Cart> tracks = new HashMap<>();
            for (Cart cart : new ArrayList<>(carts)) {
                if (tracks.containsKey(cart.position)) {
                    carts.remove(cart);
                    carts.remove(tracks.get(cart.position));
                } else {
                    tracks.put(cart.position, cart);
                }
            }
            if (carts.size() == 1) {
                System.out.println(carts.get(0).position.coords());
                break;
            }
        }
    }

    @Override
    public int getDay() {
        return 13;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private enum Orientation {
        HORIZONTAL, VERTICAL;
    }

    private enum Direction {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        final int vx, vy;

        Direction(int vx, int vy) {
            this.vx = vx;
            this.vy = vy;
        }

        public Direction rotateClockwise() {
            return switch (this) {
                case UP -> RIGHT;
                case DOWN -> LEFT;
                case LEFT -> UP;
                case RIGHT -> DOWN;
            };
        }

        public Direction rotateCounterClockwise() {
            return switch (this) {
                case UP -> LEFT;
                case DOWN -> RIGHT;
                case LEFT -> DOWN;
                case RIGHT -> UP;
            };
        }


    }

    private enum Position {
        TOPLEFT, TOPRIGHT, BOTTOMLEFT, BOTTOMRIGHT;
    }

    private sealed interface Track {
        @Override
        String toString();

        Pair coords();

        Track[][] grid();

        Map<Direction, Track> getNeighbours();
    }

    private static record Pair(int x, int y) {
        public Pair move(Direction direction) {
            return new Pair(x + direction.vx, y + direction.vy);
        }
    }

    private static record Empty() implements Track {

        @Override
        public Pair coords() {
            return null;
        }

        @Override
        public Track[][] grid() {
            return null;
        }

        @Override
        public Map<Direction, Track> getNeighbours() {
            return null;
        }

        @Override
        public String toString() {
            return " ";
        }
    }

    private static record Straight(Pair coords, Track[][] grid, Orientation orientation) implements Track {
        @Override
        public String toString() {
            return orientation == Orientation.VERTICAL ? "|" : "-";
        }

        @Override
        public Map<Direction, Track> getNeighbours() {
            Map<Direction, Track> m = new HashMap<>();
            if (orientation == Orientation.VERTICAL) {
                m.put(Direction.UP, grid[coords.move(Direction.UP).y][coords.move(Direction.UP).x]);
                m.put(Direction.DOWN, grid[coords.move(Direction.DOWN).y][coords.move(Direction.DOWN).x]);
            } else {
                m.put(Direction.LEFT, grid[coords.move(Direction.LEFT).y][coords.move(Direction.LEFT).x]);
                m.put(Direction.RIGHT, grid[coords.move(Direction.RIGHT).y][coords.move(Direction.RIGHT).x]);
            }
            return m;
        }
    }

    private static record Curved(Pair coords, Track[][] grid, Position position) implements Track {
        @Override
        public String toString() {
            return position == Position.TOPLEFT || position == Position.BOTTOMRIGHT ? "/" : "\\";
        }

        @Override
        public Map<Direction, Track> getNeighbours() {
            Map<Direction, Track> m = new HashMap<>();
            switch (position) {
                case TOPLEFT -> {
                    m.put(Direction.DOWN, grid[coords.move(Direction.DOWN).y][coords.move(Direction.DOWN).x]);
                    m.put(Direction.RIGHT, grid[coords.move(Direction.RIGHT).y][coords.move(Direction.RIGHT).x]);
                }
                case TOPRIGHT -> {
                    m.put(Direction.DOWN, grid[coords.move(Direction.DOWN).y][coords.move(Direction.DOWN).x]);
                    m.put(Direction.LEFT, grid[coords.move(Direction.LEFT).y][coords.move(Direction.LEFT).x]);
                }
                case BOTTOMLEFT -> {
                    m.put(Direction.UP, grid[coords.move(Direction.UP).y][coords.move(Direction.UP).x]);
                    m.put(Direction.RIGHT, grid[coords.move(Direction.RIGHT).y][coords.move(Direction.RIGHT).x]);
                }
                case BOTTOMRIGHT -> {
                    m.put(Direction.UP, grid[coords.move(Direction.UP).y][coords.move(Direction.UP).x]);
                    m.put(Direction.LEFT, grid[coords.move(Direction.LEFT).y][coords.move(Direction.LEFT).x]);
                }
            }
            return m;
        }
    }

    private static record Intersection(Pair coords, Track[][] grid) implements Track {
        @Override
        public String toString() {
            return "+";
        }

        public Map<Direction, Track> getNeighbours() {
            HashMap<Direction, Track> ns = new HashMap<>();

            ns.put(Direction.UP, grid[coords.move(Direction.UP).y][coords.move(Direction.UP).x]);
            ns.put(Direction.DOWN, grid[coords.move(Direction.DOWN).y][coords.move(Direction.DOWN).x]);
            ns.put(Direction.LEFT, grid[coords.move(Direction.LEFT).y][coords.move(Direction.LEFT).x]);
            ns.put(Direction.RIGHT, grid[coords.move(Direction.RIGHT).y][coords.move(Direction.RIGHT).x]);

            return ns;
        }
    }

    private static class Cart {
        Track position;
        private Direction direction;

        private int intersection = 0;

        private Cart(Track position, Direction direction) {
            this.position = position;
            this.direction = direction;
        }

        public void move() {
            position = position.getNeighbours().get(direction);

            if (position instanceof Curved) {
                if (((Curved) position).position == Position.TOPLEFT) {
                    if (direction == Direction.UP) direction = Direction.RIGHT;
                    if (direction == Direction.LEFT) direction = Direction.DOWN;
                }
                if (((Curved) position).position == Position.TOPRIGHT) {
                    if (direction == Direction.UP) direction = Direction.LEFT;
                    if (direction == Direction.RIGHT) direction = Direction.DOWN;
                }
                if (((Curved) position).position == Position.BOTTOMLEFT) {
                    if (direction == Direction.DOWN) direction = Direction.RIGHT;
                    if (direction == Direction.LEFT) direction = Direction.UP;
                }
                if (((Curved) position).position == Position.BOTTOMRIGHT) {
                    if (direction == Direction.DOWN) direction = Direction.LEFT;
                    if (direction == Direction.RIGHT) direction = Direction.UP;
                }
            }

            if (position instanceof Intersection) {
                switch (intersection) {
                    case 0 -> direction = direction.rotateCounterClockwise();
                    case 2 -> direction = direction.rotateClockwise();
                }
                intersection = (intersection + 1) % 3;
            }
        }
    }
}
