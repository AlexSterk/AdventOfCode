package util;

import java.util.List;

import static util.Line.*;

public enum Direction {
    E(1, 0), S(0, 1), W(-1, 0), N(0, -1), NE(1, -1), NW(-1, -1), SE(1, 1), SW(-1, 1);

    public final int dx, dy;

    public static final List<Direction> ALL = List.of(E, S, W, N, NE, NW, SE, SW);
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
}
