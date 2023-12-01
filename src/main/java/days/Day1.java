package days;

import setup.Day;
import util.Pair;

import java.util.List;
import java.util.regex.Pattern;

public class Day1 extends Day {
    @Override
    public void processInput() {

    }

    @Override
    public Object part1() {
        var lines = this.lines().stream().map(s -> s.replaceAll("[a-z]", "")).toList();
        var toAdd = lines.stream()
                .map(s -> s.charAt(0) + s.substring(s.length() - 1))
                .mapToInt(Integer::parseInt)
                .boxed().toList();

        return toAdd.stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public Object part2() {
        var nums = List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "\\d");
        var lines = this.lines()
                .stream()
                .map(s -> nums.stream().flatMap(n -> this.findOccurrences(s, n).stream()).toList())
                .map(l -> l.stream().sorted(Pair.comparingByB()).toList())
                .map(l -> l.get(0).a() + l.get(l.size() - 1).a())
                .toList();

        return lines.stream().mapToInt(Integer::parseInt).sum();
    }

    private List<Pair<String, Integer>> findOccurrences(String haystack, String needle) {
        var pattern = Pattern.compile(needle);
        var matcher = pattern.matcher(haystack);
        return matcher.results().map(r -> new Pair<>(this.replaceNumbers(r.group()), r.start())).toList();
    }

    private String replaceNumbers(String s) {
        return switch (s) {
            case "one" -> "1";
            case "two" -> "2";
            case "three" -> "3";
            case "four" -> "4";
            case "five" -> "5";
            case "six" -> "6";
            case "seven" -> "7";
            case "eight" -> "8";
            case "nine" -> "9";
            default -> s;
        };
    }

    @Override
    public int getDay() {
        return 1;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "55386";
    }

    @Override
    public String partTwoSolution() {
        return "54824";
    }
}
