package days;

import setup.Day;

import java.util.*;

public class Day5 extends Day {
    private Map<String, List<Mapping>> mappings;
    private List<Long> seeds;
    private List<String> mappingOrder;

    @Override
    public void processInput() {
        List<String> split = Arrays.asList(input.split("\n\n"));
        String seeds = split.get(0).replace("seeds: ", "");
        this.seeds = Arrays.stream(seeds.split(" "))
                .map(Long::parseLong)
                .toList();

        List<String> maps = split.subList(1, split.size());
        mappings = new HashMap<>();
        mappingOrder = new ArrayList<>();
        for (String map : maps) {
            String[] splitMap = map.split("\n");
            String name = splitMap[0].replace(" map:", "");
            mappingOrder.add(name);
            List<Mapping> mappingList = Arrays.stream(splitMap)
                    .skip(1)
                    .map(Mapping::parse)
                    .toList();
            mappings.put(name, mappingList);
        }
    }

    @Override
    public Object part1() {
        var locations = seeds.stream().map(this::getLocation).toList();

        return Collections.min(locations);
    }

    private long getLocation(long seed) {
        long source = seed;

        for (String map : mappingOrder) {
            List<Mapping> mappings = this.mappings.get(map);
            for (Mapping mapping : mappings) {
                long destination = mapping.getDestination(source);
                if (destination != -1) {
                    source = destination;
                    break;
                }
            }
        }

        return source;
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 5;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "340994526";
    }

    record Mapping(long source, long destination, long size) {
        public static Mapping parse(String s) {
            String[] split = s.split(" ");
            long destination = Long.parseLong(split[0]);
            long source = Long.parseLong(split[1]);
            long size = Long.parseLong(split[2]);
            return new Mapping(source, destination, size);
        }

        public long minSource() {
            return source;
        }

        public long maxSource() {
            return source + size - 1;
        }

        public long minDestination() {
            return destination;
        }

        public long maxDestination() {
            return destination + size - 1;
        }

        public long getDestination(long source) {
            if (minSource() <= source && source <= maxSource()) {
                return source + (minDestination() - minSource());
            }
            return -1;
        }
    }
}
