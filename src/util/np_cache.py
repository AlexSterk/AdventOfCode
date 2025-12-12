import numpy as np
from functools import cache, wraps

def freeze(obj):
    """Convert a NumPy array to a hashable tuple, otherwise pass through."""
    if isinstance(obj, np.ndarray):
        return ("__np__", obj.shape, obj.dtype.str, obj.tobytes())
    return obj

def unfreeze(obj):
    """Reconstruct NumPy array from frozen tuple."""
    if isinstance(obj, tuple) and len(obj) == 4 and obj[0] == "__np__":
        _, shape, dtype, raw = obj
        return np.frombuffer(raw, dtype=dtype).reshape(shape)
    return obj

def freeze_all(args, kwargs):
    """Freeze all positional and keyword arguments."""
    f_args = tuple(freeze(a) for a in args)
    f_kwargs = tuple((k, freeze(v)) for k, v in kwargs.items())
    return f_args, f_kwargs

def unfreeze_all(f_args, f_kwargs):
    """Unfreeze all positional and keyword arguments."""
    args = [unfreeze(a) for a in f_args]
    kwargs = {k: unfreeze(v) for k, v in f_kwargs}
    return args, kwargs

def np_cache(fn):
    """
    Decorator for caching functions that take NumPy arrays as arguments.
    Automatically freezes/unfreezes arrays for functools.cache.
    """
    @cache
    def cached(f_args, f_kwargs):
        args, kwargs = unfreeze_all(f_args, f_kwargs)
        return fn(*args, **kwargs)

    @wraps(fn)
    def wrapper(*args, **kwargs):
        f_args, f_kwargs = freeze_all(args, kwargs)
        return cached(f_args, f_kwargs)

    return wrapper
