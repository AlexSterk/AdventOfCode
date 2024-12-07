import functools
import re
from collections import deque

from src.setup.day import Day
from src.util.solution import solution

@functools.cache
def perform_operation(op, a, b):
    if op == "+":
        return a + b
    if op == "*":
        return a * b
    if op == "||":
        return int(str(a) + str(b))

    raise ValueError(f"Invalid operation: {op}")

class Day7(Day):
    @property
    def day(self):
        return 7

    @solution("14711933466277")
    def part1(self) -> object:
        equations = [tuple(map(int, re.split(r"\D+", line.strip() ))) for line in self.input]

        tests_only = [eq[0] for eq in equations]
        assert len(tests_only) == len(set(tests_only)), "Duplicate tests"

        q = deque(equations)

        total = set()
        while q:
            test, *nums = q.popleft()

            if len(nums) == 1:
                if test == nums[0]:
                    total.add(test)
                continue

            n1, n2, *rest = nums
            q.appendleft((test, perform_operation("+", n1, n2), *rest))
            q.appendleft((test, perform_operation("*", n1, n2), *rest))

        return sum(total)

    @solution("286580387663654")
    def part2(self) -> object:
        equations = [tuple(map(int, re.split(r"\D+", line.strip() ))) for line in self.input]

        tests_only = [eq[0] for eq in equations]
        assert len(tests_only) == len(set(tests_only)), "Duplicate tests"

        q = deque(equations)

        total = set()
        while q:
            test, *nums = q.popleft()

            if len(nums) == 1:
                if test == nums[0]:
                    total.add(test)
                continue

            n1, n2, *rest = nums
            q.appendleft((test, perform_operation("+", n1, n2), *rest))
            q.appendleft((test, perform_operation("*", n1, n2), *rest))
            q.appendleft((test, perform_operation("||", n1, n2), *rest))

        return sum(total)

# Day7("test").run()
Day7().run()
