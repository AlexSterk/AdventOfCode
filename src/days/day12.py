import json

from src.setup.day import Day
from src.util.solution import solution

class Day12(Day):
    @property
    def day(self):
        return 12

    @solution("156366")
    def part1(self) -> object:
        parsed = json.loads(self.raw_input)

        def recur(obj):
            if isinstance(obj, int):
                return obj
            elif isinstance(obj, list):
                return sum(recur(o) for o in obj)
            elif isinstance(obj, dict):
                return sum(recur(o) for o in obj.values())
            return 0

        return recur(parsed)

    @solution("96852")
    def part2(self) -> object:
        parsed = json.loads(self.raw_input)

        def recur(obj):
            if isinstance(obj, int):
                return obj
            elif isinstance(obj, list):
                return sum(recur(o) for o in obj)
            elif isinstance(obj, dict):
                if "red" in obj.values():
                    return 0
                return sum(recur(o) for o in obj.values())
            return 0

        return recur(parsed)

# Day12("test").run()
Day12().run()
