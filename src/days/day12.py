import re

import numpy as np

from src.setup.day import Day
from src.util.np_cache import np_cache
from src.util.solution import solution


class Day12(Day):
    @property
    def day(self):
        return 12

    @solution("")
    def part1(self) -> object:
        shapes, trees = self.raw_input.rsplit("\n\n", 1)
        shapes = shapes.split("\n\n")
        shapes = ["\n".join(l.splitlines()[1:]) for l in shapes]
        trees = trees.split("\n")
        p = re.compile(r"(\d+)x(\d+): (.+)")

        _trees = []
        for tree in trees:
            m = p.search(tree)
            width = int(m.group(1))
            height = int(m.group(2))
            ints = [int(n) for n in m.group(3).split(" ")]
            _trees.append((width, height, ints))
        trees = _trees
        print(shapes, trees)

        TOTAL = 0

        def to_np_array(shape):
            arr = np.array([list(l) for l in shape.splitlines()])
            arr = (arr == '#').astype(bool)
            return arr

        shapes = [to_np_array(shape) for shape in shapes]

        @np_cache
        def overlaps(space, shape):
            space = unfreeze(space)
            shape = unfreeze(shape)
            return (space & shape).sum() == 0

        sizes = [shape.sum() for shape in shapes]
        max_sizes = [shape.size for shape in shapes]
        for width, height, presents in trees:
            available_space = width * height
            total_size = sum(sizes[i] * count for i, count in enumerate(presents))
            if available_space < total_size:
                # presents will never fit, skip
                continue
            total_size = sum(max_sizes[i] * count for i, count in enumerate(presents))
            if available_space >= total_size and width % 3 == 0 and height % 3 == 0:
                TOTAL += 1
                continue

            grid = np.zeros([width, height], dtype=bool)



        return TOTAL

    @solution("")
    def part2(self) -> object:
        return None


Day12("test").run()
Day12().run()
