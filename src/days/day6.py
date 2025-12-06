import re

from attr import dataclass

from src.setup.day import Day
from src.util.solution import solution


@dataclass
class Equation:
    operand: str
    terms: list[int]

    def eval(self):
        cur = self.terms[0]
        for term in self.terms[1:]:
            if self.operand == '*':
                cur *= term
            elif self.operand == '+':
                cur += term
        return cur


class Day6(Day):
    @property
    def day(self):
        return 6

    @solution("5595593539811")
    def part1(self) -> object:
        equations = self.parsed()

        return sum(equation.eval() for equation in equations)

    @solution("")
    def part2(self) -> object:
        return None

    def parsed(self):
        data = re.split(r"\s+", self.raw_input)
        for i, s in enumerate(data):
            if re.match(r"[*+]", s):
                break
        number_of_equations = len(data[i:])
        terms_per_equation = i // number_of_equations
        equations = []
        for i in range(number_of_equations):
            terms = data[i:i + number_of_equations * (terms_per_equation + 1):number_of_equations]
            operand = terms.pop()
            terms = [int(s) for s in terms]
            equations.append(Equation(operand, terms))
        return equations


# Day6("test").run()
Day6().run()
