package days;

import setup.Day;
import util.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day2 extends Day {

    private List<String[]> guide;

    static private Map<String, Integer> map = new HashMap<>();
    static private Map<Pair<String, String>, Integer> outcomes = new HashMap<>();

    static private Map<Pair<String, String>, String> options = new HashMap<>();

    static private Map<String, Integer> stats  = new HashMap<>();

    static {
        map.put("X", 1);
        map.put("Y", 2);
        map.put("Z", 3);
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);

        stats.put("X", 0);
        stats.put("Y", 3);
        stats.put("Z", 6);

        outcomes.put(new Pair<>("A", "Y"), 6);
        outcomes.put(new Pair<>("B", "Z"), 6);
        outcomes.put(new Pair<>("C", "X"), 6);

        outcomes.put(new Pair<>("A", "X"), 3);
        outcomes.put(new Pair<>("B", "Y"), 3);
        outcomes.put(new Pair<>("C", "Z"), 3);

        outcomes.put(new Pair<>("A", "Z"), 0);
        outcomes.put(new Pair<>("B", "X"), 0);
        outcomes.put(new Pair<>("C", "Y"), 0);

        options.put(new Pair<>("A", "Y"), "A");
        options.put(new Pair<>("B", "Y"), "B");
        options.put(new Pair<>("C", "Y"), "C");
        options.put(new Pair<>("A", "X"), "C");
        options.put(new Pair<>("B", "X"), "A");
        options.put(new Pair<>("C", "X"), "B");
        options.put(new Pair<>("A", "Z"), "B");
        options.put(new Pair<>("B", "Z"), "C");
        options.put(new Pair<>("C", "Z"), "A");
    }

    @Override
    public void processInput() {
        guide = Arrays.stream(input.split("\n\r?")).map(s -> s.split("\s+")).collect(Collectors.toList());
    }

    @Override
    public Object part1() {
        int score = 0;
        for (String[] strings : guide) {
            score += map.get(strings[1]) + outcomes.get(new Pair<>(strings[0], strings[1]));
        }
        return score;
    }

    @Override
    public Object part2() {
        int score = 0;
        for (String[] strings : guide) {
            String choice = options.get(new Pair<>(strings[0], strings[1]));
            score += map.get(choice) + stats.get(strings[1]);
        }
        return score;
    }

    @Override
    public int getDay() {
        return 2;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "12458";
    }

    @Override
    public String partTwoSolution() {
        return "12683";
    }
}
