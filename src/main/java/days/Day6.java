package days;

import setup.Day;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class Day6 extends Day {
    @Override
    public void processInput() {

    }

    @Override
    public Object part1() {
        Deque<String> queue = new ArrayDeque<>();

        String[] chars = input.trim().split("");
        for (int i = 0; i < chars.length; i++) {
            String c = chars[i];
            if (queue.contains(c)) {
                queue.clear();
                queue.add(c);
            } else {
                queue.add(c);
            }
            if (queue.size() == 4) {
                System.out.println(queue);
                return i + 1;
            }
        }

        return null;
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 6;
    }

    @Override
    public String partOneSolution() {
        return "1140";
    }

    @Override
    public boolean isTest() {
        return false;
    }
}
