package days;

import setup.Day;
import util.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day8 extends Day {
    private List<Pair<List<String>, List<String>>> displays;

    @Override
    public void processInput() {
        String[] lines = input.split("\n");

        displays = Arrays.stream(lines).map(line -> {
            String[] split = line.split("\\|");
            String patterns = split[0].trim();
            String input = split[1].trim();

            return new Pair<>(Stream.of(patterns.split(" ")).sorted(Comparator.comparing(String::length)).toList(), List.of(input.split(" ")));
        }).toList();
    }

    @Override
    public Object part1() {
//        System.out.println(displays);
        Map<Integer, Integer> countSegments = new HashMap<>();
        countSegments.put(2, 1);
        countSegments.put(4, 4);
        countSegments.put(3, 7);
        countSegments.put(7, 8);

        int c = 0;

        for (Pair<List<String>, List<String>> pair : displays) {
            for (String pattern : pair.b()) {
                if (countSegments.containsKey(pattern.length())) {
                    c++;
                }
            }

        }

        return c;
    }

    @Override
    public Object part2() {
        Map<String, Integer> originalSignals = new HashMap<>();
        originalSignals.put("abcefg", 0);
        originalSignals.put("cf", 1);
        originalSignals.put("acdeg", 2);
        originalSignals.put("acdfg", 3);
        originalSignals.put("bcdf", 4);
        originalSignals.put("abdfg", 5);
        originalSignals.put("abdefg", 6);
        originalSignals.put("acf", 7);
        originalSignals.put("abcdefg", 8);
        originalSignals.put("abcdfg", 9);

        int sum = 0;

        for (Pair<List<String>, List<String>> display : displays) {
            Map<Character, Character> mapping = new HashMap<>();
            Map<Character, Set<String>> containing = new HashMap<>();
            for (Character c : new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g'}) {
                Set<String> has = display.a().stream().filter(s -> s.indexOf(c) >= 0).collect(Collectors.toSet());
                containing.put(c, has);
            }
            for (Map.Entry<Character, Set<String>> e : containing.entrySet()) {
                if (e.getValue().size() == 6) mapping.put(e.getKey(), 'b');
                if (e.getValue().size() == 9) mapping.put(e.getKey(), 'f');
                if (e.getValue().size() == 4) mapping.put(e.getKey(), 'e');
                if (e.getValue().size() == 8) {
                    if (e.getValue().stream().map(String::length).toList().contains(2)) mapping.put(e.getKey(), 'c');
                    else mapping.put(e.getKey(), 'a');
                }
                if (e.getValue().size() == 7) {
                    if (e.getValue().stream().map(String::length).toList().contains(4)) mapping.put(e.getKey(), 'd');
                    else mapping.put(e.getKey(), 'g');
                }
            }
            String number = display.b().stream().map(pattern -> {
                char[] chars = pattern.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    chars[i] = mapping.get(chars[i]);
                }
                Arrays.sort(chars);
                return new String(chars);
            }).map(originalSignals::get).map(Object::toString).collect(Collectors.joining());
            sum += Integer.parseInt(number);
        }

        return sum;
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
        return "504";
    }

    @Override
    public String partTwoSolution() {
        return "1073431";
    }
}
