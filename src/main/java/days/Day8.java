package days;

import setup.Day;
import util.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day8 extends Day {
    private static final Map<Integer, Boolean[]> SEGMENTS_TO_DISPLAY = new HashMap<>();

    static {
        SEGMENTS_TO_DISPLAY.put(0, new Boolean[]{true, true, true, false, true, true, true});
        SEGMENTS_TO_DISPLAY.put(1, new Boolean[]{false, false, true, false, false, true, false});
        SEGMENTS_TO_DISPLAY.put(2, new Boolean[]{true, false, true, true, true, false, true});
        SEGMENTS_TO_DISPLAY.put(3, new Boolean[]{true, false, true, true, false, true, true});
        SEGMENTS_TO_DISPLAY.put(4, new Boolean[]{false, true, true, true, false, true, false});
        SEGMENTS_TO_DISPLAY.put(5, new Boolean[]{true, true, false, true, false, true, true});
        SEGMENTS_TO_DISPLAY.put(6, new Boolean[]{true, true, false, true, true, true, false});
        SEGMENTS_TO_DISPLAY.put(7, new Boolean[]{true, false, true, false, false, true, false});
        SEGMENTS_TO_DISPLAY.put(8, new Boolean[]{true, true, true, true, true, true, true});
        SEGMENTS_TO_DISPLAY.put(9, new Boolean[]{true, true, true, true, false, true, true});
    }

    private List<Pair<List<String>, List<String>>> displays;

    @Override
    public void processInput() {
        String[] lines = input.split("\n");

        displays = Arrays.stream(lines).map(line -> {
            String[] split = line.split("\\|");
            String patterns = split[0].trim();
            String input = split[1].trim();

            return new Pair<>(List.of(patterns.split(" ")), List.of(input.split(" ")));
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
        return "504";
    }
}
