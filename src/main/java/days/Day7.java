package days;

import setup.Day;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day7 extends Day {

    private List<Program> programs;

    @Override
    public void processInput() {
        programs = Arrays.stream(input.split("\n")).map(Program::Program).toList();
    }

    @Override
    public Object part1() {
        Set<String> hasSupport = programs.stream().flatMap(p -> p.balancing.stream()).collect(Collectors.toSet());
        List<Program> noSupport = this.programs.stream().filter(p -> !hasSupport.contains(p.name)).toList();
        if (noSupport.size() != 1) throw new IllegalStateException();
        return noSupport.get(0).name;
    }

    @Override
    public Object part2() {
        return null;
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

    private record Program(String name, int weight, Set<String> balancing) {
        private static final Pattern PATTERN = Pattern.compile("(?<name>\\w+) \\((?<weight>\\d+)\\)(?: -> (?<support>.+))?");

        private static Program Program(String s) {
            Matcher matcher = PATTERN.matcher(s);
            matcher.matches();
            String support = matcher.group("support");
            Set<String> balancing = Collections.emptySet();
            if (support != null) {
                balancing = Set.of(support.split(", "));
            }
            return new Program(matcher.group("name"), Integer.parseInt(matcher.group("weight")), balancing);
        }
    }
}
