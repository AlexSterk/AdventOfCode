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


class Day9Alt(Day):
    @property
    def day(self):
        return 9

    @solution("4750092396")
    def part1(self) -> object:
        tiles = [Point(*map(int, line.split(","))) for line in self.input]
        max_area = 0

        sorted_combinations = ((Point(min(x1, x2), min(y1, y2)), Point(max(x1, x2), max(y1, y2))) for
                               (x1, y1), (x2, y2) in combinations(tiles, 2))

        for p1, p2 in sorted_combinations:
            area = (p2.x - p1.x + 1) * (p2.y - p1.y + 1)
            max_area = max(area, max_area)

        return max_area

    @solution("1468516555")
    def part2(self) -> object:
        red_tiles = [Point(*map(int, line.split(","))) for line in self.input]
        max_area = 0

        def sort_points(p1: Point, p2: Point) -> (Point, Point):
            return Point(min(p1.x, p2.x), min(p1.y, p2.y)), Point(max(p1.x, p2.x), max(p1.y, p2.y))

        sorted_combinations = (sort_points(p1, p2) for p1, p2 in combinations(red_tiles, 2))
        green_tiles = [sort_points(p1, p2) for p1, p2 in pairwise(red_tiles + [red_tiles[0]])]

        for p1, p2 in sorted_combinations:
            area = (p2.x - p1.x + 1) * (p2.y - p1.y + 1)
            if area > max_area:
                for (x1, y1), (x2, y2) in green_tiles:
                    if x1 < p2.x and y1 < p2.y and x2 > p1.x and y2 > p1.y:
                        break
                else:
                    max_area = area

        return max_area