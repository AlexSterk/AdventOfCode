import re
from collections import deque

from src.setup.day import Day
from src.util.point import Point, direction
from src.util.solution import solution

class Day7(Day):
    @property
    def day(self):
        return 7

    @solution("1698")
    def part1(self) -> object:
        splitters = set()
        max_x, max_y = 0, 0
        for y,line in enumerate(self.input):
            max_y = max(max_y, y)
            for x,c in enumerate(line):
                max_x = max(max_x, x)
                if c == '^':
                    splitters.add(Point(x,y))
                elif c == 'S':
                    start = Point(x,y)

        next_beams = deque()
        visited = set()

        count = 0

        next_beams.append(start)
        while next_beams:
            beam = next_beams.pop()
            if beam in visited:
                continue
            visited.add(beam)
            possible_splitter = beam + direction.down
            x, y = possible_splitter
            if possible_splitter in splitters:
                count += 1
                l, r = possible_splitter + direction.left, possible_splitter + direction.right
                if l not in splitters:
                    next_beams.append(l)
                if r not in splitters:
                    next_beams.append(r)
            elif 0 <= x <= max_x and 0 <= y <= max_y:
                next_beams.append(possible_splitter)

        return count

    @solution("")
    def part2(self) -> object:
        return None

# Day7("test").run()
Day7().run()