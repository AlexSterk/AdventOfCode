import networkx as nx
from networkx.algorithms.components import connected_components

from src.setup.day import Day
from src.util.solution import solution

def area_and_perimeter(region, grid):
    area = len(region)
    perimeter = 0
    for (x, y) in region:
        for dx, dy in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
            if (x + dx, y + dy) not in region:
                perimeter += 1
    return area, perimeter



class Day12(Day):
    @property
    def day(self):
        return 12

    @solution("")
    def part1(self) -> object:
        graph = nx.Graph()
        grid = {(x, y): c for y, line in enumerate(self.input) for x, c in enumerate(line)}

        for (x, y) in grid.keys():
            graph.add_node((x, y))

        for (x, y), c in grid.items():
            for dx, dy in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
                if (x + dx, y + dy) in grid and grid[(x + dx, y + dy)] == c:
                    graph.add_edge((x, y), (x + dx, y + dy))

        regions = connected_components(graph)
        return sum(a * p for region in regions for a, p in [area_and_perimeter(region, grid)])

    @solution("")
    def part2(self) -> object:
        return None

Day12("test").run()
Day12("test2").run()
Day12("test3").run()
Day12().run()