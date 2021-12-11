package days;

import setup.Day;

public class Day1 extends Day {
    @Override
    public void processInput() {

    }

    @Override
    public Object part1() {
        int sum = 0;

        for (int i = 0; i < input.trim().length(); i++) {
            var n = input.charAt(i);
            var k = input.charAt((i + 1) % input.trim().length());

            if (n == k) sum += Integer.parseInt(Character.toString(n));
        }

        return sum;
    }

    @Override
    public Object part2() {
        int sum = 0;
        int length = input.trim().length();
        for (int i = 0; i < length; i++) {
            var n = input.charAt(i);
            var k = input.charAt((i + length / 2) % length);

            if (n == k) sum += Integer.parseInt(Character.toString(n));
        }

        return sum;
    }

    @Override
    public int getDay() {
        return 1;
    }

    @Override
    public String partOneSolution() {
        return "1119";
    }

    @Override
    public String partTwoSolution() {
        return "1420";
    }
}
