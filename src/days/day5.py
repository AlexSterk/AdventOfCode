from collections import deque

from src.setup.day import Day
from src.util.solution import solution


def swap_nums(l, i, j):
    l[i], l[j] = l[j], l[i]


class Day5(Day):
    @property
    def day(self):
        return 5

    @solution("6949")
    def part1(self) -> object:
        rules, pages = self.raw_input.split("\n\n")

        rules = set((int(x), int(y)) for x, y in (rule.split("|") for rule in rules.split("\n")))
        pages = [[int(n) for n in row.split(",")] for row in pages.strip().split("\n")]

        total = 0
        for page in pages:
            correct = self.check_pages(page, rules)
            if correct is True:
                total += page[len(page) // 2]

        self.rules = rules
        self.pages = pages
        return total

    def check_pages(self, l, rules):
        for i in range(len(l)):
            for j in range(len(l)):
                if i == j:
                    continue
                x, y = l[i], l[j]
                if (x, y) in rules:
                    if i > j:
                        return i, j
        return True

    @solution("4145")
    def part2(self) -> object:
        rules = self.rules
        pages = self.pages

        total = 0
        q = deque([(p, 0) for p in pages])
        while q:
            page, z = q.popleft()
            correct = self.check_pages(page, rules)
            if correct is True:
                if z == 1:
                    total += page[len(page) // 2]
            else:
                i, j = correct
                swap_nums(page, i, j)
                q.append((page, 1))
        return total


# Day5("test").run()
Day5().run()
