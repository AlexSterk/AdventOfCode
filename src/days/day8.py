from collections import deque
from itertools import product, combinations

import numpy as np

from src.setup.day import Day
from src.util.solution import solution


class Day8(Day):
    @property
    def day(self):
        return 8

    @solution("46398")
    def part1(self) -> object:
        boxes = {
            coords: {coords}
            for line in self.input
            if (coords := tuple(map(int, line.split(","))))
        }

        distances = {}
        for box1, box2 in combinations(boxes, 2):
            distances[box1, box2] = np.linalg.norm(np.array(box1) - np.array(box2))

        sorted_distances = sorted(distances.items(), key=lambda x: x[1])
        runs = 10 if self.test else 1000
        for i in range(runs):
            closest = sorted_distances[i][0]
            p1, p2 = closest
            merged = boxes[p1].union(boxes[p2])
            for p in merged:
                boxes[p] = merged

        circuits = set(frozenset(s) for s in boxes.values())
        # print(circuits)
        sorted_circuits = sorted(circuits, key=len, reverse=True)

        one,two,three = sorted_circuits[0:3]

        return len(one) * len(two) * len(three)

    @solution("8141888143")
    def part2(self) -> object:
        boxes = {
            coords: {coords}
            for line in self.input
            if (coords := tuple(map(int, line.split(","))))
        }

        distances = {}
        for box1, box2 in combinations(boxes, 2):
            distances[box1, box2] = np.linalg.norm(np.array(box1) - np.array(box2))

        sorted_distances = sorted(distances.items(), key=lambda x: x[1])
        i = 0
        while True:
            closest = sorted_distances[i][0]
            p1, p2 = closest
            merged = boxes[p1].union(boxes[p2])
            for p in merged:
                boxes[p] = merged
            if len(merged) == len(boxes):
                break
            i += 1

        return p1[0] * p2[0]

# Day8("test").run()
Day8().run()