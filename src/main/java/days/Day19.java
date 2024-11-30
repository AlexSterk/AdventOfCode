package days;

import setup.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static util.Annotations.Solution;
import static util.Annotations.TestInput;

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

    @Solution("")
    @Override
    public Object part2() {
        return null;
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

    private record Part(int x, int m, int a, int s) {
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
