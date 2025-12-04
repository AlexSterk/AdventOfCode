cardinal = {
    "N": (0, -1),
    "S": (0, 1),
    "E": (1, 0),
    "W": (-1, 0),
}

diagonal = {
    "NE": (1, -1),
    "SE": (1, 1),
    "SW": (-1, 1),
    "NW": (-1, -1),
}

all = {**cardinal, **diagonal}
