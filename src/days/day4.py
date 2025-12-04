from src.setup.day import Day
from src.util.point import Point
from src.util.solution import solution
import src.util.point.direction as directions


def parse_grid(lines):
    rolls = set()
    for y, line in enumerate(lines):
        for x, c in enumerate(line):
            if c == '@':
                rolls.add(Point(x, y))
    return rolls


class Day4(Day):
    @property
    def day(self):
        return 4

    @solution("1370")
    def part1(self) -> object:
        rolls = parse_grid(self.read_input())
        removed = self.round(rolls)
        return len(removed)

    @solution("8437")
    def part2(self) -> object:
        rolls = parse_grid(self.read_input())
        orig = len(rolls)
        while True:
            removed = self.round(rolls)
            rolls = rolls.difference(removed)
            if len(removed) == 0:
                return orig - len(rolls)

    @staticmethod
    def round(rolls):
        removed = set()
        for p in rolls:
            ns = sum(p + d in rolls for d in directions.all)
            if ns < 4:
                removed.add(p)
        return removed


# Day4("test").run()
Day4().run()
