package util;

import java.util.*;
import java.util.stream.Collectors;

public class Grid<T> {

    private final List<List<Tile<T>>> grid;
    public final int width, height;

    public Grid(int w, int h) {
        this.width = w;
        this.height = h;
        this.grid = new ArrayList<>(h);
        for (int y = 0; y < h; y++) {
            this.grid.add(new ArrayList<>(Collections.nCopies(w, null)));
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
        return grid.get(y).get(x);
    }

    public List<Tile<T>> getAll() {
        return grid.stream().flatMap(Collection::stream).toList();
    }

    public Grid<T> copy() {
        Grid<T> g = new Grid<>(width, height);
        getAll().forEach(t -> g.set(t, t.data));
        return g;
    }

    public Set<Tile<T>> getAdjacent(Tile<T> tile, boolean strict) {
        Set<Tile<T>> set = new HashSet<>();
        set.add(tile.up());
        set.add(tile.down());
        set.add(tile.left());
        set.add(tile.right());
        if (!strict) {
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

    @Override
    public String toString() {
        return grid.stream().map(l -> l.stream().map(t -> t.data.toString()).collect(Collectors.joining())).collect(Collectors.joining("\n"));
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
