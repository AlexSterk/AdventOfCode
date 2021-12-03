package days;

import setup.Day;
import util.Grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day17 extends Day {
    private static final Pattern VEIN = Pattern.compile("([xy])=(\\d+), ([xy])=(\\d+)\\.\\.(\\d+)");
    private Grid<Material> ground;

    private int xOffset;

    @Override
    public void processInput() {
        String[] lines = input.split("\n");
        var veins = Arrays.stream(lines).map(line -> {
            Matcher m = VEIN.matcher(line);
            m.matches();
            return new Vein(Integer.parseInt(m.group(2)), IntStream.rangeClosed(Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5))).boxed().toList(), m.group(1).equals("y"));
        }).toList();

        var minX = veins.stream().mapToInt(v -> v.horizontal ? v.cs.get(0) : v.c).min().getAsInt() - 1;
        var maxX = veins.stream().mapToInt(v -> v.horizontal ? v.cs.get(v.cs.size() - 1) : v.c).max().getAsInt() + 1;
        var maxY = veins.stream().mapToInt(v -> v.horizontal ? v.c : v.cs.get(v.cs.size() - 1)).max().getAsInt();

//        System.out.println(minX);
//        System.out.println(maxX);
//        System.out.println(maxY);

        int width = Math.abs(maxX - minX);
        ground = new Grid<>(width + 1, maxY + 1);
        xOffset = minX;

        for (int y = 0; y < ground.height; y++) {
            for (int x = minX; x < maxX + 1; x++) {
                ground.set(x - xOffset, y, Material.SAND);
            }
        }

        for (Vein vein : veins) {
            if (vein.horizontal) {
                for (Integer x : vein.cs) {
                    ground.set(x - xOffset, vein.c, Material.CLAY);
                }
            } else {
                for (Integer y : vein.cs) {
                    ground.set(vein.c - xOffset, y, Material.CLAY);
                }
            }
        }

        ground.set(500 - xOffset, 0, Material.SOURCE);
    }

    @Override
    public void part1() {
        int n = 0;

        Grid.Tile<Material> down = ground.getTile(500 - xOffset, 0).down();
        if (down.data() != Material.SAND)
            throw new RuntimeException("Water can't flow here");
        else ground.set(down, Material.WATER_FLOWING);

        ArrayList<Grid.Tile<Material>> spreading = new ArrayList<>();
        ArrayList<Grid.Tile<Material>> oldSpreading = new ArrayList<>();
        do {
            oldSpreading.clear();
            oldSpreading.addAll(spreading);
            spreading.clear();
//            System.out.println(ground);

            List<Grid.Tile<Material>> spreadDown = ground.getAll().stream()
                    .filter(t -> t.data() == Material.WATER_FLOWING)
                    .filter(t -> t.y() + 1 < ground.height)
                    .map(Grid.Tile::down)
                    .filter(t -> t.data() == Material.SAND)
//                    .peek(t -> ground.set(t, Material.WATER_FLOWING))
                    .peek(spreading::add)
                    .toList();

            for (Grid.Tile<Material> t : spreadDown) {
                var d = t;
                var collect = new ArrayList<>(List.of(d));
                collect.clear();
                while (d.data() == Material.SAND) {
                    collect.add(d);
                    if (d.y() + 1 < ground.height) d = d.down();
                    else break;
                }
                collect.forEach(x -> ground.set(x, Material.WATER_FLOWING));
            }

            List<Grid.Tile<Material>> spreadingLeftRight = ground.getAll().stream()
                    .filter(t -> t.data() == Material.WATER_FLOWING)
                    .filter(t -> t.y() + 1 < ground.height)
                    .filter(t -> t.down().data() == Material.CLAY || t.down().data() == Material.WATER_STILL)
                    .filter(t -> t.left().data() == Material.SAND || t.right().data() == Material.SAND)
//                    .flatMap(t -> Stream.of(t.left(), t.right()))
//                    .filter(t -> t.data() == Material.SAND)
//                    .peek(t -> ground.set(t, Material.WATER_FLOWING))
                    .peek(spreading::add)
                    .toList();

            for (Grid.Tile<Material> t : spreadingLeftRight) {
                var l = t;
                List<Grid.Tile<Material>> collect = new ArrayList<>();
                while (l.left().data() == Material.SAND && l.left().down().data() != Material.SAND) {
                    l = l.left();
                    collect.add(l);
                }
                if (l.left().down().data() == Material.SAND) collect.add(l.left());
                var r = t;
                while (r.right().data() == Material.SAND && r.right().down().data() != Material.SAND) {
                    r = r.right();
                    collect.add(r);
                }
                if (r.right().down().data() == Material.SAND) collect.add(r.right());
                collect.forEach(x -> ground.set(x, Material.WATER_FLOWING));
            }

            // TODO Possible optimisation, dont visit same tile twice here in the loop.
            var leftMostEnclosedWater = ground.getAll().stream()
                    .filter(t -> t.data() == Material.WATER_FLOWING)
                    .filter(t -> t.y() + 1 < ground.height)
                    .filter(t -> t.down().data() == Material.CLAY || t.down().data() == Material.WATER_STILL)
                    .filter(t -> t.left().data() == Material.CLAY && (t.down().data() == Material.CLAY || t.down().data() == Material.WATER_STILL))
                    .toList();

            for (Grid.Tile<Material> t : leftMostEnclosedWater) {
                var l = t;
                List<Grid.Tile<Material>> collect = new ArrayList<>();
                do {
                    collect.add(l);
                    l = l.right();
                } while (l.data() == Material.WATER_FLOWING);
                if (l.data() == Material.CLAY) {
                    spreading.add(t);
                    collect.forEach(d -> ground.set(d, Material.WATER_STILL));
                }
            }

            ground.getAll().stream()
                    .filter(t -> t.data() == Material.WATER_STILL)
                    .filter(t -> t.down().data() == Material.SAND)
                    .peek(spreading::add)
                    .forEach(t -> ground.set(t.down(), Material.WATER_FLOWING));

//            System.out.println();
//            System.out.println(spreading.size());
//            System.out.println(spreading.equals(oldSpreading));
            System.out.println(spreading);
        } while (!spreading.isEmpty());
        System.out.println(ground);
        System.out.println(ground.getAll().stream().filter(t -> t.data() == Material.WATER_FLOWING || t.data() == Material.WATER_STILL).count());
    }

    @Override
    public void part2() {
        System.out.println(ground.getAll().stream().filter(t -> t.data() == Material.WATER_STILL).count());
    }

    @Override
    public int getDay() {
        return 17;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private enum Material {
        WATER_FLOWING("|"),
        WATER_STILL("~"),
        SAND("."),
        CLAY("#"),
        SOURCE("+"),
        ;

        private final String string;

        Material(String string) {
            this.string = string;
        }


        @Override
        public String toString() {
            return string;
        }
    }

    private record Vein(Integer c, List<Integer> cs, boolean horizontal) {

    }
}
