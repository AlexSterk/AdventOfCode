import os

import requests

YEAR = 2015

print(f"Download input for day: ", end="")
day = input()

url = f"https://adventofcode.com/{YEAR}/day/{day}/input"
with open("data/session.token") as f:
    token = f.read().strip()

response = requests.get(url, cookies={"session": token})
response.raise_for_status()

print(f"Saving input to data/day{day}/input.txt")
os.makedirs(f"data/day{day}", exist_ok=True)
with open(f"data/day{day}/input.txt", "w") as f:
    f.write(response.text)
# create test.txt if it doesn't exist
if not os.path.exists(f"data/day{day}/test.txt"):
    with open(f"data/day{day}/test.txt", "w") as f:
        f.write("")

# check if there is a script file, if not, generate it
try:
    with open(f"src/days/day{day}.py") as f:
        print(f"Day{day}.py already exists")
except FileNotFoundError:
    # copy day.txt
    with open("src/setup/day.txt") as f:
        template = f.read()
        # replace %Day% with day
        template = template.replace("%Day%", day)
        with open(f"src/days/day{day}.py", "w") as f:
            f.write(template)
    print(f"Day{day}.py created")
print(f"https://adventofcode.com/{YEAR}/day/{day}")
