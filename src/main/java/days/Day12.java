package days;

import setup.Day;
import util.CollectionUtil;
import util.Memoizer;

import java.util.ArrayList;
import java.util.List;

import static util.Memoizer.memoize;

public class Day12 extends Day {

    private List<Configuration> configurations;
    private Memoizer<Configuration, Integer> countWays;

    @Override
    public void processInput() {
        configurations = new ArrayList<>();
        for (String line : lines()) {
            configurations.add(Configuration.fromString(line));
        }
    }

    @Override
    public Object part1() {
        countWays = memoize(this::_countWays);

        int sum = 0;
        for (Configuration c : configurations) {
            sum += countWays.apply(c);
        }

        return sum;
    }

    private int _countWays(Configuration c) {
        String s = c.s;
        List<Integer> l = c.l;

        if (s.isEmpty()) {
            return l.isEmpty() ? 1 : 0;
        }

        if (l.isEmpty()) {
            return s.contains("#") ? 0 : 1;
        }

        if (s.length() < CollectionUtil.sum(l) + l.size() - 1) {
            return 0;
        }

        var ch = s.charAt(0);

        if (ch == '.') {
            return countWays.apply(new Configuration(s.substring(1), l));
        }

        if (ch == '#') {
            var r = l.get(0);
            var remaining = l.subList(1, l.size());

            for (int i = 0; i < r; i++) {
                if (s.charAt(i) == '.') {
                    return 0;
                }
            }
            if (s.length() >= r+1 && s.charAt(r) == '#') {
                return 0;
            }

            return countWays.apply(new Configuration(s.length() >= r+1 ?  s.substring(r + 1) : "", remaining));
        }

        var c1 = new Configuration('#' + s.substring(1), l);
        var c2 = new Configuration('.' + s.substring(1), l);

        return countWays.apply(c1) + countWays.apply(c2);
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 12;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "7090";
    }

    private record Configuration(String s, List<Integer> l) {

        @Override
        public String toString() {
            return "%s %s".formatted(s, l);
        }

        public static Configuration fromString(String s) {
            String[] parts = s.split(" ");
            String s1 = parts[0];
            List<Integer> l1 = new ArrayList<>();

            String s2 = parts[1];
            String[] listItems = s2.split(",");

            for (String listItem : listItems) {
                l1.add(Integer.parseInt(listItem));
            }

            return new Configuration(s1, List.copyOf(l1));
        }
    }
}
