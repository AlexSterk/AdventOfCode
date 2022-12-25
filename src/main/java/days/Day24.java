package days;

import setup.Day;
import util.Line.Point;

import java.util.*;
import java.util.stream.Collectors;

public class Day24 extends Day {
    private static int lastX;
    private static int lastY;
    private List<Blizzard> blizzards;
    private static int startX;
    private static int endX;

    @Override
    public void processInput() {
        List<String> lines = lines();
        startX = lines.get(0).indexOf('.');
        endX = lines.get(lines.size() - 1).indexOf('.');

        lastX = lines.get(0).length() - 1;
        lastY = lines.size() - 1;

        blizzards = new ArrayList<>();
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                Direction dir = switch (line.charAt(x)) {
                    case '^' -> Direction.NORTH;
                    case 'v' -> Direction.SOUTH;
                    case '>' -> Direction.EAST;
                    case '<' -> Direction.WEST;
                    default -> null;
                };
                if (dir != null) {
                    blizzards.add(new Blizzard(new Point(x, y), dir));
                }
            }
        }
    }

    @Override
    public Object part1() {
//        System.out.println(blizzards);
//        for (int i = 0; i < 5; i++) {
//            blizzards = blizzards.stream().map(Blizzard::move).toList();
//            System.out.println(blizzards);
//        }

        var start = new State(new Point(startX, 0), blizzards);
        int minMinutes = Integer.MAX_VALUE;
        Stack<State> stack = new Stack<>();
        stack.push(start);
        Map<State, Integer> minutes = new HashMap<>();
        minutes.put(start, 0);
        while (!stack.isEmpty()) {
            State state = stack.pop();
            var m = minutes.get(state);
            if (state.position.y() == lastY && state.position.x() == endX) {
                minMinutes = Math.min(minMinutes, m);
            }
            for (State nextState : state.nextStates()) {
                if (m + 1 < minutes.getOrDefault(nextState, Integer.MAX_VALUE)) {
                    if (!stack.contains(nextState)) stack.push(nextState);
                    minutes.put(nextState, m + 1);
                }
            }
        }

        return minMinutes;
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 24;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private record State(Point position, List<Blizzard> blizzards) {
        public List<State> nextStates() {
            List<State> possibleStates = new ArrayList<>();
            var movedBlizzards = blizzards.stream().map(Blizzard::move).toList();
            var blockedPoints = movedBlizzards.stream().map(Blizzard::position).collect(Collectors.toSet());
            if (!blockedPoints.contains(position)) {
                possibleStates.add(new State(position, movedBlizzards));
            }
            for (Direction value : Direction.values()) {
                var newPos = position.add(value.d);
                if (newPos.x() == endX && newPos.y() == lastY && !blockedPoints.contains(newPos)) {
                    possibleStates.add(new State(newPos, movedBlizzards));
                    continue;
                }
                if (newPos.x() >= 1 && newPos.x() < lastX && newPos.y() >= 1 && newPos.y() < lastY) {
                    if (!blockedPoints.contains(newPos)) {
                        possibleStates.add(new State(newPos, movedBlizzards));
                    }
                }
            }
            return possibleStates;
        }
    }

    private enum Direction {
        NORTH(new Point(0, -1)),
        SOUTH(new Point(0, 1)),
        EAST(new Point(1, 0)),
        WEST(new Point(-1, 0)),
        ;
        private final Point d;


        Direction(Point d) {
            this.d = d;
        }
    }

    private record Blizzard(Point position, Direction facing) {
        public Point nextPosition() {
                Point add = position.add(facing.d);

            if (add.y() >= lastY) {
                    add = new Point(add.x(), 1);
                }
                if (add.x() >= lastX) {
                    add = new Point(1, add.y());
                }
                if (add.y() == 0) {
                    add = new Point(add.x(), lastY - 1);
                }
                if (add.x() == 0) {
                    add = new Point(lastX - 1, add.y());
                }

            return add;
            }

            public Blizzard move() {
                return new Blizzard(nextPosition(), facing);
            }
        }
}
