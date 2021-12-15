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
        return null;
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

    private static class DuetCPU {
        private final Map<Character, Long> registers = new HashMap<>();
        private final List<Instruction> instructions;
        private int ir = 0;
        private final Deque<Long> queue = new ArrayDeque<>();
        private boolean incrementInstruction = false;
        private DuetCPU link;

        private DuetCPU(List<Instruction> instructions, long ID) {
            this.instructions = instructions;
            registers.put('p', ID);
        }

        private static void link(DuetCPU one, DuetCPU two) {
            if (one.link != null) one.link.link = null;
            if (two.link != null) two.link.link = null;
            one.link = two;
            two.link = one;
        }

        private Long run() {
            ir = 0;
            while (ir < instructions.size()) {
                try {
                    cycle();
                } catch (RecoverInstruction e) {
                    return e.recovered;
                }
            }
            return null;
        }

        private void cycle() throws RecoverInstruction {
            incrementInstruction = true;
            Instruction ins = instructions.get(ir);
            executeInstruction(ins);
            if (incrementInstruction) ir++;
        }

        private void executeInstruction(Instruction ins) throws RecoverInstruction {
            switch (ins) {
                case Instruction.Sound s -> (link == null ? this : link).queue.offer(resolve(s.x));
                case Instruction.Recover r -> {
                    if (link == null) {
                        if (resolve(r.x) != 0) throw new RecoverInstruction(queue.getLast());
                    } else if (queue.isEmpty()) incrementInstruction = false;
                    else updateRegister(r.x, queue.poll());
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
            }
        }

        private Long resolve(String a) {
            if (a.length() == 1 && Character.isAlphabetic(a.charAt(0))) return registers.getOrDefault(a.charAt(0), 0L);
            return Long.parseLong(a);
        }

        private void updateRegister(String r, long value) {
            registers.put(r.charAt(0), value);
        }

        private sealed interface Instruction {
            static Instruction Instruction(String s) {
                String[] ss = s.split(" ");
                String op = ss[0];
                String a1 = ss[1];
                String a2 = ss.length > 2 ? ss[2] : null;
                return switch (op) {
                    case "snd" -> new Sound(a1);
                    case "set" -> new Set(a1, a2);
                    case "add" -> new Add(a1, a2);
                    case "mul" -> new Mul(a1, a2);
                    case "mod" -> new Mod(a1, a2);
                    case "jgz" -> new JumpGreaterThanZero(a1, a2);
                    case "rcv" -> new Recover(a1);
                    default -> throw new IllegalStateException(op);
                };
            }

            record Sound(String x) implements Instruction {

            }

            record Set(String x, String y) implements Instruction {

            }

            record Add(String x, String y) implements Instruction {

            }

            record Mul(String x, String y) implements Instruction {

            }

            record Mod(String x, String y) implements Instruction {

            }

            record Recover(String x) implements Instruction {

            }

            record JumpGreaterThanZero(String x, String y) implements Instruction {

            }
        }

        private static class RecoverInstruction extends Exception {
            private final long recovered;


            private RecoverInstruction(Long l) {
                recovered = l;
            }
        }
    }
}
