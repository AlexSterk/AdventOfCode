package days;

import setup.Day;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

public class Day20 extends Day {

    private CircularList list;
    private List<Node> order;

    @Override
    public void processInput() {
        order = IntStream.range(0, lines().size()).mapToObj(i -> new Node(Integer.parseInt(lines().get(i)), i)).toList();
        list = new CircularList(this.order);

        assert list.size() == order.size();
    }

    @Override
    public Object part1() {
        Node zero = mix();
        return solution(zero);
    }

    private long solution(Node zero) {
        int i = list.indexOf(zero);
        return list.get(i + 1000).value + list.get(i + 2000).value + list.get(i + 3000).value;
    }

    private Node mix() {
        Node zero = null;
        for (Node n : order) {
            int i = list.indexOf(n);
            if (n.value == 0) {
                zero = n;
                continue;
            }
            list.remove(i);
            list.add(i + n.value, n);
            if (isTest()) {
                System.out.println(n + " " + list);
            }
        }
        return zero;
    }

    @Override
    public Object part2() {
        order = order.stream().map(n -> new Node(n.value * 811589153, n.originalIndex)).toList();
        list = new CircularList(this.order);

        Node zero = null;

        for (int i = 0; i < 10; i++) {
            zero = mix();
        }

        return solution(zero);
    }

    @Override
    public int getDay() {
        return 20;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "2275";
    }

    @Override
    public String partTwoSolution() {
        return "4090409331120";
    }

    record Node(long value, int originalIndex) {
        @Override
        public String toString() {
            return Long.toString(value);
        }
    }

    private static class CircularList extends ArrayList<Node> {
        public CircularList(Collection<? extends Node> c) {
            super(c);
        }

        @Override
        public Node get(int index) {
            return super.get(index % size());
        }


        public void add(long index, Node element) {
            if (index < 0) {
                index = index + -Math.floorDiv(index, size()) * size();
            }

            super.add((int) (index % size()), element);
        }
    }
}
