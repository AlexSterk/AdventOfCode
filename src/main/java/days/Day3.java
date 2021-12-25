package days;

import setup.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day3 extends Day {

    private List<Triangle> triangles;

    @Override
    public void processInput() {
        triangles = Arrays.stream(input.split("\n")).map(Triangle::stringToTriangle).collect(Collectors.toList());
    }

    @Override
    public Object part1() {
        return triangles.stream().filter(Triangle::isValid).count();
    }

    @Override
    public Object part2() {
        List<Triangle> newTriangles = new ArrayList<>();

        for (int i = 0; i < triangles.size(); i += 3) {
            Triangle one = triangles.get(i);
            Triangle two = triangles.get(i + 1);
            Triangle three = triangles.get(i + 2);

            newTriangles.add(new Triangle(one.a(), two.a(), three.a()));
            newTriangles.add(new Triangle(one.b(), two.b(), three.b()));
            newTriangles.add(new Triangle(one.c(), two.c(), three.c()));
        }

        return newTriangles.stream().filter(Triangle::isValid).count();
    }


    @Override
    public int getDay() {
        return 3;
    }

    @Override
    public String partOneSolution() {
        return "983";
    }

    @Override
    public String partTwoSolution() {
        return "1836";
    }

    private record Triangle(int a, int b, int c) {
        Triangle(String a, String b, String c) {
            this(Integer.parseInt(a), Integer.parseInt(b), Integer.parseInt(c));
        }

        static Triangle stringToTriangle(String s) {
            String[] split = s.split("\\s+");

            return new Triangle(split[1], split[2], split[3]);
        }

        boolean isValid() {
            return (a + b) > c && (a + c) > b && (b + c) > a;
        }
    }

}
