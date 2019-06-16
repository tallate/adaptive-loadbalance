package com.aliware.objpool;

/**
 * 默认实现，什么都不做
 */
public abstract class BasePooledObjectFactory<T> implements PooledObjectFactory<T> {

  @Override
  public abstract PooledObject<T> makeObject()
      throws Exception;

  @Override
  public void destroyObject(PooledObject<T> p)
      throws Exception {
  }

  @Override
  public boolean validateObject(PooledObject<T> p) {
    return true;
  }

  @Override
  public void activateObject(PooledObject<T> p)
      throws Exception {
  }

  @Override
  public void passivateObject(PooledObject<T> p)
      throws Exception {
  }
}
