package com.rbi.loyaltysystem.repository.api;


public interface Repository<T>  {

    T insert(T object);

    T findById(long id);
}
