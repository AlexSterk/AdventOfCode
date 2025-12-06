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
        data = re.split(r"\s+", self.raw_input.strip().removesuffix("\n"))
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

        return sum(equation.eval() for equation in equations)

    @solution("10153315705125")
    def part2(self) -> object:
        lines = self.input[:-1]
        operands = self.input[-1]
        i = 0
        equations = []
        for operand in re.findall(r"[*+]\s*", operands):
            i = operands.find(operand, i)
            terms = []
            for line in lines:
                end = i + len(operand)
                if len(operand) == 1:
                    end=i + len(line)
                term = line[i:end].removesuffix(" ")
                terms.append(term)
            i += 1
            max_l = len(max(terms, key=len))
            int_terms = []
            for j in range(max_l):
                s = [term.ljust(max_l, " ")[j] for term in terms]
                s = str.join("", s).strip()
                if len(s) > 0:
                    int_terms.append(int(s))
            e = Equation(operand.strip(), int_terms)
            equations.append(e)

        return sum(equation.eval() for equation in equations)


# Day6("test").run()
Day6().run()
