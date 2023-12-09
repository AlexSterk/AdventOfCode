package days;

import setup.Day;
import util.Graph;

import java.util.HashMap;
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

        var chars = directions.toCharArray();
        int i = 0;
        int steps = 0;
        while (!start.equals(end)) {
            var node = nodes.get(start);
            if (chars[i] == 'L') {
                start = node.left;
            } else {
                start = node.right;
            }
            i = (i + 1) % chars.length;
            steps++;
        }

        return steps;
    }

    @Override
    public Object part2() {
        return null;
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

    private record Node(String left, String right) {

    }
}
