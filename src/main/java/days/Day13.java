package days;

import setup.Day;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day13 extends Day {

    private Map<Integer, Layer> layers;

    @Override
    public void processInput() {
        layers = Arrays.stream(input.split("\n"))
                .map(s -> new Layer(Integer.parseInt(s.substring(0, s.indexOf(':'))), Integer.parseInt(s.substring(s.indexOf(' ') + 1))))
                .collect(Collectors.toMap(l -> l.depth, l -> l));
    }

    @Override
    public Object part1() {
        return runRound(0);
    }

    private int runRound(int x) {
        int severity = 0;

        PriorityQueue<Integer> Q = new PriorityQueue<>(layers.keySet());
        while (!Q.isEmpty()) {
            Layer layer = layers.get(x);
            if (layer != null) {
                if (layer.x == 0) severity += layer.severity();
                Q.poll();
            }
            layers.values().forEach(Layer::move);
            x++;
        }


        return severity;
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 13;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private static class Layer {
        private final int depth;
        private final int range;

        private int vx = 1;
        private int x = 0;

        private Layer(int depth, int range) {
            this.depth = depth;
            this.range = range;
        }

        private void move() {
            x += vx;
            if (x == 0 || x == range - 1) vx = -vx;
        }

        private int severity() {
            return range * depth;
        }

        private void reset() {
            x = 0;
            vx = 1;
        }
    }
}
