package days;

import setup.Day;

import java.util.*;

public class Day11 extends Day {

    private static List<Monkey> monkeys;

    @Override
    public void processInput() {
        monkeys = Arrays.stream(input.split("(\r?\n){2}")).map(Monkey::parse).toList();
    }

    @Override
    public Object part1() {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < monkeys.size(); j++) {
                Monkey monkey = monkeys.get(j);
//                System.out.println("Monkey " + j);
                monkey.turn();
            }
        }

        List<Integer> integers = new ArrayList<>(monkeys.stream().map(m -> m.inspected).sorted().toList());
        Collections.reverse(integers);


        return integers.get(0) * integers.get(1);
    }

    @Override
    public Object part2() {
        return null;
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
                int item = items.poll();
//                System.out.println("Inspecting item: " + item);
                item = applyOperation(item);
//                System.out.println("New worry: " + operation + " = " + item);
                item = item / 3;
//                System.out.println("Divided by 3 = " + item);
//                System.out.println("Test: " + item + " % " + test + " = " + item % test);
                int targetMonkey = item % test == 0 ? ifTrue : ifFalse;
//                System.out.println("Throwing to monkey " + targetMonkey);
                monkeys.get(targetMonkey).items.add(item);
                inspected++;
            }

        }

        private int applyOperation(Integer value) {
            String m = operation.replaceAll("old", value.toString());

            boolean isAdd = m.contains("+");


            return Arrays.stream(m.split(" *[*+] *")).map(Integer::parseInt).reduce(isAdd ? Integer::sum : (a, b) -> a * b).orElseThrow();
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
