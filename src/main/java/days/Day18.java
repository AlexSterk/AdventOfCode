package days;

import setup.Day;
import util.Pair;

import java.util.*;
import java.util.stream.Stream;

public class Day18 extends Day {

    private List<SnailfishNumber.PairNumber> snailfishNumbers;

    @Override
    public void processInput() {
        snailfishNumbers = Arrays.stream(input.split("\n"))
                .map(s -> {
                    ArrayDeque<Character> chars = new ArrayDeque<>();
                    for (char c : s.toCharArray()) {
                        chars.offer(c);
                    }
                    return chars;
                })
                .map(s1 -> (SnailfishNumber.PairNumber) SnailfishNumber.parse(s1, null)).toList();
    }

    @Override
    public Object part1() {
//        snailfishNumbers.forEach(System.out::println);
        SnailfishNumber.PairNumber x = snailfishNumbers.stream().reduce(SnailfishNumber::reduce).get();
        return x.magnitude();
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 18;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private static abstract class SnailfishNumber {
        private static SnailfishNumber parse(Queue<Character> s, PairNumber parent) {
            PairNumber num = new PairNumber(parent);
            while (!s.isEmpty()) {
                char c = s.poll();
                switch (c) {
                    case '[' -> num.left = parse(s, num);
                    case ',' -> num.right = parse(s, num);
                    case ']' -> {
                        return num;
                    }
                    case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                        return new SingleNumber(Character.getNumericValue(c), parent);
                    }
                }
            }

            return null;
        }

        private static PairNumber reduce(PairNumber a, PairNumber b) {
            PairNumber p = new PairNumber(null);
            a.parent = p;
            b.parent = p;
            p.left = a;
            p.right = b;

            while (true) {
                if (!explode(p) && !split(p)) break;
            }

            return p;
        }

        private static boolean explode(PairNumber p) {
            List<Pair<SingleNumber, Integer>> flat = p.flatten(0);
            Optional<Pair<SingleNumber, Integer>> first = flat.stream().filter(x -> x.b() > 4).findFirst();
            if (first.isPresent()) {
                int i = flat.indexOf(first.get());
                PairNumber num = first.get().a().parent;

                if (i > 0) {
                    SingleNumber neighbour = flat.get(i - 1).a();
                    neighbour.value += ((SingleNumber) num.left).value;
                }
                if (i < flat.size() - 2) {
                    SingleNumber neighbour = flat.get(i + 2).a();
                    neighbour.value += ((SingleNumber) num.right).value;
                }

                PairNumber parent = num.parent;
                if (parent.left == num) {
                    parent.left = new SingleNumber(0, parent);
                } else {
                    parent.right = new SingleNumber(0, parent);
                }

                return true;
            }
            return false;
        }

        private static boolean split(PairNumber p) {
            List<Pair<SingleNumber, Integer>> flat = p.flatten(0);
            Optional<Pair<SingleNumber, Integer>> first = flat.stream().filter(x -> x.a().value >= 10).findFirst();
            if (first.isPresent()) {
                SingleNumber num = first.get().a();
                if (num.parent.left == num) {
                    PairNumber left = new PairNumber(num.parent);
                    left.left = new SingleNumber((int) Math.floor(num.value / 2.0), left);
                    left.right = new SingleNumber((int) Math.ceil(num.value / 2.0), left);
                    num.parent.left = left;
                } else {
                    PairNumber right = new PairNumber(num.parent);
                    right.left = new SingleNumber((int) Math.floor(num.value / 2.0), right);
                    right.right = new SingleNumber((int) Math.ceil(num.value / 2.0), right);
                    num.parent.right = right;
                }
                return true;
            }
            return false;
        }

        @Override
        public abstract String toString();

        public abstract List<Pair<SingleNumber, Integer>> flatten(int depth);

        public abstract int magnitude();

        private static class PairNumber extends SnailfishNumber {
            private PairNumber parent;
            private SnailfishNumber left;
            private SnailfishNumber right;

            private PairNumber(PairNumber parent) {
                this.parent = parent;
            }

            @Override
            public String toString() {
                return "[%s,%s]".formatted(left, right);
            }

            @Override
            public List<Pair<SingleNumber, Integer>> flatten(int depth) {
                return Stream.concat(left.flatten(depth + 1).stream(), right.flatten(depth + 1).stream()).toList();
            }

            @Override
            public int magnitude() {
                return 3 * left.magnitude() + 2 * right.magnitude();
            }
        }

        private static class SingleNumber extends SnailfishNumber {
            private final PairNumber parent;
            private int value;

            private SingleNumber(int value, PairNumber parent) {
                this.value = value;
                this.parent = parent;
            }

            @Override
            public String toString() {
                return Integer.toString(value);
            }

            @Override
            public List<Pair<SingleNumber, Integer>> flatten(int depth) {
                return List.of(new Pair<>(this, depth));
            }

            @Override
            public int magnitude() {
                return value;
            }
        }
    }
}
