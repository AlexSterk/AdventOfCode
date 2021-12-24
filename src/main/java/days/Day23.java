package days;

import setup.Day;
import util.Graph;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static util.Line.Point;

public class Day23 extends Day {
    private boolean part2 = false;

    @Override
    public void processInput() {

    }

    @Override
    public Object part1() {
//        if (true) return null;
        State begin = State.parse(input);
        State end = new State(
                Map.of(
                        new Point(2, 1), Amphipod.A(2, 1),
                        new Point(2, 2), Amphipod.A(2, 2),
                        new Point(4, 1), Amphipod.B(4, 1),
                        new Point(4, 2), Amphipod.B(4, 2),
                        new Point(6, 1), Amphipod.C(6, 1),
                        new Point(6, 2), Amphipod.C(6, 2),
                        new Point(8, 1), Amphipod.D(8, 1),
                        new Point(8, 2), Amphipod.D(8, 2)
                )
        );

        return solve(begin, end);
    }

    private int solve(State begin, State end) {
        Graph<State> graph = new Graph<>();
        Set<State> visited = new HashSet<>();
        Queue<State> queue = new ArrayDeque<>();

        queue.add(begin);
        graph.addNode(begin);

        int s;

        while (!queue.isEmpty()) {
            s = queue.size();
            State state = queue.poll();
            if (!visited.add(state)) continue;

            if (state.equals(end)) System.out.println("END FOUND");

            for (Amphipod a : state.amphipods.values()) {
                Set<Point> targets = a.targets(state, part2);
                Optional<Point> furthestIntoBurrow = targets.stream().filter(p -> p.x() == a.target).max(Comparator.comparing(Point::y));
                if (furthestIntoBurrow.isPresent()) {
                    executeMove(graph, queue, state, a, furthestIntoBurrow.get());
                } else
                    for (Point target : targets) {
                        executeMove(graph, queue, state, a, target);
                    }
            }
        }

        System.out.println("Graph building complete");

        int distance = graph.getDistance(begin, end);
        System.out.println(distance);
        return distance;
    }

    private void executeMove(Graph<State> graph, Queue<State> queue, State state, Amphipod a, Point target) {
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

    @Override
    public Object part2() {
        part2 = true;
//        if (true) return null;
        State begin = State.parse(input);

        Set.copyOf(begin.amphipods.values()).stream().filter(a -> a.y() == 2).forEach(a -> {
            begin.amphipods.remove(new Point(a.x, a.y));
            begin.amphipods.put(new Point(a.x, 4), new Amphipod(a.x, 4, a.target, a.factor));
        });

        begin.amphipods.put(new Point(2, 2), Amphipod.D(2, 2));
        begin.amphipods.put(new Point(2, 3), Amphipod.D(2, 3));

        begin.amphipods.put(new Point(4, 2), Amphipod.C(4, 2));
        begin.amphipods.put(new Point(4, 3), Amphipod.B(4, 3));

        begin.amphipods.put(new Point(6, 2), Amphipod.B(6, 2));
        begin.amphipods.put(new Point(6, 3), Amphipod.A(6, 3));

        begin.amphipods.put(new Point(8, 2), Amphipod.A(8, 2));
        begin.amphipods.put(new Point(8, 3), Amphipod.C(8, 3));


        State end = new State(new HashMap<>());
        end.amphipods.put(new Point(2, 1), Amphipod.A(2, 1));
        end.amphipods.put(new Point(2, 2), Amphipod.A(2, 2));
        end.amphipods.put(new Point(2, 3), Amphipod.A(2, 3));
        end.amphipods.put(new Point(2, 4), Amphipod.A(2, 4));

        end.amphipods.put(new Point(4, 1), Amphipod.B(4, 1));
        end.amphipods.put(new Point(4, 2), Amphipod.B(4, 2));
        end.amphipods.put(new Point(4, 3), Amphipod.B(4, 3));
        end.amphipods.put(new Point(4, 4), Amphipod.B(4, 4));

        end.amphipods.put(new Point(6, 1), Amphipod.C(6, 1));
        end.amphipods.put(new Point(6, 2), Amphipod.C(6, 2));
        end.amphipods.put(new Point(6, 3), Amphipod.C(6, 3));
        end.amphipods.put(new Point(6, 4), Amphipod.C(6, 4));

        end.amphipods.put(new Point(8, 1), Amphipod.D(8, 1));
        end.amphipods.put(new Point(8, 2), Amphipod.D(8, 2));
        end.amphipods.put(new Point(8, 3), Amphipod.D(8, 3));
        end.amphipods.put(new Point(8, 4), Amphipod.D(8, 4));

        return solve(begin, end);
    }

    @Override
    public int getDay() {
        return 23;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "17400";
    }

    @Override
    public String partTwoSolution() {
        return "46120";
    }

    private record Amphipod(int x, int y, int target, int factor) {
        private static Amphipod A(int x, int y) {
            return new Amphipod(x, y, 2, 1);
        }

        private static Amphipod B(int x, int y) {
            return new Amphipod(x, y, 4, 10);
        }

        private static Amphipod C(int x, int y) {
            return new Amphipod(x, y, 6, 100);
        }

        private static Amphipod D(int x, int y) {
            return new Amphipod(x, y, 8, 1000);
        }

        private Set<Point> targets(State state, boolean part2) {
            Set<Point> targets = new HashSet<>();

            if (y > 0) {
                if (state.amphipods.containsKey(new Point(x, y - 1))) return Set.of();
                for (Integer i : Set.of(0, 1, 3, 5, 7, 9, 10)) {
                    targets.add(new Point(i, 0));
                }
            } else {
                if (part2) targets.add(new Point(target, 4));
                if (part2) targets.add(new Point(target, 3));
                targets.add(new Point(target, 2));
                targets.add(new Point(target, 1));
            }

            Set<Point> set = new HashSet<>(targets);
            for (Point p : targets) {

                if (
                        state.amphipods.containsKey(p)
                                || p.x() == target && p.y() <= y
                                || p.y() > 0 && state.amphipods.containsKey(new Point(p.x(), p.y() - 1))
                                || p.x() == target && state.amphipods.values().stream().anyMatch(a -> a.x == target && a.target != target)
                                || IntStream.rangeClosed(Math.min(x, p.x()) + 1, Math.max(x, p.x()) - 1).map(x1 -> state.amphipods.containsKey(new Point(x1, 0)) ? 1 : 0).sum() > 0
                ) {
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
                map.put(new Point(x, y), switch (group) {
                    case "A" -> Amphipod.A(x, y);
                    case "B" -> Amphipod.B(x, y);
                    case "C" -> Amphipod.C(x, y);
                    case "D" -> Amphipod.D(x, y);
                    default -> throw new IllegalStateException("Unexpected value: " + group);
                });
            }

            return new State(map);
        }
    }
}
