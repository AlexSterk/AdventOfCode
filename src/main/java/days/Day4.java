package days;

import setup.Day;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day4 extends Day {
    private List<Card> cards;

    @Override
    public void processInput() {
        cards = this.lines().stream().map(Card::parse).collect(Collectors.toList());
    }

    @Override
    public Object part1() {
        return cards.stream().mapToLong(Card::value).sum();
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 4;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    record Card(int id, Set<Integer> winning, Set<Integer> numbers) {
        public static Card parse(String s) {
            var split = s.split(":");
            var id = Integer.parseInt(split[0].replaceAll("\\D", ""));
            s = split[1];

            split = s.split("\\|");
            var winning = Arrays.stream(split[0].trim().split(" +")).map(Integer::parseInt).collect(Collectors.toSet());
            var numbers = Arrays.stream(split[1].trim().split(" +")).map(Integer::parseInt).collect(Collectors.toSet());

            return new Card(id, winning, numbers);
        }

        public long value() {
            var intersection = winning.stream().filter(numbers::contains).collect(Collectors.toSet());
            return (long) Math.floor(Math.pow(2, intersection.size() - 1));
        }
    }
}
