package days;

import setup.Day;
import util.Grid;
import util.Grid.Tile;
import util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day25 extends Day {

    private Grid<Character> sea;

    @Override
    public void processInput() {
        String[] lines = input.trim().split("\n");
        sea = new Grid<>(lines[0].trim().length(), lines.length);
        for (int y = 0; y < lines.length; y++) {
            String s = lines[y];
            char[] arr = s.trim().toCharArray();
            for (int x = 0; x < arr.length; x++) {
                sea.set(x, y, arr[x]);
            }
        }
    }

    @Override
    public Object part1() {
        int i = 1;

        while (sea.getAll().stream().anyMatch(t -> canMove(t) != null)) {
            var east = sea.getAll().stream().filter(t -> t.data() == '>').collect(Collectors.toCollection(ArrayList::new));

            List<Pair<Tile<Character>, Tile<Character>>> canMove = east.stream().map(t -> new Pair<>(t, canMove(t))).filter(p -> p.b() != null).toList();
            for (Pair<Tile<Character>, Tile<Character>> m : canMove) {
                sea.set(m.a(), '.');
                sea.set(m.b(), '>');
            }

            var south = sea.getAll().stream().filter(t -> t.data() == 'v').collect(Collectors.toCollection(ArrayList::new));
            canMove = south.stream().map(t -> new Pair<>(t, canMove(t))).filter(p -> p.b() != null).toList();

            for (Pair<Tile<Character>, Tile<Character>> m : canMove) {
                sea.set(m.a(), '.');
                sea.set(m.b(), 'v');
            }

            i++;
        }


        return i;
    }

    private Tile<Character> getDestination(Tile<Character> tile) {
        if (tile.data() == '>') {
            return tile.x() == sea.width - 1 ? sea.getTile(0, tile.y()) : sea.getTile(tile.x() + 1, tile.y());
        } else {
            return tile.y() == sea.height - 1 ? sea.getTile(tile.x(), 0) : sea.getTile(tile.x(), tile.y() + 1);
        }
    }

    private Tile<Character> canMove(Tile<Character> tile) {
        if (tile.data() == '.') return null;
        else {
            Tile<Character> destination = getDestination(tile);
            return destination.data() == '.' ? destination : null;
        }
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 25;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "374";
    }
}
