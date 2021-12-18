package days;

import setup.Day;
import util.Grid;
import util.Grid.Tile;
import util.Line.Point;

public class Day22 extends Day {
    private Grid<Status> grid;
    private Point center;

    @Override
    public void processInput() {
        int size = 100_000;
        grid = new Grid<>(size, size, () -> Status.CLEAN);
        String[] lines = input.split("\n");
        int midY = size / 2 - lines.length / 2;
        int midX = size / 2 - lines[0].length() / 2;

        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[y].length(); x++) {
                grid.set(midX + x, midY + y, lines[y].charAt(x) == '#' ? Status.INFECTED : Status.CLEAN);
            }
        }

        center = new Point(size / 2, size / 2);
    }

    @Override
    public Object part1() {
        int infections = 0;
        Point current = center;
        Point direction = new Point(0, -1);
        for (int i = 0; i < 10000; i++) {
            Tile<Status> tile = grid.getTile(current.x(), current.y());
            switch (tile.data()) {
                case CLEAN -> {
                    direction = direction.rotateLeft();
                    grid.set(tile, Status.INFECTED);
                    infections++;
                }
                case INFECTED -> {
                    direction = direction.rotateRight();
                    grid.set(tile, Status.CLEAN);
                }
            }
            current = current.add(direction);
        }
        return infections;
    }

    @Override
    public Object part2() {
        int infections = 0;
        Point current = center;
        Point direction = new Point(0, -1);
        for (int i = 0; i < 10000000; i++) {
            Tile<Status> tile = grid.getTile(current.x(), current.y());
            switch (tile.data()) {
                case CLEAN -> {
                    direction = direction.rotateLeft();
                    grid.set(tile, Status.WEAKENED);
                }
                case WEAKENED -> {
                    grid.set(tile, Status.INFECTED);
                    infections++;
                }
                case INFECTED -> {
                    direction = direction.rotateRight();
                    grid.set(tile, Status.FLAGGED);
                }
                case FLAGGED -> {
                    direction = direction.rotateLeft().rotateLeft();
                    grid.set(tile, Status.CLEAN);
                }
            }
            current = current.add(direction);
        }
        return infections;
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
    public boolean resetForPartTwo() {
        return true;
    }

    @Override
    public String partOneSolution() {
        return "5433";
    }

    @Override
    public String partTwoSolution() {
        return "2512599";
    }

    private enum Status {
        CLEAN("."),
        WEAKENED("W"),
        FLAGGED("F"),
        INFECTED("#");

        public final String s;

        Status(String s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return s;
        }
    }
}
