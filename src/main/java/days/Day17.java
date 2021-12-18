package days;

import setup.Day;

import java.util.ArrayList;

public class Day17 extends Day {
    public final int steps = Integer.parseInt(input.trim());

    @Override
    public void processInput() {

    }

    @Override
    public Object part1() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(0);
        int cur = 0;
        for (int i = 1; i <= 2017; i++) {
            cur = (cur + steps) % list.size() + 1;
            list.add(cur, i);
        }
        return list.get(list.indexOf(2017) + 1);
    }

    @Override
    public Object part2() {
        // 0 always in index 0 so we need to find index 1
        int cur = 0;
        int v = 0;
        for (int i = 1; i < 50_000_000; i++) {
            cur = (cur + steps) % i + 1;
            if (cur == 1) v = i;
        }

        return v;
    }

    @Override
    public int getDay() {
        return 17;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "1025";
    }

    @Override
    public String partTwoSolution() {
        return "37803463";
    }
}
