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
        L = 12
        @cache
        def build_n(s, l):
            if len(s) == L:
                return int(s)
            if l == "":
                return 0
            return max(build_n(s + l[0], l[1:]), build_n(s, l[1:]))

        res = 0
        for line in self.read_input():
            m = max(0, build_n("", line))
            print(m)
            res += m
        return res

# Day3("test").run()
Day3().run()