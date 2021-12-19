package days;

import setup.Day;

import java.util.*;

public class Day18 extends Day {

    private List<DuetCPU.Instruction> instructions;

    @Override
    public void processInput() {
        instructions = Arrays.stream(input.split("\n")).map(DuetCPU.Instruction::Instruction).toList();
    }

    @Override
    public Object part1() {
        DuetCPU duetCPU = new DuetCPU(instructions, 0L);
        return duetCPU.run();
    }

    @Override
    public Object part2() {
        DuetCPU one = new DuetCPU(instructions, 0L);
        DuetCPU two = new DuetCPU(instructions, 1L);
        DuetCPU.link(one, two);

        while (true) {
            while (!one.waiting()) one.run();
            while (!two.waiting()) two.run();
            if (one.terminated() && two.terminated()) break;
            if (one.waiting()) break;
        }

        return two.log.size();
    }

    @Override
    public int getDay() {
        return 18;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "3423";
    }

    @Override
    public String partTwoSolution() {
        return "7493";
    }

    public static class DuetCPU {
        private final Map<Character, Long> registers = new HashMap<>();
        private final List<Instruction> instructions;
        private final Deque<Long> queue = new ArrayDeque<>();
        private final List<Long> log = new ArrayList<>();
        private int ir = 0;
        private boolean incrementInstruction = false;
        private DuetCPU link = this;

        public DuetCPU(List<Instruction> instructions, long ID) {
            this.instructions = instructions;
            registers.put('p', ID);
        }

        private static void link(DuetCPU one, DuetCPU two) {
            if (one.link != null) one.link.link = null;
            if (two.link != null) two.link.link = null;
            one.link = two;
            two.link = one;
        }

        private boolean waiting() {
            return queue.isEmpty() && getNextInstruction() instanceof Instruction.Receive;
        }

        public boolean terminated() {
            return ir >= instructions.size();
        }

        public Long run() {
            try {
                while (!terminated() && !waiting()) {
                    incrementInstruction = true;
                    Instruction ins = getNextInstruction();
                    executeInstruction(ins);
                    if (incrementInstruction) ir++;
                }
            } catch (ReceiveException r) {
                return r.recovered;
            }

            return null;
        }

        private Instruction getNextInstruction() {
            return instructions.get(ir);
        }

        private void executeInstruction(Instruction ins) throws ReceiveException {
            switch (ins) {
                case Instruction.Send s -> {
                    Long resolve = resolve(s.x);
                    link.queue.offer(resolve);
                    log.add(resolve);
                }
                case Instruction.Receive r -> {
                    if (link == this) {
                        if (resolve(r.x) != 0) throw new ReceiveException(queue.getLast());
                    } else if (queue.isEmpty()) {
                        incrementInstruction = false;
                    } else {
                        updateRegister(r.x, queue.poll());
                    }
                }
                case Instruction.Set s -> updateRegister(s.x, resolve(s.y));
                case Instruction.Add a -> updateRegister(a.x, registers.getOrDefault(a.x.charAt(0), 0L) + resolve(a.y));
                case Instruction.Mul m -> updateRegister(m.x, registers.getOrDefault(m.x.charAt(0), 0L) * resolve(m.y));
                case Instruction.Mod m -> updateRegister(m.x, registers.getOrDefault(m.x.charAt(0), 0L) % resolve(m.y));
                case Instruction.JumpGreaterThanZero j -> {
                    if (resolve(j.x) > 0) {
                        ir += resolve(j.y);
                        incrementInstruction = false;
                    }
                }
                case Instruction.Sub s -> updateRegister(s.x, registers.getOrDefault(s.x.charAt(0), 0L) - resolve(s.y));
                case Instruction.JumpNotZero j -> {
                    if (resolve(j.x) != 0) {
                        ir += resolve(j.y);
                        incrementInstruction = false;
                    }
                }
            }
        }

        private Long resolve(String a) {
            if (a.length() == 1 && Character.isAlphabetic(a.charAt(0))) return registers.getOrDefault(a.charAt(0), 0L);
            return Long.parseLong(a);
        }

        private void updateRegister(String r, long value) {
            registers.put(r.charAt(0), value);
        }

        public sealed interface Instruction {
            static Instruction Instruction(String s) {
                String[] ss = s.split(" ");
                String op = ss[0];
                String a1 = ss[1];
                String a2 = ss.length > 2 ? ss[2] : null;
                return switch (op) {
                    case "snd" -> new Send(a1);
                    case "set" -> new Set(a1, a2);
                    case "add" -> new Add(a1, a2);
                    case "sub" -> new Sub(a1, a2);
                    case "mul" -> new Mul(a1, a2);
                    case "mod" -> new Mod(a1, a2);
                    case "jgz" -> new JumpGreaterThanZero(a1, a2);
                    case "jnz" -> new JumpNotZero(a1, a2);
                    case "rcv" -> new Receive(a1);
                    default -> throw new IllegalStateException(op);
                };
            }

            record Send(String x) implements Instruction {

            }

            record Set(String x, String y) implements Instruction {

            }

            record Add(String x, String y) implements Instruction {

            }

            record Sub(String x, String y) implements Instruction {

            }

            record Mul(String x, String y) implements Instruction {

            }

            record Mod(String x, String y) implements Instruction {

            }

            record Receive(String x) implements Instruction {

            }

            record JumpGreaterThanZero(String x, String y) implements Instruction {

            }

            record JumpNotZero(String x, String y) implements Instruction {

            }
        }

        private static class ReceiveException extends Exception {
            private final long recovered;


            private ReceiveException(Long l) {
                recovered = l;
            }
        }
    }
}
