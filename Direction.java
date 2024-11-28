package util;

import java.util.List;

import static util.Line.Point;

public enum Direction {
    E(1, 0),
    S(0, 1),
    W(-1, 0),
    N(0, -1),
    NE(1, -1),
    NW(-1, -1),
    SE(1, 1),
    SW(-1, 1);

    public final int dx, dy;

    public static final List<Direction> ALL = List.of(values());
    public static final List<Direction> CARDINAL = List.of(E, S, W, N);

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public Point asPoint() {
        return new Point(dx, dy);
    }

    public Point add(Point point) {
        return new Point(point.x() + dx, point.y() + dy);
    }

    public Direction opposite() {
        return switch (this) {
            case E -> W;
            case S -> N;
            case W -> E;
            case N -> S;
            case NE -> SW;
            case NW -> SE;
            case SE -> NW;
            case SW -> NE;
        };
    }

    public Direction turnRight() {
        var dx = -this.dy;
        var dy = this.dx;

        return Direction.ALL.stream()
            .filter(d -> d.dx == dx && d.dy == dy)
            .findFirst()
            .orElseThrow();
    }

    public Direction turnLeft() {
        var dx = this.dy;
        var dy = -this.dx;

        return Direction.ALL.stream()
            .filter(d -> d.dx == dx && d.dy == dy)
            .findFirst()
            .orElseThrow();
    }

    public static void main(String[] args) {
        for (var dir : Direction.ALL) {
            System.out.println(dir);
            System.out.println(dir.opposite());
            System.out.println(dir.turnRight());
            System.out.println(dir.turnLeft());
            System.out.println();
        }
    }
}
