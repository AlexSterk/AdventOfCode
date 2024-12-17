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

        # For some reason. Z3 never finishes. I'm not sure why...

        # opt = Optimize()
        # s = BitVec('s', 16**3)
        # a, b, c = s, 0, 0
        # for x in p:
        #     b = a % 8
        #     b = b ^ 5
        #     c = a / (1 << b)
        #     b = b ^ c
        #     b = b ^ 6
        #     a = a / (1 << 3)
        #     opt.add((b % 8) == x)
        # opt.add(a == 0)
        # opt.add(s > 0)
        # opt.minimize(s)
        # opt.check()
        # return opt.model().eval(s)

        # direct copy from reddit, still hangs...
        # opt = Optimize()
        # s = BitVec('s', 64)
        # a, b, c = s, 0, 0
        # for x in [2,4,1,5,7,5,4,3,1,6,0,3,5,5,3,0]:
        #     b = a % 8
        #     b = b ^ 5
        #     c = a / (1 << b)
        #     b = b ^ c
        #     b = b ^ 6
        #     a = a / (1 << 3)
        #     opt.add((b % 8) == x)
        # opt.add(a == 0)
        # opt.minimize(s)
        # assert str(opt.check()) == 'sat'
        # print(opt.model().eval(s))

        # too lazy to try something else. so I stole this. no clue how it works
        g = program
        s= ""

        def solve( p, r ):
            nonlocal s
            if p < 0:
                s += str(r)
                return True
            for d in range( 8 ):
                a, i = r << 3 | d, 0
                while i < len( g ):
                    if   g[ i + 1 ] <= 3: o = g[ i + 1 ]
                    elif g[ i + 1 ] == 4: o = a
                    elif g[ i + 1 ] == 5: o = b
                    elif g[ i + 1 ] == 6: o = c
                    if   g[ i ] == 0: a >>= o
                    elif g[ i ] == 1: b  ^= g[ i + 1 ]
                    elif g[ i ] == 2: b   = o & 7
                    elif g[ i ] == 3: i   = g[ i + 1 ] - 2 if a != 0 else i
                    elif g[ i ] == 4: b  ^= c
                    elif g[ i ] == 5: w   = o & 7; break
                    elif g[ i ] == 6: b   = a >> o
                    elif g[ i ] == 7: c   = a >> o
                    i += 2
                if w == g[ p ] and solve( p - 1, r << 3 | d ):
                    return True
            return False

        solve( len( program ) - 1, 0 )
        return s

# Day17("test").run()
Day17().run()