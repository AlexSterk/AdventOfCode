package days;

import setup.Day;
import static util.Annotations.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Day15 extends Day {

    private List<Step> steps;

    @Override
    public void processInput() {
        steps = Arrays.stream(input.trim().split(","))
                .map(Step::new)
                .toList();
    }

    @Solution("517315")
    @Override
    public Object part1() {
        return steps.stream().map(Step::toString).mapToLong(this::hash).sum();
    }

    @Solution("247763")
    @Override
    public Object part2() {
        var map = new HashMap<Integer, LinkedHashMap<String, Integer>>();

        for (Step step : steps) {
            int hash = hash(step.label);
            var box = map.computeIfAbsent(hash, k -> new LinkedHashMap<>());

            if (step.operation == '-') {
                box.remove(step.label);
            }

            if (step.operation == '=') {
                box.put(step.label, step.focalLength);
            }
        }

        long sum = 0;

        for (int i = 0; i < 255; i++) {
            var box = map.get(i);

            if (box == null || box.isEmpty()) continue;

            int j = i + 1;
            int k = 1;
            for (Integer f : box.sequencedValues()) {
                sum += (long) j * k * f;
                k++;
            }
        }

        return sum;
    }

    @Override
    public int getDay() {
        return 15;
    }

    private int hash(String s) {
        int cur = 0;

        for (int i = 0; i < s.length(); i++) {
            int ascii = s.charAt(i);
            cur += ascii;
            cur *= 17;
            cur = cur % 256;
        }

        return cur;
    }

    private static class Step {
        public final String label;
        public final char operation;
        public final Integer focalLength;

        public Step(String s) {
            var split = s.splitWithDelimiters("[=-]", 0);
            label = split[0];
            operation = split[1].charAt(0);
            focalLength = split.length > 2 ? Integer.parseInt(split[2]) : null;
        }

        @Override
        public String toString() {
            return "%s%c%s".formatted(label, operation, focalLength == null ? "" : focalLength);
        }
    }
}
