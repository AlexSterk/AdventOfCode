import re

from src.setup.day import Day
from src.util.solution import solution


# @functools.cache # cache makes it slower?!
def perform_operation(op, a, b):
    if op == "+":
        return a + b
    if op == "*":
        return a * b
    if op == "||":
        return int(str(a) + str(b))

    raise ValueError(f"Invalid operation: {op}")


def try_equations(equations, ops):
    total = set()
    for equation in equations:
        if try_equation(equation, ops):
            total.add(equation[0])
    return total

def try_equation(equation, ops):
    test, *nums = equation

    if len(nums) == 1:
        return test == nums[0]

    n1, n2, *rest = nums

    if n1 > test:
        return False

    for op in ops:
        if try_equation((test, perform_operation(op, n1, n2), *rest), ops):
            return True
    return False


class Day7(Day):
    @property
    def day(self):
        return 7

    @solution("14711933466277")
    def part1(self) -> object:
        equations = [tuple(map(int, re.split(r"\D+", line.strip()))) for line in self.input]

        tests_only = [eq[0] for eq in equations]
        assert len(tests_only) == len(set(tests_only)), "Duplicate tests"

        ops = ["+", "*"]
        total = try_equations(equations, ops)

        self.equations = equations
        return sum(total)

    @solution("286580387663654")
    def part2(self) -> object:
        equations = self.equations

        total = try_equations(equations, ["*", "||", "+"])

        return sum(total)


# Day7("test").run()
Day7().run()
