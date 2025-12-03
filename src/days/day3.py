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

    @solution("172981362045136")
    def part2(self) -> object:
        def joltage(bank, N=12):
            num = ""
            idx = 0
            L = len(bank)
            for n in range(N):
                sub = bank[idx:L - N + n + 1]
                m = max(sub)
                idx = bank.index(m, idx) + 1
                num += str(m)
            return int(num)


        c = 0
        for line in self.read_input():
            c += joltage([int(x) for x in line])
        return c

# Day3("test").run()
Day3().run()