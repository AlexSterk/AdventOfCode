package days;

import setup.Day;
import util.Grid;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Day23 extends Day {

    private final Grid.InfiniteGrid<Tile> grid = new Grid.InfiniteGrid<>(() -> Tile.EMPTY);
    private List<Elf> elves;

    @Override
    public void processInput() {
        Grid<String> stringGrid = Grid.parseGrid(input);
        stringGrid.getAll().forEach((tile) -> grid.set(tile.x() + 1000, tile.y() + 1000, tile.data().equals("#") ? Tile.ELF : Tile.EMPTY));
        elves = grid.getAll().stream().filter(tile -> tile.data() == Tile.ELF).map(Elf::new).toList();

    }

    @Override
    public Object part1() {
        for (int i = 0; i < 10; i++) {
            simulateRound();

            if (elves.stream().allMatch(Elf::allNeighboursEmpty)) {
                break;
            }
        }

        var minX = elves.stream().mapToInt(e -> e.position.x()).min().orElseThrow();
        var maxX = elves.stream().mapToInt(e -> e.position.x()).max().orElseThrow();
        var minY = elves.stream().mapToInt(e -> e.position.y()).min().orElseThrow();
        var maxY = elves.stream().mapToInt(e -> e.position.y()).max().orElseThrow();

        Grid.InfiniteGrid<Tile> subgrid = grid.subgrid(minX, maxX, minY, maxY);

        return subgrid.getAll().stream().filter(tile -> tile.data() == Tile.EMPTY).count();
    }

    @Override
    public Object part2() {
        int i = 1;
        do {
            simulateRound();
            i++;
        } while (!elves.stream().allMatch(Elf::allNeighboursEmpty));

        return i;
    }

    private void simulateRound() {
        elves.forEach(Elf::propose);
        // only move elves that have a unique proposal
        Map<Grid.Tile<Tile>, Integer> counts = new HashMap<>();
        elves.stream().map(e -> e.proposedPosition).forEach(p -> counts.merge(p, 1, Integer::sum));
        elves.stream().filter(elf -> counts.get(elf.proposedPosition) == 1).forEach(Elf::move);
    }

    @Override
    public int getDay() {
        return 23;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public boolean resetForPartTwo() {
        return true;
    }

    private enum Tile {
        ELF, EMPTY;


        @Override
        public String toString() {
            return switch (this) {
                case ELF -> "#";
                case EMPTY -> ".";
            };
        }
    }

    private enum Direction {
        E(1, 0), S(0, 1), W(-1, 0), N(0, -1), NE(1, -1), NW(-1, -1), SE(1, 1), SW(-1, 1);

        public final int dx, dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    private static class Elf {
        static final List<Direction> order = List.of(Direction.N, Direction.S, Direction.W, Direction.E);
        Grid.Tile<Tile> position;
        Grid.Tile<Tile> proposedPosition;
        Grid<Tile> grid;
        int startingDirection = -1;

        public Elf(Grid.Tile<Tile> position) {
            this.position = position;
            this.grid = position.grid();
        }

        public boolean allNeighboursEmpty() {
            return Arrays.stream(Direction.values()).allMatch(d -> grid.getTile(position.x() + d.dx, position.y() + d.dy).data() == Tile.EMPTY);
        }

        public void propose() {
            startingDirection = (startingDirection + 1) % order.size();
            proposedPosition = null;
            if (allNeighboursEmpty()) {
                proposedPosition = position;
                return;
            }
            for (int i = 0; i < order.size(); i++) {
                Direction direction = order.get((startingDirection + i) % order.size());
                switch (direction) {
                    case N -> {
                        if (Stream.of(Direction.N, Direction.NE, Direction.NW)
                                .map(d -> grid.getTile(position.x() + d.dx, position.y() + d.dy))
                                .allMatch(t -> t.data() == Tile.EMPTY))
                            proposedPosition = grid.getTile(position.x() + Direction.N.dx, position.y() + Direction.N.dy);
                    }
                    case S -> {
                        if (Stream.of(Direction.S, Direction.SE, Direction.SW)
                                .map(d -> grid.getTile(position.x() + d.dx, position.y() + d.dy))
                                .allMatch(t -> t.data() == Tile.EMPTY))
                            proposedPosition = grid.getTile(position.x() + Direction.S.dx, position.y() + Direction.S.dy);
                    }
                    case W -> {
                        if (Stream.of(Direction.W, Direction.NW, Direction.SW)
                                .map(d -> grid.getTile(position.x() + d.dx, position.y() + d.dy))
                                .allMatch(t -> t.data() == Tile.EMPTY))
                            proposedPosition = grid.getTile(position.x() + Direction.W.dx, position.y() + Direction.W.dy);
                    }
                    case E -> {
                        if (Stream.of(Direction.E, Direction.NE, Direction.SE)
                                .map(d -> grid.getTile(position.x() + d.dx, position.y() + d.dy))
                                .allMatch(t -> t.data() == Tile.EMPTY))
                            proposedPosition = grid.getTile(position.x() + Direction.E.dx, position.y() + Direction.E.dy);
                    }
                }
                if (proposedPosition != null) {
                    break;
                }
            }
        }

        public void move() {
            if (proposedPosition != null && proposedPosition != position) {
                grid.set(proposedPosition, Tile.ELF);
                grid.set(position, Tile.EMPTY);
                position = proposedPosition;
                proposedPosition = null;
            }
        }
    }
}
