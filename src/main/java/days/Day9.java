package days;

import setup.Day;

import java.util.Stack;

public class Day9 extends Day {
    @Override
    public void processInput() {

    }

    @Override
    public Object part1() {
        if (!isTest()) return parse(input, false);
        for (String s : input.split("\n")) {
            System.out.print(s + " -> ");
            System.out.println(parse(s, false));
        }
        return null;
    }

    private int parse(String s, boolean returnGarbage) {
        int totalScore = 0;
        int currentGroupScore = 0;
        int garbage = 0;
        Stack<Character> stack = new Stack<>();

        for (char c : s.trim().toCharArray()) {
            if (!stack.isEmpty()) {
                char peek = stack.peek();
                if (peek == '!') {
                    stack.pop();
                    continue;
                }
                else if (peek == '<') {
                    switch (c) {
                        case '!' -> stack.push(c);
                        case '>' -> stack.pop();
                        default -> garbage++;
                    }
                    continue;
                }
            }
            switch (c) {
                case '{' -> {
                    currentGroupScore++;
                    stack.push(c);
                }
                case '<', '!' -> stack.push(c);
                case '}' -> {
                    stack.pop();
                    totalScore += currentGroupScore;
                    currentGroupScore--;
                }
            }
        }

        return returnGarbage ? garbage : totalScore;
    }

    @Override
    public Object part2() {
        if (!isTest()) return parse(input, true);
        for (String s : input.split("\n")) {
            System.out.print(s + " -> ");
            System.out.println(parse(s, true));
        }
        return null;
    }

    @Override
    public int getDay() {
        return 9;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "11898";
    }

    @Override
    public String partTwoSolution() {
        return "5601";
    }
}
