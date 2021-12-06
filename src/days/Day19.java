package days;

import days.Day16.*;
import setup.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day19 extends Day {
    private int ip, ipRegister, temp;
    private List<? extends Instruction> instructions;

    private void swapIp(int[] registers) {
        temp = registers[ipRegister];
        registers[ipRegister] = ip;
        ip = temp;
    }

    @Override
    public void processInput() {
        String[] lines = input.split("\n");
        String ipDirective = lines[0];
        instructions = Arrays.stream(lines).skip(1).map(s -> s.split(" ")).map(a -> switch (a[0]) {
            case "addr" -> new Addr(Integer.parseInt(a[1]), Integer.parseInt(a[2]), Integer.parseInt(a[3]));
            case "addi" -> new Addi(Integer.parseInt(a[1]), Integer.parseInt(a[2]), Integer.parseInt(a[3]));
            case "mulr" -> new Mulr(Integer.parseInt(a[1]), Integer.parseInt(a[2]), Integer.parseInt(a[3]));
            case "muli" -> new Muli(Integer.parseInt(a[1]), Integer.parseInt(a[2]), Integer.parseInt(a[3]));
            case "banr" -> new Banr(Integer.parseInt(a[1]), Integer.parseInt(a[2]), Integer.parseInt(a[3]));
            case "bani" -> new Bani(Integer.parseInt(a[1]), Integer.parseInt(a[2]), Integer.parseInt(a[3]));
            case "borr" -> new Borr(Integer.parseInt(a[1]), Integer.parseInt(a[2]), Integer.parseInt(a[3]));
            case "bori" -> new Bori(Integer.parseInt(a[1]), Integer.parseInt(a[2]), Integer.parseInt(a[3]));
            case "setr" -> new Setr(Integer.parseInt(a[1]), Integer.parseInt(a[2]), Integer.parseInt(a[3]));
            case "seti" -> new Seti(Integer.parseInt(a[1]), Integer.parseInt(a[2]), Integer.parseInt(a[3]));
            case "gtir" -> new Gtir(Integer.parseInt(a[1]), Integer.parseInt(a[2]), Integer.parseInt(a[3]));
            case "gtri" -> new Gtri(Integer.parseInt(a[1]), Integer.parseInt(a[2]), Integer.parseInt(a[3]));
            case "gtrr" -> new Gtrr(Integer.parseInt(a[1]), Integer.parseInt(a[2]), Integer.parseInt(a[3]));
            case "eqir" -> new Eqir(Integer.parseInt(a[1]), Integer.parseInt(a[2]), Integer.parseInt(a[3]));
            case "eqri" -> new Eqri(Integer.parseInt(a[1]), Integer.parseInt(a[2]), Integer.parseInt(a[3]));
            case "eqrr" -> new Eqrr(Integer.parseInt(a[1]), Integer.parseInt(a[2]), Integer.parseInt(a[3]));
            default -> throw new IllegalStateException("Unexpected value: " + a[0]);
        }).toList();

        ip = 0;
        ipRegister = Character.getNumericValue(ipDirective.charAt(4));
        temp = 0;
    }

    @Override
    public Object part1() {
        int[] registers = new int[6];
        while (registers[ipRegister] < instructions.size()) {
            Instruction inst = instructions.get(registers[ipRegister]);
            Instruction.execute(inst, registers);
            registers[ipRegister]++;
        }
        return registers[0];
    }

    @Override
    public boolean resetForPartTwo() {
        return true;
    }

    /**
     * Can't solve this programatically for any given input
     * For my input the program is to sum the divisors of 10551345
     * You can find this in WolframAlpha --> "sum of divisors 10551345"
     * @return
     */
    @Override
    public Object part2() {
        int[] registers = new int[6];
        registers[0] = 1;
        List<Integer> log = new ArrayList<>();
        while (registers[ipRegister] < instructions.size()) {
            if (registers[ipRegister] == 3) {
                loop(registers);
                continue;
            }
            System.out.println(registers[ipRegister]);
            log.add(registers[ipRegister]);
            Instruction inst = instructions.get(registers[ipRegister]);
            Instruction.execute(inst, registers);
            registers[ipRegister]++;
        }
        System.out.println();
        for (Integer integer : log) {
            System.out.println(integer + " " + instructions.get(integer));
        }
        System.out.println();
        List<? extends Instruction> loop = this.instructions.subList(2, 12);
        for (Instruction instruction : loop) {
            System.out.println(instructions.indexOf(instruction) + " " + instruction);
        }


        return null;
//        return registers[0];
    }

    /*
    3 Mulr[A=1, B=2, C=3]        R3 = R1 * R2             R4 = 3
    4 Eqrr[A=3, B=5, C=3]        R3 = R3 == R5            R4 = 4
    5 Addr[A=3, B=4, C=4]        R4 = R3 (1 or 0) + R4    R4 = 5 --> 6 or 6 --> 7
    6 Addi[A=4, B=1, C=4]        R4 = R4 + 1              R4 = 6
    7 Addr[A=1, B=0, C=0]        R0 = R1 + R0             R4 = 7
    8 Addi[A=2, B=1, C=2]        R2 = R2 + 1              R4 = 8
    9 Gtrr[A=2, B=5, C=3]        R3 = R2 > R5            R4 = 9
    10 Addr[A=4, B=3, C=4]       R4 = R4 + R3 (1 or 0)    R4 = 10 --> 11 or 11 --> 12
    11 Seti[A=2, B=1, C=4]       R4 = 2                   R4 = 11
     */
    /*
        if R1 * R2 == R5:
            R0 = R1 + R0 // +=
        R2 += 1
        if R2 == R5:
            goto 12
        loop
     */
    private void loop(int[] registers) {
        do {
            if (registers[1] * registers[2] == registers[5]) {
                registers[0] += registers[1];
            }
            registers[2] += 1;
        } while (registers[2] <= registers[5]);
        registers[4] = 12;
        registers[3] = 1;
    }

    @Override
    public int getDay() {
        return 19;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}
