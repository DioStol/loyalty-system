package com.rbi.loyaltysystem.repository.api;

public interface Repository<T> {

    T add(T object);
    T findById(long id);
}
