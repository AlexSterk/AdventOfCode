import re

import numpy as np

from src.setup.day import Day
from src.util.np_cache import np_cache
from src.util.solution import solution


def to_np_array(shape):
    arr = np.array([list(l) for l in shape.splitlines()])
    arr = (arr == '#').astype(bool)
    return arr


@np_cache
def overlaps(space, shape):
    return (space & shape).sum() > 0


def all_rotations_and_flips(arr: np.ndarray):
    """
    Generate all unique rotations and flips of a 2D ndarray.
    Returns a list of arrays.
    """
    for flip in [False, True]:
        temp = np.flipud(arr) if flip else arr
        for k in range(4):
            rotated = np.rot90(temp, k)
            yield rotated


def sliding_windows(grid: np.ndarray, width, height):
    w, h = grid.shape
    for y in range(h - height + 1):
        for x in range(w - width + 1):
            yield grid[x: x + width, y: y + height]

def unique_arrays(arr_list):
    seen = set()
    unique = []
    for arr in arr_list:
        key = arr.tobytes()  # hashable representation
        if key not in seen:
            seen.add(key)
            unique.append(arr)
    return unique


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
        # print(shapes, trees)

        TOTAL = 0

        shapes = [to_np_array(shape) for shape in shapes]
        variants = {i: list(all_rotations_and_flips(shape)) for i, shape in enumerate(shapes)}
        for i, l in variants.items():
            variants[i] = unique_arrays(l)


        def presents_fit(grid, presents) -> bool:
            if all(p == 0 for p in presents):
                return True

            w, h = grid.shape
            for i, c in enumerate(presents):
                if c == 0:
                    continue
                presents_copy = presents.copy()
                presents_copy[i] -= 1
                vs = variants[i]
                for variant in vs:
                    for y in range(h - 3 + 1):
                        for x in range(w - 3 + 1):
                            subgrid = grid[x: x + 3, y: y + 3]
                            if not overlaps(subgrid, variant):
                                grid_copy = grid.copy()
                                grid_copy[x: x + 3, y: y + 3] = variant
                                if presents_fit(grid_copy, presents_copy):
                                    return True

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

            if presents_fit(grid, presents):
                TOTAL += 1

        return TOTAL

    @solution("")
    def part2(self) -> object:
        return None


Day12("test").run()
# Day12().run()
