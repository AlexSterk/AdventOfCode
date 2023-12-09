package days;

import setup.Day;
import util.Maths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Day8 extends Day {
    private Map<String, Node> nodes;
    private String directions;


    @Override
    public void processInput() {
        directions = lines().get(0);
        nodes  = new HashMap<>();

        Pattern p = Pattern.compile("\\w{3}");
        for (String s : lines().subList(2, lines().size())) {
            var m = p.matcher(s);
            m.find();
            var name = m.group();
            m.find();
            var left = m.group();
            m.find();
            var right = m.group();
            nodes.put(name, new Node(left, right));
        }
    }

    @Override
    public Object part1() {
        String start = "AAA";
        String end = "ZZZ";

        List<State> path = getPath(start, end);

        return path.size();
    }

    private boolean reachedEnd(String start, String end) {
        return start.endsWith(end.replaceAll("\\?", ""));
    }

    private List<State> getPath(String start, String end) {
        var chars = directions.toCharArray();
        int i = 0;
        List<State> path = new ArrayList<>();
        while (!reachedEnd(start, end)) {
            var node = nodes.get(start);
            char c = chars[i];
            start = performMove(c, node);
            i = (i + 1) % chars.length;
            path.add(new State(start, i));
        }
        return path;
    }

    private List<State> getLoopingPath(String start) {
        var chars = directions.toCharArray();
        int i = 0;
        List<State> path = new ArrayList<>();
        State s = new State(start, i);
        while (!path.contains(s)) {
            path.add(s);
            var node = nodes.get(start);
            char c = chars[i];
            start = performMove(c, node);
            i = (i + 1) % chars.length;
            s = new State(start, i);
        }
        path.add(s);

        return path;
    }

    private static String performMove(char c, Node node) {
        String start;
        if (c == 'L') {
            start = node.left;
        } else {
            start = node.right;
        }
        return start;
    }



    @Override
    public Object part2() {
        var endInA = nodes.keySet().stream().filter(s -> s.endsWith("A")).toList();
        var paths = endInA.stream().map(this::getLoopingPath).toList();

        long lcm = 1;

        for (List<State> path : paths) {
            var end = path.stream().filter(s -> s.current.endsWith("Z")).findAny().get();
            var stepsToEnd = path.indexOf(end);
            var loopStart = path.indexOf(path.get(path.size() - 1));
            var loopLength = path.size() - loopStart - 1;

            lcm = Maths.lcm(lcm, loopLength);

            System.out.printf("Steps to end: %d, loop start: %d, loop length: %d%n", stepsToEnd, loopStart, loopLength);
        }

        return lcm;
    }

    @Override
    public int getDay() {
        return 8;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "16531";
    }

    @Override
    public String partTwoSolution() {
        return "24035773251517";
    }

    private record Node(String left, String right) {}

    private record State(String current, int i) {}
}
