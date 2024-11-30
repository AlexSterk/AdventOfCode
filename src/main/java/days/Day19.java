package days;

import setup.Day;
import util.Pair;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static util.Annotations.Solution;

//@TestInput
public class Day19 extends Day {

    private Map<String, Workflow> workflows;
    private List<Part> parts;

    @Override
    public void processInput() {
        var split = input.split("(\r?\n){2}");
        workflows = split[0].lines().map(Workflow::fromString).collect(Collectors.toMap(Workflow::name, w -> w));
        parts = split[1].lines().map(Part::fromString).collect(Collectors.toList());
    }

    @Solution("374873")
    @Override
    public Object part1() {
        List<Part> accepted = new ArrayList<>();

        for (Part part : parts) {
            String wf = "in";

            while (true) {
                if (wf.equals("A")) {
                    accepted.add(part);
                    break;
                }

                if (wf.equals("R")) {
                    break;
                }

                Workflow workflow = workflows.get(wf);
                if (workflow == null) {
                    throw new IllegalArgumentException("Invalid workflow");
                }

                for (Rule rule : workflow.rules) {
                    if (rule.passes(part)) {
                        wf = rule.workflow;
                        break;
                    }
                }
            }
        }

        return accepted.stream().mapToLong(Part::sum).sum();
    }

    @Solution("122112157518711")
    @Override
    public Object part2() {
        var ranges = new HashMap<String, Pair<Integer, Integer>>();
        for (String l : new String[]{"x", "m", "a", "s"}) {
            ranges.put(l, new Pair<>(1, 4000));
        }

        return count(ranges, "in");
    }

    private long count(Map<String, Pair<Integer, Integer>> ranges, String wf) {
        System.out.println(wf);

        if (wf.equals("R")) {
            return 0;
        }

        if (wf.equals("A")) {
            // get the length of the ranges, multiply them all
            long prod = 1;
            for (Pair<Integer, Integer> range : ranges.values()) {
                prod *= range.b() - range.a() + 1;
            }
            return prod;
        }

        Workflow workflow = workflows.get(wf);
        long total = 0;

        for (Rule rule : workflow.rules) {
            if (!rule.variable.isEmpty()) {
                var cat = rule.variable;
                var op = rule.operator;
                var val = rule.value;

                var low = ranges.get(cat).a();
                var high = ranges.get(cat).b();

                var trueRange = op.equals("<") ? new Pair<>(low, val - 1) : new Pair<>(val + 1, high);
                var falseRange = op.equals("<") ? new Pair<>(val, high) : new Pair<>(low, val);

                if (trueRange.a() <= trueRange.b()) {
                    var rangesCopy = new HashMap<>(ranges);
                    rangesCopy.put(cat, trueRange);
                    total += count(rangesCopy, rule.workflow);
                }

                if (falseRange.a() <= falseRange.b()) {
                    ranges = new HashMap<>(ranges);
                    ranges.put(cat, falseRange);
                } else {
                    break;
                }

            } else {
                total += count(ranges, rule.workflow);
            }
        }

        return total;
    }

    @Override
    public int getDay() {
        return 19;
    }

    private record Rule(String variable, String operator, int value, String workflow) {
        public static Rule fromString(String input) {
            // possible input: a<2006:qkq
            Pattern pattern = Pattern.compile("(\\w+)([<>=!]+)(\\d+):(\\w+)");
            var matcher = pattern.matcher(input);
            if (matcher.find()) {
                return new Rule(matcher.group(1), matcher.group(2), Integer.parseInt(matcher.group(3)) ,matcher.group(4));
            }

            if (input.matches("\\w+")) {
                return new Rule("", "", 0, input);
            }

            throw new IllegalArgumentException("Invalid input: %s".formatted(input));
        }

        public boolean passes(Part part) {
            switch (operator) {
                case "<" -> {
                    return switch (variable) {
                        case "x" -> part.x < value;
                        case "m" -> part.m < value;
                        case "a" -> part.a < value;
                        case "s" -> part.s < value;
                        default -> false;
                    };
                }
                case ">" -> {
                    return switch (variable) {
                        case "x" -> part.x > value;
                        case "m" -> part.m > value;
                        case "a" -> part.a > value;
                        case "s" -> part.s > value;
                        default -> false;
                    };
                }
                case "" -> {
                    return true;
                }
                default -> throw new IllegalArgumentException("Invalid operator");
            }
        }
    }

    private record Workflow(String name, List<Rule> rules) {
        public static Workflow fromString(String input) {
            // possible input: px{a<2006:qkq,m>2090:A,rfg}
            Pattern pattern = Pattern.compile("(\\w+)\\{(.+)}");
            var matcher = pattern.matcher(input);
            if (matcher.find()) {
                var rules = matcher.group(2).split(",");
                return new Workflow(matcher.group(1), Stream.of(rules).map(Rule::fromString).toList());
            }
            throw new IllegalArgumentException("Invalid input");
        }
    }

    private record Part(Integer x, Integer m, Integer a, Integer s) {
        public static Part fromString(String input) {
            // possible input: {x=787,m=2655,a=1222,s=2876}
            Pattern pattern = Pattern.compile("\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)}");
            var matcher = pattern.matcher(input);
            if (matcher.find()) {
                return new Part(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)),
                        Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
            }
            throw new IllegalArgumentException("Invalid input");
        }

        public long sum() {
            return x + m + a + s;
        }
    }
}
