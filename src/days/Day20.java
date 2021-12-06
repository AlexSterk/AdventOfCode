package days;

import setup.Day;
import util.Grid;
import util.Grid.Tile;

import java.util.*;
import java.util.stream.Collectors;


public class Day20 extends Day {
    private String routes;
    private Grid<MapData> map;
    private Tile<MapData> startPosition;
    private List<List<Tile<MapData>>> shortestPaths;

    @Override
    public void processInput() {
        routes = input.trim().substring(1, input.indexOf('$'));
        // Rough size estimate of the grid --> Doesn't matter if we don't fill it up right?

        Map<Character, Integer> counts = routes.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toMap(c -> c, c -> 1, Integer::sum));

        int width = counts.get('E') + counts.get('W') + 1;
        int height = counts.get('N') + counts.get('S') + 1;

        map = new Grid<>(width * 2, height * 2, () -> MapData.UNKNOWN);
        map.set(width - 1, height - 1, MapData.ROOM);
        startPosition = map.getTile(width - 1, height - 1);
    }

    @SuppressWarnings({"ConstantConditions", "OptionalGetWithoutIsPresent"})
    @Override
    public Object part1() {
        Tile<MapData> position = startPosition;
        Stack<Tile<MapData>> positionsStack = new Stack<>();
        for (Character c : routes.toCharArray()) {
            switch (c) {
                case 'N', 'S' -> {
                    Tile<MapData> door = c == 'N' ? position.up() : position.down();
                    Tile<MapData> room = c == 'N' ? door.up() : door.down();
                    map.set(door, MapData.DOOR_HORIZONTAL);
                    map.set(room, MapData.ROOM);
                    map.set(door.left(), MapData.WALL);
                    map.set(door.right(), MapData.WALL);
                    position = room;
                }
                case 'E', 'W' -> {
                    Tile<MapData> door = c == 'W' ? position.left() : position.right();
                    Tile<MapData> room = c == 'W' ? door.left() : door.right();
                    map.set(door, MapData.DOOR_VERTICAL);
                    map.set(room, MapData.ROOM);
                    map.set(door.up(), MapData.WALL);
                    map.set(door.down(), MapData.WALL);
                    position = room;
                }
                case '(' -> positionsStack.add(position);
                case '|' -> position = positionsStack.peek();
                case ')' -> position = positionsStack.pop();
            }
        }
        map.getAll().stream().filter(Objects::nonNull).filter(t -> t.data() == MapData.UNKNOWN).forEach(t -> map.set(t, MapData.WALL));
        int minX = map.getAll().stream().filter(Objects::nonNull).min(Comparator.comparing(Tile::x)).get().x();
        int maxX = map.getAll().stream().filter(Objects::nonNull).max(Comparator.comparing(Tile::x)).get().x();
        int minY = map.getAll().stream().filter(Objects::nonNull).min(Comparator.comparing(Tile::y)).get().y();
        int maxY = map.getAll().stream().filter(Objects::nonNull).max(Comparator.comparing(Tile::y)).get().y();

        map = map.subgrid(minX, maxX, minY, maxY);
        map.init(() -> MapData.WALL, false);

        startPosition = map.getTile(startPosition.x() - minX, startPosition.y() - minY);
        dijkstra();
        return shortestPaths.stream()
                .mapToInt(l -> (int) l.stream()
                        .filter(t -> t.data() == MapData.DOOR_HORIZONTAL || t.data() == MapData.DOOR_VERTICAL)
                        .count())
                .max().getAsInt();
    }

    private void dijkstra() {
        var start = startPosition;
        Map<Tile<MapData>, Integer> dist = new HashMap<>();
        Map<Tile<MapData>, Tile<MapData>> prev = new HashMap<>();

        dist.put(start, 0);

        PriorityQueue<Tile<MapData>> Q = new PriorityQueue<>(Comparator.comparingInt(dist::get));

        map.getAll().stream()
                .filter(t -> t.data() == MapData.ROOM || t.data() == MapData.DOOR_HORIZONTAL || t.data() == MapData.DOOR_VERTICAL)
                .forEach(t -> {
                    if (t != start) {
                        dist.put(t, Integer.MAX_VALUE);
                        prev.put(t, null);
                    }
                });
        Q.add(start);

        while (!Q.isEmpty()) {
            Tile<MapData> u = Q.poll();
            for (Tile<MapData> v : map.getAdjacent(u, false)) {
                if (v.data() != MapData.ROOM && v.data() != MapData.DOOR_HORIZONTAL && v.data() != MapData.DOOR_VERTICAL)
                    continue;
                int alt = dist.get(u) + 1;
                if (alt < dist.get(v)) {
                    dist.put(v, alt);
                    prev.put(v, u);
                    Q.add(v);
                }
            }
        }

        shortestPaths = map.getAll().stream()
                .filter(t -> t.data() == MapData.ROOM)
                .map(end -> {
                    List<Tile<MapData>> path = new ArrayList<>();
                    var cur = end;
                    while (cur != start) {
                        path.add(cur);
                        cur = prev.get(cur);
                        if (cur == null) return null;
                    }
                    path.add(start);
                    Collections.reverse(path);
                    return path;
                })
                .toList();
    }

    @Override
    public Object part2() {
        return shortestPaths.stream()
                .mapToInt(l -> (int) l.stream()
                        .filter(t -> t.data() == MapData.DOOR_HORIZONTAL || t.data() == MapData.DOOR_VERTICAL)
                        .count())
                .filter(i -> i >= 1000).count();
    }

    @Override
    public int getDay() {
        return 20;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private enum MapData {
        ROOM("."),
        UNKNOWN("?"),
        WALL("#"),
        DOOR_HORIZONTAL("-"),
        DOOR_VERTICAL("|");

        private final String s;

        MapData(String s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return s;
        }
    }


}
