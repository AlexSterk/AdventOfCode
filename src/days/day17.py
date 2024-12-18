import re
from z3 import *

from src.setup.day import Day
from src.util.solution import solution

class Day17(Day):
    @property
    def day(self):
        return 17

    @solution("7,3,1,3,6,3,6,0,2")
    def part1(self) -> object:
        registers, program = self.raw_input.split("\n\n")
        p = r"Register (.+): (\d+)"
        registers = {k: int(v) for k, v in re.findall(p, registers)}
        program = list(map(int, program.replace("Program: ", "").split(",")))
        print(registers, program)

        # program =
        # 2,4        bst 4   B = A % 8
        # 1,5        bxl 5   B = B ^ 5
        # 7,5        cdv 5   C = A // 2 ** B
        # 1,6        bxl 6   B = B ^ 6
        # 0,3        adv 3   A = A // 2 ** 3
        # 4,0        bxc 0   B = B ^ C
        # 5,5        out 5   print(B % 8)
        # 3,0        jnz 0   if A != 0: goto 0

        def program(a):
            output = []
            while a != 0:
                b = a % 8
                b = b ^ 5
                c = a // (2 ** b)
                b = b ^ 6
                a = a // (2 ** 3)
                b = b ^ c
                output.append(b % 8)
            return output

        return ",".join(map(str, program(registers["A"])))

    @solution("105843716614554")
    def part2(self) -> object:
        program = self.raw_input.split("\n\n")[1]
        program = list(map(int, program.replace("Program: ", "").split(",")))

        solver = Solver()
        s = BitVec('s', 64)
        solver.add(s > 0)
        a, b, c = s, 0, 0
        for x in program:
            b = a % 8
            b = b ^ 5
            c = a / (1 << b)
            b = b ^ c
            b = b ^ 6
            a = a / 8
            solver.add((b % 8) == x)
        solver.add(a == 0)

        m = float('inf')
        while solver.check() == sat:
            m = min(m, solver.model().eval(s).as_long())
            solver.add(s < m)
        return m

# Day17("test").run()
Day17().run()