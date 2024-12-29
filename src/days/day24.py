from functools import reduce
from itertools import combinations

from src.setup.day import Day
from src.util.solution import solution

class Day24(Day):
    @property
    def day(self):
        return 24

    @solution("10439961859")
    def part1(self) -> object:
        packages = [int(n) for n in self.input]
        packages.sort(reverse=True)
        total_weight = sum(packages)

        group_weight = total_weight // 3

        for i in range(len(packages)):
            res = [reduce(lambda x, y: x * y, c) for c in combinations(packages, i) if sum(c) == group_weight]
            if res:
                return min(res)

    @solution("72050269")
    def part2(self) -> object:
        packages = [int(n) for n in self.input]
        packages.sort(reverse=True)
        total_weight = sum(packages)

        group_weight = total_weight // 4

        for i in range(len(packages)):
            res = [reduce(lambda x, y: x * y, c) for c in combinations(packages, i) if sum(c) == group_weight]
            if res:
                return min(res)

# Day24("test").run()
Day24().run()
