package days;

import setup.Day;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day7 extends Day {

    private Map<String, Program> programs;
    private Program root;

    @Override
    public void processInput() {
        programs = Arrays.stream(input.split("\n")).map(Program::Program).collect(Collectors.toMap(p -> p.name, p -> p));
    }

    @Override
    public Object part1() {
        Set<String> hasSupport = programs.values().stream().flatMap(p -> p.balancing.stream()).collect(Collectors.toSet());
        List<Program> noSupport = this.programs.values().stream().filter(p -> !hasSupport.contains(p.name)).toList();
        if (noSupport.size() != 1) throw new IllegalStateException();
        root = noSupport.get(0);
        return root.name;
    }

    @Override
    public Object part2() {
        Program r = root;
        Map<Integer, Integer> counts = new HashMap<>();
        List<Integer> unbalanced = getWeights(r.balancing);
        while (!unbalanced.equals(Collections.nCopies(unbalanced.size(), unbalanced.get(0)))) {
            List<Integer> finalUnbalanced = unbalanced;
            counts = unbalanced.stream().collect(Collectors.toMap(i -> (int) finalUnbalanced.stream().filter(x -> x.equals(i)).count(), i -> i, (a, b) -> a));
            var oddOneOut = counts.get(1);
            var index = unbalanced.indexOf(oddOneOut);
            r = programs.get(r.balancing.get(index));
            unbalanced = getWeights(r.balancing);
        }
        Integer target = counts.get(Collections.max(counts.keySet()));
        int x = target - counts.get(1);
        return r.weight + x;
    }

    private List<Integer> getWeights(List<String> balancing) {
        List<Integer> weights = new ArrayList<>();
        for (String s : balancing) {
            Program p = programs.get(s);
            weights.add(p.weight + getWeights(p.balancing).stream().mapToInt(Integer::intValue).sum());
        }
        return weights;
    }

    @Override
    public int getDay() {
        return 7;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "xegshds";
    }

    @Override
    public String partTwoSolution() {
        return "299";
    }

    private record Program(String name, int weight, List<String> balancing) {
        private static final Pattern PATTERN = Pattern.compile("(?<name>\\w+) \\((?<weight>\\d+)\\)(?: -> (?<support>.+))?");

        private static Program Program(String s) {
            Matcher matcher = PATTERN.matcher(s);
            matcher.matches();
            String support = matcher.group("support");
            List<String> balancing = Collections.emptyList();
            if (support != null) {
                balancing = List.of(support.split(", "));
            }
            return new Program(matcher.group("name"), Integer.parseInt(matcher.group("weight")), balancing);
        }
    }
}
