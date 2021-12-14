package days;

import setup.Day;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Day14 extends Day {
    private Map<String, String> recipes;
    private String starting;

    @Override
    public void processInput() {
        recipes = new HashMap<>();
        String[] split1 = input.split("\n\n");

        for (String s : split1[1].split("\n")) {
            String[] split = s.split(" -> ");
            recipes.put(split[0], split[1]);
        }

        starting = split1[0].trim();
    }

    @Override
    public Object part1() {
        String polymer = starting;
        for (int i = 0; i < 10; i++) {
            int offset = 0;
            int length = polymer.length();
            for (int j = 0; j < length - 1; j++) {
                int k = j + offset;
                String substring = polymer.substring(k, k + 2);
                if (recipes.containsKey(substring)) {
                    polymer = polymer.substring(0, k + 1) + recipes.get(substring) + polymer.substring(k + 1);
                    offset++;
                }
            }
        }

        HashMap<Character, Integer> counts = new HashMap<>();
        for (char c : polymer.toCharArray()) {
            counts.merge(c, 1, Integer::sum);
        }

        return Collections.max(counts.values()) - Collections.min(counts.values());
    }

    @Override
    public Object part2() {
        Map<String, Long> substrings = new HashMap<>();
        for (int i = 0; i < starting.length() - 1; i++) {
            substrings.merge(starting.substring(i, i + 2), 1L, Long::sum);
        }

        for (int i = 0; i < 40; i++) {
            var temp = new HashMap<>(substrings);
            for (String s : temp.keySet()) {
                if (recipes.containsKey(s)) {
                    Long count = temp.get(s);
                    substrings.merge(s, -count, Long::sum);
                    String x = recipes.get(s);
                    substrings.merge(s.charAt(0) + x, count, Long::sum);
                    substrings.merge(x + s.charAt(1), count, Long::sum);
                }
            }
        }

        HashMap<Character, Long> counts = new HashMap<>();
        for (Map.Entry<String, Long> e : substrings.entrySet()) {
            counts.merge(e.getKey().charAt(0), e.getValue(), Long::sum);
            counts.merge(e.getKey().charAt(1), e.getValue(), Long::sum);
        }

        Long max = Collections.max(counts.values());
        Long min = Collections.min(counts.values());
        return (max - min) / 2;
    }

    @Override
    public int getDay() {
        return 14;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "3230";
    }

    @Override
    public String partTwoSolution() {
        return "3542388214529";
    }
}
