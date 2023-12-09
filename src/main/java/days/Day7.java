package days;

import setup.Day;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day7 extends Day {
    private List<? extends Hand> hands;

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
        hands = hands.stream().map(JokerHand::fromHand).toList();

        return part1();
    }

    @Override
    public int getDay() {
        return 7;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "248422077";
    }

    @Override
    public String partTwoSolution() {
        return "249817836";
    }

    private static class Card implements Comparable<Card> {
        private static final String[] order = new String[]{
                "2", "3", "4", "5", "6", "7", "8", "9", "T",
                "J", "Q", "K", "A"
        };

        private final String value;

        private Card(String value) {
            this.value = value;
        }

        @Override
        public int compareTo(Card o) {
            return Integer.compare(this.intValue(), o.intValue());
        }

        int intValue() {
            for (int i = 0; i < order.length; i++) {
                if (order[i].equals(value)) {
                    return i;
                }
            }
            return -1;
        }

        String value() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private static class Hand implements Comparable<Hand> {
        final List<? extends Card> cards;
        final int bid;

        private Hand(List<? extends Card> cards, int bid) {
            this.cards = cards;
            this.bid = bid;
        }

        private static Hand parse(String s) {
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
            return Integer.compare(otherType, thisType);
        }

        @Override
        public String toString() {
            return "%s %d (%s)".formatted(cards.stream().map(Card::value).collect(Collectors.joining()), bid, typeString());
        }

        Map<String, Long> getCardCounts() {
            return cards.stream().map(Card::value).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        }

        int getType() {
            var map = getCardCounts();

            if (map.size() == 1) return 0; // Five of a kind
            if (map.containsValue(4L)) return 1; // Four of a kind
            if (map.containsValue(3L) && map.containsValue(2L)) return 2; // Full house
            if (map.containsValue(3L)) return 3; // Three of a kind
            if (map.values().stream().filter(v -> v == 2L).count() == 2) return 4; // Two pair
            if (map.containsValue(2L)) return 5; // One pair
            return 6; // High card
        }

        private String typeString() {
            return switch (getType()) {
                case 0 -> "Five of a kind";
                case 1 -> "Four of a kind";
                case 2 -> "Full house";
                case 3 -> "Three of a kind";
                case 4 -> "Two pair";
                case 5 -> "One pair";
                case 6 -> "High card";
                default -> throw new IllegalStateException("Unexpected value: " + getType());
            };
        }
    }

    private static class JokerCard extends Card {
        private JokerCard(String value) {
            super(value);
        }

        @Override
        int intValue() {
            return value().equals("J") ? -1 : super.intValue();
        }
    }

    private static class JokerHand extends Hand {
        private JokerHand(List<? extends JokerCard> cards, int bid) {
            super(cards, bid);
        }

        private static JokerHand fromHand(Hand hand) {
            return new JokerHand(hand.cards.stream().map(Card::value).map(JokerCard::new).toList(), hand.bid);
        }

        @Override
        int getType() {
            var map = getCardCounts();
            var jokers = map.remove("J");
            if (jokers == null) {
                return super.getType();
            }

            if (map.size() <= 1) return 0; // Five of a kind
            Long highest = Collections.max(map.values());
            if (highest + jokers >= 4) return 1; // Four of a kind
            if (map.size() == 2) return 2; // Full house
            if (highest + jokers >= 3) return 3; // Three of a kind
            // Two pair is impossible with jokers
            return 5; // One pair
            // High card is impossible with jokers
        }
    }
}
