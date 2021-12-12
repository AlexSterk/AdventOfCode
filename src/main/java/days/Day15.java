package days;

import setup.Day;

public class Day15 extends Day {
    private Generator A;
    private Generator B;

    @Override
    public void processInput() {
        String[] lines = input.split("\n");
        String substring = lines[0].substring(24);
        A = new Generator(Integer.parseInt(substring), 16807);
        B = new Generator(Integer.parseInt(lines[1].substring(24)), 48271);
    }

    @Override
    public Object part1() {
        int count = 0;

        for (int i = 0; i < 40_000_000; i++) {
            String a = A.nextBitString().substring(16);
            String b = B.nextBitString().substring(16);
            if (a.equals(b)) count++;
        }

        return count;
    }

    @Override
    public boolean resetForPartTwo() {
        return true;
    }

    @Override
    public Object part2() {
        A.picky = 4;
        B.picky = 8;

        int count = 0;

        for (int i = 0; i < 5_000_000; i++) {
            String a = A.nextBitString().substring(16);
            String b = B.nextBitString().substring(16);
            if (a.equals(b)) count++;
        }

        return count;
    }

    @Override
    public int getDay() {
        return 15;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "592";
    }

    private static class Generator {
        private static final int DIV = 2147483647;
        private long prev;
        private final int factor;
        private int picky = -1;

        private Generator(int starting, int factor) {
            this.prev = starting;
            this.factor = factor;
        }

        private int next() {
            do {
                prev = prev * factor % DIV;
            } while (picky > 0 && prev % picky != 0);
            return (int) prev;
        }

        private String nextBitString() {
            String s = Integer.toBinaryString(next());
            return "0".repeat(32 - s.length()) + s;
        }
    }
}
