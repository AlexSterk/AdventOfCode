from itertools import combinations

from shapely.geometry.polygon import Polygon

from src.setup.day import Day
from src.util.point import Point
from src.util.solution import solution


class Day9(Day):
    @property
    def day(self):
        return 9

    @solution("4750092396")
    def part1(self) -> object:
        self.tiles = tiles = [Point(*map(int, line.split(","))) for line in self.input]
        max_area = 0
        for p1,p2 in combinations(tiles, 2):
            x1, y1 = p1.x, p1.y
            x2, y2 = p2.x, p2.y

            area = (abs(x1 - x2) + 1) * (abs(y1 - y2) + 1)
            max_area = max(area, max_area)

        return max_area

    @solution("1468516555")
    def part2(self) -> object:
        polygon = Polygon([p for p in self.tiles])

        max_area = 0
        for p1,p2 in combinations(self.tiles, 2):
            x1, y1 = p1
            x2, y2 = p2

            rectangle = Polygon([(x1, y1), (x1, y2), (x2, y2), (x2, y1)])
            if rectangle.within(polygon):
                area = (abs(x1 - x2) + 1) * (abs(y1 - y2) + 1)
                max_area = max(area, max_area)

        return max_area
# Day9("test").run()
Day9().run()