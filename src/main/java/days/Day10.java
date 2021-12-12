package days;

import setup.Day;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day10 extends Day {

    private int current, skip;
    private List<Integer> lengths;
    private List<Integer> numbers;

    @Override
    public void processInput() {

    }

    @Override
    public Object part1() {
        current = 0;
        skip = 0;
        lengths = Arrays.stream(input.trim().split(",")).map(Integer::parseInt).toList();
        numbers = IntStream.rangeClosed(0, 255).boxed().collect(Collectors.toCollection(CircularList::new));

        knotHashRound(lengths, numbers);

        return numbers.get(0) * numbers.get(1);
    }

    @Override
    public Object part2() {
        current = 0;
        skip = 0;
        lengths = Stream.concat(input.trim().chars().boxed(), Stream.of(17, 31, 73, 47, 23)).toList();
        numbers = IntStream.rangeClosed(0, 255).boxed().collect(Collectors.toCollection(CircularList::new));

        IntStream.range(0, 64).forEach(i -> knotHashRound(lengths, numbers));

        List<Integer> dense = IntStream.range(0, 16)
                .mapToObj(i -> numbers.subList(i * 16, i * 16 + 16))
                .map(sub -> sub.stream().reduce((a, b) -> a ^ b))
                .map(Optional::get).toList();

        String hash = dense.stream()
                .map(i -> Integer.toString(i, 16))
                .map(s -> s.length() == 1 ? "0" + s : s)
                .collect(Collectors.joining());
        System.out.println(hash.length());
        return hash;
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
