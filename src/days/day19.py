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
        return None

# Day19("test").run()
Day19().run()