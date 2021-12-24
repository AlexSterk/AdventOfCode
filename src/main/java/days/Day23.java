package days;

import setup.Day;
import util.Graph;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static util.Line.Point;

public class Day23 extends Day {
    @Override
    public void processInput() {

    }

    @Override
    public Object part1() {
        State begin = State.parse(input);
        State end = new State(
                Map.of(
                        new Point(2, 1), new Amphipod(2, 1, 2, 1),
                        new Point(2, 2), new Amphipod(2, 2, 2, 1),
                        new Point(4, 1), new Amphipod(4, 1, 4, 10),
                        new Point(4, 2), new Amphipod(4, 2, 4, 10),
                        new Point(6, 1), new Amphipod(6, 1, 6, 100),
                        new Point(6, 2), new Amphipod(6, 2, 6, 100),
                        new Point(8, 1), new Amphipod(8, 1, 8, 1000),
                        new Point(8, 2), new Amphipod(8, 2, 8, 1000)
                )
        );

        Graph<State> graph = new Graph<>();
        Set<State> visited = new HashSet<>();
        Queue<State> queue = new ArrayDeque<>();

        queue.add(begin);
        graph.addNode(begin);

        while (!queue.isEmpty()) {
            State state = queue.poll();
            if (visited.contains(state)) continue;
            visited.add(state);

            for (Amphipod a : state.amphipods.values()) {
                Set<Point> targets = a.targets(state);
                for (Point target : targets) {
                    int energy = Math.abs(a.x - target.x()) + a.y + target.y();
                    energy *= a.factor;
                    HashMap<Point, Amphipod> newStateMap = new HashMap<>(state.amphipods);
                    newStateMap.remove(new Point(a.x, a.y));
                    newStateMap.put(target, new Amphipod(target.x(), target.y(), a.target, a.factor));

                    State newState = new State(newStateMap);
                    queue.offer(newState);
                    graph.addNode(newState);
                    graph.addEdge(state, newState, energy, true);
                }
            }
        }

        return graph.getDistance(begin, end);
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 23;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private record Amphipod(int x, int y, int target, int factor) {
        private Set<Point> targets(State state) {
            if (x == target) return Set.of();
//            if (y == 2 && state.amphipods.containsKey(new Point(x, 1))) return Set.of();
            Set<Point> targets = new HashSet<>();

            if (y > 0) {
                if (state.amphipods.containsKey(new Point(x, y - 1))) return Set.of();
                for (Integer i : Set.of(0, 1, 3, 5, 7, 9, 10)) {
                    targets.add(new Point(i, 0));
                }
            } else {
                targets.add(new Point(target, 2));
                targets.add(new Point(target, 1));
            }

//            targets.removeIf(state.amphipods::containsKey);
//            targets.removeIf(p -> state.amphipods.keySet().stream().anyMatch(a -> p.x() < x && p.x() < a.x() && a.x() < x));
//            targets.removeIf(p -> state.amphipods.keySet().stream().anyMatch(a -> p.x() > x && p.x() > a.x() && a.x() > x));
//            targets.removeIf(p -> p.y() == 2 && state.amphipods.containsKey(new Point(p.x(), 1)));

            Set<Point> set = new HashSet<>(targets);
            for (Point p : targets) {
                var b1 = state.amphipods.containsKey(p);
                var b2 = state.amphipods.keySet().stream().filter(a -> a.y() == 0 && p.x() < x && p.x() < a.x() && a.x() < x).findAny();
                var b3 = state.amphipods.keySet().stream().filter(a -> a.y() == 0 && p.x() > x && p.x() > a.x() && a.x() > x).findAny();
                var b4 = p.y() == 2 && state.amphipods.containsKey(new Point(p.x(), 1));
                if (b1
                        || b2.isPresent()
                        || b3.isPresent()
                        || b4) {
                    set.remove(p);
                }
            }
            targets = set;

            return targets;
        }
    }

    private record State(Map<Point, Amphipod> amphipods) {

        public static final Pattern PATTERN = Pattern.compile("[A-Z]");

        private static State parse(String s) {
            List<MatchResult> matchResults = PATTERN.matcher(s).results().toList();
            HashMap<Point, Amphipod> map = new HashMap<>();

            for (int i = 0; i < matchResults.size(); i++) {
                int x, y;
                x = (i % 4) * 2 + 2;
                y = i / 4 + 1;
                String group = matchResults.get(i).group();
                map.put(new Point(x, y), new Amphipod(x, y, switch (group) {
                    case "A" -> 2;
                    case "B" -> 4;
                    case "C" -> 6;
                    case "D" -> 8;
                    default -> throw new IllegalStateException("Unexpected value: " + group);
                }, switch (group) {
                    case "A" -> 1;
                    case "B" -> 10;
                    case "C" -> 100;
                    case "D" -> 1000;
                    default -> throw new IllegalStateException("Unexpected value: " + group);
                }));
            }

            return new State(map);
        }
    }
}
