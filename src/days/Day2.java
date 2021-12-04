package days;

import setup.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Day2 extends Day {
    List<String> ids;

    @Override
    public void processInput() {
        ids = Arrays.asList(input.split("\n"));
    }

    @Override
    public Object part1() {
        int count2s = 0;
        int count3s = 0;

        for (String id : ids) {
            var map = new HashMap<Character, Integer>();
            for (char c : id.toCharArray()) {
                map.put(c, map.getOrDefault(c, 0) + 1);
            }

            if (map.containsValue(2)) count2s++;
            if (map.containsValue(3)) count3s++;
        }

        return count2s * count3s;
    }

    @Override
    public Object part2() {
        for (String id : ids) {
            l:
            for (String id2 : ids) {
                if (id.equals(id2)) continue;

                int diffs = 0;
                List<Character> common = new ArrayList<>();

                for (int i = 0; i < id.length(); i++) {
                    if (id.charAt(i) != id2.charAt(i)) diffs++;
                    else common.add(id.charAt(i));
                    if (diffs > 1) continue l;
                }

                return common.stream().map(Object::toString).collect(Collectors.joining());
            }
        }
        throw new RuntimeException("idk");
    }

    @Override
    public int getDay() {
        return 2;
    }
}
