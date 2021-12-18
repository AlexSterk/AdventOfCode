import functools
import math
import typing

IN = '../../../../data/day18/input.txt'

ns = []


class Tree:
    left = None
    right = None
    parent = None

    def __init__(self, pair, parent):
        self.parent = parent
        if type(pair[0]) == Tree:
            self.left = pair[0]
        elif type(pair[0]) == int:
            self.left = Leaf(pair[0], self)
        else:
            self.left = Tree(pair[0], self)

        if type(pair[1]) == Tree:
            self.right = pair[1]
        elif type(pair[1]) == int:
            self.right = Leaf(pair[1], self)
        else:
            self.right = Tree(pair[1], self)

        self.left.parent = self
        self.right.parent = self

    def __repr__(self):
        return f"[{self.left},{self.right}]"

    def _tuple(self):
        return eval(self.__repr__().replace("[", "(").replace("]", ")"))

    def copy(self):
        return Tree(self._tuple(), None)

    @property
    def depth(self):
        return max(self.left.depth, self.right.depth) + 1

    def leaves(self, depth=0) -> typing.List[typing.Tuple["Leaf", int]]:
        return self.left.leaves(depth + 1) + self.right.leaves(depth + 1)

    @property
    def magnitude(self):
        return 3 * self.left.magnitude + 2 * self.right.magnitude


class Leaf(Tree):
    value = None
    depth = 0

    def __init__(self, value, parent):
        self.value = value
        self.parent = parent

    def __repr__(self):
        return str(self.value)

    def leaves(self, depth=0):
        return [(self, depth)]

    @property
    def magnitude(self):
        return self.value


def str_to_pair(s):
    s = s.replace("[", "(").replace("]", ")")
    return Tree(eval(s.strip()), None)


def parse():
    global ns
    with open(IN) as i:
        ns = i.readlines()
    ns = list(map(str_to_pair, ns))

def explode(p):
    leaves = p.leaves()
    try:
        explode = next(t for t in leaves if t[1] > 4)
        i = leaves.index(explode)
        pair = explode[0].parent
        if i > 0:
            left_neighbour = leaves[i - 1]
            left_neighbour[0].value += pair.left.value
        if i < len(leaves) - 2:
            right_neighbour = leaves[i + 2]
            right_neighbour[0].value += pair.right.value

        parent = pair.parent
        if parent.left == pair:
            parent.left = Leaf(0, parent)
        else:
            parent.right = Leaf(0, parent)
        return True
    except StopIteration:
        return False

def split(p):
    leaves = p.leaves()
    try:
        split = next(t for t in leaves if t[0].value >= 10)
        parent = split[0].parent
        if parent.left == split[0]:
            parent.left = Tree((
                math.floor(split[0].value / 2),
                math.ceil(split[0].value / 2)
            ), parent)
        else:
            parent.right = Tree((
                math.floor(split[0].value / 2),
                math.ceil(split[0].value / 2)
            ), parent)
        return True
    except StopIteration:
        return False

def reduce(a, b):
    c = Tree((a, b), None)
    while explode(c) or split(c):
        pass

    return c


def part1():
    red = functools.reduce(reduce, ns)
    print(red.magnitude)


def part2():
    m = -1
    for n1 in ns:
        for n2 in ns:
            s = reduce(n1.copy(), n2.copy())
            if s.magnitude > m:
                m = s.magnitude
            s = reduce(n2.copy(), n1.copy())
            if s.magnitude > m:
                m = s.magnitude
    print(m)


parse()
part1()

parse()
part2()
