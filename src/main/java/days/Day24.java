package days;

import setup.Day;
import util.Annotations.TestInput;
import util.Pair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import static util.Annotations.Solution;

//@TestInput
public class Day24 extends Day {

    private List<Hailstone> hailstones;

    @Override
    public void processInput() {
        hailstones = lines().stream().map(Hailstone::parse).toList();
    }

    @Solution("12938")
    @Override
    public Object part1() {
        var testArea = isTest() ? new Pair<>(7L, 27L) : new Pair<>(200000000000000L, 400000000000000L);
        long intersections = 0;

        for (int i = 0; i < hailstones.size(); i++) {
            for (int j = i+1; j < hailstones.size(); j++) {
                var h1 = hailstones.get(i);
                var h2 = hailstones.get(j);

                var intersection = h1.intersectsWithXY(h2);
                if (intersection != null) {
                    double xi = intersection.a(), yi = intersection.b();
                    // print time at position
                    if (xi >= testArea.a() && xi <= testArea.b()
                            && yi >= testArea.a() && yi <= testArea.b()
                            && h1.timeAtPosition(xi) >= 0 && h2.timeAtPosition(xi) >= 0) {
                        intersections++;
                    }
                }
            }
        }

        return intersections;
    }

    @Override
    public Object part2() {
        // z3 in python
        try {
            // Command to execute the Python script
            String[] command = {"python", "src/main/java/days/Day24.py"};

            // Create a ProcessBuilder
            ProcessBuilder pb = new ProcessBuilder(command);

            // Start the process
            Process process = pb.start();

            // Read the output from the Python script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                return line;
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getDay() {
        return 24;
    }

    private record Hailstone(double x, double y, double z, double vx, double vy, double vz) {
        public Hailstone(String[] in) {
            this(
                    Double.parseDouble(in[0]),
                    Double.parseDouble(in[1]),
                    Double.parseDouble(in[2]),
                    Double.parseDouble(in[3]),
                    Double.parseDouble(in[4]),
                    Double.parseDouble(in[5])
            );
        }

        public static Hailstone parse(String in) {
            return new Hailstone(in.split("\\s*[,@]\\s*"));
        }

        public double gradient() {
            if (vx == 0) throw new IllegalArgumentException("Vertical line");
            return vy / vx;
        }

        public double yIntercept() {
            return y - gradient() * x;
        }

        public Pair<Double, Double> intersectsWithXY(Hailstone other) {
            if (gradient() == other.gradient()) {
                System.out.println("Parallel lines");
                return null;
            }

            double xi = (other.yIntercept() - yIntercept()) / (gradient() - other.gradient());
            double yi = gradient() * xi + yIntercept();

            return new Pair<>(xi, yi);
        }

        public double timeAtPosition(double x) {
            return (x - this.x) / vx;
        }
    }
}
