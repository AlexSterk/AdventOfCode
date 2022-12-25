package days;

import setup.Day;

public class Day25 extends Day {
    @Override
    public void processInput() {

    }

    private long parse(String number) {
        int l = number.length();
        long sum = 0;
        for (int i = 0; i < l; i++) {
            long num = switch (number.charAt(i)) {
                case '=' -> -2;
                case '-' -> -1;
                default -> number.charAt(i) - '0';
            } * (long) Math.pow(5, l - i - 1);
            sum += num;
        }
        return sum;
    }

    private String reverseParse(long n) {
        String radix5 = Long.toString(n, 5);
        // chars over 2 should carry
        int[] digits = new int[radix5.length()];
        for (int i = 0; i < radix5.length(); i++) {
            digits[i] = radix5.charAt(i) - '0';
        }
        for (int i = digits.length - 1; i > 0; i--) {
            int c = digits[i];
            if (c > 2) {
                digits[i] = c-5;
            }

            digits[i-1] += c/3;
        }

        StringBuilder sb = new StringBuilder();
        for (int digit : digits) {
            if (digit == -2) {
                sb.append('=');
            } else if (digit == -1) {
                sb.append('-');
            } else {
                sb.append(digit);
            }
        }

        return sb.toString();
    }

    @Override
    public Object part1() {
        long sum = lines().stream().mapToLong(this::parse).sum();
        return reverseParse(sum);
    }

    @Override
    public String partOneSolution() {
        return "2----0=--1122=0=0021";
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 25;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}
