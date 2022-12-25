package days;

import setup.Day;

import java.util.*;

public class Day11 extends Day {

    private static List<Monkey> monkeys;

    private static boolean p1 = true;

    private static int mod;

    @Override
    public void processInput() {
        monkeys = Arrays.stream(input.split("(\r?\n){2}")).map(Monkey::parse).toList();
    }

    @Override
    public Object part1() {
        p1 = true;
        for (int i = 0; i < 20; i++) {
            for (Monkey monkey : monkeys) {
                monkey.turn();
            }
        }

        List<Integer> integers = new ArrayList<>(monkeys.stream().map(m -> m.inspected).sorted().toList());
        Collections.reverse(integers);


        return integers.get(0) * integers.get(1);
    }

    @Override
    public Object part2() {
        p1 = false;
        mod = monkeys.stream().map(m -> m.test).reduce((a, b) -> a * b).orElseThrow();
        for (int i = 0; i < 10000; i++) {
            for (Monkey monkey : monkeys) {
                monkey.turn();
            }
        }

        List<Integer> integers = new ArrayList<>(monkeys.stream().map(m -> m.inspected).sorted().toList());
        Collections.reverse(integers);

        return (long) integers.get(0) * (long) integers.get(1);
    }

    @Override
    public boolean resetForPartTwo() {
        return true;
    }

    @Override
    public int getDay() {
        return 11;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "110264";
    }

    @Override
    public String partTwoSolution() {
        return "23612457316";
    }

    private static class Monkey {

        private final Queue<Integer> items = new ArrayDeque<>();
        private final String operation;

        private final int test, ifTrue, ifFalse;

        private int inspected = 0;

        private Monkey(String operation, int test, int ifTrue, int ifFalse) {
            this.operation = operation;
            this.test = test;
            this.ifTrue = ifTrue;
            this.ifFalse = ifFalse;
        }

        private static Monkey parse(String input) {
            // get lines of input and trim each line
            String[] lines = input.lines().map(String::trim).skip(1).toArray(String[]::new);
            var startingItems = Arrays.stream(lines[0].replaceAll("Starting items: ", "").split(", ")).map(Integer::parseInt).toList();
            var operation = lines[1].replaceAll("Operation: new = ", "");
            var test = Integer.parseInt(lines[2].replaceAll("Test: divisible by ", ""));
            var ifTrue = Integer.parseInt(lines[3].replaceAll("If true: throw to monkey ", ""));
            var ifFalse = Integer.parseInt(lines[4].replaceAll("If false: throw to monkey ", ""));


            Monkey monkey = new Monkey(operation, test, ifTrue, ifFalse);
            monkey.items.addAll(startingItems);
            return monkey;
        }

        private void turn() {
            while (!items.isEmpty()) {
                long item = items.poll();

                item = applyOperation(item);

                if (p1) {
                    item = item / 3;
                } else {
                    item = item % mod;
                }

                int targetMonkey = item % test == 0 ? ifTrue : ifFalse;

                monkeys.get(targetMonkey).items.add((int) item);
                inspected++;
            }

        }

        private long applyOperation(Long value) {
            String m = operation.replaceAll("old", value.toString());

            boolean isAdd = m.contains("+");

            return Arrays.stream(m.split(" *[*+] *")).map(Long::parseLong).reduce(isAdd ? Long::sum : (a, b) -> a * b).orElseThrow();
        }

        @Override
        public String toString() {
            return "Monkey{" +
                    "items=" + items +
                    ", operation='" + operation + '\'' +
                    ", test=" + test +
                    ", ifTrue=" + ifTrue +
                    ", ifFalse=" + ifFalse +
                    '}';
        }
    }
}
