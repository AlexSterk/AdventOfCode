package days;

import setup.Day;

import java.util.*;

public class Day5 extends Day {
    private Map<String, List<Mapping>> mappings;
    private List<Long> seeds;
    private List<String> mappingOrder;
    private List<Mapping> seedRanges;

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

    private long getSeedFromLocation(long location) {
        long destination = location;
        var reversedMappingOrder = new ArrayList<>(mappingOrder);
        Collections.reverse(reversedMappingOrder);
        for (String map : reversedMappingOrder) {
            List<Mapping> mappings = this.mappings.get(map);
            for (Mapping mapping : mappings) {
                long source = mapping.inverseDestination(destination);
                if (source != -1) {
                    destination = source;
                    break;
                }
            }
        }

        return destination;
    }

    private boolean seedExists(long seed) {
        return seedRanges.stream().anyMatch(mapping -> mapping.getDestination(seed) != -1);
    }

    @Override
    public Object part2() {
        seedRanges = new ArrayList<>();
        for (int i = 0; i < seeds.size(); i+=2) {
            var seed = seeds.get(i);
            var size = seeds.get(i + 1);

            seedRanges.add(new Mapping(seed, seed, size));
        }

        for (long i = 0; i < 1_000_000_000; i++) {
            if (seedExists(getSeedFromLocation(i))) {
                return i;
            }
        }

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

    @Override
    public String partTwoSolution() {
        return "52210644";
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

        public long inverseDestination(long destination) {
            if (minDestination() <= destination && destination <= maxDestination()) {
                return destination + (minSource() - minDestination());
            }
            return -1;
        }
    }
}
