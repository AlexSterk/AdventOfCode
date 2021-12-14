package days;

import setup.Day;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

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
        return null;
    }

    @Override
    public int getDay() {
        return 14;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}
