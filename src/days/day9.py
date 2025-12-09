from itertools import combinations

from src.setup.day import Day
from src.util.point import Point
from src.util.solution import solution


class Day9(Day):
    @property
    def day(self):
        return 9

    @solution("4750092396")
    def part1(self) -> object:
        tiles = [Point(*map(int, line.split(","))) for line in self.input]
        max_area = 0
        for p1,p2 in combinations(tiles, 2):
            x1, y1 = p1.x, p1.y
            x2, y2 = p2.x, p2.y

            area = (abs(x1 - x2) + 1) * (abs(y1 - y2) + 1)
            max_area = max(area, max_area)

        return max_area

    @solution("")
    def part2(self) -> object:
        return None

# Day9("test").run()
Day9().run()