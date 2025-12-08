from functools import cache
from functools import cache
from itertools import combinations

import numpy as np

from src.setup.day import Day
from src.util.solution import solution


def merge_boxes(boxes, i, sorted_distances):
    closest = sorted_distances[i][0]
    p1, p2 = closest
    merged = boxes[p1].union(boxes[p2])
    for p in merged:
        boxes[p] = merged
    return merged, p1, p2


class Day8(Day):
    @property
    def day(self):
        return 8

    @solution("46398")
    def part1(self) -> object:
        boxes, sorted_distances = self.set_up()
        runs = 10 if self.test else 1000
        for i in range(runs):
            merge_boxes(boxes, i, sorted_distances)

        circuits = set(frozenset(s) for s in boxes.values())
        # print(circuits)
        sorted_circuits = sorted(circuits, key=len, reverse=True)

        one,two,three = sorted_circuits[0:3]

        return len(one) * len(two) * len(three)

    @solution("8141888143")
    def part2(self) -> object:
        boxes, sorted_distances = self.set_up()
        i = 0
        while True:
            merged, p1, p2 = merge_boxes(boxes, i, sorted_distances)
            if len(merged) == len(boxes):
                break
            i += 1

        return p1[0] * p2[0]

    @cache
    def set_up(self):
        boxes = {
            coords: {coords}
            for line in self.input
            if (coords := tuple(map(int, line.split(","))))
        }
        distances = {}
        for box1, box2 in combinations(boxes, 2):
            distances[box1, box2] = np.linalg.norm(np.array(box1) - np.array(box2))
        sorted_distances = sorted(distances.items(), key=lambda x: x[1])
        return boxes, sorted_distances


# Day8("test").run()
Day8().run()