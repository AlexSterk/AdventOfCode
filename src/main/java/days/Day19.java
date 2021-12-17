package days;

import setup.Day;
import util.Line.Point;

import java.util.Arrays;

public class Day19 extends Day {
    private static final Point DOWN = new Point(0, 1);
    private static final Point UP = new Point(0, -1);
    private static final Point LEFT = new Point(-1, 0);
    private static final Point RIGHT = new Point(1, 0);
    private char[][] routing;
    private Point start;
    private Point direction;
    private int steps = 1;

    private static Point turnLeft(Point dir) {
        /*
        0,1 --> 1,0
        1,0 --> 0,-1
         */
        return new Point(dir.y(), -dir.x());
    }

    private static Point turnRight(Point dir) {
        /*
        0,1 --> -1,0
        -1,0 --> 0,-1
         */
        return new Point(-dir.y(), dir.x());
    }

    @Override
    public void processInput() {
        String[] lines = input.split("\n");
        int maxLength = Arrays.stream(lines).mapToInt(String::length).max().getAsInt();
        routing = new char[lines.length][maxLength];
        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[y].length(); x++) {
                char c = lines[y].charAt(x);
                if (c != ' ') {
                    routing[y][x] = c;
                    if (y == 0) start = new Point(x, y);
                }
            }
        }
        direction = DOWN;
    }

    @Override
    public Object part1() {
        Point current = start;
        StringBuilder s = new StringBuilder();
        while (true) {
            if (canMove(current, direction)) {
                current = current.add(direction);
                char c = routing[current.y()][current.x()];
                steps++;
                if (Character.isAlphabetic(c)) s.append(c);
            } else {
                if (canMove(current, turnLeft(direction))) {
                    direction = turnLeft(direction);
                } else if (canMove(current, turnRight(direction))) {
                    direction = turnRight(direction);
                } else break;
            }
        }
        return s.toString();
    }

    private boolean canMove(Point current, Point dir) {
        var add = current.add(dir);
        return add.x() >= 0 && add.x() < routing[0].length && add.y() >= 0 && add.y() < routing.length && routing[add.y()][add.x()] != 0;
    }

    @Override
    public Object part2() {
        return steps;
    }

    @Override
    public int getDay() {
        return 19;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "YOHREPXWN";
    }

    @Override
    public String partTwoSolution() {
        return "16734";
    }
}
