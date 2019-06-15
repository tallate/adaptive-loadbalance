package com.aliware.objpool;

import java.lang.ref.WeakReference;

public class PooledObject<T> {

    WeakReference<T> wrap(T obj);
}
