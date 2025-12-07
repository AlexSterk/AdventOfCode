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

        state = start, (start,)
        q = deque()
        q.append(state)
        visited = set()

        count = 0

        def in_grid(point):
            return 0 <= point.x <= max_x and 0 <= point.y <= max_y

        while q:
            state = q.pop()
            beam, path = state
            if beam.y == max_y:
                count += 1
                visited.add(state)
            if state in visited or not in_grid(beam):
                continue
            visited.add(state)
            d = beam + direction.down
            if d in splitters:
                l, r = d + direction.left, d + direction.right
                n_state = (l, (*path, l))
                if l not in splitters:
                    q.append(n_state)
                n_state = (r, (*path, r))
                if r not in splitters:
                    q.append(n_state)
            else:
                q.append((d, (*path, d)))

        return count

Day7("test").run()
# Day7().run()