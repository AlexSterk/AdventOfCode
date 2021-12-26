package days;

import setup.Day;
import util.Pair;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day9 extends Day {

    private final Pattern parser = Pattern.compile("\\((\\d+)x(\\d+)\\)");

    @Override
    public void processInput() {

    }

    @Override
    public Object part1() {
        String decode = decode(input.trim());

        return decode.length();
    }

    private String decode(String in) {
        List<MatchResult> matchResults = parser.matcher(in).results().collect(Collectors.toCollection(ArrayList::new));
        matchResults.removeIf(m -> matchResults.stream().anyMatch(m2 -> m.start() >= m2.end() && m.start() < m2.end() + Integer.parseInt(m2.group(1))));

        return matchResults.stream().map(matchResult -> {
            int toRead = Integer.parseInt(matchResult.group(1));
            int toRepeat = Integer.parseInt(matchResult.group(2));

            String read = in.substring(matchResult.end(), matchResult.end() + toRead);
            String repeat = read.repeat(toRepeat);

            return new Pair<>(matchResult.group() + read, repeat);
        }).reduce(in, (s, p) -> s.replaceFirst(Pattern.quote(p.a()), p.b()), (s1, s2) -> s2);
    }

    @Override
    public Object part2() {
        Map<String, Long> counts = new HashMap<>();

        counts.put(input.trim(), 1L);

        while (counts.keySet().stream().anyMatch(s -> parser.matcher(s).find())) {
            for (String s : Set.copyOf(counts.keySet())) {
                List<MatchResult> res = parser.matcher(s).results().collect(Collectors.toCollection(ArrayList::new));
                res.removeIf(m -> res.stream().anyMatch(m2 -> m.start() >= m2.end() && m.start() < m2.end() + Integer.parseInt(m2.group(1))));

                if (res.size() > 0) {
                    long val = counts.remove(s);
                    String remainder = s;
                    for (MatchResult m : res) {
                        String substring = s.substring(m.end(), m.end() + Integer.parseInt(m.group(1)));
                        counts.merge(substring, Integer.parseInt(m.group(2)) * val, Long::sum);

                        remainder = remainder.replaceFirst(Pattern.quote(m.group() + substring), "");
                    }
                    counts.merge(remainder, val, Long::sum);
                }
            }
        }

        return counts.entrySet().stream().mapToLong(e -> e.getKey().length() * e.getValue()).sum();
    }

    @Override
    public int getDay() {
        return 9;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "152851";
    }

    @Override
    public String partTwoSolution() {
        return "11797310782";
    }
}
