package days;

import setup.Day;
import util.Direction;
import util.Grid;

import java.util.*;

public class Day16 extends Day {
    private Grid<String> grid;
    private final Queue<Beam> beams = new ArrayDeque<>();

    @Override
    public void processInput() {
        grid = Grid.parseGrid(input);
    }

    @Override
    public Object part1() {
        Beam beam = new Beam(Direction.E, grid.getTile(0, 0));
        beams.add(beam);

        run();

        return beam.visited.size();
    }

    @Override
    public Object part2() {
        List<Beam> toTry = new ArrayList<>();

        for (Grid.Tile<String> t : grid.getColumn(0)) {
            toTry.add(new Beam(Direction.E, t));
        }
        for (Grid.Tile<String> t : grid.getColumn(grid.width - 1)) {
            toTry.add(new Beam(Direction.W, t));
        }
        for (Grid.Tile<String> t : grid.getRow(0)) {
            toTry.add(new Beam(Direction.S, t));
        }
        for (Grid.Tile<String> t : grid.getRow(grid.height - 1)) {
            toTry.add(new Beam(Direction.N, t));
        }

        for (Beam b : toTry) {
            beams.add(b);
            run();
        }

        int max = 0;
        for (Beam b : toTry) {
            max = Math.max(max, b.visited.size());
        }

        return max;
    }

    public void run() {
        Set<Beam> visited = new HashSet<>();
        while (!beams.isEmpty()) {
            var beam = beams.poll();

            if (beam.position == null || visited.contains(beam)) { // we already visited this beam, terminate the run
                continue;
            }
            visited.add(beam);

            switch (beam.position.data()) {
                case "." -> beams.add(beam.move(beam.dir));
                case "-" -> {
                    if (beam.dir == Direction.E || beam.dir == Direction.W) {
                        beams.add(beam.move(beam.dir));
                    } else {
                        beams.add(beam.move(Direction.W));
                        beams.add(beam.move(Direction.E));
                    }
                }
                case "|" -> {
                    if (beam.dir == Direction.N || beam.dir == Direction.S) {
                        beams.add(beam.move(beam.dir));
                    } else {
                        beams.add(beam.move(Direction.N));
                        beams.add(beam.move(Direction.S));
                    }
                }
                case "/" -> beams.add(beam.move(switch (beam.dir) {
                    case E -> Direction.N;
                    case S -> Direction.W;
                    case W -> Direction.S;
                    case N -> Direction.E;
                    default -> throw new IllegalStateException("Unexpected value: " + beam.dir);
                }));
                case "\\" -> beams.add(beam.move(switch (beam.dir) {
                    case E -> Direction.S;
                    case S -> Direction.E;
                    case W -> Direction.N;
                    case N -> Direction.W;
                    default -> throw new IllegalStateException("Unexpected value: " + beam.dir);
                }));
            }
        }
    }

    @Override
    public int getDay() {
        return 16;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "7632";
    }

    @Override
    public String partTwoSolution() {
        return "8023";
    }

    private record Beam(Direction dir, Grid.Tile<String> position, Set<Grid.Tile<String>> visited) {
        public Beam(Direction dir, Grid.Tile<String> position) {
            this(dir, position, new HashSet<>());
        }

        public Beam move(Direction dir) {
            visited.add(position);
            return new Beam(dir, position.grid().getTile(position.asPoint().add(dir.asPoint())), visited);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Beam beam = (Beam) o;
            return dir == beam.dir && Objects.equals(position, beam.position);
        }

        @Override
        public int hashCode() {
            return Objects.hash(dir, position);
        }

        @Override
        public String toString() {
            return "Beam{" +
                    "dir=" + dir +
                    ", position=" + position +
                    '}';
        }
    }
}
