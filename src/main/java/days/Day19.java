package days;

import setup.Day;

import java.util.*;

public class Day19 extends Day {
    private List<Scanner> scanners;
    private Map<Coord, Scanner> scannerCoords;

    @Override
    public void processInput() {
        String[] split = input.split("\n\n");
        scanners = new ArrayList<>();
        for (String s : split) {
            String[] lines = s.split("\n");
            Scanner e = new Scanner();
            scanners.add(e);
            for (int i = 1; i < lines.length; i++) {
                e.beacons.add(Coord.Coord(lines[i]));
            }
        }
    }

    @Override
    public Object part1() {
        Scanner base = scanners.remove(0);
        Queue<Scanner> queue = new ArrayDeque<>(scanners);
        scannerCoords = new HashMap<>();
        scannerCoords.put(new Coord(0, 0, 0), base);
        o: while (!queue.isEmpty()) {
            Scanner poll = queue.poll();
            for (Scanner allRotation : poll.allRotations()) {
                Coord translation = base.translation(allRotation);
                if (translation != null) {
                    scannerCoords.put(translation, allRotation);
                    allRotation.beacons.stream()
                            .map(c -> c.add(translation))
                            .filter(c -> !base.beacons.contains(c))
                            .forEach(base.beacons::add);
                    continue o;
                }
            }
            queue.offer(poll);
        }

        return base.beacons.size();
    }

    @Override
    public Object part2() {
        int max = Integer.MIN_VALUE;

        for (Coord c1 : scannerCoords.keySet()) {
            for (Coord c2 : scannerCoords.keySet()) {
                int dist = c1.dist(c2);
                if (dist > max) max = dist;
            }
        }

        return max;
    }

    @Override
    public int getDay() {
        return 19;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private static class Scanner {
        private final List<Coord> beacons = new ArrayList<>();

        private List<Scanner> allRotations() {
            List<List<Coord>> coords = beacons.stream().map(Coord::allRotations).toList();
            List<Scanner> ret = new ArrayList<>();
            for (int i = 0; i < 24; i++) {
                Scanner e = new Scanner();
                ret.add(e);
                for (int j = 0; j < beacons.size(); j++) {
                    e.beacons.add(coords.get(j).get(i));
                }
            }
            return ret;
        }

        private Coord translation(Scanner s) {
            Map<Coord, Integer> dist = new HashMap<>();
            for (Coord a : beacons) {
                for (Coord b : s.beacons) {
                    Coord c = a.subtract(b);
                    dist.merge(c, 1, Integer::sum);
                }
            }
            List<Map.Entry<Coord, Integer>> entries = dist.entrySet().stream().filter(e -> e.getValue() >= 12).toList();
            if (entries.size() == 0) {
                return null;
            }
            if (entries.size() == 1) {
                return entries.get(0).getKey();
            }
            throw new IllegalStateException();
        }
    }

    private record Coord(int x, int y, int z) {
        private static Coord Coord(String s) {
            String[] split = s.split(",");
            return new Coord(
                    Integer.parseInt(split[0]),
                    Integer.parseInt(split[1]),
                    Integer.parseInt(split[2])
            );
        }

        private Coord subtract(Coord o) {
            return new Coord(x - o.x, y - o.y, z - o.z);
        }

        private Coord add(Coord o) {
            return new Coord(x + o.x, y + o.y, z + o.z);
        }

        private int dist(Coord o) {
            return Math.abs(x - o.x) + Math.abs(y - o.y) + Math.abs(z - o.z);
        }

        private Coord roll() {
            return new Coord(x, z, -y);
        }

        private Coord turn() {
            return new Coord(-y, x, z);
        }

        private List<Coord> allRotations() {
            Coord v = this;
            List<Coord> a = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    v = v.roll();
                    a.add(v);
                    for (int k = 0; k < 3; k++) {
                        v = v.turn();
                        a.add(v);
                    }
                }
                v = v.roll().turn().roll();
            }
            return a;
        }
    }
}
