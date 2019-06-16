package com.aliware.objpool;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * @author hgc
 * @date 6/15/19
 */
public class SynchronizedPooledObjectFactory<T> implements PooledObjectFactory<T> {

  private final WriteLock writeLock = new ReentrantReadWriteLock().writeLock();

  private final PooledObjectFactory<T> delegator;

  public SynchronizedPooledObjectFactory(PooledObjectFactory<T> delegator) {
    if (delegator == null) {
      throw new IllegalArgumentException("Factory delefator must not be null.");
    }
    this.delegator = delegator;
  }


  @Override
  public PooledObject<T> makeObject() throws Exception {
    writeLock.lock();
    try {
      return delegator.makeObject();
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public void destroyObject(PooledObject<T> p) throws Exception {
    writeLock.lock();
    try {
      delegator.destroyObject(p);
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public boolean validateObject(PooledObject<T> p) {
    writeLock.lock();
    try {
      return delegator.validateObject(p);
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public void activateObject(PooledObject<T> p) throws Exception {
    writeLock.lock();
    try {
      delegator.activateObject(p);
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public void passivateObject(PooledObject<T> p) throws Exception {
    writeLock.lock();
    try {
      delegator.passivateObject(p);
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public String toString() {
    return "SynchronizedPooledObjectFactory{" +
        "delegator=" + delegator +
        '}';
  }
}
