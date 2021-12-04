package days;

import setup.Day;

import java.util.PrimitiveIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day9 extends Day {

    private int players;
    private int last;

    @Override
    public void processInput() {
        Pattern p = Pattern.compile("\\D*(\\d+)\\D+(\\d+)\\D*");
        Matcher m = p.matcher(input);
        m.matches();
        players = Integer.parseInt(m.group(1));
        last = Integer.parseInt(m.group(2));
    }

    @Override
    public Object part1() {
        PrimitiveIterator.OfInt nextMarbles = IntStream.range(0, last + 1).iterator();
        Marble current = new Marble(nextMarbles.next());
        long[] scores = new long[players];

        game:
        while (true) {
            for (int i = 0; i < players; i++) {
                if (nextMarbles.hasNext()) {
                    Marble next = new Marble(nextMarbles.next());
                    if (next.value % 23 == 0) {
                        scores[i] += next.value;
                        Marble toRemove = current;
                        for (int j = 0; j < 7; j++) {
                            toRemove = toRemove.counterClockwise;
                        }
                        current = toRemove.clockwise;
                        scores[i] += toRemove.value;
                        toRemove.removeThis();
                    } else {
                        next.insertThisAfter(current.clockwise);
                        current = next;
                    }
                } else {
                    break game;
                }
            }
        }

        long max = Integer.MIN_VALUE;
        for (long score : scores) {
            max = Math.max(score, max);
        }
        return max;
    }

    @Override
    public Object part2() {
        last *= 100;
        return part1();
    }

    @Override
    public int getDay() {
        return 9;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private static class Marble {
        public final int value;

        Marble clockwise = this;
        Marble counterClockwise = this;

        Marble(int value) {
            this.value = value;
        }

        void insertThisAfter(Marble marbleToInsertAfter) {
            Marble oldClockwise = marbleToInsertAfter.clockwise;
            marbleToInsertAfter.clockwise = this;
            this.counterClockwise = marbleToInsertAfter;
            this.clockwise = oldClockwise;
            oldClockwise.counterClockwise = this;
        }

        void removeThis() {
            counterClockwise.clockwise = clockwise;
            clockwise.counterClockwise = counterClockwise;
        }
    }
}
