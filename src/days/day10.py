from collections import deque

from src.setup.day import Day
from src.util.solution import solution

ld = {".": 0, "#": 1}

class Day10(Day):
    @property
    def day(self):
        return 10

    @solution("")
    def part1(self) -> object:
        total = 0
        for line in self.input:
            end, *buttons, _ = line.split(" ")
            end = tuple([ld[c] for c in end.strip("[]")])
            buttons = [e if type(e) == tuple else (e,) for s in buttons if (e := eval(s))]

            state = (0,) * len(end), 0
            visited = set()
            q = deque([state])
            while q:
                state = q.popleft()
                lights, buttons_pressed = state
                if state in visited:
                    continue
                visited.add(state)
                if lights == end:
                    break
                for b in buttons:
                    n_lights = [*lights]
                    for toggle in b:
                        n_lights[toggle] ^= 1
                    n_state = tuple(n_lights), buttons_pressed + 1
                    q.append(n_state)
            total += buttons_pressed
        return total



    @solution("")
    def part2(self) -> object:
        return None


Day10("test").run()
# Day10().run()
