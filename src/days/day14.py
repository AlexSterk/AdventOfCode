from collections import defaultdict

from src.setup.day import Day
from src.util.solution import solution

class Day14(Day):
    @property
    def day(self):
        return 14

    @solution("2640")
    def part1(self) -> object:
        end = 2503

        reindeer = {}

        for line in self.input:
            parts = line.split(" ")
            name = parts[0]
            speed = int(parts[3])
            fly = int(parts[6])
            rest = int(parts[13])
            reindeer[name] = (speed, fly, rest)

        distances = {}

        for name, (speed, fly, rest) in reindeer.items():
            time = 0
            distance = 0
            while time < end:
                distance += speed * min(fly, end - time)
                time += fly + rest
            distances[name] = distance

        self.reindeer = reindeer
        return max(distances.values())


    @solution("1102")
    def part2(self) -> object:
        reindeer = self.reindeer
        points = defaultdict(int)
        distances = defaultdict(int)

        for i in range(2503):
            for name, (speed, fly, rest) in reindeer.items():
                if i % (fly + rest) < fly:
                    distances[name] += speed
            max_distance = max(distances.values())
            for name, distance in distances.items():
                if distance == max_distance:
                    points[name] += 1

        return max(points.values())

# Day14("test").run()
Day14().run()
