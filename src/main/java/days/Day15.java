package days;

import setup.Day;

import java.util.Arrays;
import java.util.List;

public class Day15 extends Day {

    private List<String> steps;

    @Override
    public void processInput() {
        steps = Arrays.stream(input.trim().split(",")).toList();
    }

    @Override
    public Object part1() {
        return steps.stream().mapToLong(this::hash).sum();
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 15;
    }

    @Override
    public boolean isTest() {
        return false;
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
}
