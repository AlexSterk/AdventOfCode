import dataclasses
import itertools
import re

from src.setup.day import Day
from src.util.solution import solution

@dataclasses.dataclass
class Node:
    def __init__(self, size, used, avail):
        self.size = size
        self.used = used
        self.avail = avail

    def __repr__(self):
        return f"{self.size} {self.used} {self.avail}"

class Day22(Day):
    @property
    def day(self):
        return 22

    @solution("950")
    def part1(self) -> object:
        p = r"\/dev\/grid\/node-x(\d+)-y(\d+)\s+(\d+)T\s+(\d+)T\s+(\d+)T\s+(\d+)%"
        nodes = {}
        for line in self.input[2:]:
            x, y, size, used, avail, _ = map(int, re.match(p, line).groups())
            nodes[(x, y)] = Node(size, used, avail)

        viable_pairs = 0
        for a,b in itertools.permutations(nodes.values(), 2):
            if a.used == 0:
                continue
            if a.used <= b.avail:
                viable_pairs += 1

        self.nodes = nodes
        return viable_pairs

    @solution("")
    def part2(self) -> object:
        nodes = self.nodes
        max_x = max(x for x, _ in nodes.keys())

        empty = next(k for k, v in nodes.items() if v.used == 0)

        steps = 0
        # move to the left side of the grid
        steps += empty[0]
        # move to the top of the grid
        steps += empty[1]
        # move to the right side of the grid (1 step to the left of the goal)
        steps += (max_x - 1)
        # move to the goal
        steps += 5 * (max_x - 1)
        # move the goal to 0,0
        steps += 1
        return steps

# Day22("test").run()
Day22().run()
