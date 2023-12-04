package days;

import setup.Day;

import java.util.*;
import java.util.stream.Collectors;


/**
 * I'm aware that a dynamic programming approach would be better, but I'm too lazy to implement it.
 */
public class Day4 extends Day {
    private List<Card> cards;
    private Map<Integer, Card> cardsMap;

    @Override
    public void processInput() {
        cards = this.lines().stream().map(Card::parse).collect(Collectors.toList());
        cardsMap = cards.stream().collect(Collectors.toMap(Card::id, c -> c));
    }

    @Override
    public Object part1() {
        return cards.stream().mapToLong(Card::value).sum();
    }

    @Override
    public Object part2() {
        new ArrayList<>(cards).forEach(this::processWins);

        return cards.size();
    }

    private void processWins(Card card) {
        var wins = card.wins();
        var id = card.id();

        for (int i = 1; i <= wins; i++) {
            var nCard = cardsMap.get(id + i);
            cards.add(nCard);
            this.processWins(nCard);
        }
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
            var wins = this.wins();
            return (long) Math.floor(Math.pow(2, wins - 1));
        }

        public int wins() {
            var intersection = winning.stream().filter(numbers::contains).collect(Collectors.toSet());
            return intersection.size();
        }
    }

    @Override
    public String partOneSolution() {
        return "20407";
    }

    @Override
    public String partTwoSolution() {
        return "23806951";
    }
}
