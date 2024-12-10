from src.setup.day import Day
from src.util.dijkstra import shortest_path
from src.util.solution import solution


class Day10(Day):
    @property
    def day(self):
        return 10

    @solution("430")
    def part1(self) -> object:
        grid = {(x, y): int(c) for y, row in enumerate(self.input) for x, c in enumerate(row)}

        def neighbours(node):
            X, Y, C = node
            dirs = [(0, 1), (0, -1), (1, 0), (-1, 0)]
            ns = [(X + dx, Y + dy) for dx, dy in dirs]
            ns = [(x, y, grid.get((x, y))) for x, y in ns if (x, y) in grid]
            ns = [(x, y, c) for x, y, c in ns if c == C + 1]
            return ns

        starts = [(*k, v) for k, v in grid.items() if v == 0]
        total = 0
        for start in starts:
            dist, _, _ = shortest_path(start, lambda _: False, neighbours, lambda a, b: 1)
            total += sum(1 for x,y,c in dist if c == 9)

        return total

    @solution("")
    def part2(self) -> object:
        return None


# Day10("test").run()
Day10().run()
