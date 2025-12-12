import re
import numpy as np

from src.setup.day import Day
from src.util.np_cache import np_cache
from src.util.solution import solution


def to_np_array(shape):
    """Convert string shape to boolean ndarray."""
    arr = np.array([list(l) for l in shape.splitlines()])
    arr = (arr == '#').astype(bool)
    return arr


def all_rotations_and_flips(arr: np.ndarray):
    """
    Generate all rotations and vertical flips of a 2D ndarray.
    Yields arrays.
    """
    for flip in [False, True]:
        temp = np.flipud(arr) if flip else arr
        for k in range(4):
            rotated = np.rot90(temp, k)
            yield rotated


def unique_arrays(arr_list):
    """Remove duplicates from a list of ndarrays."""
    seen = set()
    unique = []
    for arr in arr_list:
        key = arr.tobytes()
        if key not in seen:
            seen.add(key)
            unique.append(arr)
    return unique


# 3x3 grids represented as 9-bit integers
def to_int(grid):
    return sum((1 << i) if val else 0 for i, val in enumerate(grid.flatten()))


def overlaps_int(grid_int, variant_int):
    return (grid_int & variant_int) != 0


def variants_to_ints(variants):
    return [to_int(v) for v in variants]


def sliding_windows(grid, height, width):
    H, W = grid.shape
    for y in range(H - height + 1):
        for x in range(W - width + 1):
            yield x, y


class Day12(Day):
    @property
    def day(self):
        return 12

    @solution("")
    def part1(self) -> object:
        shapes_str, trees_str = self.raw_input.rsplit("\n\n", 1)
        shapes = shapes_str.split("\n\n")
        shapes = ["\n".join(l.splitlines()[1:]) for l in shapes]

        p = re.compile(r"(\d+)x(\d+): (.+)")
        trees = []
        for tree in trees_str.split("\n"):
            m = p.search(tree)
            width = int(m.group(1))
            height = int(m.group(2))
            ints = [int(n) for n in m.group(3).split(" ")]
            trees.append((width, height, ints))

        TOTAL = 0

        # Convert shapes to boolean arrays
        shapes_bool = [to_np_array(shape) for shape in shapes]

        # Precompute unique rotated/flipped variants
        variants_list = [
            unique_arrays(list(all_rotations_and_flips(shape)))
            for shape in shapes_bool
        ]

        # Convert variants to 3x3 integers
        variant_ints_list = [variants_to_ints(vs) for vs in variants_list]

        sizes = [shape.sum() for shape in shapes_bool]
        max_sizes = [shape.size for shape in shapes_bool]

        @np_cache
        def presents_fit(grid, presents_tuple):
            """
            Recursive in-place backtracking to check if presents fit.
            grid_bytes: flattened boolean grid as bytes
            grid_shape: shape of grid
            presents_tuple: tuple of remaining counts
            """
            if all(p == 0 for p in presents_tuple):
                return True

            presents = list(presents_tuple)
            grid = grid.copy()

            for i, count in enumerate(presents):
                if count == 0:
                    continue

                presents_copy = presents.copy()
                presents_copy[i] -= 1

                for variant_int in variant_ints_list[i]:
                    for x, y in sliding_windows(grid, 3, 3):
                        subgrid = grid[y:y + 3, x:x + 3]
                        subgrid_int = to_int(subgrid)

                        if not overlaps_int(subgrid_int, variant_int):
                            # Place variant in-place
                            variant = variants_list[i][variant_ints_list[i].index(variant_int)]
                            subgrid[:,:] = variant

                            if presents_fit(grid, tuple(presents_copy)):
                                return True

                            # Backtrack
                            subgrid[:, :] = False
            return False

        for width, height, presents in trees:
            available_space = width * height
            total_size = sum(sizes[i] * count for i, count in enumerate(presents))
            if available_space < total_size:
                continue
            total_size_max = sum(max_sizes[i] * count for i, count in enumerate(presents))
            if available_space >= total_size_max and width % 3 == 0 and height % 3 == 0:
                TOTAL += 1
                continue

            grid = np.zeros([height, width], dtype=bool)
            if presents_fit(grid, tuple(presents)):
                TOTAL += 1

        return TOTAL

    @solution("")
    def part2(self) -> object:
        return None


# Day12("test").run()
Day12().run()
