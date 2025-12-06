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
        # Split into list of terms, don't care about new lines for now
        tokens = re.split(r"\s+", self.raw_input.strip().removesuffix("\n"))
        # find the first index of an operand, that is where the ints end
        first_operand_index = next(i for i,s in enumerate(tokens) if re.match(r"[*+]", s))
        # calculate how many operands, and therefore, equations there are
        number_of_equations = len(tokens) - first_operand_index
        # calculate how many terms each equation has
        terms_per_equation = first_operand_index // number_of_equations
        # build equation list
        equations = []
        for i in range(number_of_equations):
            # for each equation, get all the terms
            terms = tokens[i:first_operand_index:number_of_equations]
            # get the operand
            operand = tokens[first_operand_index + i]
            # map terms
            terms = [int(s) for s in terms]
            # convert to equation
            equations.append(Equation(operand, terms))

        # eval equations
        return sum(equation.eval() for equation in equations)

    @solution("10153315705125")
    def part2(self) -> object:
        term_lines = self.input[:-1]
        operand_line = self.input[-1]

        equations = []
        last_index = 0
        for op_match in re.findall(r"[*+]\s*", operand_line):
            last_index = operand_line.find(op_match, last_index)
            terms = []
            for line in term_lines:
                end = last_index + len(op_match)
                if len(op_match) == 1:
                    end=last_index + len(line)
                term = line[last_index:end].removesuffix(" ")
                terms.append(term)
            last_index += 1
            max_l = len(max(terms, key=len))
            int_terms = []
            for j in range(max_l):
                s = [term.ljust(max_l, " ")[j] for term in terms]
                s = str.join("", s).strip()
                if len(s) > 0:
                    int_terms.append(int(s))
            e = Equation(op_match.strip(), int_terms)
            equations.append(e)

        return sum(equation.eval() for equation in equations)


# Day6("test").run()
Day6().run()
