from functools import cache

from src.setup.day import Day
from src.util.solution import solution

class Day19(Day):
    @property
    def day(self):
        return 19

    @solution("216")
    def part1(self) -> object:
        available, patterns = self.raw_input.split("\n\n")
        available = available.split(", ")
        patterns = patterns.split("\n")


        def check_pattern(pattern, s):
            if pattern == s:
                return True
            if pattern.startswith(s):
                return any(check_pattern(pattern, s + a) for a in available)
            return False


        return sum(check_pattern(pattern, "") for pattern in patterns)

    @solution("")
    def part2(self) -> object:
        available, patterns = self.raw_input.split("\n\n")
        available = available.split(", ")
        patterns = patterns.split("\n")


        @cache
        def check_pattern(pattern, s):
            if pattern == s:
                return 1
            if pattern.startswith(s):
                return sum(check_pattern(pattern, s + a) for a in available)
            return 0


        return sum(check_pattern(pattern, "") for pattern in patterns)

# Day19("test").run()
Day19().run()