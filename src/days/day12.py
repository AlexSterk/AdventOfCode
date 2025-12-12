import re

from src.setup.day import Day
from src.util.solution import solution

class Day12(Day):
    @property
    def day(self):
        return 12

    @solution("528")
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
            _trees.append(((width, height), ints))
        trees = _trees

        def fits(size, presents):
            width, height = size
            presents_total = sum(presents)
            # each present is max. 3 wide and 3 tall
            return presents_total <= (width // 3) * (height // 3)

        shape_sizes = [shape.count("#") for shape in shapes]

        total = 0
        for tree in trees:
            size, presents = tree
            if size[0] * size[1] < sum(shape_sizes[i] * c for i, c in enumerate(presents)):
                continue # definitely wont fit
            if fits(size, presents):
                total += 1
            else:
                print("idk...")

        return total

    @solution("")
    def part2(self) -> object:
        return None

# Day12("test").run()
Day12().run()