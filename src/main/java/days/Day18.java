package days;

import setup.Day;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day18 extends Day {

    private DuetCPU duetCPU;

    @Override
    public void processInput() {
        List<DuetCPU.Instruction> instructions = Arrays.stream(input.split("\n")).map(DuetCPU.Instruction::Instruction).toList();
        duetCPU = new DuetCPU(instructions);
    }

    @Override
    public Object part1() {
        return duetCPU.runUntilRecover();
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
        private Long sound = null;
        private boolean recover = false;
        private boolean jump = false;

        private DuetCPU(List<Instruction> instructions) {
            this.instructions = instructions;
        }

        private void run() {
            ir = 0;
            while (ir < instructions.size()) {
                Instruction ins = instructions.get(ir);
                ir++;
                executeInstruction(ins);
            }
        }

        private long runUntilRecover() {
            ir = 0;
            while (ir < instructions.size() && !recover) {
                jump = false;
                Instruction ins = instructions.get(ir);
                executeInstruction(ins);
                if (!jump) ir++;
            }
            return sound;
        }

        private void executeInstruction(Instruction ins) {
            switch (ins) {
                case Instruction.Sound s -> sound = resolve(s.x);
                case Instruction.Recover r -> {
                    if (resolve(r.x()) != 0) {
                        System.out.println(sound);
                        recover = true;
                    }
                }
                case Instruction.Set s -> updateRegister(s.x, resolve(s.y));
                case Instruction.Add a -> updateRegister(a.x, registers.getOrDefault(a.x.charAt(0), 0L) + resolve(a.y));
                case Instruction.Mul m -> updateRegister(m.x, registers.getOrDefault(m.x.charAt(0), 0L) * resolve(m.y));
                case Instruction.Mod m -> updateRegister(m.x, registers.getOrDefault(m.x.charAt(0), 0L) % resolve(m.y));
                case Instruction.JumpGreaterThanZero j -> {
                    if (resolve(j.x) > 0) {
                        ir += resolve(j.y);
                        jump = true;
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
    }
}
