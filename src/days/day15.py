from paho.mqtt.properties import readUTF

from src.setup.day import Day
from src.util.solution import solution


class Day15(Day):
    @property
    def day(self):
        return 15

    @solution("1509074")
    def part1(self) -> object:
        grid, moves = self.raw_input.split("\n\n")
        grid = {(x, y): c for y, row in enumerate(grid.splitlines()) for x, c in enumerate(row)}
        moves = "".join(moves.splitlines())

        def move(p, v):
            x, y = p
            dx, dy = v
            n = (x + dx, y + dy)
            o = grid.get(n, "#")
            if o == ".":
                grid[n] = grid[p]
                grid[p] = "."
                return n
            if o == "O":
                if move(n, v) != n:
                    grid[n] = grid[p]
                    grid[p] = "."
                    return n
                return p
            return p

        def print_grid():
            max_x = max(x for x, _ in grid.keys())
            max_y = max(y for _, y in grid.keys())

            for y in range(max_y + 1):
                for x in range(max_x + 1):
                    print(grid.get((x, y), " "), end="")
                print()

        current = next(k for k, v in grid.items() if v == "@")
        dirs = {"^": (0, -1), "v": (0, 1), "<": (-1, 0), ">": (1, 0)}
        for i, m in enumerate(moves):
            current = move(current, dirs[m])
        return sum(100 * y + x for (x, y), v in grid.items() if v == "O")

    @solution("1521453")
    def part2(self) -> object:
        grid, moves = self.raw_input.split("\n\n")
        n_grid = {}

        grid = grid.splitlines()

        for y in range(len(grid)):
            for x in range(len(grid[y])):
                v = grid[y][x]
                n_grid[(x * 2, y)] = {"#": "#", ".": ".", "O": "[", "@": "@"}[v]
                n_grid[(x * 2 + 1, y)] = {"#": "#", ".": ".", "O": "]", "@": "."}[v]

        grid = n_grid

        moves = "".join(moves.splitlines())

        def can_move_box(box, d):
            left, right = box
            dx, dy = d

            if dy == -1:
                u1, u2 = (left[0], left[1] - 1), (right[0], right[1] - 1)
            if dy == 1:
                u1, u2 = (left[0], left[1] + 1), (right[0], right[1] + 1)
            o1, o2 = grid.get(u1, "#"), grid.get(u2, "#")
            if o1 == "#" or o2 == "#":
                return False
            if o1 == "." and o2 == ".":
                return True
            if o1 == "[" and o2 == "]":
                return can_move_box([u1, u2], d)
            n_boxes = []
            if o1 == "]":
                l = (u1[0] - 1, u1[1])
                n_boxes.append([l, u1])
            if o2 == "[":
                r = (u2[0] + 1, u2[1])
                n_boxes.append([u2, r])
            return all(can_move_box(b, d) for b in n_boxes)

        def move(to_move, d):
            dx, dy = d

            if len(to_move) == 1:
                to_move = to_move[0]
                x, y = to_move
                n = (x + dx, y + dy)
                o = grid.get(n, "#")

                if o == ".":
                    grid[n] = grid[to_move]
                    grid[to_move] = "."
                    return n
                if o == "[":
                    n_to_move = [n, (n[0] + 1, n[1])]
                    i = 0
                if o == "]":
                    n_to_move = [(n[0] - 1, n[1]), n]
                    i = 1
                if o in "[]":
                    if move(n_to_move, d) != n_to_move:
                        grid[n] = grid[to_move]
                        grid[to_move] = "."
                        # print_grid()
                        return n
                return to_move

            if len(to_move) == 2:
                l, r = to_move
                if dx == -1:
                    if (t := move([l], d)) != l:
                        return [t, move([r], d)]
                if dx == 1:
                    if (t := move([r], d)) != r:
                        return [move([l], d), t]
                if dy != 0:
                    if can_move_box(to_move, d):
                        return [move([l], d), move([r], d)]
                return to_move

        def print_grid():
            max_x = max(x for x, _ in grid.keys())
            max_y = max(y for _, y in grid.keys())

            for y in range(max_y + 1):
                for x in range(max_x + 1):
                    print(grid.get((x, y), " "), end="")
                print()

        # print_grid()

        current = next(k for k, v in grid.items() if v == "@")
        dirs = {"^": (0, -1), "v": (0, 1), "<": (-1, 0), ">": (1, 0)}
        for i, m in enumerate(moves):
            # print(m)
            current = move([current], dirs[m])
            # print_grid()
        return sum(100 * y + x for (x, y), v in grid.items() if v == "[")


# Day15("test").run()
# Day15("test2").run()
# Day15("test3").run()
Day15().run()
