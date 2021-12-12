package days;

import setup.Day;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day10 extends Day {

    private int current, skip;

    @Override
    public void processInput() {

    }

    @Override
    public Object part1() {
        current = 0;
        skip = 0;
        List<Integer> lengths = Arrays.stream(input.trim().split(",")).map(Integer::parseInt).toList();
        List<Integer> numbers = IntStream.rangeClosed(0, 255).boxed().collect(Collectors.toCollection(CircularList::new));

        knotHashRound(lengths, numbers);

        return numbers.get(0) * numbers.get(1);
    }

    @Override
    public Object part2() {
        return KnotHash.getHash(input.trim());
    }

    private void knotHashRound(List<Integer> lengths, List<Integer> numbers) {
        for (Integer length : lengths) {
            List<Integer> sublist = IntStream.range(current, current + length)
                    .boxed()
                    .map(numbers::get)
                    .collect(Collectors.toCollection(ArrayList::new));
            Collections.reverse(sublist);
            int finalCurrent = current;
            IntStream.range(0, sublist.size()).forEach(i -> numbers.set(i + finalCurrent, sublist.get(i)));
            current += (length + skip++) % numbers.size();
        }
    }

    @Override
    public String partOneSolution() {
        return "5577";
    }

    @Override
    public String partTwoSolution() {
        return "44f4befb0f303c0bafd085f97741d51d";
    }

    @Override
    public int getDay() {
        return 10;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    public static class KnotHash {
        private static final List<Integer> suffix = List.of(17, 31, 73, 47, 23);
        private final List<Integer> numbers = IntStream.rangeClosed(0, 255).boxed().collect(Collectors.toCollection(CircularList::new));
        private final List<Integer> lengths;
        private int current = 0;
        private int skip;
        private KnotHash(List<Integer> lengths) {
            this.lengths = lengths;
        }

        public static String getHash(String s) {
            return new KnotHash(Stream.concat(s.chars().boxed(), suffix.stream()).toList()).getHash();
        }

        public static String getBits(String hash) {
            StringBuilder bitString = new StringBuilder(128);

            for (int i = 0; i < hash.length(); i++) {
                String c = hash.substring(i, i + 1);
                int n = Integer.parseInt(c, 16);
                String bits = Integer.toBinaryString(n);
                bits = "0".repeat(4 - bits.length()) + bits;
                bitString.append(bits);
            }
            return bitString.toString();
        }

        private void round() {
            for (Integer length : lengths) {
                List<Integer> sublist = IntStream.range(current, current + length)
                        .boxed()
                        .map(numbers::get)
                        .collect(Collectors.toCollection(ArrayList::new));
                Collections.reverse(sublist);
                int finalCurrent = current;
                IntStream.range(0, sublist.size()).forEach(i -> numbers.set(i + finalCurrent, sublist.get(i)));
                current += (length + skip++) % numbers.size();
            }
        }

        private void runRounds() {
            IntStream.range(0, 64).forEach(i -> round());
        }

        private List<Integer> denseHash() {
            List<Integer> dense = new ArrayList<>();
            for (int i = 0; i < 16; i++) {
                List<Integer> sub = getSublist(i);
                Integer integer = xorSublist(sub);
                dense.add(integer);
            }
            return dense;
        }

        private Integer xorSublist(List<Integer> sub) {
            Optional<Integer> reduce = sub.stream().reduce((a, b) -> a ^ b);
            return reduce.get();
        }

        private List<Integer> getSublist(int i) {
            return numbers.subList(i * 16, i * 16 + 16);
        }

        private String getHash() {
            runRounds();
            List<Integer> denseHash = denseHash();
            return denseHash.stream()
                    .map(i -> Integer.toString(i, 16))
                    .map(s -> s.length() == 1 ? "0" + s : s)
                    .collect(Collectors.joining());
        }
    }

    private static class CircularList<T> extends ArrayList<T> {
        @Override
        public T get(int index) {
            return super.get(index % size());
        }

        @Override
        public T set(int index, T element) {
            return super.set(index % size(), element);
        }
    }
}
