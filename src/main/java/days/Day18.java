package days;

import setup.Day;
import util.Direction;
import util.Grid;
import util.Line;
import util.Maths;

import java.util.*;

public class Day18 extends Day {

    private List<Instruction> instructions;

    @Override
    public void processInput() {
        instructions = isPart2() ? lines().stream().map(Instruction::fromStringP2).toList() : lines().stream().map(Instruction::fromStringP1).toList();
    }

    @Override
    public Object part1() {
        List<Line.Point> vertices = new ArrayList<>();
        Line.Point current = new Line.Point(0,0);

        vertices.add(current);

        for (Instruction instruction : instructions) {
            var d = instruction.dir.asPoint().multiply(instruction.amount);
            current = current.add(d);
            vertices.add(current);
        }

        var overallArea = Maths.shoelaceArea(vertices);
        long perimeterArea = 0;
        for (int i = 0; i < vertices.size(); i++) {
            var p1 = vertices.get(i);
            var p2 = vertices.get((i+1) % vertices.size());
            perimeterArea += p1.manhattanDistance(p2);
        }

        return Maths.PicksTheorem.interiorArea(overallArea, perimeterArea) + perimeterArea;
    }

    @Override
    public Object part2() {
        return part1();
    }

    @Override
    public int getDay() {
        return 18;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "70026";
    }

    @Override
    public String partTwoSolution() {
        return "68548301037382";
    }

    @Override
    public boolean resetForPartTwo() {
        return true;
    }

    private record Instruction(Direction dir, int amount) {
        public static Instruction fromStringP1(String s) {
            var split = s.split(" ");
            var dir = split[0];
            var amount = Integer.parseInt(split[1]);

            var mapDir = switch (dir) {
                case "U" -> Direction.N;
                case "D" -> Direction.S;
                case "L" -> Direction.W;
                case "R" -> Direction.E;
                default -> throw new IllegalArgumentException("Invalid direction: " + dir);
            };

            return new Instruction(mapDir, amount);
        }

        public static Instruction fromStringP2(String s) {
            var split = s.split(" ");
            var hex = split[2].replaceAll("[()#]", "");
            var amount = Integer.parseInt(hex.substring(0,5), 16);

            var mapDir = switch (hex.substring(5)) {
                case "0" -> Direction.E;
                case "1" -> Direction.S;
                case "2" -> Direction.W;
                case "3" -> Direction.N;
                default -> throw new IllegalArgumentException("Invalid direction: " + hex);
            };

            return new Instruction(mapDir, amount);
        }
    }
}
