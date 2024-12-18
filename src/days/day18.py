from dataclasses import dataclass

from src.setup.day import Day
from src.util import directions
from src.util.dijkstra import shortest_path, reconstruct_path
from src.util.solution import solution

class Day18(Day):
    @property
    def day(self):
        return 18

    @solution("314")
    def part1(self) -> object:
        grid_size = 70 if not self.test else 6
        bytes_falling = 1024 if not self.test else 12

        start = (0,0)
        def end(n):
            return n[0] == grid_size and n[1] == grid_size

        walls = self.input[:bytes_falling]
        def ns(node):
            x, y = node
            for d in directions.cardinal.values():
                nx, ny = x + d[0], y + d[1]
                if 0 <= nx <= grid_size and 0 <= ny <= grid_size and f"{nx},{ny}" not in walls:
                    yield nx, ny

        dist_map, end, d = shortest_path(start, end, ns)
        return d

    @solution("15,20")
    def part2(self) -> object:
        grid_size = 70 if not self.test else 6
        bytes_falling = 1024 if not self.test else 12

        start = (0,0)
        def end(n):
            return n == (grid_size, grid_size)

        def ns(node):
            x, y = node
            for d in directions.cardinal.values():
                nx, ny = x + d[0], y + d[1]
                if 0 <= nx <= grid_size and 0 <= ny <= grid_size and f"{nx},{ny}" not in walls:
                    yield nx, ny

        min_i = bytes_falling
        max_i = len(self.input)

        # binary search to find the maximum number of bytes that can be walls and still reach the end
        while min_i < max_i:
            i = (min_i + max_i) // 2
            walls = self.input[:i]
            if shortest_path(start, end, ns)[1] is not None:
                min_i = i + 1
            else:
                max_i = i

        # First byte position that can't be a wall
        return self.input[max_i - 1]

# Day18("test").run()
Day18().run()