import re

from src.setup.day import Day
from src.util.solution import solution


class Day14(Day):
    @property
    def day(self):
        return 14

    @solution("214400550")
    def part1(self) -> object:
        robots = []
        for line in self.input:
            x, y, dx, dy = re.findall(r'-?\d+', line)
            robots.append({
                "x": int(x),
                "y": int(y),
                "dx": int(dx),
                "dy": int(dy)
            })

        w = 11 if self.test else 101
        h = 7 if self.test else 103

        for robot in robots:
            robot["x"] = (robot["x"] + robot["dx"] * 100) % w
            robot["y"] = (robot["y"] + robot["dy"] * 100) % h

        middle_x = w // 2
        middle_y = h // 2

        q1 = 0
        q2 = 0
        q3 = 0
        q4 = 0

        for robot in robots:
            if robot["x"] < middle_x and robot["y"] < middle_y:
                q1 += 1
            elif robot["x"] > middle_x and robot["y"] < middle_y:
                q2 += 1
            elif robot["x"] < middle_x and robot["y"] > middle_y:
                q3 += 1
            elif robot["x"] > middle_x and robot["y"] > middle_y:
                q4 += 1

        return q1 * q2 * q3 * q4

    @solution("8149")
    def part2(self) -> object:
        robots = []
        for line in self.input:
            x, y, dx, dy = re.findall(r'-?\d+', line)
            robots.append({
                "x": int(x),
                "y": int(y),
                "dx": int(dx),
                "dy": int(dy)
            })

        w = 11 if self.test else 101
        h = 7 if self.test else 103

        def get_state():
            return tuple((robot["x"], robot["y"]) for robot in robots)

        i = 0
        while True:
            i += 1
            for robot in robots:
                robot["x"] = (robot["x"] + robot["dx"]) % w
                robot["y"] = (robot["y"] + robot["dy"]) % h
            if len(robots) == len(set(get_state())):
                grid = [["." for _ in range(w)] for _ in range(h)]
                for robot in robots:
                    grid[robot["y"]][robot["x"]] = "#"
                for row in grid:
                    print("".join(row))
                return i


# Day14("test").run()
Day14().run()
