package days;

import setup.Day;

import java.util.*;
import java.util.stream.Collectors;

public class Day15 extends Day {

    private Grid grid;

    @Override
    public void processInput() {
        String[] lines = input.split("\n");
        Tile[][] g = new Tile[lines.length][lines[0].length()];
        List<Unit> units = new ArrayList<>();
        grid = new Grid(g, units);
        for (int y = 0; y < lines.length; y++) {
            char[] line = lines[y].toCharArray();
            for (int x = 0; x < line.length; x++) {
                switch (line[x]) {
                    case '#' -> g[y][x] = new Tile(grid, new Position(x, y), TileType.WALL);
                    case '.' -> g[y][x] = new Tile(grid, new Position(x, y), TileType.EMPTY);
                    case 'G' -> {
                        Tile tile = new Tile(grid, new Position(x, y), TileType.EMPTY);
                        g[y][x] = tile;
                        units.add(new Unit(tile, grid, UnitType.GOBLIN));
                    }
                    case 'E' -> {
                        Tile tile = new Tile(grid, new Position(x, y), TileType.EMPTY);
                        g[y][x] = tile;
                        units.add(new Unit(tile, grid, UnitType.ELF));
                    }
                }
            }
        }
    }

    @Override
    public void part1() {
        System.out.println(Arrays.stream(grid.grid).flatMap(Arrays::stream).filter(t -> t.type == TileType.EMPTY).count());

        int rounds = 0;
        Map<UnitType, List<Unit>> units = grid.units.stream().collect(Collectors.groupingBy(u -> u.type));
        o:
        while (true) {
            System.out.println(grid);
            System.out.println();
            Collections.sort(grid.units);
            for (Unit unit : grid.units) {
                System.out.println(unit);
                if (units.values().stream().anyMatch(us -> us.stream().noneMatch(Unit::alive))) {
                    break o;
                }
                if (unit.alive())
                    unit.turn(true);
            }
            rounds++;
        }
        int hp = grid.units.stream().filter(Unit::alive).mapToInt(u -> u.hp).sum();
        System.out.println("Rounds: " + rounds);
        System.out.println("Remaining HP: " + hp);
        System.out.println("Result: " + (rounds * hp));
    }

    @Override
    public void part2() {

    }

    @Override
    public int getDay() {
        return 15;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private enum TileType {
        WALL,
        EMPTY
    }

    private enum UnitType {
        GOBLIN,
        ELF
    }

    private record Tile(Grid grid, Position position, TileType type) {
        public List<Tile> adjacent() {
            return grid.adjacent(position);
        }

        public String toGridString() {
            return type == TileType.EMPTY ? "." : "#";
        }

        @Override
        public String toString() {
            return position.toString();
        }
    }

    private static final class Grid {
        public final Tile[][] grid;
        public final List<Unit> units;

        public Grid(Tile[][] grid, List<Unit> units) {
            this.grid = grid;
            this.units = units;
        }

        public List<Tile> adjacent(Position pos) {
            List<Tile> res = new ArrayList<>();
            if (pos.y - 1 >= 0) res.add(grid[pos.y - 1][pos.x]);
            if (pos.x - 1 >= 0) res.add(grid[pos.y][pos.x - 1]);
            if (pos.y + 1 < grid.length) res.add(grid[pos.y + 1][pos.x]);
            if (pos.x + 1 < grid[0].length) res.add(grid[pos.y][pos.x + 1]);
            res.sort(Comparator.comparing(Tile::position));
            return res;
        }

        public Optional<Unit> occupying(Tile tile) {
            return units.stream()
                    .filter(u -> u.standingOn == tile && u.alive()).findFirst();
        }

        public List<Tile> getPath(Tile start, Tile end) {
            Map<Tile, Integer> dist = new HashMap<>();
            Map<Tile, List<Tile>> prevs = new HashMap<>();

            dist.put(start, 0);

            PriorityQueue<Tile> Q = new PriorityQueue<>(Comparator.comparingInt(dist::get));

            Arrays.stream(grid)
                    .flatMap(Arrays::stream)
                    .filter(t -> t.type == TileType.EMPTY)
                    .forEach(t -> {
                        if (t != start) {
                            dist.put(t, Integer.MAX_VALUE);
                            prevs.put(t, new ArrayList<>());
                        } else Q.add(t);
//                        Q.add(t);
                    });

            while (!Q.isEmpty()) {
                Tile u = Q.poll();
                System.out.println(u);
                for (Tile v : u.adjacent()) {
                    if (v.type == TileType.WALL || occupying(v).isPresent()) continue;
                    int alt = dist.get(u) + 1;
                    if (alt < dist.get(v)) {
                        dist.put(v, alt);
                        prevs.get(v).clear();
                        prevs.get(v).add(u);
                        Q.add(v);
                    } else if (alt == dist.get(v)) {
                        prevs.get(v).add(u);
                        Q.add(v);
                    }
                }
            }
            List<Tile> path = new ArrayList<>();
            Optional<Tile> cur = Optional.of(end);
            while (cur.get() != start) {
                path.add(cur.get());
                cur = prevs.get(cur.get()).stream().min(Comparator.comparing(Tile::position));
                if (cur.isEmpty()) return null;
            }

            Collections.reverse(path);
            return path;
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();

            var us = units.stream()
                    .filter(Unit::alive)
                    .collect(Collectors.toMap(u -> u.standingOn.position, u -> u));

            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid[y].length; x++) {
                    Position key = new Position(x, y);
                    if (us.containsKey(key)) {
                        b.append(us.get(key));
                    } else {
                        b.append(grid[y][x].toGridString());
                    }
                }
                b.append("   ");
                int finalY = y;
                b.append(units.stream()
                        .filter(u -> u.standingOn.position.y == finalY)
                        .sorted()
                        .map(u -> u + "(" + u.hp + ")")
                        .collect(Collectors.joining(", ")));
                b.append("\n");
            }

            return b.toString();
        }

    }

    private static final class Unit implements Comparable<Unit> {
        public final Grid grid;
        public final UnitType type;
        private Tile standingOn;
        private int hp = 200;
        private int attack = 3;

        private Unit(Tile standingOn, Grid grid, UnitType type) {
            this.standingOn = standingOn;
            this.grid = grid;
            this.type = type;
        }

        public boolean alive() {
            return hp > 0;
        }

        private void attack(Unit target) {
            target.hp -= attack;
        }

        public void turn(boolean canMove) {
            var targets = grid.units.stream()
                    .filter(u -> u.type != this.type)
                    .filter(Unit::alive)
                    .toList();

            var targetInRange = targets.stream()
                    .filter(u -> u.standingOn.adjacent().contains(standingOn))
                    .min(Comparator.comparing((Unit u) -> u.hp).thenComparing(Unit::compareTo));

            if (targetInRange.isPresent()) {
                attack(targetInRange.get());
                return;
            }

            if (canMove) move(targets);
        }

        private void move(List<Unit> targets) {
            var inRange = targets.stream().map(u -> u.standingOn)
                    .toList().stream()
                    .flatMap(t -> t.adjacent().stream())
                    .filter(t -> t.type == TileType.EMPTY)
                    .filter(t -> grid.occupying(t).isEmpty())
                    .toList();

            if (inRange.isEmpty()) return;

            var reachable = inRange.stream()
                    .map(t -> grid.getPath(standingOn, t))
                    .filter(Objects::nonNull)
                    .toList();
            var minPath = reachable.stream().mapToInt(List::size).min();
            if (minPath.isPresent()) {
                var moveTo = reachable.stream()
                        .filter(p -> p.size() == minPath.getAsInt())
                        .min(Comparator.comparing(p -> p.get(p.size() - 1).position))
                        .get();
                standingOn = moveTo.get(0);
                turn(false);
            }
        }

        @Override
        public String toString() {
            return type == UnitType.GOBLIN ? "G" : "E";
        }

        @Override
        public int compareTo(Unit o) {
            return standingOn.position.compareTo(o.standingOn.position);
        }
    }

    private record Position(int x, int y) implements Comparable<Position> {
        @Override
        public int compareTo(Position o) {
            int a = Integer.compare(y, o.y);
            return a == 0 ? Integer.compare(x, o.x) : a;
        }
    }
}