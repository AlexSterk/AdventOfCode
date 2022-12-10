package days;

import setup.Day;

import java.util.ArrayList;
import java.util.List;

public class Day10 extends Day {

    private int X = 1;
    private List<Integer> strengths  = new ArrayList<>();
    private int cycles = 1;

    @Override
    public void processInput() {

    }

    @Override
    public Object part1() {
        boolean wait = false;
        for (int i = 0; i < lines().size(); i++) {
            if ((cycles + 20) % 40 == 0) {
                strengths.add(cycles * X);
            }

            String[] args = lines().get(i).split(" ");

            if (wait) {
                X += Integer.parseInt(args[1]);
                wait = false;
            } else if (args[0].equals("addx")) {
                wait = true;
                i--;
            }

            cycles++;
        }


        return strengths.stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 10;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "12980";
    }
}
