from functools import cache

from src.setup.day import Day
from src.util.solution import solution

class Day3(Day):
    @property
    def day(self):
        return 3

    @solution("17346")
    def part1(self) -> object:
        res = 0
        for line in self.read_input():
            m = 0
            for i, x in enumerate(line[:-1]):
                for y in line[i + 1:]:
                    m = max(m, int(x + y))
            res += m
        return res

    @solution("")
    def part2(self) -> object:
        def joltage(bank, N=12):
            num = []
            idx = 0
            L = len(bank)
            for n in range(N):
                m = max(bank[idx:L-N+n+1])
                idx = bank.index(m, idx) + 1
                num.append(m * 10**(N-1-n))
            return sum(num)


        c = 0
        for line in self.read_input():
            c += joltage([int(x) for x in line])
        return c

# Day3("test").run()
Day3().run()