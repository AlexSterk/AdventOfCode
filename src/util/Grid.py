def print_grid(grid: dict[tuple[int,int],object]) -> None:
    maxx, maxy = 0,0
    for x,y in grid.keys():
        maxx = max(maxx,x)
        maxy = max(maxy,y)
    for y in range(maxy+1):
        for x in range(maxx+1):
            print(grid[x,y], end='')
        print()
