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
        beams.add(new Beam(Direction.E, new Grid.Tile<>(-1, 0,".", grid)));

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

        if (pastBeams.contains(beam)) {
            return;
        }
        pastBeams.add(beam);

        if (grid.getTile(beam.position.asPoint()) != null) {
            visited.add(beam.position);
        }

        var next = beam.position.asPoint().add(beam.dir.asPoint());
        var nextTile = grid.getTile(next);

        if (nextTile == null) {
            return;
        }

        switch (nextTile.data()) {
            case "." -> beams.add(new Beam(beam.dir, nextTile));
            case "-" -> {
                if (beam.dir == Direction.E || beam.dir == Direction.W) {
                    beams.add(new Beam(beam.dir, nextTile));
                } else {
                    beams.add(new Beam(Direction.W, nextTile));
                    beams.add(new Beam(Direction.E, nextTile));
                }
            }
            case "|" -> {
                if (beam.dir == Direction.N || beam.dir == Direction.S) {
                    beams.add(new Beam(beam.dir, nextTile));
                } else {
                    beams.add(new Beam(Direction.N, nextTile));
                    beams.add(new Beam(Direction.S, nextTile));
                }
            }
            case "/" -> beams.add(new Beam(switch (beam.dir) {
                case E -> Direction.N;
                case S -> Direction.W;
                case W -> Direction.S;
                case N -> Direction.E;
                default -> throw new IllegalStateException("Unexpected value: " + beam.dir);
            }, nextTile));
            case "\\" -> beams.add(new Beam(switch (beam.dir) {
                case E -> Direction.S;
                case S -> Direction.E;
                case W -> Direction.N;
                case N -> Direction.W;
                default -> throw new IllegalStateException("Unexpected value: " + beam.dir);
            }, nextTile));
        }

    }

    private record Beam(Direction dir, Grid.Tile<String> position) {
    }
}
