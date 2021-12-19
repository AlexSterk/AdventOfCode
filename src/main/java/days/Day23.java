package days;

import setup.Day;

import java.util.Arrays;
import java.util.List;

import static days.Day18.DuetCPU;
import static days.Day18.DuetCPU.Instruction;

public class Day23 extends Day {

    private List<Instruction> instructions;

    @Override
    public void processInput() {
        instructions = Arrays.stream(input.split("\n")).map(Instruction::Instruction).toList();
    }

    @Override
    public Object part1() {
        DuetCPU cpu = new DuetCPU(instructions);
        cpu.run();
        return cpu.mulCount;
    }

    @Override
    public Object part2() {
        /*
                               do:
            set f 1                f = 1
            set d 2                d = 2
            set e 2                e = 2
                                   do:
                                       do:
            set g d                         g = d
            mul g e                         g *= e
            sub g b                         g -= b
            jnz g 2                         if g == 0:        h is incremented when f = 0, this is true when
            set f 0                             f = 0         d * e - b == 0 --> d * e == b
            sub e -1                        e += 1
            set g e                         g = e             if d and e are factors of b, increment h
            sub g b                         g -= b            then add 17 to b until b == c
            jnz g -8                   while g != 0
            sub d -1                   d += 1
            set g d                    g = d
            sub g b                    g -= b
            jnz g -13              while g != 0
            jnz f 2                if f == 0:
            sub h -1                   h += 1
            set g b                g = b
            sub g c                g -= c                    b - c == 0 --> b == c exits the program
            jnz g 2                if g == 0:                what is h when that is true?
            jnz 1 3                    return
            sub b -17              b += 17
            jnz 1 -23          while True
         */

        int b = 93;
        int c = 93;
        b *= 100;
        b += 100000;
        c = b;
        c += 17000;
        int h = 0;
        while (true) {
            boolean f = false;
            int d = 2;
            do {
                if (b % d == 0) {
                    f = true;
                    break;
                }
                d++;
            } while (d != b);
            if (f) h++;
            if (b == c) break;
            b += 17;
        }

        return h;
    }

    @Override
    public int getDay() {
        return 23;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "8281";
    }

    @Override
    public String partTwoSolution() {
        return "911";
    }
}
