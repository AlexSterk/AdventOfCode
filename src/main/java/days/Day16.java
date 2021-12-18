package days;

import setup.Day;

import java.util.*;
import java.util.stream.Collectors;

public class Day16 extends Day {
    private String bits;
    private Queue<String> bitQueue;
    private List<Packet> allPackets;
    private Packet packet;

    private static String toBitstring(String s) {
        String bits = Integer.toBinaryString(Integer.parseInt(s, 16));
        return "0".repeat(4 - bits.length()) + bits;
    }

    @Override
    public void processInput() {
        bitQueue = new ArrayDeque<>();
        bits = input.trim().chars()
                .mapToObj(c -> String.valueOf((char) c))
                .map(Day16::toBitstring)
                .peek(s -> {
                    for (int i = 0; i < s.length(); i++) {
                        bitQueue.offer(s.substring(i, i + 1));
                    }
                })
                .collect(Collectors.joining());
        allPackets = new ArrayList<>();
    }

    @Override
    public Object part1() {
        packet = parse(bitQueue);
        return allPackets.stream().mapToInt(p -> p.version).sum();
    }

    private Packet parse(Queue<String> bits) {
        Packet p = _parse(bits);
        allPackets.add(p);
        return p;
    }

    private Packet _parse(Queue<String> bits) {
        int version = Integer.parseInt(bits.poll() + bits.poll() + bits.poll(), 2);
        int type = Integer.parseInt(bits.poll() + bits.poll() + bits.poll(), 2);

        if (type == 4) {
            boolean lastGroup = false;
            StringBuilder bitString = new StringBuilder();
            while (!lastGroup) {
                lastGroup = bits.poll().equals("0");
                for (int i = 0; i < 4; i++) {
                    bitString.append(bits.poll());
                }
            }
            long number = Long.parseLong(bitString.toString(), 2);
            return new Packet.ValuePacket(version, number);
        } else { //Subpackets
            int lengthType = Integer.parseInt(bits.poll(), 2);
            if (lengthType == 0) {
                StringBuilder bitString = new StringBuilder();
                for (int i = 0; i < 15; i++) {
                    bitString.append(bits.poll());
                }
                int totalLength = Integer.parseInt(bitString.toString(), 2);
                int goal = bits.size() - totalLength;
                List<Packet> sub = new ArrayList<>();
                while (bits.size() != goal) {
                    sub.add(parse(bits));
                }
                return new Packet.OperatorPacket(version, type, sub);
            } else {
                StringBuilder bitString = new StringBuilder();
                for (int i = 0; i < 11; i++) {
                    bitString.append(bits.poll());
                }
                int subpacketsCount = Integer.parseInt(bitString.toString(), 2);
                List<Packet> sub = new ArrayList<>();
                while (sub.size() != subpacketsCount) {
                    sub.add(parse(bits));
                }
                return new Packet.OperatorPacket(version, type, sub);
            }
        }
    }

    @Override
    public Object part2() {
        return packet.compute();
    }

    @Override
    public int getDay() {
        return 16;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "901";
    }

    @Override
    public String partTwoSolution() {
        return "110434737925";
    }

    private static abstract class Packet {
        public final int version;
        public final int type;

        private Packet(int version, int type) {
            this.version = version;
            this.type = type;
        }

        abstract long compute();

        private static class ValuePacket extends Packet {
            public final long value;

            private ValuePacket(int version, long value) {
                super(version, 4);
                this.value = value;
            }

            @Override
            public String toString() {
                return "ValuePacket{" +
                        "version=" + version +
//                        ", type=" + type +
                        ", value=" + value +
                        '}';
            }

            @Override
            long compute() {
                return value;
            }
        }

        private static class OperatorPacket extends Packet {
            public final List<Packet> subpackets;

            private OperatorPacket(int version, int type, List<Packet> subpackets) {
                super(version, type);
                this.subpackets = subpackets;
            }

            @Override
            public String toString() {
                return "OperatorPacket{" +
                        "version=" + version +
                        ", type=" + type +
                        ", subpackets=" + subpackets +
                        '}';
            }

            @Override
            long compute() {
                List<Long> sub = subpackets.stream().map(Packet::compute).toList();

                return switch (type) {
                    case 0 -> sub.stream().mapToLong(l -> l).sum();
                    case 1 -> sub.stream().mapToLong(l -> l).reduce((a,b) -> a * b).getAsLong();
                    case 2 -> sub.stream().mapToLong(l -> l).min().getAsLong();
                    case 3 -> sub.stream().mapToLong(l -> l).max().getAsLong();
                    case 5 -> sub.get(0) > sub.get(1) ? 1 : 0;
                    case 6 -> sub.get(0) < sub.get(1) ? 1 : 0;
                    case 7 -> Objects.equals(sub.get(0), sub.get(1)) ? 1 : 0;
                    default -> throw new IllegalStateException(String.valueOf(type));
                };
            }
        }
    }
}
