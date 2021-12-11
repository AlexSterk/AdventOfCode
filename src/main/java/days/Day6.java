package days;

import setup.Day;

import java.util.*;

public class Day6 extends Day {
    private List<Integer> banks;
    private List<List<Integer>> oldConfigs;

    @Override
    public void processInput() {
        banks = new ArrayList<>();
        for (String s : input.trim().split("\\s+")) {
            banks.add(Integer.parseInt(s));
        }
    }

    @Override
    public Object part1() {
        oldConfigs = new ArrayList<>();
        while (!oldConfigs.contains(banks)) {
            oldConfigs.add(List.copyOf(banks));
            int max = Collections.max(banks);
            int index = banks.indexOf(max);
            banks.set(index, 0);
            while (max > 0) {
                index = (index + 1) % banks.size();
                banks.set(index, banks.get(index) + 1);
                max--;
            }
        }

        return oldConfigs.size();
    }

    @Override
    public Object part2() {
        return oldConfigs.size() - oldConfigs.indexOf(banks);
    }

    @Override
    public int getDay() {
        return 6;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "11137";
    }

    @Override
    public String partTwoSolution() {
        return "1037";
    }
}
