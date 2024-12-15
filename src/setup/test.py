import os
import sys
import importlib.util

# Define the relative path to the directory containing .py files
directory = os.path.join(os.getcwd(), 'src', 'days')

# Ensure the directory exists
if not os.path.isdir(directory):
    print(f"Error: Directory '{directory}' does not exist.")
    sys.exit(1)

# List all .py files in the directory
py_files = [f for f in os.listdir(directory) if f.endswith('.py')]

if not py_files:
    print("No Python files found in 'src/days'.")
    sys.exit(0)

# Iterate through each .py file and run it
for py_file in py_files:
    file_path = os.path.join(directory, py_file)
    print(f"Running {py_file}...")

    # Dynamically load and execute the script
    spec = importlib.util.spec_from_file_location(py_file, file_path)
    module = importlib.util.module_from_spec(spec)

    try:
        spec.loader.exec_module(module)
    except Exception as e:
        print(f"Error running {py_file}: {e}")
    else:
        print(f"Finished running {py_file}.\n")
