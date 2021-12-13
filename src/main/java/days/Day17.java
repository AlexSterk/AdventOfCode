package days;

import setup.Day;

import java.util.ArrayList;
import java.util.ListIterator;

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
            for (int j = 0; j < steps; j++) {
                cur = (cur + 1) % list.size();
            }
            cur++;
            list.add(cur, i);
        }

        return list.get(list.indexOf(2017) + 1);
    }

    @Override
    public Object part2() {
        return null;
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
}
