package days;

import setup.Day;

import java.util.*;
import java.util.stream.Collectors;

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
        boolean crash = false;
        while (!crash) {
            Collections.sort(carts);
            for (Cart cart : new ArrayList<>(carts)) {
                cart.move();
                List<Cart> crashes = carts.stream().filter(c -> c != cart).filter(cart::checkCrash).collect(Collectors.toList());
                if (!crashes.isEmpty()) {
                    System.out.println(cart.x + "," + cart.y);
                    carts.removeAll(crashes);
                    carts.remove(cart);
                    crash = true;
                }
            }
        }
    }

    @Override
    public void part2() {
        while (carts.size() > 1) {
            Collections.sort(carts);
            for (Cart cart : new ArrayList<>(carts)) {
                cart.move();
                List<Cart> crashes = carts.stream().filter(c -> c != cart).filter(cart::checkCrash).collect(Collectors.toList());
                if (!crashes.isEmpty()) {
                    carts.removeAll(crashes);
                    carts.remove(cart);
                }
            }
        }
        Cart cart = carts.get(0);
        System.out.println(cart.x + "," + cart.y);
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

    private static class Cart implements Comparable<Cart> {
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

        public boolean checkCrash(Cart o) {
            return x == o.x && y == o.y;
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

        @Override
        public int compareTo(Cart o) {
            final int compare = Integer.compare(y, o.y);
            return compare == 0 ? Integer.compare(x, o.x) : compare;
        }
    }
}
