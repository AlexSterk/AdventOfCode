from itertools import permutations

from src.setup.day import Day
from src.util.solution import solution

import networkx as nx

class Day9(Day):
    @property
    def day(self):
        return 9

    @solution("251")
    def part1(self) -> object:
        g = nx.Graph()

        for line in self.input:
            a, _, b, _, n = line.split()
            g.add_edge(a, b, weight=int(n))

        places = list(g.nodes)
        min_distance = float("inf")
        for path in permutations(places):
            distance = 0
            for i in range(len(path) - 1):
                distance += g[path[i]][path[i + 1]]["weight"]
            min_distance = min(min_distance, distance)

        self.graph = g
        return min_distance

    @solution("")
    def part2(self) -> object:
        graph = self.graph
        places = list(graph.nodes)
        max_distance = 0
        for path in permutations(places):
            distance = 0
            for i in range(len(path) - 1):
                distance += graph[path[i]][path[i + 1]]["weight"]
            max_distance = max(max_distance, distance)
        return max_distance

# Day9("test").run()
Day9().run()
