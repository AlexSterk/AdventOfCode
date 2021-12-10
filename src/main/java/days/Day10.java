package days;

import setup.Day;

import java.util.List;
import java.util.Stack;

public class Day10 extends Day {
    private List<String> lines;

    @Override
    public void processInput() {
        lines = List.of(input.split("\n"));
    }

    @Override
    public Object part1() {
        return lines.stream().mapToInt(Day10::findIllegal).sum();
    }

    private static int findIllegal(String line) {
        Stack<Character> stack = new Stack<>();
        for (char c : line.toCharArray()) {
            switch (c) {
                case '<', '{', '[', '(' -> stack.add(c);
                case '>' -> {
                    if (stack.pop() != '<') return 25137;
                }
                case ']' -> {
                    if (stack.pop() != '[') return 57;
                }
                case '}' -> {
                    if (stack.pop() != '{') return 1197;
                }
                case ')' -> {
                    if (stack.pop() != '(') return 3;
                }
            }
        }
        return 0;
    }

    private static long autocomplete(String line) {
        Stack<Character> stack = new Stack<>();
        for (char c : line.toCharArray()) {
            switch (c) {
                case '<', '{', '[', '(' -> stack.add(c);
                case '>', '}', ']', ')' -> stack.pop();
            }
        }
        long score = 0;
        while (!stack.isEmpty()) {
            char c = stack.pop();
            score *= 5;
            score += switch (c) {
                case '(' -> 1;
                case '[' -> 2;
                case '{' -> 3;
                case '<' -> 4;
                default -> throw new IllegalStateException();
            };
        }
        return score;
    }

    @Override
    public Object part2() {
        List<Long> scores = lines.stream()
                .filter(s -> findIllegal(s) == 0)
                .map(Day10::autocomplete)
                .sorted()
                .toList();

        return scores.get(scores.size() / 2);
    }

    @Override
    public int getDay() {
        return 10;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}
