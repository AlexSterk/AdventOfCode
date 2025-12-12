from functools import cache, wraps

import numpy as np


def np_cache(fn):
    def freeze(obj: np.ndarray):
        return obj.shape, obj.dtype.str, obj.tobytes()

    def unfreeze(obj):
        shape, dtype, raw = obj
        return np.frombuffer(raw, dtype=dtype).reshape(shape)

    def freeze_all(args, kwargs):
        f_args = tuple(freeze(a) for a in args)
        f_kwargs = tuple((k, freeze(v)) for k, v in kwargs.items())
        return f_args, f_kwargs

    def unfreeze_all(f_args, f_kwargs):
        args = [unfreeze(a) for a in f_args]
        kwargs = {k: unfreeze(v) for k, v in f_kwargs}
        return args, kwargs

    @cache
    def cached(f_args, f_kwargs):
        args, kwargs = unfreeze_all(f_args, f_kwargs)
        return fn(*args, **kwargs)

    @wraps(fn)
    def wrapper(*args, **kwargs):
        f_args, f_kwargs = freeze_all(args, kwargs)
        return cached(f_args, f_kwargs)

    return wrapper
