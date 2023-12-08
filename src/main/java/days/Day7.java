package days;

import setup.Day;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day7 extends Day {
    private List<Hand> hands;

    @Override
    public void processInput() {
        hands = lines().stream().map(Hand::parse).collect(Collectors.toList());
    }

    @Override
    public Object part1() {
        var sorted = hands.stream().sorted().toList();

        long sum = 0;
        for (int i = 0; i < sorted.size(); i++) {
            var hand = sorted.get(i);
            sum += (long) hand.bid * (i + 1);
        }

        return sum;
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 7;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private record Card(String value) implements Comparable<Card> {
        private static final String[] order = new String[]{
                "2", "3", "4", "5", "6", "7", "8", "9", "T",
                "J", "Q", "K", "A"
        };

        @Override
        public int compareTo(Card o) {
            return Integer.compare(this.intValue(), o.intValue());
        }

        private int intValue() {
            for (int i = 0; i < order.length; i++) {
                if (order[i].equals(value)) {
                    return i;
                }
            }
            return -1;
        }
    }

    private record Hand(List<Card> cards, int bid) implements Comparable<Hand> {
        public static Hand parse(String s) {
            var split = s.split(" ");
            var bid = Integer.parseInt(split[1]);
            var cards = Stream.of(split[0].split("")).map(Card::new).collect(Collectors.toList());
            return new Hand(cards, bid);
        }

        @Override
        public int compareTo(Hand o) {
            var thisType = getType();
            var otherType = o.getType();
            if (thisType == otherType) {
                int i = 0;
                do {
                    var thisCard = cards.get(i);
                    var otherCard = o.cards.get(i);
                    int c = thisCard.compareTo(otherCard);
                    if (c != 0) {
                        return c;
                    }
                    i++;
                } while (i < cards.size());
            }
            return Integer.compare(otherType.ordinal(), thisType.ordinal());
        }

        @Override
        public String toString() {
            return "%s %d (%s)".formatted(cards.stream().map(Card::value).collect(Collectors.joining()), bid, getType());
        }

        private Type getType() {
            for (Type type : Type.values()) {
                if (type.matches.test(cards)) {
                    return type;
                }
            }
            return null;
        }

        private enum Type {
            FIVE_OF_A_KIND(h -> h.stream().map(Card::value).distinct().count() == 1),
            FOUR_OF_A_KIND(h -> h.stream().map(Card::value).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).containsValue(4L)),
            FULL_HOUSE(h -> {
                var map = h.stream().map(Card::value).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
                return map.containsValue(3L) && map.containsValue(2L);
            }),
            THREE_OF_A_KIND(h -> h.stream().map(Card::value).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).containsValue(3L)),
            TWO_PAIR(h -> h.stream().map(Card::value).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).values().stream().filter(v -> v == 2L).count() == 2),
            ONE_PAIR(h -> h.stream().map(Card::value).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).containsValue(2L)),
            HIGH_CARD(h -> true);

            private final Predicate<List<Card>> matches;

            Type(Predicate<List<Card>> matches) {
                this.matches = matches;
            }
        }
    }
}
