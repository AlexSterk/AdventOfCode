package days;

import setup.Day;

import java.util.LinkedList;

public class Day6 extends Day {
    @Override
    public void processInput() {

    }

    @Override
    public Object part1() {
        return getMarker(4);
    }

    private Integer getMarker(int targetSize) {
        LinkedList<String> queue = new LinkedList<>();
        String[] chars = input.trim().split("");
        for (int i = 0; i < chars.length; i++) {
            String c = chars[i];
            int j = queue.indexOf(c);
            if (j != -1) {
                for (int i1 = 0; i1 < j + 1; i1++) {
                    queue.removeFirst();
                }
            }
            queue.add(c);
            if (queue.size() == targetSize) {
                return i + 1;
            }
        }
        return null;
    }

    @Override
    public Object part2() {
        return getMarker(14);
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
    public String partTwoSolution() {
        return "3495";
    }

    @Override
    public boolean isTest() {
        return false;
    }
}
