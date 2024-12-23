from networkx import Graph
from networkx.algorithms.clique import enumerate_all_cliques, make_max_clique_graph

from src.setup.day import Day
from src.util.solution import solution


class Day23(Day):
    @property
    def day(self):
        return 23

    @solution("1269")
    def part1(self) -> object:
        g = Graph()

        for line in self.input:
            a, b = line.split("-")
            g.add_edge(a, b)

        total = 0
        for cl in enumerate_all_cliques(g):
            if len(cl) != 3:
                continue
            for n in cl:
                if n.startswith("t"):
                    total += 1
                    break

        self.graph = g
        return total

    @solution("")
    def part2(self) -> object:
        g = self.graph

        max_cl = max(enumerate_all_cliques(g), key=lambda x: len(x))
        max_cl.sort()

        return ",".join(max_cl)

# Day23("test").run()
Day23().run()
