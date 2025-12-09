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
        self.tiles = tiles = [Point(*map(int, line.split(","))) for line in self.input]
        max_area = 0
        for p1,p2 in combinations(tiles, 2):
            x1, y1 = p1.x, p1.y
            x2, y2 = p2.x, p2.y

            area = (abs(x1 - x2) + 1) * (abs(y1 - y2) + 1)
            max_area = max(area, max_area)

        return max_area

    @solution("")
    def part2(self) -> object:
        red_tiles = self.tiles

        min_x = min(p.x for p in red_tiles)
        max_x = max(p.x for p in red_tiles)
        min_y = min(p.y for p in red_tiles)
        max_y = max(p.y for p in red_tiles)

        def is_inside(point: Point) -> bool:
            x, y = point
            n = len(red_tiles)

            for i in range(n):
                p1 = red_tiles[i]
                p2 = red_tiles[(i + 1) % n]

                cross_product = (p2.y - p1.y) * (x - p1.x) - (p2.x - p1.x) * (y - p1.y)
                if abs(cross_product) < 1e-9:
                    if min(p1.x, p2.x) <= x <= max(p1.x, p2.x) and min(p1.y, p2.y) <= y <= max(p1.y, p2.y):
                        return True

            inside = False
            p1 = red_tiles[0]
            for i in range(n + 1):
                p2 = red_tiles[i % n]

                if min(p1.y, p2.y) < y <= max(p1.y, p2.y) and x <= max(p1.x, p2.x):
                    if p1.y != p2.y:
                        x_intersection = (y - p1.y) * (p2.x - p1.x) / (p2.y - p1.y) + p1.x

                    if p1.x == p2.x or x <= x_intersection:
                        inside = not inside

                p1 = p2
            return inside


        max_area = 0
        for p1,p2 in combinations(red_tiles, 2):
            x1, y1 = p1
            x2, y2 = p2

            c1 = Point(x1, y1)
            c2 = Point(x2, y2)
            c3 = Point(x1, y2)
            c4 = Point(x2, y1)

            if all(is_inside(c) for c in [c1, c2, c3, c4]):
                area = (abs(x1 - x2) + 1) * (abs(y1 - y2) + 1)
                max_area = max(area, max_area)

        return max_area

# Day9("test").run()
Day9().run()