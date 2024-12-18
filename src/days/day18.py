from src.setup.day import Day
from src.util import directions
from src.util.dijkstra import shortest_path
from src.util.solution import solution

class Day18(Day):
    @property
    def day(self):
        return 18

    @solution("")
    def part1(self) -> object:
        walls = set()

        l = 12 if self.test else 1024
        for line in self.input[:l]:
            x, y = map(int, line.split(","))
            walls.add((x, y))

        start = (0,0)
        end = (6,6) if self.test else (70,70)

        def ns(node):
            x, y = node
            for d in directions.cardinal.values():
                nx, ny = x + d[0], y + d[1]
                if (nx, ny) not in walls and 0 <= nx <= 70 and 0 <= ny <= 70:
                    yield nx, ny

        return shortest_path(start, lambda n: n == end, ns)[2]

    @solution("")
    def part2(self) -> object:
        return None

Day18("test").run()
# Day18().run()