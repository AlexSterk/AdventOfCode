from collections import defaultdict

from src.setup.day import Day
from src.util.solution import solution

class Day17(Day):
    @property
    def day(self):
        return 17

    @solution("4372")
    def part1(self) -> object:
        containers = [int(x) for x in self.input]
        containers.sort(reverse=True)
        fit = 150

        def find_combinations(containers, fit):
            if fit == 0:
                return 1
            if fit < 0 or len(containers) == 0:
                return 0
            return find_combinations(containers[1:], fit) + find_combinations(containers[1:], fit - containers[0])

        return find_combinations(containers, fit)

    @solution("4")
    def part2(self) -> object:
        containers = [int(x) for x in self.input]
        containers.sort(reverse=True)
        fit = 150

        counts = defaultdict(int)

        def find_combinations(containers, fit, used_containers=[]):
            if fit == 0:
                counts[len(used_containers)] += 1
                return 1
            if fit < 0 or len(containers) == 0:
                return 0
            return find_combinations(containers[1:], fit, used_containers) + find_combinations(containers[1:], fit - containers[0], used_containers + [containers[0]])

        find_combinations(containers, fit)
        return counts[min(counts.keys())]

# Day17("test").run()
Day17().run()
