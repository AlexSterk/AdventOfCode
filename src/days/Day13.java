package days;

import setup.Day;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day13 extends Day {

    private char[][] grid;
    private List<Cart> carts;

    @Override
    public void processInput() {
        final String[] lines = input.split("\n");
        grid = new char[lines.length][];
        for (int y = 0; y < lines.length; y++) {
            grid[y] = lines[y].toCharArray();
        }

        carts = new ArrayList<>();
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                switch (grid[y][x]) {
                    case '^' -> {
                        grid[y][x] = '|';
                        carts.add(new Cart(x, y, 0, -1, grid));
                    }
                    case '>' -> {
                        grid[y][x] = '-';
                        carts.add(new Cart(x, y, 1, 0, grid));
                    }
                    case 'v' -> {
                        grid[y][x] = '|';
                        carts.add(new Cart(x, y, 0, 1, grid));
                    }
                    case '<' -> {
                        grid[y][x] = '-';
                        carts.add(new Cart(x, y, -1, 0, grid));
                    }
                }
            }
        }
    }

    @Override
    public void part1() {
        while (true) {
            Map<Pair, Cart> taken = new HashMap<>();
            for (Cart cart : carts) {
                cart.move();
                Pair pos = new Pair(cart.x, cart.y);
                if (taken.containsKey(pos)) {
                    System.out.println(pos);
                    return;
                }
                taken.put(pos, cart);
            }
        }
    }

    @Override
    public void part2() {

    }

    @Override
    public int getDay() {
        return 13;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private static record Pair(int x, int y) {

    }

    private static class Cart {
        private final char[][] grid;
        private int x, y;
        private int vx, vy;
        private int intersection = 0;

        private Cart(int x, int y, int vx, int vy, char[][] grid) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.grid = grid;
        }

        public void move() {
            x += vx;
            y += vy;

            char pos = grid[y][x];
            switch (pos) {
                case '/' -> {
                    int t = vx;
                    vx = -vy;
                    vy = -t;
                }
                case '\\' -> {
                    int t = vx;
                    vx = vy;
                    vy = t;
                }
                case '+' -> {
                    switch (intersection) {
                        case 0 -> {
                            int t = vx;
                            vx = vy;
                            vy = -t;
                        }
                        case 2 -> {
                            int t = vx;
                            vx = -vy;
                            vy = t;
                        }
                    }
                    intersection = (intersection + 1) % 3;
                }
                case ' ' -> throw new RuntimeException();
            }
        }
    }
}
