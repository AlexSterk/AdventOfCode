package days;

import setup.Day;

import java.util.*;
import java.util.stream.Collectors;

public class Day8 extends Day {

    private static class Node {
        List<Integer> metadata = new ArrayList<>();
        List<Node> children = new ArrayList<>();

        int partOneMetadataSum() {
            return metadata.stream().mapToInt(x -> x).sum() + children.stream().mapToInt(Node::partOneMetadataSum).sum();
        }

        int partTwoMetadataSum() {
            return children.size() > 0 ?
                    metadata.stream()
                            .filter(i -> i <= children.size() && i > 0)
                            .map(i -> i - 1)
                            .map(children::get)
                            .mapToInt(Node::partTwoMetadataSum).sum() :
                    metadata.stream().mapToInt(x -> x).sum();
        }
    }

    static record NodeResult(Node node, int offset) {

    }

    Node root;

    @Override
    public void processInput() {
        List<Integer> numbers = Arrays.stream(input.trim().split("\s+")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
        NodeResult rootResult = parseNode(numbers);
        root = rootResult.node;
    }

    private NodeResult parseNode(List<Integer> numbers) {
        Node n = new Node();
        int children = numbers.get(0);
        int metadata = numbers.get(1);

        int offset = 2;

        for (int i = 0; i < children; i++) {
            List<Integer> newNumbers = numbers.subList(offset, numbers.size());
            NodeResult result = parseNode(newNumbers);
            n.children.add(result.node);
            offset += result.offset;
        }

        n.metadata = numbers.subList(offset, offset + metadata);
        return new NodeResult(n, offset + metadata);
    }

    @Override
    public void part1() {
        System.out.println(root.partOneMetadataSum());
    }

    @Override
    public void part2() {
        System.out.println(root.partTwoMetadataSum());
    }

    @Override
    public int getDay() {
        return 8;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}
