package days;

import setup.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day1 extends Day {
    private List<Operation> operations;

    private static Operation stringToOperation(String s) {
        Operator o = s.charAt(0) == '+' ? Operator.ADDITION : Operator.SUBTRACTION;
        int n = Integer.parseInt(s.substring(1).trim());
        return new Operation(o, n);
    }

    public static void main(String[] args) {
        Day1 day1 = new Day1();
        day1.part1();
        day1.part2();
    }

    @Override
    public void processInput() {
        operations = Arrays.stream(input.trim().split("\n")).map(Day1::stringToOperation).collect(Collectors.toList());
    }

    @Override
    public void part1() {
        int freq = 0;
        for (Operation operation : operations) {
            freq = operation.execute(freq);
        }
        System.out.println(freq);
    }

    @Override
    public void part2() {
        int freq = 0;
        List<Integer> freqs = new ArrayList<>();

        int i = 0;

        while (!freqs.contains(freq)) {
            freqs.add(freq);
            freq = operations.get(i).execute(freq);
            i = (i + 1) % operations.size();
        }
        System.out.println(freq);
    }

    @Override
    public int getDay() {
        return 1;
    }

    private enum Operator {
        ADDITION,
        SUBTRACTION;

        int execute(int previous, int n) {
            if (this == ADDITION) return previous + n;
            else if (this == SUBTRACTION) return previous - n;
            return -1;
        }
    }

    private static record Operation(Operator operator, int number) {
        int execute(int previous) {
            return operator.execute(previous, number);
        }
    }

}



