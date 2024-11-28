package days;

import setup.Day;
import util.Direction;
import util.Grid;

import java.util.*;

public class Day16 extends Day {
    private Grid<String> grid;
    private Queue<Beam> beams = new ArrayDeque<>();
    private Set<Grid.Tile<String>> visited = new HashSet<>();

    private Set<Beam> pastBeams = new HashSet<>();

    @Override
    public void processInput() {
        grid = Grid.parseGrid(input);
    }

    @Override
    public Object part1() {
        beams.add(new Beam(Direction.E, grid.getTile(0, 0)));

        while (!beams.isEmpty()) {
            move();
        }

        return visited.size();
    }

    @Override
    public Object part2() {
        return null;
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

    private void move() {
        var beam = beams.poll();

        if (beam == null) {
            return;
        }

        if (pastBeams.contains(beam) || beam.position == null) {
            return;
        }
        pastBeams.add(beam);
        visited.add(beam.position);

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

    private record Beam(Direction dir, Grid.Tile<String> position) {
        public Beam move(Direction dir) {
            return new Beam(dir, position.grid().getTile(position.asPoint().add(dir.asPoint())));
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
    }
}
