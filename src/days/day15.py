from src.setup.day import Day
from src.util.solution import solution


def print_grid(grid):
    max_x = max(x for x, _ in grid.keys())
    max_y = max(y for _, y in grid.keys())

    for y in range(max_y + 1):
        for x in range(max_x + 1):
            print(grid.get((x, y), " "), end="")
        print()


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
                grid[n], grid[p] = grid[p], grid[n]
                return n
            if o == "O":
                if move(n, v) != n:
                    grid[n], grid[p] = grid[p], grid[n]
                    return n
                return p
            return p

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

        # scaling up the grid width
        for y in range(len(grid)):
            for x in range(len(grid[y])):
                v = grid[y][x]
                n_grid[(x * 2, y)] = {"#": "#", ".": ".", "O": "[", "@": "@"}[v]
                n_grid[(x * 2 + 1, y)] = {"#": "#", ".": ".", "O": "]", "@": "."}[v]

        grid = n_grid

        moves = "".join(moves.splitlines())

        # When we are moving a box vertically, we need to check if the space above or below has 1 or 2 boxes.
        # If it does, we need to move those as well.
        def can_move_box(box, d):
            left, right = box
            _, dy = d
            assert dy != 0

            if dy == -1:
                u1, u2 = (left[0], left[1] - 1), (right[0], right[1] - 1)
            if dy == 1:
                u1, u2 = (left[0], left[1] + 1), (right[0], right[1] + 1)
            o1, o2 = grid.get(u1, "#"), grid.get(u2, "#")
            if o1 == "#" or o2 == "#":  # We can't move a box into a wall
                return False
            if o1 == "." and o2 == ".":  # Both spaces are free, so we can move the box
                return True
            if o1 == "[" and o2 == "]":  # There is one box above/below us
                return can_move_box([u1, u2], d)
            n_boxes = []  # There are 1 or 2 boxes above/below us
            if o1 == "]":  # Box diagonally left
                l = (u1[0] - 1, u1[1])
                n_boxes.append([l, u1])
            if o2 == "[":  # Box diagonally right
                r = (u2[0] + 1, u2[1])
                n_boxes.append([u2, r])
            return all(
                can_move_box(b, d) for b in n_boxes)  # We need to move both boxes, before we can move the current box

        def move(to_move, d):
            dx, dy = d

            if len(to_move) == 1:  # We are moving the robot, or 1/2 a box
                p = to_move[0]
                x, y = p
                n = (x + dx, y + dy)
                o = grid.get(n, "#")

                if o == ".":  # We can move
                    grid[n], grid[p] = grid[p], grid[n]
                    return n
                if o == "[":
                    n_to_move = [n, (n[0] + 1, n[1])]
                if o == "]":
                    n_to_move = [(n[0] - 1, n[1]), n]
                if o in "[]":  # We ran into a box, we must move it
                    if move(n_to_move, d) != n_to_move:
                        # now that the box is moved, we can move whatever we were moving
                        grid[n], grid[p] = grid[p], grid[n]
                        return n
                # we can't move, keep the current position
                return p

            if len(to_move) == 2:  # We are moving a full box
                l, r = to_move
                if dx == -1:  # When we move left/right, we can move as normal, recursively checking for free spaces
                    if (t := move([l], d)) != l:
                        return [t, move([r], d)]
                if dx == 1:
                    if (t := move([r], d)) != r:
                        return [move([l], d), t]
                if dy != 0:
                    # When we move up/down, we need to check two spaces before we can move
                    # Because move() only moves one space at a time,
                    # we need to make sure that it is safe to perform that move.
                    if can_move_box(to_move, d):  # We determined that moving is safe, move each half separately
                        return [move([l], d), move([r], d)]
                # We can't move, keep the current position
                return to_move

        current = next(k for k, v in grid.items() if v == "@")
        dirs = {"^": (0, -1), "v": (0, 1), "<": (-1, 0), ">": (1, 0)}
        for i, m in enumerate(moves):
            current = move([current], dirs[m])
        return sum(100 * y + x for (x, y), v in grid.items() if v == "[")


# Day15("test").run()
# Day15("test2").run()
# Day15("test3").run()
Day15().run()
