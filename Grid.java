package util;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
public class Grid<T> {

    public final int width, height;
    private final List<List<Tile<T>>> grid;
    private Supplier<T> empty;

    public Grid(int w, int h) {
        this.width = w;
        this.height = h;
        this.grid = new ArrayList<>(h);
        for (int y = 0; y < h; y++) {
            this.grid.add(new ArrayList<>(Collections.nCopies(w, null)));
        }
    }

    public Grid(int w, int h, Supplier<T> empty) {
        this(w, h);
        this.empty = empty;
    }

    public void init(Supplier<T> data, boolean overwrite) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (_getTile(x, y) == null || overwrite) set(x, y, data.get());
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grid<?> grid1 = (Grid<?>) o;
        return grid.equals(grid1.grid);
    }

    @Override
    public int hashCode() {
        return grid.hashCode();
    }

    public void set(int x, int y, T data) {
        grid.get(y).set(x, new Tile<>(x, y, data, this));
    }

    public void set(Tile<T> tile, T data) {
        set(tile.x, tile.y, data);
    }

    public Tile<T> getTile(int x, int y) {
        Tile<T> tTile = _getTile(x, y);
        return tTile == null && empty != null ? new Tile<>(x, y, empty.get(), this) : tTile;
    }

    private Tile<T> _getTile(int x, int y) {
        return grid.get(y).get(x);
    }

    public List<Tile<T>> getAll() {
        return grid.stream().flatMap(Collection::stream).toList();
    }

    public Grid<T> copy() {
        Grid<T> g = new Grid<>(width, height, empty);
        getAll().forEach(t -> g.set(t, t.data));
        return g;
    }

    public Grid<T> subgrid(int xStart, int xEnd, int yStart, int yEnd) {
        Grid<T> sub = new Grid<>(xEnd - xStart + 1, yEnd - yStart + 1, empty);
        getAll().stream()
                .filter(Objects::nonNull)
                .filter(t -> t.x >= xStart && t.x <= xEnd && t.y >= yStart && t.y <= yEnd)
                .forEach(t -> sub.set(t.x - xStart, t.y - yStart, t.data));
        return sub;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Grid<T> subgridNoNull() {
        int minX = getAll().stream().filter(Objects::nonNull).min(Comparator.comparing(Tile::x)).get().x();
        int maxX = getAll().stream().filter(Objects::nonNull).max(Comparator.comparing(Tile::x)).get().x();
        int minY = getAll().stream().filter(Objects::nonNull).min(Comparator.comparing(Tile::y)).get().y();
        int maxY = getAll().stream().filter(Objects::nonNull).max(Comparator.comparing(Tile::y)).get().y();

        return subgrid(minX, maxX, minY, maxY);
    }

    public Set<Tile<T>> getAdjacent(Tile<T> tile, boolean includeDiagonals) {
        Set<Tile<T>> set = new HashSet<>();
        set.add(tile.up());
        set.add(tile.down());
        set.add(tile.left());
        set.add(tile.right());
        if (includeDiagonals) {
            Tile<T> left = tile.left();
            if (left != null) {
                set.add(left.up());
                set.add(left.down());
            }
            Tile<T> right = tile.right();
            if (right != null) {
                set.add(right.up());
                set.add(right.down());
            }
        }
        set.remove(null);
        return set;
    }

    public List<Tile<T>> getRow(int y) {
        return new ArrayList<>(grid.get(y));
    }

    public List<Tile<T>> getColumn(int x) {
        List<Tile<T>> arr = new ArrayList<>();
        grid.forEach(l -> arr.add(l.get(x)));
        return arr;
    }

    public List<List<Tile<T>>> getRows() {
        return new ArrayList<>(grid);
    }

    public List<List<Tile<T>>> getColumns() {
        return IntStream.range(0, width).mapToObj(this::getColumn).toList();
    }

    @Override
    public String toString() {
        return grid.stream()
                .map(l -> l.stream().map(t -> t == null ? "\033[31m\u25A1\033[0m" : t.data.toString()).collect(Collectors.joining()))
                .collect(Collectors.joining("\n"));
    }

    public String toString(Map<Tile<T>, String> custom) {
        return grid.stream()
                .map(l -> l.stream().map(t -> {
                    if (custom.containsKey(t)) return custom.get(t);
                    return t == null ? " " : t.data.toString();
                }).collect(Collectors.joining()))
                .collect(Collectors.joining("\n"));
    }

    public Grid<T> flip(boolean vertical) {
        Grid<T> copy = this.copy();

        if (vertical) {
            Collections.reverse(copy.grid);
        } else {
            copy.grid.forEach(Collections::reverse);
        }

        for (int y = 0; y < copy.grid.size(); y++) {
            for (int x = 0; x < copy.grid.get(y).size(); x++) {
                copy.set(x, y, copy.getTile(x, y).data);
            }
        }
        return copy;
    }

    public interface Weighted {
        Integer getWeight();
    }

    public static <T extends Weighted> Graph<Tile<T>> gridToGraph(Grid<T> grid) {
        Graph<Tile<T>> graph = new Graph<>();

        for (Tile<T> tTile : grid.getAll()) {
            graph.addNode(tTile);
        }

        for (Tile<T> tTile : grid.getAll()) {
            for (Tile<T> tile : grid.getAdjacent(tTile, false)) {
                graph.addEdge(tTile, tile, tile.data.getWeight(), true);
            }
        }

        return graph;
    }

    public record Tile<T>(int x, int y, T data, Grid<T> grid) {

        public Tile<T> up() {
            return y - 1 >= 0 ? grid.getTile(x, y - 1) : null;
        }

        public Tile<T> down() {
            return y + 1 < grid.height ? grid.getTile(x, y + 1) : null;
        }

        public Tile<T> left() {
            return x - 1 >= 0 ? grid.getTile(x - 1, y) : null;
        }

        public Tile<T> right() {
            return x + 1 < grid.width ? grid.getTile(x + 1, y) : null;
        }

        @Override
        public String toString() {
            return "Tile{" +
                    "x=" + x +
                    ", y=" + y +
                    ", data=" + data +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tile<?> tile = (Tile<?>) o;
            return x == tile.x && y == tile.y && Objects.equals(data, tile.data);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, data);
        }
    }
}
