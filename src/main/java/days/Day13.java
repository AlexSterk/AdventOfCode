package days;

import setup.Day;

import java.util.*;
import java.util.stream.Collectors;

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
        int x = 0;
        int severity = 0;

        PriorityQueue<Integer> Q = new PriorityQueue<>(layers.keySet());
        while (!Q.isEmpty()) {
            Layer layer = layers.get(x);
            if (layer != null) {
                if (layer.x(x) == 0) severity += layer.severity();
                Q.poll();
            }
            x++;
        }

        return severity;
    }

    @Override
    public Object part2() {
        /*
        f_i(t) = t % (range_i * 2 - 2)
        if f_i(t) == 0 --> caught (scanner in position 0 at time t)
        find delay 'd' s.t. f_i(d + range_i) != 0 for all i
         */
        o: for (int t = 0; ; t++) {
            for (Layer layer : layers.values()) {
                int i = layer.x(t + layer.depth);
                if (i == 0) {
                    continue o;
                }
            }
            return t;
        }
    }

    @Override
    public int getDay() {
        return 13;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private record Layer(int depth, int range) {
        private int severity() {
            return range * depth;
        }

        private int x(int t) {
            return t % (range * 2 - 2);
        }
    }

    @Override
    public String partOneSolution() {
        return "1728";
    }

    @Override
    public String partTwoSolution() {
        return "3946838";
    }
}
