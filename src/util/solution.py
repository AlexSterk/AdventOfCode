# @solution("...") should compare the return value of the function with the string in the decorator.
# If they match, the test is considered passed.
# If they don't match, the test is considered failed.

def solution(expected: str):
    def decorator(func):
        def wrapper(*args, **kwargs):
            skip = kwargs.pop("skip_test", False)
            result = func(*args, **kwargs)
            if expected and not skip and str(result) != expected:
                print(f"Test failed: {result} != {expected}")
            return result
        return wrapper
    return decorator