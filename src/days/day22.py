from math import floor

from src.setup.day import Day
from src.util.solution import solution

def next_number(n):
    r = n
    a = r * 64
    r = (r ^ a) % 16777216
    b = r // 32
    r = (r ^ b) % 16777216
    c = r * 2048
    r = (r ^ c) % 16777216

    return r

def next_number_repeat(n, i):
    for i in range(i):
        n = next_number(n)
    return n

class Day22(Day):
    @property
    def day(self):
        return 22

    @solution("16999668565")
    def part1(self) -> object:
        return sum(next_number_repeat(i, 2000) for i in map(int, self.input))

    @solution("")
    def part2(self) -> object:
        return None

# Day22("test").run()
Day22().run()